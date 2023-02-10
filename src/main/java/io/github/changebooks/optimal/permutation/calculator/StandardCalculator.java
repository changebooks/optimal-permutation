package io.github.changebooks.optimal.permutation.calculator;

import io.github.changebooks.optimal.permutation.*;

import java.util.Arrays;

/**
 * 标准算法
 * 数组长度：size
 * 计算次数：(size * size!)
 *
 * @author changebooks@qq.com
 */
public class StandardCalculator<T, R extends Comparable<R>> implements Calculator<T, R> {
    /**
     * 生成数组下标的全排列
     */
    private final Permutations permutations;

    public StandardCalculator(Permutations p) {
        Preconditions.checkNonNull(p, "permutations can't be null");
        this.permutations = p;
    }

    @Override
    public Result<R> calculate(T[] values, Function<T, R> compute) {
        Preconditions.checkNonNull(values, "values can't be null");
        Preconditions.checkNonNull(compute, "compute can't be null");

        // 数组下标（values-index）的全排列
        int[][] permutations = getPermutations().compute(values.length);
        Preconditions.checkNonNull(permutations, "index permutations can't be null");

        // 最优结果
        Result<R> result = null;

        for (int[] indexes : permutations) {
            Preconditions.checkNonNull(indexes, "indexes can't be null");

            // 本次计算结果
            R data;

            try {
                // 按下标排列（indexes），重排数组（values）的所有元素，并计算结果（data）
                data = compute.apply(values, indexes, 0, indexes.length, null);
            } catch (StopException tr) {
                break;
            }

            // 比较“之前的最优结果（result）”和“本次计算结果（data）”，选“新的最优结果”
            result = chooseResult(result, data, indexes);
        }

        return result;
    }

    /**
     * 比较“之前的最优结果（result）”和“本次计算结果（data）”，选“新的最优结果”
     *
     * @param result  之前的最优结果
     * @param data    本次计算结果
     * @param indexes 本次计算的下标排列
     * @return 新的最优结果
     */
    public Result<R> chooseResult(Result<R> result, R data, int[] indexes) {
        if (data == null || indexes == null) {
            // 选“之前的最优结果”
            return result;
        }

        if (result == null) {
            result = new Result<>();
        }

        if (chooseFirst(data, result.getData())) {
            // 选“本次计算结果”
            result.setData(data);
            result.setIndexes(Arrays.copyOf(indexes, indexes.length));
        }

        return result;
    }

    /**
     * 比较2个结果，判断第1个结果是否比第2个结果更优
     *
     * @param first  第1个结果
     * @param second 第2个结果
     * @return 选第1个结果吗？true-选第1个结果；false-选第2个结果
     */
    public boolean chooseFirst(R first, R second) {
        if (first == null) {
            // 选第2个结果
            return false;
        }

        if (second == null) {
            // 选第1个结果
            return true;
        }

        // 比较2个结果，选1个
        return first.compareTo(second) < 0;
    }

    public Permutations getPermutations() {
        return permutations;
    }

}
