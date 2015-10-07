# macros

A few helper macros 

# JSMacro :

It converts scala case class/class primary constructor fields to js object 

Example : 

```scala

 case class Test(x: String,@rename("y1") y: Int) {
   def toJS = JSMacro[Test](this)
 }
 
 val t = Test("hello",5)
 
 > t.toJS
 { x : "hello", y1 : 5}

```