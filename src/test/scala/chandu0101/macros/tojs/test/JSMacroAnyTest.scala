package chandu0101.macros.tojs.test

import chandu0101.macros.tojs.{JSMacro, JSMacroAny}
import org.scalatest.FunSuite

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.JSON


case class FunctionTest2(fn0: js.Function0[Int] = () => 5, fn1: js.Function1[Double,String] = (d: Double) => s"$d x")

case class ScalaAnyTest[T](val x : T)

class JSMacroAnyTest extends FunSuite {

  test("simple fields test") {
    val plain = JSMacroAny[Plain](Plain("bpt", "rice")).asInstanceOf[js.Dynamic]
    assert(plain.name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("simple fields test with custom field names") {
    val plain = JSMacroAny[PlainKeys](PlainKeys("bpt", "rice")).asInstanceOf[js.Dynamic]
    assert(plain.custom_name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("should handle seq") {
    val result = JSMacroAny[SeqTest](SeqTest()).asInstanceOf[js.Dynamic]
    println(s"result array ${JSON.stringify(result)}")
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")

    val result2 = JSMacroAny[SeqUndefTest](SeqUndefTest()).asInstanceOf[js.Dynamic]
    println(s"result2 array ${JSON.stringify(result2)}")
    assert(result2.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result2.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }

  test("should handle sets") {
    val result = JSMacroAny[SetTest](SetTest()).asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }


  test("should handle arrays") {
    val result = JSMacroAny[ArrayTest](ArrayTest()).asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }

  test("should handle maps") {
    val result = JSMacroAny[MapTest](MapTest()).asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(result.ma.asInstanceOf[js.Dictionary[js.Dynamic]].get("address").get.country.toString == "India")
  }

  test("should handle js.Dictionary") {
    val result = JSMacroAny[JSDictTest](JSDictTest()).asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(result.ma.asInstanceOf[js.Dictionary[js.Dynamic]].get("address").get.country.toString == "India")
  }


  test("should handle functions") {
    val result = JSMacroAny[FunctionTest2](FunctionTest2()).asInstanceOf[js.Dynamic]
    assert(result.fn0.asInstanceOf[js.Function0[Int]]() == 5)
    assert(result.fn1.asInstanceOf[js.Function1[Double, String]](1.2) == "1.2 x")
  }

  test("should handle any vals") {
    val result = JSMacroAny[AnyValTest2](AnyValTest2()).asInstanceOf[js.Dynamic]
    assert(result.st.toString == "rice")
  }

  test("should handle scala any") {
    val result = JSMacroAny[ScalaAnyTest[Any]](ScalaAnyTest(3)).asInstanceOf[js.Dynamic]
    assert(result.x.toString == "3")
  }


}
