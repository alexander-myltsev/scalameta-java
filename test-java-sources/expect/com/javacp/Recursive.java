package com.javacp;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Recursive/*<=com.javacp.Recursive#`<init>`().*//*<=com.javacp.Recursive#*/<
        A/*<=com.javacp.Recursive#[A]*/ extends Recursive<A, B> & Serializable,
        B/*<=com.javacp.Recursive#[B]*/ extends Recursive.Inner<A, B>> {

    public abstract static class Inner/*<=com.javacp.Recursive#Inner#*/<
            A/*<=com.javacp.Recursive#Inner#[A]*/ extends Recursive<A, B> & Serializable,
            B/*<=com.javacp.Recursive#Inner#[B]*/ extends Inner<A, B>> {
    }

    public abstract class Inner2/*<=com.javacp.Recursive#Inner2#*/<C/*<=com.javacp.Recursive#Inner2#[C]*/ extends Comparable<C>> {}

    public <Anon/*<=com.javacp.Recursive#foo().[Anon]*/> ArrayList<Anon> foo/*<=com.javacp.Recursive#foo().*/() {
        return new ArrayList<Anon>() {
            @Override
            public boolean remove/*<=*/(Object o/*??(o)*/) {
                return true;
            }
        };
    }

}
