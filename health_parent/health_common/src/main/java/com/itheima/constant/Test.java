package com.itheima.constant;

public  class Test {
    private static  int i=1;

    static {
        System.out.println(111);
    }
    public static void main(String[] args) {
       // System.out.println();
        /*
             考虑 this  产生的时机  当前对象初始化后才会产生this

         */
      //  System.out.println(this.get());

        System.out.println(Test.i);

//        System.out.println(new Test().i);
    }

    public static String get(){
        /*
           加上static修饰的方式 不再单单属于某个对象了 而是属于某个类
           但是这个this指带的就是当前对象(非常明确的某个对象)
         */
       // System.out.println(this.i);
        return "jhh";
    }

    public void hao(){

        System.out.println(this.i);
    }

}
