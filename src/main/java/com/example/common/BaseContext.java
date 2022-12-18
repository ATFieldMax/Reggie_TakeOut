package com.example.common;

/*
  什麼是ThreadLocal?
  ThreadLocal並不是一個Thread，而是Thread的局部變量。
  當使用ThreadLocal維護變量時，ThreadLocal為每個使用該變量的線程提供獨立的變量副本，
  所以每一個線程可以獨立改變自己的副本，不會影響其他線程對應的副本。
  ThreadLocal為每個線程提供單獨一份儲存空間，具有線程隔離的效果，只有在線程內才能獲取到對應的值，線程外則不能訪問。
*/


//基於ThreadLocal封裝工具類，用於保存和獲取當前用戶id
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
