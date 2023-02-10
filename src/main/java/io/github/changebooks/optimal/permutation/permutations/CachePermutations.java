package io.github.changebooks.optimal.permutation.permutations;

import io.github.changebooks.optimal.permutation.Permutations;
import io.github.changebooks.optimal.permutation.Preconditions;

/**
 * 预缓存，优化算法
 *
 * @author changebooks@qq.com
 */
public class CachePermutations implements Permutations {
    /**
     * 生成数组下标的全排列
     */
    private final Permutations permutations;

    /**
     * 最大排列长度（包含）
     */
    private final int maxsize;

    /**
     * 缓存的全排列
     * 如，maxsize = 3
     * 将生成4个全排列，如下，
     * <pre>
     * 第一个全排列的下标：0
     *          指定长度：0
     *    生成的数组下标：[]
     *  数组下标的全排列：[]
     * </pre>
     * <pre>
     * 第二个全排列的下标：1
     *          指定长度：1
     *    生成的数组下标：[0]
     *  数组下标的全排列：[0]
     * </pre>
     * <pre>
     * 第三个全排列的下标：2
     *          指定长度：2
     *    生成的数组下标：[0, 1]
     *  数组下标的全排列：[0, 1], [1, 0]
     * </pre>
     * <pre>
     * 第四个全排列的下标：3
     *          指定长度：3
     *    生成的数组下标：[0, 1, 2]
     *  数组下标的全排列：[0, 1, 2], [0, 2, 1], [2, 0, 1], [2, 1, 0], [1, 0, 2], [1, 2, 0]
     * </pre>
     */
    private final int[][][] elements;

    /**
     * <pre>
     * (maxsize = 1) : (elements = [ [], [[0]] ])
     * (maxsize = 2) : (elements = [ [], [[0]], [[0, 1], [1, 0]] ])
     * (maxsize = 3) : (elements = [ [], [[0]], [[0, 1], [1, 0]], [[0, 1, 2], [0, 2, 1], [2, 0, 1], [2, 1, 0], [1, 0, 2], [1, 2, 0]] ])
     * ... ...
     * </pre>
     *
     * @param p       生成数组下标的全排列
     * @param maxsize 最大排列长度（包含）
     */
    public CachePermutations(Permutations p, int maxsize) {
        Preconditions.checkNonNull(p, "permutations can't be null");
        Preconditions.checkArgument(maxsize > 0, "maxsize must be greater than 0");

        this.permutations = p;
        this.maxsize = maxsize;
        this.elements = new int[maxsize + 1][][];
        initialize();
    }

    /**
     * 初始化缓存
     */
    private void initialize() {
        for (int size = 0; size <= maxsize; size++) {
            this.elements[size] = permutations.compute(size);
        }
    }

    @Override
    public int[][] compute(int size) {
        Preconditions.checkArgument(size >= 0 && size <= maxsize,
                String.format("unsupported size: %d, supported range: [0-%d]", size, maxsize));

        return elements[size];
    }

    public Permutations getPermutations() {
        return permutations;
    }

    public int getMaxsize() {
        return maxsize;
    }

    public int[][][] getElements() {
        return elements;
    }

}
