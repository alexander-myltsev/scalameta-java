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
      arg += pd.sym -> (arg(pd.sym) :+ pd)
    }

    override def visit(vd: jp.ast.body.VariableDeclarator, arg: SymbolTable): Unit = {
      super.visit(vd, arg)
      arg += vd.sym -> (arg(vd.sym) :+ vd)
    }

    override def visit(cd: jp.ast.body.ConstructorDeclaration, arg: SymbolTable): Unit = {
      super.visit(cd, arg)
      arg += cd.sym -> (arg(cd.sym) :+ cd)
    }

    override def visit(cid: jp.ast.body.ClassOrInterfaceDeclaration, arg: SymbolTable): Unit = {
      super.visit(cid, arg)
      arg += cid.sym -> (arg(cid.sym) :+ cid)
    }

    override def visit(p: jp.ast.body.Parameter, arg: SymbolTable): Unit = {
      super.visit(p, arg)
      arg += p.sym -> (arg(p.sym) :+ p)
    }

    override def visit(md: jp.ast.body.MethodDeclaration, arg: SymbolTable): Unit = {
      super.visit(md, arg)
      arg += md.sym -> (arg(md.sym) :+ md)
    }

    override def visit(ad: jp.ast.body.AnnotationDeclaration, arg: SymbolTable): Unit = {
      super.visit(ad, arg)
      arg += ad.sym -> (arg(ad.sym) :+ ad)
    }

    override def visit(tp: jp.ast.`type`.TypeParameter, arg: SymbolTable): Unit = {
      super.visit(tp, arg)
      arg += tp.sym -> (arg(tp.sym) :+ tp)
    }

    override def visit(ecd: jp.ast.body.EnumConstantDeclaration, arg: SymbolTable): Unit = {
      super.visit(ecd, arg)
      arg += ecd.sym -> (arg(ecd.sym) :+ ecd)
    }

    override def visit(ed: jp.ast.body.EnumDeclaration, arg: SymbolTable): Unit = {
      super.visit(ed, arg)
      arg += ed.sym -> (arg(ed.sym) :+ ed)
    }

    override def visit(coit: jp.ast.`type`.ClassOrInterfaceType, arg: SymbolTable): Unit = {
      super.visit(coit, arg)
      try {
        coit.resolve()
        arg += coit.sym -> (arg(coit.sym) :+ coit)
      } catch {
        case _: jp.resolution.UnsolvedSymbolException => ()
        case _: java.lang.UnsupportedOperationException => ()
      }
    }

    override def visit(pt: jp.ast.`type`.PrimitiveType, arg: SymbolTable): Unit = {
      super.visit(pt, arg)
      arg += pt.sym -> (arg(pt.sym) :+ pt)
    }
  }
}
