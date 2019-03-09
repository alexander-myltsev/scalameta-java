package com.javacp;

public class ClassSuffix/*<=com.javacp.ClassSuffix#`<init>`().*//*<=com.javacp.ClassSuffix#*/ {
    class Inner/*<=com.javacp.ClassSuffix#Inner#*/<A/*<=com.javacp.ClassSuffix#Inner#[A]*/> {
        class Bar/*<=com.javacp.ClassSuffix#Inner#Bar#*/ {
            class Fuz/*<=com.javacp.ClassSuffix#Inner#Bar#Fuz#*/<B/*<=com.javacp.ClassSuffix#Inner#Bar#Fuz#[B]*/> {}
        }
    }
    public ClassSuffix.Inner<String>.Bar.Fuz<Integer> suffix/*<=com.javacp.ClassSuffix#suffix.*/;
}
