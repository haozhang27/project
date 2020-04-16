package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import task.DBInit;

import javax.sql.DataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 获取数据库连接 数据库资源
 *
 * @author haozhang
 * @date 2019/09/19
 */
public class DBUtil {

    private static volatile DataSource DATA_SOURCE;

    /**
     * 提供获取数据库连接池的功能：
     * 使用单例模式(多线程安全版本)
     *
     * @return
     */
    private static DataSource getDataSource() {
        //目的：提高效率
        if (DATA_SOURCE == null) {

            //刚开始所有进入这行代码的线程，DATA_SOURCE对象都是null
                //可能是第一个进去的线程，这时候DATA_SOURCE对象都是null
                //可能时第一个线程之后的线程进入并执行
            synchronized (DBUtil.class) {
                if (DATA_SOURCE == null) {
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATA_PATTERN);

                    //初始化操作，使用volatile关键字禁止指令重排序，建立内存屏障
                    DATA_SOURCE = new SQLiteDataSource(config);
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }
            }
        }

        return DATA_SOURCE;
    }

    /**
     * 获取sqlite数据库文件url的方法
     * @return
     */
    private static String getUrl() {
        try {
            //获取target编译文件夹的路径
            //通过classLoader.getResource()获取
            //默认的根路径为编译文件夹路径(target/classes)
            URL classesURL = DBInit.class.getClassLoader().getResource("./");

            //获取target/classes文件夹的父目录路径
            String dir = new File(classesURL.getPath()).getParent();
            String url = "jdbc:sqlite://" + dir + File.separator + "FileSearch.db";

            //new SqliteDateSource(),把这个对象的url设置进去，才会创建这个文件，如果文件
            //已经存在就会读取这个文件
            url = URLDecoder.decode(url, "UTF-8");
            System.out.println("获取数据库文件路径： " + url);

            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库文件路径失败", e);
        }
    }

    /**
     * 提供获取数据库连接的方法：
     * 从数据库连接池DataSource.getConnection()来获取数据库连接
     * @return
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());
    }


    public static void close(Connection connection, Statement statement) {
        close(connection, statement, null);
    }

    /**
     * 释放数据库资源
     * @param connection 数据库连接
     * @param statement sql执行对象
     * @param resultSet 结果集
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放资源错误", e);
        }
    }
}
