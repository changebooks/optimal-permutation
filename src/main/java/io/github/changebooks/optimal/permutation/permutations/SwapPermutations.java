package io.github.changebooks.optimal.permutation.permutations;

import io.github.changebooks.optimal.permutation.Permutations;
import io.github.changebooks.optimal.permutation.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 递归交换法
 *
 * @author changebooks@qq.com
 */
public class SwapPermutations implements Permutations {

    @Override
    public int[][] compute(int size) {
        Preconditions.checkArgument(size >= 0, "size can't be less than 0");

        int[] value = range(size);
        List<int[]> result = calculate(value);

        return result.toArray(new int[0][]);
    }

    /**
     * 全排列数组（value）中的所有元素
     *
     * @param value 待重排元素的数组
     * @return 排列结果
     */
    public List<int[]> calculate(int[] value) {
        Preconditions.checkNonNull(value, "value can't be null");

        List<int[]> result = new ArrayList<>();
        calculateNext(result, value, 0, value.length);

        return result;
    }

    /**
     * 按指定下标（start），重排数组（value）中的元素
     *
     * @param result 排列结果
     * @param value  待重排元素的数组
     * @param start  待重排元素的下标
     * @param size   待重排数组的长度
     */
    public void calculateNext(List<int[]> result, int[] value, int start, int size) {
        if (start == size) {
            result.add(Arrays.copyOf(value, size));
            return;
        }

        for (int i = start; i < size; i++) {
            swap(value, start, i);
            calculateNext(result, value, start + 1, size);
            swap(value, start, i);
        }
    }

    /**
     * 按指定长度（size），生成按1递增的有序数字（int[]）
     * (size = 0) : []
     * (size = 1) : [0]
     * (size = 2) : [0, 1]
     * (size = 3) : [0, 1, 2]
     *
     * @param size 待生成的数组长度
     * @return 按1递增的有序数字
     */
    public int[] range(int size) {
        return IntStream.range(0, size).toArray();
    }

    /**
     * 按指定下标（i, j），交换数组（value）中的元素
     *
     * @param value 待交换元素的数组
     * @param i     待交换元素的下标
     * @param j     另一个待交换元素的下标
     */
    public void swap(int[] value, int i, int j) {
        if (i != j) {
            int temp = value[i];
            value[i] = value[j];
            value[j] = temp;
        }
    }

}
