package com.myltsev

import com.github.{javaparser => jp}
import scala.collection.mutable
import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.annotation.switch
import com.github.{javaparser => jp}
import scala.meta.internal.semanticdb.Scala.{Descriptor => d, _}
import scala.meta.internal.{semanticdb => s}
import scala.meta.internal.semanticdb.SymbolInformation.{Kind => k}
import scala.meta.internal.semanticdb.SymbolInformation.{Property => p}


trait SyntheticNodes { semantics: Semantics =>
  class SyntheticNodesGenerator extends jp.ast.visitor.VoidVisitorAdapter[Unit] {
    override def visit(cid: jp.ast.body.ClassOrInterfaceDeclaration, arg: Unit): Unit = {
      if (cid.isInterface) {
        cid.getMethods.asScala.foreach { md =>
          if (md.getModifiers.isEmpty) {
            md.addModifier(jp.ast.Modifier.Keyword.ABSTRACT)
          }
        }
      } else {
        if (cid.getConstructors.isEmpty) {
          val cd = cid.addConstructor()
          for (r <- cd.getName.getRange.asScala) {
            cd.setRange(r)
            cd.getName.setRange(r)
          }
        }
      }
    }

    override def visit(ed: jp.ast.body.EnumDeclaration, arg: Unit): Unit = {
      // add synthetic `values` method
      val valuesMethodName = "values"
      if (ed.getMethodsByName(valuesMethodName).isEmpty) {
        val m = ed.addMethod(valuesMethodName,
          jp.ast.Modifier.Keyword.PUBLIC, jp.ast.Modifier.Keyword.STATIC)
        for (r <- ed.getName.getRange.asScala) {
          m.setRange(r)
          m.getName.setRange(r)
        }
      }

      // add synthetic `valueOf(string)` method
      val valueOfMethodName = "valueOf"
      if (ed.getMethodsByName(valueOfMethodName).isEmpty) {
        val m = ed.addMethod(valueOfMethodName,
          jp.ast.Modifier.Keyword.PUBLIC, jp.ast.Modifier.Keyword.STATIC)
        val stringType = new jp.JavaParser().parseClassOrInterfaceType("java.lang.String").getResult.get
        stringType.setRange(null)
        m.addParameter(
          new jp.ast.body.Parameter(stringType, new jp.ast.expr.SimpleName("name"))
        )
        for (r <- ed.getName.getRange.asScala) {
          m.setRange(r)
          m.getName.setRange(r)
        }
      }
    }
  }
}
