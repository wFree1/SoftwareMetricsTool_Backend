/**
 * 软件度量工具控制器，提供各种软件度量指标的计算接口
 */
package cn.edu.csu.smproject.controller;

import cn.edu.csu.smproject.service.*;
import cn.edu.csu.smproject.domain.*;
import cn.edu.csu.smproject.domain.DF.*;
import cn.edu.csu.smproject.domain.DF.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class SscController {

    @Autowired
    private HistoryService historyService;

    /**
     * 测试接口，用于验证XML格式的请求和响应
     * @param ticketRequest 测试请求对象
     * @return 测试响应对象
     */
    @PostMapping(value = "/test", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public TicketResponse test(@RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse = new TicketResponse();
        List<OrderResponse> orders = new ArrayList<OrderResponse>();
        OrderResponse o = new OrderResponse();
        o.setMsg("投注成功");
        orders.add(o);
        ticketResponse.setOrderList(orders);
        return ticketResponse;
    }

    /**
     * 计算CK度量指标
     * @param model UML模型对象
     * @param projectName 项目名称（可选）
     * @return CK度量指标响应
     */
    @PostMapping(value = "/CKMetrics", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public CKResponse CKMetrics(@RequestBody UMLXML model,
                                @RequestParam(required = false) String projectName){
        CKUtil.statClassAndInterface(model);
        CKUtil.countOwnedOperations(model);
        CKUtil.computeDepth(model);
        CKUtil.computeCBO(model);
        CKUtil.statAttribute(model);
        CKUtil.computeRFC(model);
        CKUtil.computeLCOM(model);

        CKResponse ckResponse = new CKResponse();
        List<CKResult> ckResults = new ArrayList<CKResult>();
        for(int i=0; i<model.getPackagedElements().size(); i++){
            if(model.getPackagedElements().get(i).getType().equals("uml:Class") || model.getPackagedElements().get(i).getType().equals("uml:Interface")){
                CKResult ckResult = new CKResult();
                ckResult.setName(model.getPackagedElements().get(i).getName());
                ckResult.setWmc(model.getPackagedElements().get(i).getWmc());
                ckResult.setDit(model.getPackagedElements().get(i).getDepth());
                ckResult.setNoc(model.getPackagedElements().get(i).getNoc());
                ckResult.setCbo(model.getPackagedElements().get(i).getCbo());
                ckResult.setRfc(model.getPackagedElements().get(i).getRfc());
                ckResult.setLcom(model.getPackagedElements().get(i).getLcom());
                ckResults.add(ckResult);
            }
        }
        ckResponse.setCkResultList(ckResults);

        if(projectName != null && !projectName.isEmpty()){
            try{
                Map<String, Object> data = new HashMap<>();
                data.put("results", ckResults);
                historyService.saveHistory(projectName, "CK", data);
            }catch(Exception e){
                System.err.println("Failed to save CK history: " + e.getMessage());
            }
        }

        return ckResponse;
    }

    /**
     * 计算LK度量指标
     * @param model UML模型对象
     * @param projectName 项目名称（可选）
     * @return LK度量指标响应
     */
    @PostMapping(value = "/LKMetrics", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public LKResponse LKMetrics(@RequestBody UMLXML model,
                                @RequestParam(required = false) String projectName){
        LKUtil.statClassAndInterface(model);
        LKUtil.countOwnedOperations(model);
        LKUtil.statAttribute(model);
        LKUtil.countCS(model);
        LKUtil.computeDepth(model);
        LKUtil.computeSi(model);

        LKResponse lkResponse = new LKResponse();
        List<LKResult> lkResults = new ArrayList<LKResult>();
        for(int i=0; i<model.getPackagedElements().size(); i++){
            if(model.getPackagedElements().get(i).getType().equals("uml:Class")){
                PackagedElement packagedElement = model.getPackagedElements().get(i);
                LKResult lkResult = new LKResult();
                lkResult.setName(packagedElement.getName());
                lkResult.setTotalNumberOfMethod(packagedElement.getTotalNumberOfMethod());
                lkResult.setTotalNumberOfAttr(packagedElement.getTotalNumberOfAttr());
                lkResult.setNoo(packagedElement.getNoo());
                lkResult.setNoa(packagedElement.getNoa());
                lkResult.setSi(packagedElement.getSi());
                lkResults.add(lkResult);
            }
        }
        lkResponse.setLkResultList(lkResults);

        if(projectName != null && !projectName.isEmpty()){
            try{
                Map<String, Object> data = new HashMap<>();
                data.put("results", lkResults);
                historyService.saveHistory(projectName, "LK", data);
            }catch(Exception e){
                System.err.println("Failed to save LK history: " + e.getMessage());
            }
        }

        return lkResponse;
    }

    /**
     * 计算VG度量指标
     * @param model UML模型对象
     * @param projectName 项目名称（可选）
     * @return VG度量指标响应
     */
    @PostMapping(value = "/VGMetrics", consumes = { MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Map<String, Object> VGMetrics(@RequestBody UMLXML model,
                                         @RequestParam(required = false) String projectName){
        Map<String, Object> response = new HashMap<>();
        int vg = 0;
        VGUtil.computeVG(model);
        for(PackagedElement packagedElement : model.getPackagedElements()){
            vg = packagedElement.getVg();
        }
        response.put("vg", vg);

        if(projectName != null && !projectName.isEmpty()){
            try{
                historyService.saveHistory(projectName, "VG", response);
            }catch(Exception e){
                System.err.println("Failed to save VG history: " + e.getMessage());
            }
        }

        return response;
    }

    /**
     * 代码行数统计
     * @param files 上传的代码文件
     * @param projectName 项目名称（可选）
     * @return 代码统计结果
     */
    @PostMapping(value = "/countCode")
    public Map<String, Object> countCode(@RequestParam("files") MultipartFile[] files,
                                        @RequestParam(required = false) String projectName){
        Map<String, Object> response = new HashMap<>();
        CodeCounterUtil codeCounterUtil = new CodeCounterUtil();
        ArrayList<Code> codes = codeCounterUtil.countCode(files);

        response.put("data", codes);
        response.put("count", codes.size());

        if(projectName != null && !projectName.isEmpty()){
            try{
                historyService.saveHistory(projectName, "LOC", response);
            }catch(Exception e){
                System.err.println("Failed to save LOC history: " + e.getMessage());
            }
        }

        return response;
    }

    /**
     * 计算UCP度量指标
     * @param model UML模型对象
     * @param projectName 项目名称（可选）
     * @return UCP度量指标响应
     */
    @PostMapping(value = "/UCPMetrics", consumes = { MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public UCPResponse UCPMetrics(@RequestBody UMLXML model,
                                  @RequestParam(required = false) String projectName){
        UCPResponse ucpResponse = new UCPResponse();
        ucpResponse.setActors(UCPUtil.statActor(model));
        ucpResponse.setUsecases(UCPUtil.statUseCase(model));

        if(projectName != null && !projectName.isEmpty()){
            try{
                Map<String, Object> data = new HashMap<>();
                data.put("actors", ucpResponse.getActors());
                data.put("usecases", ucpResponse.getUsecases());
                historyService.saveHistory(projectName, "UCP", data);
            }catch(Exception e){
                System.err.println("Failed to save UCP history: " + e.getMessage());
            }
        }

        return ucpResponse;
    }

    /**
     * 计算FP度量指标
     * @param model DF模型对象
     * @param projectName 项目名称（可选）
     * @return FP度量指标响应
     */
    @PostMapping(value="/FPMetrics", consumes = { MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public FPResponse FPMetrics(@RequestBody DFXML model,
                                @RequestParam(required = false) String projectName){
        FPResponse fpResponse = new FPResponse();

        ArrayList<Process> processArrayList = model.getRootObject().getChildren().getModel().getProcessArrayList();
        fpResponse.setProcessArrayList(processArrayList);

        ArrayList<Flow> flowArrayList = model.getRootObject().getChildren().getModel().getFlowArrayList();
        fpResponse.setFlowArrayList(flowArrayList);

        ArrayList<OrganizationUnit> organizationUnitArrayList = model.getRootObject().getChildren().getModel().getOrganizationUnitArrayList();
        fpResponse.setOrganizationUnitArrayList(organizationUnitArrayList);

        ArrayList<Resource> resourceArrayList = model.getRootObject().getChildren().getModel().getResourceArrayList();
        fpResponse.setResourceArrayList(resourceArrayList);

        if(projectName != null && !projectName.isEmpty()){
            try{
                Map<String, Object> data = new HashMap<>();
                data.put("processes", processArrayList);
                data.put("flows", flowArrayList);
                data.put("organizationUnits", organizationUnitArrayList);
                data.put("resources", resourceArrayList);
                historyService.saveHistory(projectName, "FP", data);
            }catch(Exception e){
                System.err.println("Failed to save FP history: " + e.getMessage());
            }
        }

        return fpResponse;
    }
}
