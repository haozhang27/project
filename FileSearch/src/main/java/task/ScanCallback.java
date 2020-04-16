package task;

import java.io.File;

/**
 * 文件扫描回调接口
 *
 * @author haozhang
 * @date 2020/03/10
 */
public interface ScanCallback {

    /**
     * 对文件夹扫描任务进行回调：处理文件夹，将文件夹下一级的子文件夹保存到数据库
     * @param dir
     */
    void callback(File dir);
}
