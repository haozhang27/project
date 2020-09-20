package compile;

/**
 * 3、
 * 一次编译运行过程中都产生了哪写数据
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class Answer {

    /**
     * 通过error来表示当前的错误类型
     * 0 表示没错
     * 1 表示编译出错
     * 2 表示运行出错
     */
    private int error;

    /**
     * 表示具体的出错原因。可能是编译错误，也可能是运行错误（异常信息）
     */
    private String reason;

    /**
     * 执行时标准输出对应的内容
     */
    private String stdout;

    /**
     * 执行时标准错误对应的内容
     */
    private String stderr;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "error=" + error +
                ", reason='" + reason + '\'' +
                ", stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                '}';
    }
}
