package problem;

import common.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 8、
 * 数据访问层
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class ProblemDAO {

    /**
     * 获取所有题目信息
     */
    public List<Problem> selectAll() {
        List<Problem> ret = new ArrayList<>();
        //1.获取数据库连接
        Connection connection = DBUtil.getConnection();
        //2.拼装sql语句
        String sql = "select * from oj_table";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            //3.执行sql语句
            resultSet = statement.executeQuery();

            //4.遍历结果集
            while (resultSet.next()) {
                Problem problem = new Problem();
                problem.setId(resultSet.getInt("id"));
                problem.setTitle(resultSet.getString("title"));
                problem.setLevel(resultSet.getString("level"));
//                problem.setDescription(resultSet.getString("description"));
//                problem.setTemplateCode(resultSet.getString("templateCode"));
//                problem.setTestCode(resultSet.getString("testCode"));
                ret.add(problem);
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5.关闭连接
            DBUtil.close(connection, statement, resultSet);
        }

        return null;
    }

    /**
     * 获取指定id题目信息
     * @param id
     * @return
     */
    public Problem selectOne(int id) {
        //1.建立连接
        Connection connection = DBUtil.getConnection();
        //2.拼装sql语句
        String sql = "select * from oj_table where id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            //3.执行sql语句
            resultSet = statement.executeQuery();
            //4.处理结果集
            if (resultSet.next()) {
                Problem problem = new Problem();
                problem.setId(resultSet.getInt("id"));
                problem.setTitle(resultSet.getString("title"));
                problem.setLevel(resultSet.getString("level"));
                problem.setDescription(resultSet.getString("description"));
                problem.setTemplateCode(resultSet.getString("templateCode"));
                problem.setTestCode(resultSet.getString("testCode"));
                return problem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5.关闭连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 新增一个题目到数据库中
     * @param problem
     */
    public void insert(Problem problem) {
        //1.获取连接
        Connection connection = DBUtil.getConnection();
        //2.拼装sql语句
        String sql = "insert into oj_table values (null, ?, ?, ?, ?, ?)";
        //3.执行sql语句
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, problem.getTitle());
            statement.setString(2, problem.getLevel());
            statement.setString(3, problem.getDescription());
            statement.setString(4, problem.getTemplateCode());
            statement.setString(5, problem.getTestCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //4.关闭连接
            DBUtil.close(connection, statement, null);
        }
    }

    /**
     * 删除指定id题目
     * @param id
     */
    public void delete(int id) {
        //1.建立连接
        Connection connection = DBUtil.getConnection();
        //2.拼接sql
        String sql = "delete from oj_table where id = ?";
        //3.执行sql
        PreparedStatement statement = null;
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            //4.关闭连接
            DBUtil.close(connection, statement, null);
        }
    }

    public static void main(String[] args) {
        String s11 = "abd";
        String s22 = "db2";
        StringBuilder sb3 = new StringBuilder(s11);
        sb3.reverse();
        Problem problem = new Problem();
        problem.setTitle("各位相加");
        problem.setLevel("简单");
        problem.setDescription("给定一个非负整数 num，反复将各个位上的数字相加，直到结果为一位数。\n" +
                "\n" +
                "示例:\n" +
                "\n" +
                "输入: 38\n" +
                "输出: 2 \n" +
                "解释: 各位相加的过程为：3 + 8 = 11, 1 + 1 = 2。 由于 2 是一位数，所以返回 2。\n" +
                "\n");
        problem.setTemplateCode("public class Solution {\n" +
                "    public int addDigits(int num) {\n" +
                "\n" +
                "    }\n" +
                "}");
        problem.setTestCode(
                        "public static void main(String[] args) {\n" +
                        "    Solution s = new Solution();\n" +
                        "    if (s.addDigits(1) == 1 && s.addDigits(38) == 2) {\n" +
                        "        System.out.println(\"Test OK\");\n" +
                        "    } else {\n" +
                        "        System.out.println(\"Test Failed\");\n" +
                        "    }\n" +
                        "}\n");

        ProblemDAO problemDAO = new ProblemDAO();
        problemDAO.insert(problem);
        System.out.println("Insert OK");


        Problem problem1 = new Problem();
        problem1.setTitle("数字颠倒");
        problem1.setLevel("简单");
        problem1.setDescription("输入一个整数，将这个整数以字符串的形式逆序输出" +
                "程序不考虑负数的情况，若数字含有0，则逆序形式也含有0，如输入为100，则输出为001\n" +
                "\n" +
                "示例:\n" +
                "\n" +
                "输入: 1516000\n" +
                "输出: 0006151 \n" +
                "解释: 将这个整数以字符串的形式逆序输出\n" +
                "\n");
        problem1.setTemplateCode("public class Solution {\n" +
                "    public String reverseNumber(int num) {\n" +
                "\n" +
                "    }\n" +
                "}");
        problem1.setTestCode(
                "public static void main(String[] args) {\n" +
                        "    Solution s = new Solution();\n" +
                        "    int num = 1516000000;        \n" +
                        "    String str = \"0000006151\"; \n" +
                        "    if (s.reverseNumber(num).equals(str)) {\n" +
                        "        System.out.println(\"Test OK\");\n" +
                        "    } else {\n" +
                        "        System.out.println(\"Test Failed\");\n" +
                        "    }\n" +
                        "}\n");

        ProblemDAO problemDAO1 = new ProblemDAO();
        problemDAO1.insert(problem1);
        System.out.println("Insert OK");


        Problem problem2 = new Problem();
        problem2.setTitle("字符串反转");
        problem2.setLevel("简单");
        problem2.setDescription("写出一个程序，接受一个字符串，然后输出该字符串反转后的字符串。（字符串长度不超过1000）\n" +
                "\n" +
                "示例:\n" +
                "\n" +
                "输入: abcd\n" +
                "输出: dcba \n" +
                "解释: 输出该字符串反转后的字符串\n" +
                "\n");
        problem2.setTemplateCode("public class Solution {\n" +
                "    public String reverseStr(String str) {\n" +
                "\n" +
                "    }\n" +
                "}");
        problem2.setTestCode(
                "public static void main(String[] args) {\n" +
                        "    Solution s = new Solution();\n" +
                        "    String str = \"abcdefghaaa\";\n" +
                        "    String rstr = \"aaahgfedcba\";\n" +
                        "    if (s.reverseStr(str).equals(rstr)) {\n" +
                        "        System.out.println(\"Test OK\");\n" +
                        "    } else {\n" +
                        "        System.out.println(\"Test Failed\");\n" +
                        "    }\n" +
                        "}\n");

        ProblemDAO problemDAO2 = new ProblemDAO();
        problemDAO2.insert(problem2);
        System.out.println("Insert OK");



//        //2.测试selectAll
//        ProblemDAO problemDAO = new ProblemDAO();
//        List<Problem> problems = problemDAO.selectAll();
//        System.out.println("selectAll:" + problems);
//
//        //3.测试selectOne
//        ProblemDAO problemDAO = new ProblemDAO();
//        Problem problem = problemDAO.selectOne(1);
//        System.out.println(problem);

    }


}
