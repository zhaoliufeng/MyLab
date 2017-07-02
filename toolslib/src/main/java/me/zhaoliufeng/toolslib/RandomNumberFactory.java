package me.zhaoliufeng.toolslib;

/**
 * 随机数生成器
 */

public class RandomNumberFactory {

    private static String TAG = "RandomNumberFactory";

    public static Number getRandomNumber(Number minVal, Number maxVal){
        boolean isMinValBigger = false;
        if (minVal.floatValue() > maxVal.floatValue())
            isMinValBigger = true;

        //判断数字类型
        if (minVal.getClass().toString().equals("java.lang.Integer")){
            if (isMinValBigger)
                return (int)(Math.random()*((int)minVal - (int)maxVal)) + (int)maxVal;
            else
                return (int)(Math.random()*((int)maxVal - (int)minVal)) + (int)minVal;
        }else {
            if (isMinValBigger)
                return (float)(Math.random()*((float)minVal - (float)maxVal)) + (float)maxVal;
            else
                return (float)(Math.random()*((float)maxVal - (float)minVal)) + (float)minVal;
        }
    }
}
