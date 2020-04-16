package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 通过汉字获取拼音拼写 和 拼音首字母拼写
 *
 * @author haozhang
 * @date 2020/03/01
 */
public class PinyinUtil {

    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    /**
     * 汉语拼音格式化
     */
    private static final HanyuPinyinOutputFormat FORMAT =
            new HanyuPinyinOutputFormat();

    static {
        //设置拼音小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置带V字符，如 绿Lv
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public static boolean containsChinese(String name) {
        return name.matches(".*" + CHINESE_PATTERN + ".*");
    }

    /**
     * 通过文件名获取  全拼+拼音首字母
     * @param name 文件名称
     * @return 拼音全拼字符串 + 拼音首字母字符串  数组
     */
    public static String[] get(String name) {
        String[] result = new String[2];

        //全拼
        StringBuilder pinyin = new StringBuilder();

        //拼音首字母
        StringBuilder pinyinFirst = new StringBuilder();

        //中  和
        for (char c : name.toCharArray()) {
            try {
                //[zhong]  [he, hu, huo ...]
                String[] pinyinSpell = PinyinHelper.
                        toHanyuPinyinStringArray(c, FORMAT);
                if (pinyinSpell == null || pinyinSpell.length == 0) {
                    pinyin.append(c);
                    pinyinFirst.append(c);
                } else {
                    //全拼：和 --> he
                    pinyin.append(pinyinSpell[0]);

                    //拼音首字母：和 --> h
                    pinyinFirst.append(pinyinSpell[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                pinyin.append(c);
                pinyinFirst.append(c);
            }
        }
        result[0] = pinyin.toString();
        result[1] = pinyinFirst.toString();
        return result;
    }

    /**
     * 通过汉字枚举所有拼音组合
     * 和[he, hu, huo, ...]长[zhang, chang]和[he, hu, huo, ...]
     * hezhanghe/hezhanghu/hezhanghuo/...
     * @param name 文件名称
     * @param isFullSpell true表示全拼      false取拼音首字母
     * @return 包含多音字的字符串二维数组：
     * [[he, hu, huo,...], [zhang, chang], [he, hu, huo, ...]]
     */
    public static String[][] get(String name, boolean isFullSpell) {
        char[] chars = name.toCharArray();
        String[][] result = new String[chars.length][];
        for (int i = 0; i < chars.length; i++) {
            try {
                //去除相同音调：只[zhi, zhi]
                String[] pinyinSpell = PinyinHelper.
                        toHanyuPinyinStringArray(chars[i], FORMAT);

                //   1:[]
                if (pinyinSpell == null || pinyinSpell.length == 0) {
                    result[i] = new String[]{String.valueOf(chars[i])};
                } else {
                    result[i] = wipOffRepetition(pinyinSpell, isFullSpell);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                result[i] = new String[]{String.valueOf(chars[i])};
            }
        }
        return result;
    }

    /**
     * 字符串数组去重操作
     * @param arr
     * @param isFullSpell
     * @return
     */
    public static String[] wipOffRepetition(String[] arr, boolean isFullSpell) {
        Set<String> set = new HashSet<>();
        for (String s : arr) {
            if (isFullSpell) {
                set.add(s);
            } else {
                set.add(String.valueOf(s.charAt(0)));
            }
        }

        return set.toArray(new String[set.size()]);
    }

    /**
     * 枚举汉字拼音的所有组合
     * 每个中文字符返回拼音时字符串数组，每两个字符串数组合并为一个字符串数组之后以此类推
     * @param pinyinArray
     * @return
     */
    public static String[] compose(String[][] pinyinArray) {
        if (pinyinArray == null || pinyinArray.length == 0) {
            return null;
        } else if (pinyinArray.length == 1) {
            return pinyinArray[0];
        } else {
            for (int i = 1; i < pinyinArray.length; i++) {

                //这里只需要不断更新第一组数据就可以
                pinyinArray[0] = compose(pinyinArray[0], pinyinArray[i]);
            }
            return pinyinArray[0];
        }
    }

    /**
     * 合并两个拼音数组为1个
     * @param pinyins1 [he, hu, huo, ...]
     * @param pinyins2 [zhang, chang]
     * @return hezhang,hechang,huzhang,huchang,huozhang,huochang,...
     */
    public static String[] compose(String[] pinyins1, String[] pinyins2) {
        String[] result = new String[pinyins1.length * pinyins2.length];
        for (int i = 0; i < pinyins1.length; i++) {
            for (int j = 0; j < pinyins2.length; j++) {
                result[i * pinyins2.length + j] = pinyins1[i] + pinyins2[j];
            }
        }
        return result;
    }
}





















