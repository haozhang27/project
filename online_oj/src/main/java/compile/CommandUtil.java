package compile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 1、
 * 借助这个类让Java代码能够去执行一个具体的命令
 * 如 javac Test.java
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class CommandUtil {

    /**
     *
     * @param cmd 要执行的命令
     * @param stdoutFile 表示标准输出结果重定向到那个文件中  如果为null表示不需要重定向
     * @param stderrFile 表示标准错误结果重定向到哪个文件中
     * @throws IOException
     */
    public static int run(String cmd, String stdoutFile, String stderrFile) throws IOException, InterruptedException {
        //1.获取Runtime对象，Runtime对象是一个单例的
        Runtime runtime = Runtime.getRuntime();

        //2.通过Runtime中的 exec 方法来执行一个指令 相当于在命令行中执行cmd命令
        Process process = runtime.exec(cmd);

        //3.针对标准输出进行重定向
        if (stdoutFile != null) {
            InputStream stdoutFrom = process.getInputStream();
            OutputStream stdoutTo = new FileOutputStream(stdoutFile);

            int ch = -1;
            while ((ch = stdoutFrom.read()) != -1) {
                stdoutTo.write(ch);
            }

            stdoutFrom.close();
            stdoutTo.close();
        }

        //4.针对标准错误重定向
        if (stderrFile != null) {
            InputStream stderrFrom = process.getErrorStream();
            OutputStream stderrTo = new FileOutputStream(stderrFile);

            int ch = -1;
            while ((ch = stderrFrom.read()) != -1) {
                stderrTo.write(ch);
            }

            stderrFrom.close();
            stderrTo.close();
        }

        //5.为了确保子进程先执行完  就需要加上进程等待的逻辑
        //父进程会在这里阻塞，直到子进程执行结束，才继续往下执行
        int exitCode = process.waitFor();
        return exitCode;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        run("javac", "d:/oj/stdout.txt", "d:/oj/stderr.txt");
    }
}
