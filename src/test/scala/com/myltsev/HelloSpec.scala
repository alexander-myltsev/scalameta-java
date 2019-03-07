package com.myltsev

import java.nio.file.Paths

import org.scalatest._

class HelloSpec extends FunSuite with Matchers {
  test("pass easy test") {
    val path = Paths.get(getClass.getResource("/test/Hello.java").toURI)
    val occurrences = Occurrences.apply(path)
    val occurrence = occurrences.find("test/Hello#")

    occurrence shouldBe defined
    occurrence.get.range shouldBe defined
  }
}
