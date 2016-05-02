# macros

A few helper macros 

```scala
libraryDependencies += "com.github.chandu0101" %%% "macros" % "2016.5.0"
```

# JSMacro :

It converts scala case class/class primary constructor fields to js object 

Example : 

```scala

 case class Test(x: String,@rename("y1") y: Int,@exclude z : String) {
   def toJS = JSMacro[Test](this)
 }
 
 > val t = Test("hello",5,"invisble")
 
 > t.toJS
 { x : "hello", y1 : 5}

```
***````-
# FunctionMacro :

It converts function params to javascript object


Example : 

```scala

 def Test(x: String,@rename("y1") y: Int,@exclude z : String): js.Object {
   val p = FunctionMacro()
   p
 }
 
 > val t = Test("hello",5,"invisible")
 
 > t
 { x : "hello", y1 : 5}

```