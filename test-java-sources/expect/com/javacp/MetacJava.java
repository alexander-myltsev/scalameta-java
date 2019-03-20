package com.javacp;

public class MetacJava/*<=com.javacp.MetacJava#`<init>`().*//*<=com.javacp.MetacJava#*/ {
    public static class StaticInner/*<=com.javacp.MetacJava#StaticInner#*/ {
        public void isNotStatic/*<=com.javacp.MetacJava#StaticInner#isNotStatic().*/() {}
        public static void isStatic/*<=com.javacp.MetacJava#StaticInner#isStatic().*/() {}
        public class NonStatic/*<=com.javacp.MetacJava#StaticInner#NonStatic#*/ {
            public void method/*<=com.javacp.MetacJava#StaticInner#NonStatic#method().*/(NonStatic e/*<=com.javacp.MetacJava#StaticInner#NonStatic#method().(e)*/) {}
        }
    }
    public class Overload1/*<=com.javacp.MetacJava#Overload1#*/ { public class A/*<=com.javacp.MetacJava#Overload1#A#*/ {} }
    public static class Overload3/*<=com.javacp.MetacJava#Overload3#*/ {
        public static class A/*<=com.javacp.MetacJava#Overload3#A#*/ {}
    }
    public class Overload2/*<=com.javacp.MetacJava#Overload2#*/ { public class A/*<=com.javacp.MetacJava#Overload2#A#*/ {} }
    public void overload/*<=com.javacp.MetacJava#overload().*/(Overload1.A a/*<=com.javacp.MetacJava#overload().(a)*/) {}
    // NOTE: Overload3 is intentionally placed before Overload2 in order to test
    // that methods are sorted by whether they're static or not.
    public static void overload/*<=com.javacp.MetacJava#overload(+2).*/(Overload3.A a/*<=com.javacp.MetacJava#overload(+2).(a)*/) {}
    public void overload/*<=com.javacp.MetacJava#overload(+1).*/(Overload2.A a/*<=com.javacp.MetacJava#overload(+1).(a)*/) {}
}
