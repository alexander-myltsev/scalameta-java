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
import java.nio.file.Path


class Occurrences(parseResult: jp.ParseResult[jp.ast.CompilationUnit]) extends Semantics {
  val symbolTable: SymbolTable = {
    val stg = new SymbolTableGenerator()
    val symbolTable = emptySymbolTable
    val cu = parseResult.getResult.get
    stg.visit(cu, symbolTable)
    symbolTable
  }

  def find(sym: String): Option[s.SymbolOccurrence] = {
    val range = for {
      nodeOpt <- symbolTable.get(sym)
      node <- nodeOpt
      _ <- node.getRange.asScala
      rangeOpt = node match {
        case n: jp.ast.nodeTypes.NodeWithSimpleName[_] => n.getName.getRange.asScala
        case n: jp.ast.nodeTypes.NodeWithName[_] => n.getName.getRange.asScala
        case _ => None
      }
      range <- rangeOpt
    } yield {
      s.Range(
        startLine = range.begin.line - 1,
        startCharacter = range.begin.column - 1,
        endLine = range.end.line - 1,
        endCharacter = range.end.column
      )
    }

    val occurence = s.SymbolOccurrence(symbol = sym, range = range)
    Some(occurence)
  }
}

object Occurrences {
  def apply(code: String): Occurrences = {
    val pr = new jp.JavaParser().parse(code)
    new Occurrences(pr)
  }

  def apply(path: Path): Occurrences = {
    val pr = new jp.JavaParser().parse(path)
    new Occurrences(pr)
  }
}
