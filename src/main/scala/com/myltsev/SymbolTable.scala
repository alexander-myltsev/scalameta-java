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
  type SymbolTable = mutable.Map[String, Option[jp.ast.Node]]
  def emptySymbolTable: SymbolTable =
    mutable.Map.empty[String, Option[jp.ast.Node]]
  val symbolTable: SymbolTable

  class SymbolTableGenerator extends jp.ast.visitor.VoidVisitorAdapter[SymbolTable] {
    override def visit(pd: jp.ast.PackageDeclaration, arg: SymbolTable): Unit = {
      super.visit(pd, arg)
      arg += pd.sym -> Some(pd)
    }

    override def visit(vd: jp.ast.body.VariableDeclarator, arg: SymbolTable): Unit = {
      super.visit(vd, arg)
      arg += vd.sym -> Some(vd)
    }

    override def visit(cd: jp.ast.body.ConstructorDeclaration, arg: SymbolTable): Unit = {
      super.visit(cd, arg)
      arg += cd.sym -> Some(cd)
    }

    override def visit(cid: jp.ast.body.ClassOrInterfaceDeclaration, arg: SymbolTable): Unit = {
      super.visit(cid, arg)
      arg += cid.sym -> Some(cid)
    }

    override def visit(p: jp.ast.body.Parameter, arg: SymbolTable): Unit = {
      super.visit(p, arg)
      arg += p.sym -> Some(p)
    }

    override def visit(md: jp.ast.body.MethodDeclaration, arg: SymbolTable): Unit = {
      super.visit(md, arg)
      arg += md.sym -> Some(md)
    }

    override def visit(ad: jp.ast.body.AnnotationDeclaration, arg: SymbolTable): Unit = {
      super.visit(ad, arg)
      arg += ad.sym -> Some(ad)
    }

    override def visit(tp: jp.ast.`type`.TypeParameter, arg: SymbolTable): Unit = {
      super.visit(tp, arg)
      arg += tp.sym -> Some(tp)
    }

    override def visit(ecd: jp.ast.body.EnumConstantDeclaration, arg: SymbolTable): Unit = {
      super.visit(ecd, arg)
      arg += ecd.sym -> Some(ecd)
    }

    override def visit(ed: jp.ast.body.EnumDeclaration, arg: SymbolTable): Unit = {
      super.visit(ed, arg)
      arg += ed.sym -> Some(ed)
    }
  }
}
