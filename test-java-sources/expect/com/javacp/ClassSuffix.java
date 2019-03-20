package com.javacp;

public class ClassSuffix/*<=com.javacp.ClassSuffix#`<init>`().*//*<=com.javacp.ClassSuffix#*/ {
    class Inner/*<=com.javacp.ClassSuffix#Inner#*/<A/*<=com.javacp.ClassSuffix#Inner#[A]*/> {
        class Bar/*<=com.javacp.ClassSuffix#Inner#Bar#*/ {
            class Fuz/*<=com.javacp.ClassSuffix#Inner#Bar#Fuz#*/<B/*<=com.javacp.ClassSuffix#Inner#Bar#Fuz#[B]*/> {}
        }
    }
    public ClassSuffix/*=>com.javacp.ClassSuffix*/.Inner/*=>com.javacp.ClassSuffix.Inner*/<String/*=>java.lang.String*/>.Bar/*=>com.javacp.ClassSuffix.Inner.Bar*/.Fuz/*=>com.javacp.ClassSuffix.Inner.Bar.Fuz*/<Integer/*=>java.lang.Integer*/> suffix/*<=com.javacp.ClassSuffix#suffix.*/;
}
