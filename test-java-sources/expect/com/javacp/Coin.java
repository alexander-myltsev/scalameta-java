package com.javacp;

public enum Coin/*<=com.javacp.Coin#*/ {
  PENNY/*<=com.javacp.Coin#PENNY.*/(1), NICKEL/*<=com.javacp.Coin#NICKEL.*/(5), DIME/*<=com.javacp.Coin#DIME.*/(10), QUARTER/*<=com.javacp.Coin#QUARTER.*/(25);
  Coin/*<=com.javacp.Coin#`<init>`().*/(int value/*??com.javacp.Coin#`<init>`().(value)*/) { this.value = value; }

  private final int value/*<=com.javacp.Coin#value.*/;
  public int value/*<=com.javacp.Coin#value().*/() { return value; }
}
