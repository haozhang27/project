package task;

import app.FileMeta;
import util.DBUtil;
import util.Util;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件信息保存
 *
 * @author haozhang
 * @date 2020/03/01
 */
public class FileSave implements ScanCallback {
    /**
     * 将文件夹下一级子文件和子文件夹保存到数据库
     * 获取本地目录下一级子文件和子文件夹
     * 集合框架中使用自定义类型，判断是否某个对象在集合存在：比对两个集合中的元素
     * @param dir
     */
    @Override
    public void callback(File dir) {
        File[] children = dir.listFiles();
        List<FileMeta> locals = new ArrayList<>();
        if (children != null) {
            for (File child : children) {
                locals.add(new FileMeta(child));
            }
        }

        //获取数据库保存的dir目录的下一级子文件和子文件夹
        List<FileMeta> metas = query(dir);

        //本地有，数据库没有，做插入
        for (FileMeta meta : metas) {
            if (!locals.contains(meta)) {
                //删除meta
                //  1.删除meta信息本身
                //  2.如果是目录，还要将meta所有的子文件，子文件夹都删除
                delete(meta);
            }
        }

        //数据库有，本地没有，做删除
        for (FileMeta meta : locals) {
            if (!metas.contains(meta)) {
                save(meta);
            }
        }

    }

    /**
     * 查询数据库是否包含dir文件夹
     * @param dir
     * @return
     */
    private List<FileMeta> query(File dir) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FileMeta> metas = new ArrayList<>();
        try {
            //1.创建数据库连接
            connection = DBUtil.getConnection();
            String sql = "select name, path, is_directory, size, last_modified" +
                    " from file_meta where path = ?";

            //2.创建jdbc操作命令对象statement
            ps = connection.prepareStatement(sql);
            ps.setString(1, dir.getPath());

            //3.执行sql语句
            rs = ps.executeQuery();

            //4.处理结果集 ResultSet
            while (rs.next()) {
                String name = rs.getString("name");
                String path = rs.getString("path");
                Boolean isDirectory = rs.getBoolean("is_directory");
                Long size = rs.getLong("size");
                Timestamp lastModified = rs.getTimestamp("last_modified");
                FileMeta meta = new FileMeta(name, path, isDirectory, size,
                        new java.util.Date(lastModified.getTime()));
                System.out.printf("查询文件信息：name = %s, path = %s, " +
                        "is_directory = %s, size = %s, last_modified = %s\n ",
                        name, path, String.valueOf(isDirectory), String.valueOf(size),
                        Util.parseDate(new java.util.Date(lastModified.getTime())));
                metas.add(meta);
            }

            return metas;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询文件信息出错, 检查sql查询语句", e);
        } finally {
            //5.释放资源
            DBUtil.close(connection, ps, rs);
        }
    }

    /**
     * 文件信息保存到数据库
     * @param meta
     */
    private void save(FileMeta meta) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1.获取数据库连接
            connection = DBUtil.getConnection();
            String sql = "insert into file_meta" +
                    " (name, path, is_directory, size, last_modified, pinyin, pinyin_first)" +
                    " values(?, ?, ?, ?, ?, ?, ?)";

            //2.获取sql操作命令对象
            statement = connection.prepareStatement(sql);
            statement.setString(1, meta.getName());
            statement.setString(2, meta.getPath());
            statement.setBoolean(3, meta.isDirectory());
            statement.setLong(4, meta.getSize());
            statement.setString(5, meta.getLastModifiedText());
            statement.setString(6, meta.getPinyin());
            statement.setString(7, meta.getPinyinFirst());

            System.out.printf("insert name=%s, path=%s\n",
                    meta.getName(), meta.getPath());

            //3.执行sql
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("文件保存失败,检查sql insert语句", e);
        } finally {
            //4.释放资源
            DBUtil.close(connection, statement);
        }
    }

    /**
     * 删除meta
     *  1.删除meta信息本身
     *  2.如果是目录，还要将meta所有的子文件，子文件夹都删除
     * @param meta
     */
    public void delete(FileMeta meta) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "delete from file_meta where" +

                    //输出文件自己本身
                    " (name=? and path=? and is_directory=?)";

            //如果是文件夹，还要删除文件夹的子文件夹和文件夹
            if (meta.isDirectory()) {

                //匹配数据库文件夹的儿子
                sql += " or path=?" +

                        //匹配数据库文件夹的孙后辈
                        " or path like ?";
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1, meta.getName());
            ps.setString(2, meta.getPath());
            ps.setBoolean(3, meta.isDirectory());
            if (meta.isDirectory()) {
                ps.setString(4, meta.getPath()
                        + File.separator + meta.getName());
                ps.setString(5, meta.getPath() +
                        File.separator + meta.getName() + File.separator);
            }

            System.out.println("删除文件信息，dir=%s\n" +
                    meta.getPath() + File.separator + meta.getName());
            ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除文件信息出错，检查delete语句", e);
        } finally {
            DBUtil.close(connection, ps);
        }
    }
}
