/**
 *2011-10-22 下午07:12:41
 */
package com.cntinker.util;


/**
 * @author : bin_liu
 */
public class MathHelper{

    /**
     * 冒泡排序
     */
    public static final String TYPE_BUBBLE = "TYPE_BUBBLE";

    /**
     * 插入排序
     */
    public static final String TYPE_INSERTSORT = "TYPE_INSERTSORT";

    /**
     * 直接选择排序
     */
    public static final String TYPE_SELECTSORT = "TYPE_SELECTSORT";

    /**
     * 对分排序（非递归）
     */
    public static final String TYPE_BINARYSEARCH = "TYPE_BINARYSEARCH";

    /**
     * 对分排序（递归）
     */
    public static final String TYPE_BINARYSEARCH_RECURSIVE = "TYPE_BINARYSEARCH_RECURSIVE";

    /**
     * 快速排序
     */
    public static final String TYPE_QUICKSORT = "TYPE_QUICKSORT";

    public static int[] sort(int[] nums,String sortType,boolean asc){
        if(TYPE_BUBBLE.equals(sortType)){
            return bubble(nums,asc);
        }else if(TYPE_INSERTSORT.equals(sortType)){
            return insertSort(nums,asc);
        }else if(TYPE_SELECTSORT.equals(sortType)){
            return selectSort(nums,asc);
        }

        return null;
    }

    /**
     * 一组数中的最大
     * 
     * @param nums
     * @return int
     */
    public static int getMaxNumber(int[] nums){
        return bubble(nums,true)[nums.length - 1];
    }

    /**
     * 一组数中的最小
     * 
     * @param nums
     * @return int
     */
    public static int getMinNumber(int[] nums){
        return bubble(nums,true)[0];
    }

    /**
     * 冒泡排序<br>
     * 方法：相邻两元素进行比较，如有需要则进行交换，每完成一次循环就将最大元素排在最后（如从小到大排序），<br>
     * 下一次循环是将其他的数进行类似操作。<br>
     * 性能：比较次数O(n^2),n^2/2；交换次数O(n^2),n^2/4<br>
     * 
     * @param nums
     * @param asc
     *            是否升序
     * @return int[]
     */
    public static int[] bubble(int[] nums,boolean asc){
        int a,b,t;
        for(a = 1;a < nums.length;a ++ ){
            for(b = nums.length - 1;b >= a;b -- ){
                if(asc){
                    if(nums[b - 1] > nums[b]){
                        t = nums[b - 1];
                        nums[b - 1] = nums[b];
                        nums[b] = t;
                    }
                }else{
                    if(nums[b - 1] < nums[b]){
                        t = nums[b - 1];
                        nums[b - 1] = nums[b];
                        nums[b] = t;
                    }
                }
            }
        }
        return nums;
    }

    /**
     * 直接选择排序法----选择排序的一种 方法：每一趟从待排序的数据元素中选出最小（或最大）的一个元素，
     * 顺序放在已排好序的数列的最后，直到全部待排序的数据元素排完。 性能：比较次数O(n^2),n^2/2 交换次数O(n),n
     * 交换次数比冒泡排序少多了，由于交换所需CPU时间比比较所需的CUP时间多，所以选择排序比冒泡排序快。
     * 但是N比较大时，比较所需的CPU时间占主要地位，所以这时的性能和冒泡排序差不太多，但毫无疑问肯定要快些。
     * 
     * @param data
     * @param asc
     * @return int[]
     */
    public static int[] selectSort(int[] data,boolean asc){

        if(asc){ // 正排序，从小排到大
            int index;
            for(int i = 1;i < data.length;i ++ ){
                index = 0;
                for(int j = 1;j <= data.length - i;j ++ ){
                    if(data[j] > data[index]){
                        index = j;
                    }
                }
                // 交换在位置data.length-i和index(最大值)两个数
                swap(data,data.length - i,index);
            }
        }else{ // 倒排序，从大排到小
            int index;
            for(int i = 1;i < data.length;i ++ ){
                index = 0;
                for(int j = 1;j <= data.length - i;j ++ ){
                    if(data[j] < data[index]){
                        index = j;
                    }
                }

                // 交换在位置data.length-i和index(最大值)两个数
                swap(data,data.length - i,index);
            }

        }
        return data;// 输出直接选择排序后的数组值

    }

    /**
     * 插入排序 方法：将一个记录插入到已排好序的有序表（有可能是空表）中,从而得到一个新的记录数增1的有序表。 性能：比较次数O(n^2),n^2/4
     * 复制次数O(n),n^2/4 比较次数是前两者的一般，而复制所需的CPU时间较交换少，所以性能上比冒泡排序提高一倍多，而比选择排序也要快。
     * 
     * @param data
     *            要排序的数组
     * @param sortType
     *            排序类型
     */

    public static int[] insertSort(int[] data,boolean sortType){

        if(sortType){ // 正排序，从小排到大

            // 比较的轮数
            for(int i = 1;i < data.length;i ++ ){
                // 保证前i+1个数排好序
                for(int j = 0;j < i;j ++ ){
                    if(data[j] > data[i]){
                        // 交换在位置j和i两个数
                        swap(data,i,j);
                    }
                }
            }

        }else{ // 倒排序，从大排到小

            // 比较的轮数
            for(int i = 1;i < data.length;i ++ ){
                // 保证前i+1个数排好序
                for(int j = 0;j < i;j ++ ){
                    if(data[j] < data[i]){
                        // 交换在位置j和i两个数
                        swap(data,i,j);
                    }
                }
            }

        }

        return data;// 输出插入排序后的数组值

    }

