package com.javacp;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Recursive/*<=com.javacp.Recursive#`<init>`().*//*<=com.javacp.Recursive#*/<
        A/*<=com.javacp.Recursive#[A]*/ extends Recursive<A, B> & Serializable,
        B/*<=com.javacp.Recursive#[B]*/ extends Recursive.Inner<A, B>> {

    public abstract static class Inner/*<=com.javacp.Recursive#Inner#*/<
            A/*<=com.javacp.Recursive#Inner#[A]*/ extends Recursive/*=>com.javacp.Recursive*/<A, B> & Serializable/*=>java.io.Serializable*/,
            B/*<=com.javacp.Recursive#Inner#[B]*/ extends Inner/*=>com.javacp.Recursive.Inner*/<A, B>> {
    }

    public abstract class Inner2/*<=com.javacp.Recursive#Inner2#*/<C/*<=com.javacp.Recursive#Inner2#[C]*/ extends Comparable/*=>java.lang.Comparable*/<C>> {}

    public <Anon/*<=com.javacp.Recursive#foo().[Anon]*/> ArrayList/*=>java.util.ArrayList*/<Anon> foo/*<=com.javacp.Recursive#foo().*/() {
        return new ArrayList<Anon>() {
            @Override
            public boolean/*=>scala.Boolean#*/ remove/*<=*/(Object/*=>java.lang.Object*/ o/*<=(o)*/) {
                return true;
            }
        };
    }

}
