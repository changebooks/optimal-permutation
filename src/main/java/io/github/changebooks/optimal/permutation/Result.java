package io.github.changebooks.optimal.permutation;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 计算结果和下标排列
 *
 * @author changebooks@qq.com
 */
public class Result<R extends Comparable<R>> implements Serializable {
    /**
     * 计算结果
     */
    private R data;

    /**
     * 下标排列
     */
    private int[] indexes;

    public Result() {
    }

    public Result(R data, int[] indexes) {
        this.data = data;
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        R data = getData();
        int[] indexes = getIndexes();
        return "{" +
                "\"data\": " + (data instanceof String ? "\"" + data + "\"" : data) + ", " +
                "\"indexes\": " + Arrays.toString(indexes) +
                "}";
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    public int[] getIndexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

}
