package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import compile.Answer;
import compile.Question;
import compile.Task;
import problem.Problem;
import problem.ProblemDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 10、
 *
 * @author haozhang
 * @date 2020/09/06
 */
public class CompileServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().create();

    /**
     * 创建两个辅助的类,完成请求解析和响应构建
     *
     * 用来辅助解析 body 中的数据请求
     */

    static class CompileRequest {
        /**
         * 表示题目id
         */
        private int id;

        /**
         * 表示用户提交的代码
         */
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * 用于构造最终响应数据
     */
    static class CompileResponse {
        /**
         * 表示是否成功
         */
        private int ok;

        /**
         *
         */
        private String reason;

        private String stdout;

        public int getOk() {
            return ok;
        }

        public void setOk(int ok) {
            this.ok = ok;
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //1.读取 请求 的 body 的所有数据
        String body = readBody(req);

        //2.按照API约定的格式来解析json数据，得到CompileRequest对象
        CompileRequest compileRequest = gson.fromJson(body, CompileRequest.class);

        //3.按照 id 从数据库中读取出对应的测试用例代码
        ProblemDAO problemDAO = new ProblemDAO();
        Problem problem = problemDAO.selectOne(compileRequest.getId());

        //得到该题目的测试代码
        String testCode = problem.getTestCode();

        //得到改题目的用户输入的代码
        String requestCode = compileRequest.getCode();

        //4.把用户输入的代码和测试用例进行组装，组装成一个完整的可以运行编译的代码
        String finalCode = mergeCode(requestCode, testCode);

        //5.创建task对象对刚才拼装的代码进行编译运行
        Question question = new Question();
        question.setCode(finalCode);
        question.setStdin("");
        Task task = new Task();
        Answer answer = null;
        try {
            answer = task.compileAndRun(question);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //6.把运行结果构造成响应数据写回给客户端
        CompileResponse compileResponse = new CompileResponse();
        compileResponse.setOk(answer.getError());
        compileResponse.setReason(answer.getReason());
        compileResponse.setStdout(answer.getStdout());
        String jsonString = gson.toJson(compileResponse);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(jsonString);

    }

    /**
     * 把testCode中的main方法内容嵌入到requestCode中
     * @param requestCode 用户的代码
     * @param testCode 测试用例代码
     * @return
     */
    private String mergeCode(String requestCode, String testCode) {
        //合并之前考虑清楚这两部分的代码都是什么样的
        //1.先找到requestCode中最后一个 }
        //2.把requestCode中最后一个 } 删除之后，再把testCode内容拼接上
        //3.拼接完之后再补一个 }
        int pos = requestCode.lastIndexOf('}');
        if (pos == -1) {
            return null;
        }

        return requestCode.substring(0, pos) + testCode + "\n}";
    }

    private String readBody(HttpServletRequest req) {
        //body 的长度在 header 中的一个 Content-Length 字段中
        //contentLength 的单位就是字节
        int contentLength = req.getContentLength();
        byte[] buf = new byte[contentLength];
        try (InputStream is = req.getInputStream()) {
            is.read(buf, 0, contentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buf);
    }
}
