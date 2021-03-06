package com.myltsev

import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.annotation.switch
import com.github.{javaparser => jp}
import scala.meta.internal.semanticdb.Scala.{Descriptor => d, _}
import scala.meta.internal.{semanticdb => s}
import scala.meta.internal.semanticdb.SymbolInformation.{Kind => k}
import scala.meta.internal.semanticdb.SymbolInformation.{Property => p}

trait Sym { semantics: Semantics =>
  var _suffixId = 0
  val _sym2Suffix = mutable.Map.empty[jp.ast.Node, String]
  def getSuffix(node: jp.ast.Node): String = {
    if (!_sym2Suffix.contains(node)) {
      _sym2Suffix += node -> _suffixId.toString
      _suffixId += 1
    }
    val suffix = _sym2Suffix(node)
    suffix
  }

  implicit class NodeOps(node: jp.ast.Node) {
    def enclosingPackage: Option[jp.ast.PackageDeclaration] = node match {
      case e: jp.ast.CompilationUnit => e.getPackageDeclaration.asScala
      case n => n.getParentNode.asScala.flatMap { _.enclosingPackage }
    }

    def owner: String = {
      node.getParentNode.asScala match {
        case Some(parent) =>
          parent match {
            case elem: jp.ast.CompilationUnit =>
              elem.getPackageDeclaration.asScala.map { _.sym }.getOrElse("")
            case elem @ (
              _: jp.ast.body.TypeDeclaration[_] | _: jp.ast.body.CallableDeclaration[_] |
              _: jp.ast.PackageDeclaration
              ) =>
              elem.sym
            case _ =>
              parent.owner
          }
        case _ =>
          throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
      }
    }

    def symbolName: String = node match {
      case _: jp.ast.body.ConstructorDeclaration =>
        "<init>"
      case elem: jp.ast.nodeTypes.NodeWithSimpleName[_] =>
        elem.getNameAsString
      case _ =>
        throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
    }

    def sym: String = node match {
      case elem: jp.ast.CompilationUnit =>
        elem.getPackageDeclaration.asScala.map { _.sym }.getOrElse("")
      case elem: jp.ast.PackageDeclaration =>
        val qualName = elem.getName.asString
        if (qualName == "") {
          Symbols.EmptyPackage
        } else {
          qualName.replace('.', '/') + "/"
        }
      case _: jp.ast.body.TypeDeclaration[_] =>
        Symbols.Global(owner, d.Type(symbolName))
      case _: jp.ast.stmt.LocalClassDeclarationStmt =>
        ""
      case _: jp.ast.body.Parameter =>
        Symbols.Global(owner, d.Parameter(symbolName))
      case elem: jp.ast.body.CallableDeclaration[_] =>
        elem.getParentNode.asScala match {
          case Some(owner: jp.ast.body.TypeDeclaration[_]) =>
            val disambig = {
              val siblingMethods: mutable.Buffer[jp.ast.body.CallableDeclaration[_]] = {
                if (elem.isConstructorDeclaration) {
                  val siblingMethods = owner
                    .asInstanceOf[jp.ast.nodeTypes.NodeWithConstructors[_]]
                    .getConstructors
                    .asScala
                  siblingMethods.map { _.asCallableDeclaration }
                } else {
                  val siblingMethods = owner.getMethodsByName(symbolName).asScala
                  val (instance, static) = siblingMethods.partition { method =>
                    !method.getModifiers.contains(jp.ast.Modifier.staticModifier())
                  }
                  val r = instance ++ static
                  r.map { _.asCallableDeclaration }
                }
              }
              val methodPlace = siblingMethods.indexOf(elem)
              (methodPlace: @switch) match {
                case -1 =>
                  throw new RuntimeException("Unexpected state. Please, submit the issue")
                case 0 => "()"
                case x => s"(+$x)"
              }
            }
            Symbols.Global(owner.sym, d.Method(symbolName, disambig))
          case Some(_: jp.ast.expr.ObjectCreationExpr) =>
            ""
          case _ =>
            throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
        }
      case _: jp.ast.body.EnumConstantDeclaration =>
        Symbols.Global(owner, d.Term(symbolName))
      case _: jp.ast.body.VariableDeclarator =>
        node.getParentNode.asScala match {
          case Some(_: jp.ast.expr.VariableDeclarationExpr) =>
            Symbols.Local(semantics.getSuffix(node))
          case Some(_: jp.ast.body.FieldDeclaration) =>
            val sn = symbolName
            Symbols.Global(owner, d.Term(sn))
          case _ =>
            throw new RuntimeException("Unexpected kind of parent node. Please, submit the issue")
        }
      case _: jp.ast.`type`.TypeParameter =>
        Symbols.Global(owner, d.TypeParameter(symbolName))
      case coit: jp.ast.`type`.ClassOrInterfaceType =>
        val coitResolved = coit.resolve()
        coitResolved.getQualifiedName.replace('.', '/')
      case ne: jp.ast.expr.NameExpr =>
        try {
          ne.resolve() match {
            case jppd: jp.symbolsolver.javaparsermodel.declarations.JavaParserParameterDeclaration =>
              jppd.getWrappedNode.sym
            case jpsd: jp.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration =>
              jpsd.getWrappedNode.sym
            case jpfd: jp.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration =>
              jpfd.getVariableDeclarator.sym
            case _ =>
              throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
          }
        } catch {
          case ex: jp.resolution.UnsolvedSymbolException => ""
        }
      case mce: jp.ast.expr.MethodCallExpr =>
        mce.resolve() match {
          case rmd: jp.symbolsolver.reflectionmodel.ReflectionMethodDeclaration =>
            val siblingMethods = rmd.declaringType()
              .getAllMethods.asScala.toVector
              .filter { m => m.getName == rmd.getName }
              .sortBy { x => x.getDeclaration.isStatic }
            val methodPlace = siblingMethods.indexWhere { m =>
              rmd.getReturnType.toString == m.getDeclaration.getReturnType.toString &&
                rmd.getNumberOfParams == m.getDeclaration.getNumberOfParams
            }
            val disambig = (methodPlace: @switch) match {
              case -1 =>
                throw new RuntimeException("Unexpected state. Please, submit the issue")
              case 0 => "()"
              case x => s"(+$x)"
            }
            Symbols.Global(
              rmd.declaringType.getQualifiedName.replace('.', '/') + '#',
              d.Method(rmd.getName, disambig))
          case _ =>
            throw new RuntimeException("Unexpected kind of declaration. Please, submit the issue")
        }
      case _ =>
        throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
    }

    def kind: s.SymbolInformation.Kind = node match {
      case _: jp.ast.PackageDeclaration =>
        k.PACKAGE
      case _: jp.ast.`type`.TypeParameter =>
        k.TYPE_PARAMETER
      case _: jp.ast.body.FieldDeclaration =>
        k.FIELD
      case _: jp.ast.body.VariableDeclarator =>
        node.getParentNode.asScala match {
          case Some(_: jp.ast.expr.VariableDeclarationExpr) =>
            k.LOCAL
          case Some(_: jp.ast.body.FieldDeclaration) =>
            k.FIELD
          case _ =>
            throw new RuntimeException("Unexpected kind of parent node. Please, submit the issue")
        }
      case _: jp.ast.body.EnumConstantDeclaration =>
        k.FIELD
      case _: jp.ast.body.Parameter =>
        k.PARAMETER
      case elem: jp.ast.body.CallableDeclaration[_] =>
        if (elem.isConstructorDeclaration) {
          k.CONSTRUCTOR
        } else if (elem.isMethodDeclaration) {
          k.METHOD
        } else {
          sys.error(elem.toString)
        }
      case elem: jp.ast.body.ClassOrInterfaceDeclaration =>
        if (elem.isInterface) {
          k.INTERFACE
        } else {
          k.CLASS
        }
      case _: jp.ast.body.EnumDeclaration =>
        k.CLASS
      case _: jp.ast.body.AnnotationDeclaration =>
        k.INTERFACE
      case _: jp.ast.`type`.ClassOrInterfaceType =>
        k.TYPE
      case _: jp.ast.`type`.PrimitiveType =>
        k.TYPE
      case ne: jp.ast.expr.NameExpr =>
        try {
          ne.resolve() match {
            case jppd: jp.symbolsolver.javaparsermodel.declarations.JavaParserParameterDeclaration =>
              k.LOCAL
            case jpsd: jp.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration =>
              k.LOCAL
            case jpfd: jp.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration =>
              k.FIELD
            case _ =>
              throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
          }
        } catch {
          case ex: jp.resolution.UnsolvedSymbolException => k.UNKNOWN_KIND
        }
      case mce: jp.ast.expr.MethodCallExpr =>
        k.METHOD
      case n => sys.error(n.toString)
    }

    def role: s.SymbolOccurrence.Role = kind match {
      case k.LOCAL =>
        node match {
          case ne: jp.ast.expr.NameExpr =>
            ne.resolve() match {
              case jppd: jp.symbolsolver.javaparsermodel.declarations.JavaParserParameterDeclaration =>
                s.SymbolOccurrence.Role.REFERENCE
              case jpsd: jp.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration =>
                s.SymbolOccurrence.Role.REFERENCE
              case _ =>
                throw new RuntimeException("Unexpected kind of node. Please, submit the issue")
            }
          case _ =>
            s.SymbolOccurrence.Role.DEFINITION
        }
      case k.METHOD =>
        node match {
          case mce: jp.ast.expr.MethodCallExpr =>
            s.SymbolOccurrence.Role.REFERENCE
          case cd: jp.ast.body.CallableDeclaration[_] =>
            s.SymbolOccurrence.Role.DEFINITION
        }
      case k.PACKAGE | k.FIELD | k.TYPE_PARAMETER | k.CONSTRUCTOR | k.INTERFACE |
           k.CLASS | k.PARAMETER =>
        s.SymbolOccurrence.Role.DEFINITION
      case k.TYPE =>
        s.SymbolOccurrence.Role.REFERENCE
      case _ =>
        s.SymbolOccurrence.Role.UNKNOWN_ROLE
    }
  }
}
