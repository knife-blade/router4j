package com.suchtool.router4j.server.common.util;

/**
 * 异常工具类
 */
public class ThrowableUtil {
    /**
     * 获取以指定包名为前缀的堆栈信息
     *
     * @param e 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable e) {
        StringBuilder s = new StringBuilder().append(e);
        for (StackTraceElement traceElement : e.getStackTrace()) {
            s.append("    at ").append(traceElement);
        }
        return s.toString();
    }

    /**
     * 获取最后n条堆栈信息
     *
     * @param e 异常对象
     * @param n 最后n行
     * @return 错误信息
     */
    public static String getLastStackTrace(Throwable e, Integer n) {
        Integer lineNumber = n;
        if (lineNumber == null) {
            lineNumber = 10;
        }

        StringBuilder s = new StringBuilder().append(e);
        int i = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            s.append("    at ").append(traceElement);
            if (i >= lineNumber) {
                break;
            }
            i++;
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的所有堆栈信息
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getStackTraceByPackage(Throwable e, String packagePrefix) {
        StringBuilder s = new StringBuilder().append(e);

        for (StackTraceElement traceElement : e.getStackTrace()) {
            // 如果是指定的包
            if (traceElement.getClassName().startsWith(packagePrefix)) {
                s.append("    at ").append(traceElement);
            }
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的最后n条堆栈信息
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getLastStackTraceByPackage(Throwable e, String packagePrefix, Integer n) {
        Integer lineNumber = n;
        if (lineNumber == null) {
            lineNumber = 10;
        }

        StringBuilder s = new StringBuilder().append(e);
        int i = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            // 如果是指定的包
            if (traceElement.getClassName().startsWith(packagePrefix)) {
                s.append("    at ").append(traceElement);
            }
            if (i >= lineNumber) {
                break;
            }
            i++;
        }
        return s.toString();
    }

    /**
     * 获取最后n条简略堆栈信息（方法名加行号）
     *
     * @param e 异常对象
     * @param n 最后n行
     * @return 错误信息
     */
    public static String getBriefLastStackTrace(Throwable e, Integer n) {
        Integer lineNumber = n;
        if (lineNumber == null) {
            lineNumber = 10;
        }

        StringBuilder s = new StringBuilder().append(e);
        int i = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            String info = traceElement.getMethodName() + ":" + traceElement.getLineNumber();

            s.append("    at ").append(info);
            if (i >= lineNumber) {
                break;
            }
            i++;
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的所有简略堆栈信息（方法名+包名）
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getBriefStackTraceByPackage(Throwable e, String packagePrefix) {
        StringBuilder s = new StringBuilder().append(e);

        for (StackTraceElement traceElement : e.getStackTrace()) {
            // 如果是指定的包
            if (traceElement.getClassName().startsWith(packagePrefix)) {
                String info = traceElement.getMethodName() + ":" + traceElement.getLineNumber();
                s.append("    at ").append(info);
            }
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的最后n行简略堆栈信息（方法名+包名）
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getLastBriefStackTraceByPackage(Throwable e, String packagePrefix, Integer n) {
        Integer lineNumber = n;
        if (lineNumber == null) {
            lineNumber = 10;
        }

        StringBuilder s = new StringBuilder().append(e);
        int i = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            // 如果是指定的包
            if (traceElement.getClassName().startsWith(packagePrefix)) {
                String info = traceElement.getMethodName() + ":" + traceElement.getLineNumber();
                s.append("    at ").append(info);
            }
            if (i >= lineNumber) {
                break;
            }
            i++;
        }
        return s.toString();
    }
}
