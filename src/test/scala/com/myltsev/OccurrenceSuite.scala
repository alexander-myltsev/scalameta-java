package com.myltsev

// Originally borrowed from
// https://github.com/scalameta/scalameta/blob/
// d3bd1725918253daf966e391f43ef73692a4ec67/
// tests/jvm/src/test/scala/scala/meta/tests/semanticdb/OccurrenceSuite.scala

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import org.scalatest.FunSuite
import scala.meta.inputs.Input
import scala.meta.inputs.Position
import scala.meta.internal.io.FileIO
import scala.meta.internal.io.PathIO
import scala.meta.internal.semanticdb.Language
import scala.meta.internal.semanticdb.Scala._
import scala.meta.internal.semanticdb.Schema
import scala.meta.internal.semanticdb.SymbolOccurrence
import scala.meta.internal.semanticdb.TextDocument
import scala.meta.io.AbsolutePath
import scala.meta.testkit.DiffAssertions

class OccurrenceSuite extends FunSuite with DiffAssertions {
  ScalaVersion.doIf212("OccurrenceSuite") {
    OccurrenceSuite.testCases().foreach { t =>
      test(t.name) {
        val body = t.body()
        assertNoDiff(body.obtained, body.expected)
      }
    }
  }
}

object OccurrenceSuite {
  def isExcluded(path: AbsolutePath): Boolean =
    path.toNIO.endsWith("Exclude.scala")
  val expectroot = AbsolutePath(Paths.get(BuildInfo.testSourceDirectory.getAbsolutePath, "expect"))
  case class TestBody(
                       obtained: String,
                       expected: String,
                       expectpath: AbsolutePath
                     )
  case class TestCase(
                       name: String,
                       body: () => TestBody
                     )
  def testCases(): Seq[TestCase] = {
    val absdir = AbsolutePath(Paths.get(BuildInfo.testSourceDirectory.getAbsolutePath, "input"))
    for {
      source <- FileIO.listAllFilesRecursively(absdir)
      if PathIO.extension(source.toNIO) == "java"
      if !isExcluded(source)
    } yield {
      val relpath = source.toRelative(absdir)
      val testname = relpath.toURI(false).toString
      TestCase(
        testname,
        () => {
          val text = new String(source.readAllBytes)
          val occurrences = Occurrences(text).all()
          val textdocument = TextDocument(schema = Schema.SEMANTICDB4,
                                          language = Language.JAVA,
                                          text = text,
                                          occurrences = occurrences)
          val expectpath = expectroot.resolve(relpath)
          val obtained = OccurrenceSuite.printTextDocument(textdocument)
          val expected =
            if (expectpath.isFile) {
              FileIO.slurp(expectpath, StandardCharsets.UTF_8)
            } else {
              "// missing expect file\n"
            }
          TestBody(
            obtained,
            expected,
            expectpath
          )
        }
      )
    }
  }
  def saveExpected(): Unit = {
    testCases().foreach { t =>
      val body = t.body()
      Files.createDirectories(body.expectpath.toNIO.getParent)
      Files.write(body.expectpath.toNIO, body.obtained.getBytes(StandardCharsets.UTF_8))
    }
  }
  def printTextDocument(doc: TextDocument): String = {
    val symtab = doc.symbols.iterator.map(info => info.symbol -> info).toMap
    val sb = new StringBuilder
    val occurrences = doc.occurrences.sorted
    val input = Input.String(doc.text)
    var offset = 0
    occurrences.foreach { occ =>
      val range = occ.range.get
      val pos = Position.Range(
        input,
        range.startLine,
        range.startCharacter,
        range.endLine,
        range.endCharacter
      )
      sb.append(doc.text.substring(offset, pos.end))
      val isPrimaryConstructor =
        symtab.get(occ.symbol).exists(_.isPrimary)
      if (!occ.symbol.isPackage && !isPrimaryConstructor) {
        printSymbol(sb, occ.symbol, occ.role)
      }
      offset = pos.end
    }
    sb.append(doc.text.substring(offset))
    sb.toString()
  }

  def printSymbol(sb: StringBuilder, symbol: String, role: SymbolOccurrence.Role): Unit = {
    val arrow = if (role.isDefinition) "<=" else "=>"
    sb.append("/*")
      .append(arrow)
      // replace package / with dot . to not upset GitHub syntax highlighting.
      .append(symbol.replace('/', '.'))
      .append("*/")
  }

  implicit val occurrenceOrdering: Ordering[SymbolOccurrence] =
    new Ordering[SymbolOccurrence] {
      override def compare(x: SymbolOccurrence, y: SymbolOccurrence): Int = {
        if (x.range.isEmpty) 0
        else if (y.range.isEmpty) 0
        else {
          val a = x.range.get
          val b = y.range.get
          val byLine = Integer.compare(
            a.startLine,
            b.startLine
          )
          if (byLine != 0) {
            byLine
          } else {
            val byCharacter = Integer.compare(
              a.startCharacter,
              b.startCharacter
            )
            byCharacter
          }
        }
      }
    }
}
