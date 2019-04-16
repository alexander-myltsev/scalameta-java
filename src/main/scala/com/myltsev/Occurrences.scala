package com.myltsev

import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._
import com.github.{javaparser => jp}
import scala.meta.internal.semanticdb.Scala.{Descriptor => d, _}
import scala.meta.internal.{semanticdb => s}
import scala.meta.internal.semanticdb.SymbolInformation.{Kind => k}
import scala.meta.internal.semanticdb.SymbolInformation.{Property => p}
import java.nio.file.Path
import java.io.File


class Occurrences(parseResult: jp.ParseResult[jp.ast.CompilationUnit]) extends Semantics {
  {
    if (!parseResult.isSuccessful) {
      val problems = for (problem <- parseResult.getProblems.asScala) yield {
        problem.getMessage
      }
      throw new Exception(
        s"""Parsing was now successful:
           |${problems.mkString("\n")}""".stripMargin)
    }
  }

  val symbolTable: SymbolTable = {
    val symbolTable = emptySymbolTable
    val cu = parseResult.getResult.get
    new SyntheticNodesGenerator().visit(cu, ())
    new SymbolTableGenerator().visit(cu, symbolTable)
    symbolTable
  }

  def all(): Seq[s.SymbolOccurrence] = {
    val occurrences = for {
      sym <- symbolTable.keys
      occ <- find(sym) if occ.range.isDefined
    } yield occ
    occurrences.toSeq
  }

  def find(sym: String): Vector[s.SymbolOccurrence] = {
    val occurrences = for {
      node <- symbolTable(sym)
      if node.getRange.asScala.isDefined
      rangeOpt = node match {
        case n: jp.ast.nodeTypes.NodeWithSimpleName[_] => n.getName.getRange.asScala
        case n: jp.ast.nodeTypes.NodeWithName[_] => n.getName.getRange.asScala
        case n: jp.ast.Node => n.getRange.asScala
        case _ => None
      }
      range <- rangeOpt
    } yield {
      val r = s.Range(
        startLine = range.begin.line - 1,
        startCharacter = range.begin.column - 1,
        endLine = range.end.line - 1,
        endCharacter = range.end.column
      )

      val occurrence = s.SymbolOccurrence(
        symbol = sym,
        range = Some(r),
        role = node.role,
      )
      occurrence
    }

    occurrences
  }
}

object Occurrences {
  private def createJavaParser(): jp.JavaParser = {
    val combinedTypeSolver = new jp.symbolsolver.resolution.typesolvers.CombinedTypeSolver(
      new jp.symbolsolver.resolution.typesolvers.ReflectionTypeSolver(),
      new jp.symbolsolver.resolution.typesolvers.JavaParserTypeSolver(
        new File("test-java-sources/input")
      )
    )
    val symbolSolver = new jp.symbolsolver.JavaSymbolSolver(combinedTypeSolver)
    val javaParser = new jp.JavaParser()
    javaParser.getParserConfiguration.setSymbolResolver(symbolSolver)
    javaParser
  }

  def apply(code: String): Occurrences = {
    val jprs = createJavaParser()
    val pr = jprs.parse(code)
    new Occurrences(pr)
  }

  def apply(path: Path): Occurrences = {
    val jprs = createJavaParser()
    val pr = jprs.parse(path)
    new Occurrences(pr)
  }
}
