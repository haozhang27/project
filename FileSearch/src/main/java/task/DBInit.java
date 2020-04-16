package task;

import util.DBUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

/**
 * 1.初始化数据库
 * 2.并且读取sql文件
 * 3.执行sql语句来初始化表
 *
 * @author haozhang
 * @date 2020/03/01
 */
public class DBInit {

    /**
     * 读取数据库文件
     * @return
     */
    public static String[] readSQL() {
        try {
            //通过ClassLoader就获取流
            InputStream is = DBInit.class.getClassLoader().
                    getResourceAsStream("init.sql");

            //字节流转换为字符流:需要通过字节和字符转换流来操作
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {

                //去掉 -- 注释的代码
                if (line.contains("--")) {
                    line = line.substring(0, line.indexOf("--"));
                }
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            return sqls;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取sql文件出错！", e);
        }
    }

    /**
     * 初始化数据库
     */
    public static void init() {
        //数据库jdbc操作：sql语句执行
        Connection connection = null;
        Statement statement = null;
        try {
            //1.建立数据库连接Connection
            connection = DBUtil.getConnection();

            //2.创建sql语句执行对象Statement
            statement = connection.createStatement();
            String[] sqls = readSQL();
            for (String sql : sqls) {

                //3.执行sql语句
                statement.executeUpdate(sql);
            }

            //4.如果是查询操作，获取结果集ResultSet并处理结果集
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库表操作失败！", e);
        } finally {
            //5.释放资源
            DBUtil.close(connection, statement);
        }
    }
}
