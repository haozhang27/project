package compile;

/**
 * 2、
 * 一次编译运行过程中都依赖哪写数据
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class Question {
    /**
     * 要编译和执行的代码
     */
    private String code;

    /**
     * 执行时标准输入要输入的内容
     *    我们实际上没用
     */
    private String stdin;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }
}
