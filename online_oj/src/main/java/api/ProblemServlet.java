package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import problem.Problem;
import problem.ProblemDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 9、
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class ProblemServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (id == null || "".equals(id)) {
            //没有id这个参数， 执行查找全部
            selectAll(resp);
        } else {
            //存在id这个参数，执行查找指定题目id
            selectOne(Integer.parseInt(id), resp);
        }
    }

    private void selectOne(int problemId, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        ProblemDAO problemDAO = new ProblemDAO();
        Problem problem = problemDAO.selectOne(problemId);
        //测试用例不应该被显示出来，手动改掉
        problem.setTestCode("");
        String json = gson.toJson(problem);
        resp.getWriter().write(json);
    }

    private void selectAll(HttpServletResponse resp) throws IOException {
        //ContentType 描述了body的数据类型是啥样的
        //常见取值
        //html：text/html
        //图片：image/png  image/jpg
        //json：application/json
        //css：text/css
        //javascript：application/javascript
        resp.setContentType("application/json; charset=utf-8");
        ProblemDAO problemDAO = new ProblemDAO();
        List<Problem> problems = problemDAO.selectAll();

        //把结果组织成json结构
        //注意：需要把problems中的某些字段去掉
        String jsonString = gson.toJson(problems);
        resp.getWriter().write(jsonString);

    }
}
