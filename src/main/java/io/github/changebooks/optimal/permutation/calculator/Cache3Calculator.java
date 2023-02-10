package io.github.changebooks.optimal.permutation.calculator;

import io.github.changebooks.optimal.permutation.Permutations;
import io.github.changebooks.optimal.permutation.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * 预缓存，优化算法
 * 缓存前3个元素的计算结果
 * <pre>
 * 数组长度：7
 * 缓存元素：3
 * 计算次数：(3 * 7!) / ((7 - 3)!) + (7 - 3) * 7! = 20790
 * </pre>
 * <pre>
 * 数组长度：6
 * 缓存元素：3
 * 计算次数：(3 * 6!) / ((6 - 3)!) + (6 - 3) * 6! = 2520
 * </pre>
 *
 * @author changebooks@qq.com
 */
public class Cache3Calculator<T, R extends Comparable<R>> extends AbstractCacheCalculator<T, R> {
    /**
     * 预缓存前3个元素
     */
    public static final int CACHE_SIZE = 3;

    /**
     * 最大支持的数组长度（包含）
     */
    public static final int SUPPORTED_SIZE = 7;

    public Cache3Calculator(Permutations p) {
        super(p);
    }

    @Override
    public Map<Integer, R> newCache() {
        // cacheSize = 3
        // supportedSize = 7
        // Map capacity = (cacheSize * supportedSize!) / ((supportedSize - cacheSize)!)
        // Map capacity = (3 * 7!) / ((7 - 3)!) = 630
        return new HashMap<>(640);
    }

    @Override
    public int getCacheKey(int[] indexes) {
        Preconditions.checkNonNull(indexes, "indexes can't be null");
        Preconditions.checkArgument(indexes.length > 3, "indexes size must be greater than 3");
        return (indexes[0] << 6) + (indexes[1] << 3) + (indexes[2]);
    }

    @Override
    public int getCacheSize() {
        return CACHE_SIZE;
    }

    @Override
    public int getSupportedSize() {
        return SUPPORTED_SIZE;
    }

}
