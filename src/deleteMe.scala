val a:Array[String]=new Array[String](10)
for((x,i) <- a.view.zipWithIndex) {
	if(i==6){

		a(i)="MIMIMIMI"
	}
	println("String #" + i + " is " + x)
}

for((x,i) <- a.view.zipWithIndex) {
	println("String #" + i + " is " + x)
}