package app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import util.PinyinUtil;
import util.Util;

import java.io.File;
import java.util.Date;

/**
 * 通过文件设置属性
 *
 *
 * 如果没有下载lombok插件的话会飘红，要么自己装一下，
 * 要么就自己生成Getter Setter ToString方法
 *
 * @author haozhang
 * @date 2020/03/01
 */
@Getter
@Setter
@ToString
public class FileMeta {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件所在父目录路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件上次修改时间
     */
    private Date lastModified;

    /**
     * 是否是文件夹
     */
    private boolean isDirectory;

    /**
     * 给客户端空间使用，和app.fxml中定义的名称要一致
     */
    private String sizeText;

    /**
     * 和app.fxml中定义的名称要一致
     */
    private String lastModifiedText;

    /**
     * 文件名拼音
     */
    private String pinyin;

    /**
     * 文件名拼音首字母
     */
    private String pinyinFirst;


    /**
     * 通过文件设置属性
     * @param file
     */
    public FileMeta(File file) {
        this(file.getName(), file.getParent(), file.isDirectory(),
                file.length(), new Date(file.lastModified()));
    }

    /**
     * 通过数据库获取的数据设置FileMeta
     *
     */
    public FileMeta(String name, String path, Boolean isDirectory,
                    long size, Date lastModified) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.lastModified = lastModified;

        if (PinyinUtil.containsChinese(name)) {
            String[] pinyinSpell = PinyinUtil.get(name);
            pinyin = pinyinSpell[0];
            pinyinFirst = pinyinSpell[1];
        }

        //客户端表格控件文件大小，文件上次修改时间设置
        sizeText = Util.parseSize(size);
        lastModifiedText = Util.parseDate(lastModified);
    }
}
