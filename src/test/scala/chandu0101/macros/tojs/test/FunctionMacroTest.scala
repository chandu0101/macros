package chandu0101.macros.tojs.test

import chandu0101.macros.tojs.{exclude, rename, FunctionMacro}
import org.scalatest.FunSuite

import scala.scalajs.js
import scala.scalajs.js.JSON


class FunctionMacroTest extends FunSuite {

  def Plain(name: String, category: String,  peracre: js.UndefOr[Int] = js.undefined, address: Address = null): js.Object = {
    val p = FunctionMacro()
    p
  }

  def PlainKeys(@rename("custom_name") name: String, category: String, peracre: js.UndefOr[Int] = js.undefined, address: Address = null): js.Object = {
    val p = FunctionMacro()
    p
  }

  def SeqTest(s: Seq[String] = Seq("dude"), as: Seq[Address] = Seq(Address("India"), null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def SeqUndefTest(s: js.UndefOr[Seq[String]] = Seq("dude"), as: js.UndefOr[Seq[Address]] = Seq(Address("India"), null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def SetTest(s: Set[String] = Set("dude"), as: js.UndefOr[Set[Address]] = Set(Address("India"), null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def ArrayTest(s: Array[String] = Array("dude"), as: js.UndefOr[Array[Address]] = Array(Address("India"), null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def MapTest(m: Map[String, String] = Map("key" -> "0"), ma: js.UndefOr[Map[String, Address]] = Map("address" -> Address("India"), "address2" -> null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def JSDictTest(m: js.Dictionary[String] = js.Dictionary("key" -> "0"), ma: js.UndefOr[js.Dictionary[Address]] = js.Dictionary("address" -> Address("India"), "address2" -> null)): js.Object = {
    val p = FunctionMacro()
    p
  }

  def FunctionTest(fn0: () => Int = () => 5, fn1: js.UndefOr[Double => String] = (d: Double) => s"$d x"): js.Object = {
    val p = FunctionMacro()
    p
  }

 def TPTest[T <: SelectOption](o : js.UndefOr[js.Array[T]]): js.Object = {
   val p = FunctionMacro()
   p
 }

 def AnyValTest2(st: SeedType2 = SeedType2.RICE): js.Object = {
   val p = FunctionMacro()
   p
 }

  def ExcludeTest(name : String= "Hello",@exclude age : Int = 1) : js.Object = {
    val p = FunctionMacro()
    p
  }

  def printResult(result: js.Any) = {
    println(s"Result is : ${JSON.stringify(result)}")
  }

  test("simple fields test") {
    val plain = (Plain("bpt", "rice")).asInstanceOf[js.Dynamic]
    assert(plain.name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("simple fields test with custom field names") {
    val plain = PlainKeys("bpt", "rice").asInstanceOf[js.Dynamic]
    assert(plain.custom_name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("should handle seq") {
    val result = SeqTest().asInstanceOf[js.Dynamic]
    println(s"result array ${JSON.stringify(result)}")
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")

    val result2 = SeqUndefTest().asInstanceOf[js.Dynamic]
    println(s"result2 array ${JSON.stringify(result2)}")
    assert(result2.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result2.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }
  test("should handle sets") {
    val result = SetTest().asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }


  test("should handle arrays") {
    val result = ArrayTest().asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(result.as.asInstanceOf[js.Array[js.Dictionary[String]]].head("country") == "India")
  }

  test("should handle maps") {
    val result = MapTest().asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(result.ma.asInstanceOf[js.Dictionary[js.Dynamic]].get("address").get.country.toString == "India")
  }

  test("should handle js.Dictionary") {
    val result = JSDictTest().asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(result.ma.asInstanceOf[js.Dictionary[js.Dynamic]].get("address").get.country.toString == "India")
  }


  test("should handle functions") {
    val result = FunctionTest().asInstanceOf[js.Dynamic]
    assert(result.fn0.asInstanceOf[js.Function0[Int]]() == 5)
    assert(result.fn1.asInstanceOf[js.Function1[Double, String]](1.2) == "1.2 x")
  }

    test("should handle any vals") {
      val result = AnyValTest2().asInstanceOf[js.Dynamic]
      assert(result.st.toString == "rice")
    }

    test("should work with custom traits") {
      val result = TPTest(js.Array(SampleOption()))
      printResult(result)
    }

   test("should not include excluded fields") {
      val result = ExcludeTest().asInstanceOf[js.Dynamic]
      assert(result.name.toString == "Hello")
      assert(js.isUndefined(result.age))
    }


}
