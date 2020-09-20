package common;

import java.io.*;

/**
 * 5、
 * 工具类，方便读写文件
 *
 * @author haozhang
 * @date 2020/09/04
 */
public class FileUtil {
    /**
     * 读文件：一下把整个文件内容读到String中
     * @param filePath 表示要从哪读数据
     * @return
     */
    public static String readFile(String filePath) {
        //当涉及到编译错误，标准输出结果等文件内容都是文本文件。此处使用字符流方式来实现
        //try() ()中的内容是可以被自动关闭的
        try(FileReader fileReader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader)) {
            StringBuilder sb = new StringBuilder();
            //按行读取文件内容
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写文件：一下把String的内容写到指定文件中
     * @param filePath 表示要把数据写到哪个文件中
     * @param content 表示要写的文件内容
     */
    public static void writeFile(String filePath, String content) {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
