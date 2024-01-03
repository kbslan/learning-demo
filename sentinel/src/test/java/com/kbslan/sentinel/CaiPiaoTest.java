package com.kbslan.sentinel;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * https://www.zhcw.com/kjxx/ssq/
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/18 16:46
 */
public class CaiPiaoTest {

    @Test
    @SneakyThrows
    public void test() {
        String[] fileArray = new String[]{"1.txt", "2.txt", "3.txt", "4.txt"};
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < fileArray.length; i++) {
            File jsonFile = ResourceUtils.getFile("classpath:" + fileArray[i]);
            String content = FileUtils.readFileToString(jsonFile);
            DoubleColor doubleColor = JSON.parseObject(content, DoubleColor.class);
            System.out.println(doubleColor);
            list.addAll(doubleColor.getData());
        }
        System.out.println(list.size());
        Map<String, Integer> front = new HashMap<>();
        Map<String, Integer> last = new HashMap<>();
        Map<String, Integer> all = new HashMap<>();
        for (Item item : list) {
            String frontWinningNum = item.getFrontWinningNum();
            String[] split = frontWinningNum.split(" ");
            for (String s : split) {
                Integer frontCount = front.get(s);
                if (frontCount == null) {
                    frontCount = 0;
                }
                frontCount++;
                front.put(s, frontCount);
                Integer allCount = all.get(s);
                if (allCount == null) {
                    allCount = 0;
                }
                allCount++;
                all.put(s, allCount);
            }
            String backWinningNum = item.getBackWinningNum();
            Integer lastCount = last.get(backWinningNum);
            if (lastCount==null) {
                lastCount = 0;
            }
            lastCount++;
            last.put(backWinningNum, lastCount);
            Integer allCount = all.get(backWinningNum);
            if (allCount == null) {
                allCount =0;
            }
            allCount++;
            all.put(backWinningNum, allCount);
            System.out.println(item.getOpenTime() + " " + item.getIssue() + " " + item.getFrontWinningNum() + " " + item.getBackWinningNum());
        }

        System.out.println("front:" + front);



        front.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
        System.out.println("last:" + last);
        last.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
        System.out.println("all:" + all);
        all.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }
}
