package com.kbslan.esl.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 15:34
 */
public class MiscUtils {

    /**
     * 基站 扫描码 转换为 MAC
     * 汉朔接口需要转换mac地址格式为:分隔
     *
     * @param originAp 原始基站mac
     * @return 转换后的基站mac
     */
    public static String formatApBarcode(String originAp) {
        return StringUtils.isBlank(originAp) ? originAp : originAp.trim().replace("-", ":");
    }

    /**
     * 还原基站mac地址
     *
     * @param apMac 基站mac
     * @return 还原后的基站mac
     */
    public static String convertApBarcode(String apMac) {
        return StringUtils.isBlank(apMac) ? apMac : apMac.trim().replace(":", "-");
    }

    /**
     * 价签设备 格式化条码
     *
     * @param originPriceTagId 原始价签ID
     * @return 格式化后的价签ID
     */
    public static String formatOriginPriceTagId(String originPriceTagId) {
        //10.8寸生鲜价签
        if (originPriceTagId.length() == 6) {
            return String.format("%s-%s-%s-99", originPriceTagId.substring(0, 2), originPriceTagId.substring(2, 4), originPriceTagId.substring(4, 6));
        }
        String rawValue = originPriceTagId.substring(originPriceTagId.length() - 10);
        String hex = Long.toHexString(Long.parseLong(rawValue)).toUpperCase();
        return String.format("%s-%s-%s-%s", hex.substring(0, 2), hex.substring(2, 4), hex.substring(4, 6), hex.substring(6, 8));
    }

    /**
     * 价签ID 16进制转10进制
     *
     * @param priceTagId 价签ID
     * @return 10进制价签ID
     */
    public static String convertPriceTagId(String priceTagId) {
        if (StringUtils.isBlank(priceTagId)) {
            return priceTagId;
        }
        if (priceTagId.contains("-")) {
            String hex = priceTagId.replaceAll("-", "").trim();
            return String.format("%010d", new BigInteger(hex, 16));
        }
        return priceTagId;
    }

}
