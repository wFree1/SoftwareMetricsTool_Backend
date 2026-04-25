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

    @PostMapping(value = "/test", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public TicketResponse test(@RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse=new TicketResponse();
        List<OrderResponse> orders=new ArrayList<OrderResponse>();
        OrderResponse o=new OrderResponse();
        o.setMsg("投注成功");
        orders.add(o);
        ticketResponse.setOrderList(orders);
        return ticketResponse;
    }

//    @PostMapping(value = "/test2", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
//    @ResponseBody
//    public CKResponse test2(@RequestBody UMLXML model){
//        Util.statClassAndInterface(model);
//        Util.countOwnedOperations(model);
//        Util.computeDepth(model);
//        Util.computeCBO(model);
//        //统计每个类的属性
//        Util.statAttribute(model);
//        Util.computeRFC(model);
//        Util.computeLCOM(model);
//
//        for(int i=0;i<model.getPackagedElements().size();i++){
//            //如果是我们的Class类型
//            if(model.getPackagedElements().get(i).getType().equals("uml:Class")){
//                //String id = model.getPackagedElements().get(i).getId();
//                System.out.println("对于类别："+model.getPackagedElements().get(i).getName()
//                        +",wmc是："+model.getPackagedElements().get(i).getWmc()
//                        +"，深度是："+ model.getPackagedElements().get(i).getDepth()
//                        +",子类数量是："+model.getPackagedElements().get(i).getNoc()
//                        +",CBO数量是："+model.getPackagedElements().get(i).getCbo()
//                        +",RFC数量是："+model.getPackagedElements().get(i).getRfc()
//                        +",LCOM数量是："+model.getPackagedElements().get(i).getLcom());
//            }else if(model.getPackagedElements().get(i).getType().equals("uml:Interface")){
//                System.out.println("对于接口："+model.getPackagedElements().get(i).getName()
//                        +",wmc是："+model.getPackagedElements().get(i).getWmc()
//                        +"，深度是："+ model.getPackagedElements().get(i).getDepth()
//                        +",子类数量是："+model.getPackagedElements().get(i).getNoc()
//                        +",CBO数量是："+model.getPackagedElements().get(i).getCbo()
//                        +",RFC数量是："+model.getPackagedElements().get(i).getRfc()
//                        +",LCOM数量是："+model.getPackagedElements().get(i).getLcom());
//            }
//        }
//
//        //基本的功能我们已经实现了，接下来我们来设计一下如何返回我们的xml文件
//        CKResponse ckResponse = new CKResponse();
//        List<CKResult> ckResults = new ArrayList<CKResult>();
//        for(int i=0;i<model.getPackagedElements().size();i++){
//            //如果是我们的Class类型
//            if(model.getPackagedElements().get(i).getType().equals("uml:Class")){
//                //String id = model.getPackagedElements().get(i).getId();
//                CKResult ckResult = new CKResult();
//                ckResult.setName(model.getPackagedElements().get(i).getName());
//                ckResult.setWmc(model.getPackagedElements().get(i).getWmc());
//                ckResult.setDit(model.getPackagedElements().get(i).getDepth());
//                ckResult.setNoc(model.getPackagedElements().get(i).getNoc());
//                ckResult.setCbo(model.getPackagedElements().get(i).getCbo());
//                ckResult.setRfc(model.getPackagedElements().get(i).getRfc());
//                ckResult.setLcom(model.getPackagedElements().get(i).getLcom());
//                ckResults.add(ckResult);
//            }
//        }
//        ckResponse.setCkResultList(ckResults);
//        return ckResponse;
//    }

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

        for(int i=0;i<model.getPackagedElements().size();i++){
            if(model.getPackagedElements().get(i).getType().equals("uml:Class")){
                System.out.println("对于类别："+model.getPackagedElements().get(i).getName()
                        +",wmc是："+model.getPackagedElements().get(i).getWmc()
                        +"，深度是："+ model.getPackagedElements().get(i).getDepth()
                        +",子类数量是："+model.getPackagedElements().get(i).getNoc()
                        +",CBO数量是："+model.getPackagedElements().get(i).getCbo()
                        +",RFC数量是："+model.getPackagedElements().get(i).getRfc()
                        +",LCOM数量是："+model.getPackagedElements().get(i).getLcom());
            }else if(model.getPackagedElements().get(i).getType().equals("uml:Interface")){
                System.out.println("对于接口："+model.getPackagedElements().get(i).getName()
                        +",wmc是："+model.getPackagedElements().get(i).getWmc()
                        +"，深度是："+ model.getPackagedElements().get(i).getDepth()
                        +",子类数量是："+model.getPackagedElements().get(i).getNoc()
                        +",CBO数量是："+model.getPackagedElements().get(i).getCbo()
                        +",RFC数量是："+model.getPackagedElements().get(i).getRfc()
                        +",LCOM数量是："+model.getPackagedElements().get(i).getLcom());
            }
        }

        CKResponse ckResponse = new CKResponse();
        List<CKResult> ckResults = new ArrayList<CKResult>();
        for(int i=0;i<model.getPackagedElements().size();i++){
            if(model.getPackagedElements().get(i).getType().equals("uml:Class") || model.getPackagedElements().get(i).getType().equals("uml:Interface") ){
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
        for(int i=0;i<model.getPackagedElements().size();i++){
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

    @PostMapping(value = "/VGMetrics", consumes = { MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Map<String, Object> VGMetrics(@RequestBody UMLXML model,
                                         @RequestParam(required = false) String projectName){
        Map<String, Object> response = new HashMap<>();
        int vg = 0;
        VGUtil.computeVG(model);
        for(PackagedElement packagedElement : model.getPackagedElements()){
            System.out.println(packagedElement.getVg());
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

    @PostMapping(value="/FPMetrics",consumes = { MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public FPResponse FPMetrics(@RequestBody DFXML model,
                                @RequestParam(required = false) String projectName){
        FPResponse fpResponse = new FPResponse();

        ArrayList<Process> processArrayList = model.getRootObject().getChildren().getModel().getProcessArrayList();
        for(Process process:processArrayList){
            System.out.println(process.getName());
        }
        fpResponse.setProcessArrayList(processArrayList);

        ArrayList<Flow> flowArrayList = model.getRootObject().getChildren().getModel().getFlowArrayList();
        for(Flow flow:flowArrayList){
            if(flow.getObject1().getProcess()!=null){
                System.out.println(flow.getObject1().getProcess().getRef());
            }
        }
        fpResponse.setFlowArrayList(flowArrayList);

        ArrayList<OrganizationUnit> organizationUnitArrayList = model.getRootObject().getChildren().getModel().getOrganizationUnitArrayList();
        for(OrganizationUnit organizationUnit:organizationUnitArrayList){
            System.out.println(organizationUnit.getName());
        }
        fpResponse.setOrganizationUnitArrayList(organizationUnitArrayList);

        ArrayList<Resource> resourceArrayList = model.getRootObject().getChildren().getModel().getResourceArrayList();
        for(Resource resource:resourceArrayList){
            System.out.println(resource.getName());
        }
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
