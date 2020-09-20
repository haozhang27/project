package compile;

import common.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * 4、
 * 借助这个类来描述一次编译运行的过程
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class Task {

    /**
     * 编译过程中依赖了一些临时文件，需要约定临时文件名称
     *
     * 这些临时文件就是为了把编译执行过程中涉及到的各种中间结果记录下来
     */

    /**
     * 所有临时文件都放在 tmp 中
     */
    private static final String WORK_DIR = "./tmp/";

    /**
     * 要编译的代码的类名
     */
    private static final String CLASS = "Solution";

    /**
     * 要编译的代码对应的文件名   要和类名一致
     */
    private static final String CODE = WORK_DIR + "Solution.java";

    /**
     * 标准输入对应的文件（其实也没用到）
     */
    private static final String STDIN = WORK_DIR + "stdin.txt";

    /**
     *标准输出对应的文件（编译执行的代码结果放到这个文件中）
     */
    private static final String STDOUT = WORK_DIR + "stdout.txt";

    /**
     * 标准错误对应的文件（编译执行的代码结果放到这个文件中）
     */
    private static final String STDERR = WORK_DIR + "stderr.txt";

    /**
     * 编译错误对应的文件（编译出错时的具体原因）
     */
    private static final String COMPILE_ERROR = WORK_DIR + "compile_error.txt";

    public Answer compileAndRun(Question question) throws IOException, InterruptedException {
        Answer answer = new Answer();
        //0.先创建好存放临时文件的目录
        File wordDir = new File(WORK_DIR);
        if (!wordDir.exists()) {
            wordDir.mkdirs();
        }
        //1.根据question去构造一些需要的临时文件
        FileUtil.writeFile(CODE, question.getCode());
        FileUtil.writeFile(STDIN, question.getStdin());
        //2.构造编译命令并执行
            //2.1 形如  javac -encoding UTF-8 ./tmp/Solution.java -d ./tmp/
        String cmd = String.format(
                "javac -encoding UTF-8 %s -d %s", CODE, WORK_DIR
        );
        System.out.println("编译命令:" + cmd);
        CommandUtil.run(cmd, null, COMPILE_ERROR);
            //2.2编译完成之后，判读编译是否出错，如果出错就不需要再执行
            //认为 COMPILE_ERROR 文件为空表示编译未出错，非空表示编译出错
        String compileError = FileUtil.readFile(COMPILE_ERROR);
        if (!"".equals(compileError)) {
            System.out.println("编译出错");
            answer.setError(1);
            answer.setReason(compileError);
            return answer;
        }
        //3.构造运行命令并执行  形如 java -classpath /.tmp/ Solution
        //为了能让Java命令正确找到类对应的.class文件，需要指定加载路径。-classpath 选项来指定
        cmd = String.format(
            "java -classpath %s %s", WORK_DIR, CLASS
        );
        System.out.println("运行命令：" + cmd);
        CommandUtil.run(cmd, STDOUT, STDERR);
        //判断运行是否出错   查看STDERR 是否为空
        String stdErr = FileUtil.readFile(STDERR);
        if (!"".equals(stdErr)) {
            System.out.println("运行出错");
            answer.setError(2);
            answer.setReason(stdErr);
            return answer;
        }
        //4.将运行结果包装到Answer对象中
        answer.setError(0);
        answer.setStdout(FileUtil.readFile(STDOUT));
        return answer;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Question question = new Question();
        question.setCode(
                "public class Solution {\n" +
                        " public static void main(String[] args) {\n" +
                            "String s = null;\n" +
                            "System.out.println(s.length());\n" +
                        "}\n" +
                "}\n"
        );
        question.setStdin("");
        Task task = new Task();
        Answer answer = task.compileAndRun(question);
        System.out.println(answer);
    }
}
