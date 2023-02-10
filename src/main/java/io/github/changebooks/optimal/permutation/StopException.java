package io.github.changebooks.optimal.permutation;

/**
 * 终止计算
 *
 * @author changebooks@qq.com
 */
public class StopException extends RuntimeException {

    public StopException() {
        super("stop the calculate");
    }

}
