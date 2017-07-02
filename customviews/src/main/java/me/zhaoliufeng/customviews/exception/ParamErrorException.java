package me.zhaoliufeng.customviews.exception;

/**
 * 参数错误异常.
 */

public class ParamErrorException extends RuntimeException {
    public ParamErrorException(String errorMsg){
        super(errorMsg);
    }
}
