import scala.util.matching.Regex

def foo(a:String,b:String){
	def bar(c:String)={
		println(a+b)
	}
	bar(a)
}

foo("AA","CC")