    /**
     * 快速排序 快速排序使用分治法（Divide and conquer）策略来把一个序列（list）分为两个子序列（sub-lists）。 步骤为：
     * 1. 从数列中挑出一个元素，称为 "基准"（pivot）， 2.
     * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分割之后，
     * 该基准是它的最后位置。这个称为分割（partition）操作。 3.
     * 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
     * 递回的最底部情形，是数列的大小是零或一，也就是永远都已经被排序好了。虽然一直递回下去，但是这个算法总会结束，因为在每次的迭代（iteration）
     * 中，它至少会把一个元素摆到它最后的位置去。
     * 
     * @param data
     *            待排序的数组
     * @param asc
     */
    public void quickSort(int[] data,boolean asc){

        if(asc){ // 正排序，从小排到大

            qsort_asc(data,0,data.length - 1);

        }else{ // 倒排序，从大排到小
            qsort_desc(data,0,data.length - 1);
        }

    }

    /**
     * 快速排序的具体实现，排正序
     * 
     * @param data
     * @param low
     * @param high
     */

    private void qsort_asc(int data[],int low,int high){

        int i,j,x;

        if(low < high){ // 这个条件用来结束递归

            i = low;
            j = high;
            x = data[i];

            while(i < j){
                while(i < j && data[j] > x){
                    j -- ; // 从右向左找第一个小于x的数
                }

                if(i < j){
                    data[i] = data[j];
                    i ++ ;
                }

                while(i < j && data[i] < x){
                    i ++ ; // 从左向右找第一个大于x的数
                }

                if(i < j){
                    data[j] = data[i];
                    j -- ;
                }
            }
            data[i] = x;
            qsort_asc(data,low,i - 1);
            qsort_asc(data,i + 1,high);

        }

    }

    /**
     * 快速排序的具体实现，排倒序
     * 
     * @param data
     * @param low
     * @param high
     */

    private void qsort_desc(int data[],int low,int high){

        int i,j,x;

        if(low < high){ // 这个条件用来结束递归
            i = low;
            j = high;
            x = data[i];

            while(i < j){
                while(i < j && data[j] < x){
                    j -- ; // 从右向左找第一个小于x的数
                }
                if(i < j){
                    data[i] = data[j];
                    i ++ ;
                }

                while(i < j && data[i] > x){
                    i ++ ; // 从左向右找第一个大于x的数
                }

                if(i < j){
                    data[j] = data[i];
                    j -- ;
                }
            }
            data[i] = x;
            qsort_desc(data,low,i - 1);
            qsort_desc(data,i + 1,high);

        }

    }



    /**
     * 对分查找特定整数在整型数组中的位置(递归) 查找线性表必须是有序列表
     * 
     * @param dataset
     * @param data
     * @param beginIndex
     * @param endIndex
     * @return int
     */
    public int binarySearch(int[] dataset,int data,int beginIndex,int endIndex){

        int midIndex = ( beginIndex + endIndex ) >>> 1; // 相当于mid = (low + high)
        // /
        // 2，但是效率会高些

        if(data < dataset[beginIndex] || data > dataset[endIndex]
                || beginIndex > endIndex)
            return -1;

        if(data < dataset[midIndex]){
            return binarySearch(dataset,data,beginIndex,midIndex - 1);
        }else if(data > dataset[midIndex]){
            return binarySearch(dataset,data,midIndex + 1,endIndex);
        }else{
            return midIndex;
        }

    }

    /**
     * 二分查找特定整数在整型数组中的位置(非递归) 查找线性表必须是有序列表
     * 
     * @param dataset
     * @param data
     * @return int
     */
    public int binarySearch(int[] dataset,int data){

        int beginIndex = 0;
        int endIndex = dataset.length - 1;
        int midIndex = -1;

        if(data < dataset[beginIndex] || data > dataset[endIndex]
                || beginIndex > endIndex)
            return -1;

        while(beginIndex <= endIndex){
            midIndex = ( beginIndex + endIndex ) >>> 1; // 相当于midIndex =
            // (beginIndex +
            // endIndex) / 2，但是效率会高些
            if(data < dataset[midIndex]){
                endIndex = midIndex - 1;
            }else if(data > dataset[midIndex]){
                beginIndex = midIndex + 1;
            }else{
                return midIndex;
            }
        }

        return -1;

    }

    /**
     * 交换数组中指定的两元素的位置
     * 
     * @param data
     * @param x
     * @param y
     */
    private static void swap(int[] data,int x,int y){

        int temp = data[x];

        data[x] = data[y];

        data[y] = temp;

    }

    /**
     * 反转数组的方法
     * 
     * @param data
     *            源数组
     */

    public static int[] reverse(int[] data){

        int length = data.length;
        int temp = 0;// 临时变量

        for(int i = 0;i < length / 2;i ++ ){
            temp = data[i];
            data[i] = data[length - 1 - i];
            data[length - 1 - i] = temp;
        }
        return data;
    }

    public static void main(String args[]) throws Exception{
        int nums[] = {1,33,44,24,15,61,47,18,69,80,99,74};

        int[] a = bubble(nums,false);
        for(int i = 0;i < a.length;i ++ )
            System.out.print(a[i] + ",");

        System.out.println("\n==============");

        System.out.println(getMaxNumber(nums));
        System.out.println(getMinNumber(nums));

        System.out.println("\n==============");
        int[] b = selectSort(nums,false);
        for(int i = 0;i < b.length;i ++ )
            System.out.print(b[i] + ",");
        System.out.println();
    }
}
