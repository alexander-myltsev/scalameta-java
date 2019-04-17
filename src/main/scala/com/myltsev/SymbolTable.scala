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

trait SymbolTable { semantics: Semantics =>
  type SymbolTable = mutable.Map[String, Vector[jp.ast.Node]]
  def emptySymbolTable: SymbolTable =
    mutable.Map.empty[String, Vector[jp.ast.Node]].withDefaultValue(Vector())
  val symbolTable: SymbolTable

  class SymbolTableGenerator extends jp.ast.visitor.VoidVisitorAdapter[SymbolTable] {
    override def visit(pd: jp.ast.PackageDeclaration, arg: SymbolTable): Unit = {
      super.visit(pd, arg)
      val s = pd.sym
      arg += s -> (arg(s) :+ pd)
    }

    override def visit(vd: jp.ast.body.VariableDeclarator, arg: SymbolTable): Unit = {
      super.visit(vd, arg)
      val s = vd.sym
      arg += s -> (arg(s) :+ vd)
    }

    override def visit(cd: jp.ast.body.ConstructorDeclaration, arg: SymbolTable): Unit = {
      super.visit(cd, arg)
      val s = cd.sym
      arg += s -> (arg(s) :+ cd)
    }

    override def visit(cid: jp.ast.body.ClassOrInterfaceDeclaration, arg: SymbolTable): Unit = {
      super.visit(cid, arg)
      val s = cid.sym
      arg += s -> (arg(s) :+ cid)
    }

    override def visit(p: jp.ast.body.Parameter, arg: SymbolTable): Unit = {
      super.visit(p, arg)
      val s = p.sym
      arg += s -> (arg(s) :+ p)
    }

    override def visit(md: jp.ast.body.MethodDeclaration, arg: SymbolTable): Unit = {
      super.visit(md, arg)
      val s = md.sym
      arg += s -> (arg(s) :+ md)
    }

    override def visit(ad: jp.ast.body.AnnotationDeclaration, arg: SymbolTable): Unit = {
      super.visit(ad, arg)
      val s = ad.sym
      arg += s -> (arg(s) :+ ad)
    }

    override def visit(tp: jp.ast.`type`.TypeParameter, arg: SymbolTable): Unit = {
      super.visit(tp, arg)
      val s = tp.sym
      arg += s -> (arg(s) :+ tp)
    }

    override def visit(ecd: jp.ast.body.EnumConstantDeclaration, arg: SymbolTable): Unit = {
      super.visit(ecd, arg)
      val s = ecd.sym
      arg += s -> (arg(s) :+ ecd)
    }

    override def visit(ed: jp.ast.body.EnumDeclaration, arg: SymbolTable): Unit = {
      super.visit(ed, arg)
      val s = ed.sym
      arg += s -> (arg(s) :+ ed)
    }

    override def visit(ne: jp.ast.expr.NameExpr, arg: SymbolTable): Unit = {
      super.visit(ne, arg)
      val s = ne.sym
      arg += s -> (arg(s) :+ ne)
    }

    override def visit(mce: jp.ast.expr.MethodCallExpr, arg: SymbolTable): Unit = {
      super.visit(mce, arg)
      val s = mce.sym
      arg += s -> (arg(s) :+ mce)
    }

    override def visit(coit: jp.ast.`type`.ClassOrInterfaceType, arg: SymbolTable): Unit = {
      super.visit(coit, arg)
      try {
        coit.resolve()
        val s = coit.sym
        arg += s -> (arg(s) :+ coit)
      } catch {
        case _: jp.resolution.UnsolvedSymbolException => ()
        case _: java.lang.UnsupportedOperationException => ()
      }
    }

  }
}
