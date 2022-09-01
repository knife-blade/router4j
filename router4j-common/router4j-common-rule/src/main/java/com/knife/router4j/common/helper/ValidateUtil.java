package com.knife.router4j.common.helper;

/**
 * 校验工具
 */
public class ValidateUtil {

    /**
     * 校验实例地址是否有效。正确格式：127.0.0.1:8080
     * @param instanceAddress 实例地址
     */
    public static void instanceAddressValid(String instanceAddress) {
        char ch = ':';
        int frequency = 0;
        int firstIndex = 0;
        for(int i = 0; i < instanceAddress.length(); i++) {
            if(ch == instanceAddress.charAt(i)) {
                if (frequency == 0) {
                    firstIndex = i;
                }
                ++frequency;
            }
        }

        if (frequency != 1
            && ((firstIndex == 0)
                || (firstIndex == instanceAddress.length() - 1))) {
            throw new RuntimeException("实例地址不符合格式。" +
                    "格式示例：127.0.0.1:8080");
        }
    }

    /**
     * 校验应用名字是否有效。不能包含冒号
     * @param applicationName 应用名字
     */
    public static void applicationNameValid(String applicationName) {
        char ch = ':';
        int frequency = 0;
        for(int i = 0; i < applicationName.length(); i++) {
            if(ch == applicationName.charAt(i)) {
                ++frequency;
            }
        }

        if (frequency >= 1) {
            throw new RuntimeException("实例地址不符合格式。" +
                    "不能包含冒号（:）");
        }
    }

}
