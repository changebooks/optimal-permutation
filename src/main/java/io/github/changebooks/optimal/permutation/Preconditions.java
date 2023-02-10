package io.github.changebooks.optimal.permutation;

/**
 * 前提条件
 *
 * @author changebooks@qq.com
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * 非空对象？
     *
     * @param obj     校验对象
     * @param message 错误提示
     * @throws NullPointerException if the object is null
     */
    public static void checkNonNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 参数正确？
     *
     * @param expression 表达式
     * @param message    错误提示
     * @throws IllegalArgumentException if the expression is false
     */
    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}
