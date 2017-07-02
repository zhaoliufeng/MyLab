package me.zhaoliufeng.toolslib;

/**
 * 查找一个数字集合中的某个值的工具类
 */

public class NumberFindUtils {

    /**
     * 获取集合中的最大值
     * @param nums  数字集合
     * @return  最大值
     */
    public static float getMaxNumber(float... nums){
        float maxNum = nums[0];
        for (float num: nums) {
            if (num > maxNum)
                maxNum = num;
        }
        return maxNum;
    }

    /**
     * 获取集合中的最大值
     * @param nums  数字集合
     * @return  最大值
     */
    public static int getMaxNumber(int... nums){
        int maxNum = -nums[0];
        for (int num: nums) {
            if (num > maxNum)
                maxNum = num;
        }
        return maxNum;
    }

    public static void removeMaxNumber(int index, float... nums){
        System.arraycopy(nums, index + 1, nums, index, nums.length - 1 - index);
    }

    public static void removeMaxNumber(int index, int... nums){
        System.arraycopy(nums, index + 1, nums, index, nums.length - 1 - index);
    }

    /**
     * 获取集合中的最小值
     * @param nums  数字集合
     * @return  最小值
     */
    public static float getMinNumber(float... nums){
        float minNum = nums[0];
        for (float num: nums) {
            if (num < minNum)
                minNum = num;
        }
        return minNum;
    }

    /**
     * 获取集合中的最小值
     * @param nums  数字集合
     * @return  最小值
     */
    public static int getMinNumber(int... nums){
        int minNum = nums[0];
        for (int num: nums) {
            if (num < minNum)
                minNum = num;
        }
        return minNum;
    }

    /**
     * 获取集合中的最大值下标
     * @param nums  数字集合
     * @return  最大值下标
     */
    public static int getMaxNumIndex(float... nums){
        float maxNum = nums[0];
        int flag = -1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > maxNum){
                maxNum = nums[i];
                flag = i;
            }
        }
        return flag;
    }

    /**
     * 获取集合中的最大值下标
     * @param nums  数字集合
     * @return  最大值下标
     */
    public static int getMaxNumIndex(int... nums){
        int maxNum = nums[0];
        int flag = -1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > maxNum){
                maxNum = nums[i];
                flag = i;
            }
        }
        return flag;
    }

    /**
     * 获取集合中的最小值下标
     * @param nums  数字集合
     * @return  最小值下标
     */
    public static int getMinNumIndex(float... nums){
        float minNum = nums[0];
        int flag = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < minNum){
                minNum = nums[i];
                flag = i;
            }
        }
        return flag;
    }

    /**
     * 获取集合中的最小值下标
     * @param nums  数字集合
     * @return  最小值下标
     */
    public static int getMinNumIndex(int... nums){
        int minNum = nums[0];
        int flag = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < minNum){
                minNum = nums[i];
                flag = i;
            }
        }
        return flag;
    }

    /**
     * 获取前三个数字
     * @param nums  数据源的数组
     * @return  前三个数字数组
     */
    public static float[] getTopThree(float... nums){
        float[] numbers = nums;
        float[] numthree = new float[3];
        for (int i = 0; i < numthree.length; i++) {
            numthree[i] = getMaxNumber(numbers);
            removeMaxNumber(getMaxNumIndex(numbers), numbers);
        }
        return numthree;
    }
    /**
     * 获取前三个数字
     * @param nums  数据源的数组
     * @return  前三个数字数组
     */
    public static int[] getTopThree(int... nums){
        int[] numbers = nums;
        int[] numthree = new int[3];
        for (int i = 0; i < numthree.length; i++) {
            numthree[i] = getMaxNumber(numbers);
            removeMaxNumber(getMaxNumIndex(numbers), numbers);
        }
        return numthree;
    }

    /**
     * 以逗号区分 拆分一个数组字符串
     * @param intStr   数组字符串
     * @param flag    拆分数组的标志位字符
     * @return  字符串中的数组集合
     */
    public static int[] splitStrToIntArray(String intStr, String flag){
        String intArray[] = intStr.split(flag);
        int[] arr = new int[intArray.length];
        for (int i = 0; i < arr.length; i++){
            try {
                arr[i] = Integer.valueOf(intArray[i]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return arr;
    }
}

