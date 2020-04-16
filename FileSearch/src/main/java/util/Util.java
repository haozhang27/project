package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设置日期格式 解析文件大小
 *
 * @author haozhang
 * @date 2020/03/01
 */
public class Util {

    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 解析文件大小为中文描述
     * @param size
     * @return
     */
    public static String parseSize(long size) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int index = 0;
        while (size > 1024 && index < units.length - 1) {
            size /= 1024;
            index++;
        }
        return size + units[index];
    }

    /**
     * 解析日期为中文日期描述
     * @param lastModified
     * @return
     */
    public static String parseDate(Date lastModified) {
        return new SimpleDateFormat(DATA_PATTERN).format(lastModified);
    }
}
