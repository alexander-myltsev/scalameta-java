package com.javacp;

import javax.naming.Context;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

public final class Test/*<=com.javacp.Test#*//*<=com.javacp.Test#`<init>`().*/<A/*<=com.javacp.Test#[A]*/ extends CharSequence/*=>java.lang.CharSequence*/ & Serializable, B/*<=com.javacp.Test#[B]*/> extends ArrayList<A> implements Comparable/*=>java.lang.Comparable*/<B>, Serializable {

    @Override
    public final int compareTo/*<=com.javacp.Test#compareTo().*/(B b/*<=com.javacp.Test#compareTo().(b)*/) {
        return 0;
    }

    public void wildcard/*<=com.javacp.Test#wildcard().*/(ArrayList<?> wildcard/*<=com.javacp.Test#wildcard().(wildcard)*/) {}
    public void wildcard/*<=com.javacp.Test#wildcard(+1).*/(Collection<? extends Number> a/*<=com.javacp.Test#wildcard(+1).(a)*/, Collection/*=>java.util.Collection*/<? super Number/*=>java.lang.Number*/> b/*<=com.javacp.Test#wildcard(+1).(b)*/) {}

    class InnerShadowTypeParam/*<=com.javacp.Test#InnerShadowTypeParam#*/<A/*<=com.javacp.Test#InnerShadowTypeParam#[A]*/> {
        public void move/*<=com.javacp.Test#InnerShadowTypeParam#move().*/(A a/*<=com.javacp.Test#InnerShadowTypeParam#move().(a)*/, B b/*<=com.javacp.Test#InnerShadowTypeParam#move().(b)*/) { }
    }

    ArrayList<A> genericField/*<=com.javacp.Test#genericField.*/;
    final int[] arrayField/*<=com.javacp.Test#arrayField.*/ = null;

    ArrayList<A> genericMethod/*<=com.javacp.Test#genericMethod().*/() {
        return null;
    }
    int[] arrayMethod/*<=com.javacp.Test#arrayMethod().*/() {
        return new int[0];
    }
    void genericParams/*<=com.javacp.Test#genericParams().*/(A a/*<=com.javacp.Test#genericParams().(a)*/, B b/*<=com.javacp.Test#genericParams().(b)*/) { }
    void primitiveParams/*<=com.javacp.Test#primitiveParams().*/(int a/*<=com.javacp.Test#primitiveParams().(a)*/, long b/*<=com.javacp.Test#primitiveParams().(b)*/, float c/*<=com.javacp.Test#primitiveParams().(c)*/, double d/*<=com.javacp.Test#primitiveParams().(d)*/, short e/*<=com.javacp.Test#primitiveParams().(e)*/, byte f/*<=com.javacp.Test#primitiveParams().(f)*/, boolean g/*<=com.javacp.Test#primitiveParams().(g)*/, char h/*<=com.javacp.Test#primitiveParams().(h)*/) { }
    void typeParams/*<=com.javacp.Test#typeParams().*/(ArrayList/*=>java.util.ArrayList*/<HashMap/*=>java.util.HashMap*/<A, String[]>> a/*<=com.javacp.Test#typeParams().(a)*/, Hashtable/*=>java.util.Hashtable*/<String, B> b/*<=com.javacp.Test#typeParams().(b)*/) { }
    <C/*<=com.javacp.Test#methodTypeParams().[C]*/ extends Integer/*=>java.lang.Integer*/> void methodTypeParams/*<=com.javacp.Test#methodTypeParams().*/(C c/*<=com.javacp.Test#methodTypeParams().(c)*/) { }

    public void overload/*<=com.javacp.Test#overload().*/(java.util.logging.Logger/*=>java.util.logging.Logger*/ a/*<=com.javacp.Test#overload().(a)*/) { }
    public void overload/*<=com.javacp.Test#overload(+1).*/(Logger/*=>com.javacp.Logger*/ a/*<=com.javacp.Test#overload(+1).(a)*/) { }

    // primitive fields
    public int Int/*<=com.javacp.Test#Int.*/;
    public long Long/*<=com.javacp.Test#Long.*/;
    public float Float/*<=com.javacp.Test#Float.*/;
    public short Short/*<=com.javacp.Test#Short.*/;
    public byte Byte/*<=com.javacp.Test#Byte.*/;
    public boolean Boolean/*<=com.javacp.Test#Boolean.*/;
    public char Char/*<=com.javacp.Test#Char.*/;


    private int privateField/*<=com.javacp.Test#privateField.*/;
    protected int protectedField/*<=com.javacp.Test#protectedField.*/;
    public int publicField/*<=com.javacp.Test#publicField.*/;
    int packagePrivateField/*<=com.javacp.Test#packagePrivateField.*/;

    private void privateMethod/*<=com.javacp.Test#privateMethod().*/() { }
    protected void protectedMethod/*<=com.javacp.Test#protectedMethod().*/() { }
    public void publicMethod/*<=com.javacp.Test#publicMethod().*/() { }
    void packagePrivateMethod/*<=com.javacp.Test#packagePrivateMethod().*/() { }

    public Serializable/*=>java.io.Serializable*/ anonymous/*<=com.javacp.Test#anonymous.*/ = new Serializable() { };

    static int staticField/*<=com.javacp.Test#staticField.*/;
    static void staticMethod/*<=com.javacp.Test#staticMethod().*/() {}
    static class StaticClass/*<=com.javacp.Test#StaticClass#*/ {}

    void vararg/*<=com.javacp.Test#vararg().*/(int a/*<=com.javacp.Test#vararg().(a)*/, String/*=>java.lang.String*/... args/*<=com.javacp.Test#vararg().(args)*/) {}

    strictfp void strictfpMethod/*<=com.javacp.Test#strictfpMethod().*/() {}

    <T/*<=com.javacp.Test#m1().[T]*/ extends X> void m1/*<=com.javacp.Test#m1().*/() {}
    <T/*<=com.javacp.Test#m2().[T]*/ extends X & Y> void m2/*<=com.javacp.Test#m2().*/() {}
    <T/*<=com.javacp.Test#m3().[T]*/ extends X/*=>com.javacp.X*/ & Y/*=>com.javacp.Y*/ & Z/*=>com.javacp.Z*/> void m3/*<=com.javacp.Test#m3().*/() {}
}

interface X/*<=com.javacp.X#*/ {
}

interface Y/*<=com.javacp.Y#*/ {
}

interface Z/*<=com.javacp.Z#*/ {
}
