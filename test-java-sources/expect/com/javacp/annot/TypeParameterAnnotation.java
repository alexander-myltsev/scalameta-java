package com.javacp.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_PARAMETER)
public @interface TypeParameterAnnotation/*<=com.javacp.annot.TypeParameterAnnotation#*/{}
