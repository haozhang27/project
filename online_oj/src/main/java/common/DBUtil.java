package common;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 6、
 * 和数据库建立连接，进一步操作数据库
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/online_oj?characterEncoding=utf8&useSSL=false";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "";

    private static DataSource dataSource = null;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource)(dataSource)).setURL(URL);
                    ((MysqlDataSource)(dataSource)).setUser(USERNAME);
                    ((MysqlDataSource)(dataSource)).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            //内置了数据库连接池
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet result) {
        try {
            if (result != null) {
                result.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
