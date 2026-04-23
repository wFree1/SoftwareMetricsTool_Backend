package cn.edu.csu.smproject.domain;

public class Code {
    private String fileName; /**     * 文件名属性     */
    private int codeLines; /**     * 代码行数属性     */
    private int commentLines; /**     * 注释行数属性     */
    private int blankLines; /**     * 空行数属性     */

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCodeLines() {
        return codeLines;
    }

    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }

    public int getCommentLines() {
        return commentLines;
    }

    public void setCommentLines(int commentLines) {
        this.commentLines = commentLines;
    }

    public int getBlankLines() {
        return blankLines;
    }

    public void setBlankLines(int blankLines) {
        this.blankLines = blankLines;
    }

    @Override
    public String toString(){
        System.out.println(fileName+codeLines+commentLines+blankLines);
        return "OK";
    }
}
