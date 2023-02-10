package io.github.changebooks.optimal.permutation.calculator;

import io.github.changebooks.optimal.permutation.*;

import java.util.Map;

/**
 * 预缓存，优化算法
 * 缓存前n个元素的计算结果
 * 数组长度：size
 * 缓存元素：cacheSize
 * 计算次数：(cacheSize * size!) / ((size - cacheSize)!) + (size - cacheSize) * size!
 *
 * @author changebooks@qq.com
 */
public abstract class AbstractCacheCalculator<T, R extends Comparable<R>> extends StandardCalculator<T, R> implements Calculator<T, R> {

    public AbstractCacheCalculator(Permutations p) {
        super(p);
    }

    @Override
    public Result<R> calculate(T[] values, Function<T, R> compute) {
        Preconditions.checkNonNull(values, "values can't be null");
        Preconditions.checkNonNull(compute, "compute can't be null");

        int size = values.length;
        int supportedSize = getSupportedSize();
        Preconditions.checkArgument(size <= supportedSize,
                String.format("unsupported size: %d, supported maxsize: %d", size, supportedSize));

        int cacheSize = getCacheSize();
        if (size <= cacheSize) {
            // 不满足预计算条件
            // 用标准算法，不预缓存
            return super.calculate(values, compute);
        }

        // 数组下标（values-index）的全排列
        int[][] permutations = getPermutations().compute(size);
        Preconditions.checkNonNull(permutations, "index permutations can't be null");

        // 最优结果
        Result<R> result = null;

        // 预缓存结果，prepareKey : prepareResult
        Map<Integer, R> cache = newCache();

        for (int[] indexes : permutations) {
            Preconditions.checkNonNull(indexes, "indexes can't be null");

            // 缓存key
            int prepareK = getCacheKey(indexes);
            // 预计算结果
            R prepareR = cache.get(prepareK);
            if (prepareR == null) {
                try {
                    // 按下标排列（indexes），重排数组（values）中[0, cacheSize)元素，并计算[0, cacheSize)元素结果（prepareR）
                    prepareR = compute.apply(values, indexes, 0, cacheSize, null);
                    cache.put(prepareK, prepareR);
                } catch (StopException tr) {
                    break;
                }
            }

            // 本次计算结果
            R data;

            try {
                // 按下标排列（indexes），重排数组（values）中[cacheSize, size)元素，并计算[cacheSize, size)元素结果（data）
                data = compute.apply(values, indexes, cacheSize, size, prepareR);
            } catch (StopException tr) {
                break;
            }

            // 比较“之前的最优结果（result）”和“本次计算结果（data）”，选“新的最优结果”
            result = chooseResult(result, data, indexes);
        }

        return result;
    }

    /**
     * 初始化缓存
     * Map capacity = (cacheSize * supportedSize!) / ((supportedSize - cacheSize)!)
     *
     * @return 空缓存
     */
    public abstract Map<Integer, R> newCache();

    /**
     * 生成缓存key
     *
     * @param indexes 下标排列
     * @return 缓存key
     */
    public abstract int getCacheKey(int[] indexes);

    /**
     * 预缓存前n个元素
     *
     * @return 预缓存的元素数量
     */
    public abstract int getCacheSize();

    /**
     * 最大支持的数组长度（包含）
     *
     * @return 参与计算的数组长度
     */
    public abstract int getSupportedSize();

}
