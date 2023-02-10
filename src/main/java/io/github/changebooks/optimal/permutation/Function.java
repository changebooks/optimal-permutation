package io.github.changebooks.optimal.permutation;

/**
 * 计算过程
 *
 * @author changebooks@qq.com
 */
@FunctionalInterface
public interface Function<T, R extends Comparable<R>> {

    /**
     * 按下标排列（indexes），重排数组（values）的元素
     * 按重排后的数组（ranged-values），计算结果（data）
     *
     * @param values         参与计算的数组
     * @param indexes        数组下标（values-index）的排列
     * @param startInclusive 下标排列（indexes）中，开始参与计算元素的下标（包含）
     * @param endExclusive   下标排列（indexes）中，结束参与计算元素的下标（不包含）
     * @param prepareR       预计算结果，如：下标排列（indexes）中前3个下标的计算结果，缓存预计算结果，减少计算次数
     * @return 计算结果
     * @throws StopException 终止计算（不计入本次计算结果）
     */
    R apply(T[] values, int[] indexes, int startInclusive, int endExclusive, R prepareR);

}
