package chandu0101.macros.tojs


import scala.collection.{GenMap, GenTraversableOnce}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.scalajs.js


/**
 *  Big thanks to @ddispaltro  for this macro :)
 */
object FunctionMacro {

  val REGULAR = "regular"

  val UNDEFS = "undefs"

  type TOJS = {
    val toJS: js.Object
  }

  def apply(): js.Object = macro macroImpl

  def macroImpl(c: blackbox.Context)(): c.Tree = {
    import c.universe._
    val f = c.internal.enclosingOwner

    if (!f.owner.isMethod) {
      c.abort(c.enclosingPosition, "This macro can only be used directly within a function.")
    }
    val p = f.owner.asMethod.paramLists

    if (p.isEmpty) {
      c.abort(c.enclosingPosition, "This macro can only be used in a function that has at least one parameter list with one item.")
    }

    def isNotPrimitiveAnyVal(tpe: Type) = {
      !tpe.typeSymbol.fullName.startsWith("scala.")
    }

    def customName(sym: c.Symbol): Option[String] = {
      sym.annotations
        .find(_.tree.tpe == typeOf[rename])
        .flatMap(_.tree.children.tail.headOption)
        .map{case Literal(Constant(s)) => s.toString}
    }


    val finalTerm = TermName(c.freshName())
    val x = p.head
    val rawParams = p.head.filterNot(s => s.annotations.exists(a => a.tree.tpe == typeOf[exclude])).map { field =>
      val name = field.asTerm.name
      val decoded = customName(field).getOrElse(name.decodedName.toString)
      val symToJs = typeOf[TOJS].typeSymbol
      val returnType = field.typeSignature

      def getJSValueTree(returnType: Type, undef: Boolean = false) = {
        if (returnType <:< typeOf[TOJS])
          if (undef) q"""v.toJS""" else q"""if($name == null) null else $name.toJS"""
        else if (returnType <:< typeOf[Enumeration#Value])
          if (undef) q"""v.toString()""" else q"""$name.toString()"""
        else if (returnType <:< typeOf[AnyVal] && isNotPrimitiveAnyVal(returnType))
          if (undef) q"""v.value""" else q"""$name.value"""
        else if (returnType <:< typeOf[GenMap[String, _]] &&
          returnType.typeArgs(1) <:< typeOf[TOJS])
          if (undef) q"""v.map{ case (k, o) => k -> (if(o == null) null else o.toJS)}.toJSDictionary""" else q"""$name.map{ case (k, o) => k -> (if(o == null) null else o.toJS) }.toJSDictionary"""
        else if (returnType <:< typeOf[GenMap[String, _]])
          if (undef) q"""v.toJSDictionary""" else q"""$name.toJSDictionary"""
        else if ((returnType <:< typeOf[GenTraversableOnce[_]] ||
          returnType <:< typeOf[Array[_]]) &&
          returnType.typeArgs.head <:< typeOf[TOJS])
          if (undef) q"""v.map((o: $symToJs) => if(o == null) null else o.toJS).toJSArray""" else q"""$name.map((o: $symToJs) => if(o == null) null else o.toJS).toJSArray"""
        else if (returnType <:< typeOf[GenTraversableOnce[_]] ||
          returnType <:< typeOf[Array[_]])
          if (undef) q"""v.toJSArray""" else q"""$name.toJSArray"""
        else if (returnType <:< typeOf[js.Dictionary[_]] &&
          returnType.typeArgs.head <:< typeOf[TOJS])
          if (undef) q"""v.map{ case(k, o) => (k, if(o == null) null else o.toJS)}.toJSDictionary""" else q"""$name.map{ case (k: String, o: $symToJs) => (k, if(o == null) null else o.toJS)}.toJSDictionary"""
        else if (returnType <:< typeOf[js.Array[_]] &&
          returnType.typeArgs.head <:< typeOf[TOJS])
          if (undef) q"""v.map((o: $symToJs) => if(o == null) null else o.toJS)""" else q"""$name.map((o: $symToJs) => if(o == null) null else o.toJS)"""
        else if (undef) q"""v""" else q"""$name"""
      }

      if (returnType <:< typeOf[Option[_]] ||
        returnType <:< typeOf[js.UndefOr[_]]) {
        val arg0 = returnType.typeArgs(0)
        val valueTree = getJSValueTree(arg0, true)
        (UNDEFS, q"""$name.foreach(v => $finalTerm.updateDynamic($decoded)($valueTree))""")
      } else {
        val jsValueTree = getJSValueTree(returnType)
        (REGULAR, q"""$decoded -> $jsValueTree """)
      }

    }

    def getTrees(key: String) = rawParams.filter(_._1 == key).map(_._2)

    val params = getTrees(REGULAR)
    val undefs = getTrees(UNDEFS)

    val result = q"""
      {
        import scala.language.reflectiveCalls
        import scalajs.js.JSConverters._
        val $finalTerm = scala.scalajs.js.Dynamic.literal(..$params)
        ..$undefs
        $finalTerm
      }
    """

    result
  }

}