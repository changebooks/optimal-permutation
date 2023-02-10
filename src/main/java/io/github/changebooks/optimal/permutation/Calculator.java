package io.github.changebooks.optimal.permutation;

/**
 * 计算数组的最优排列
 *
 * @author changebooks@qq.com
 */
public interface Calculator<T, R extends Comparable<R>> {

    /**
     * 全排列数组
     * 计算所有排列的结果，从中选最优结果
     *
     * @param values  参与计算的数组
     * @param compute 计算过程
     * @return 最优结果和下标排列
     */
    Result<R> calculate(T[] values, Function<T, R> compute);

}
