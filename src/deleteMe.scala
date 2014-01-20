import scala.util.matching.Regex

val cliRegex=new scala.util.matching.Regex("""-(\w+)\s+(\"([\w ]+)\"|\w+)?""","command","value","quotedValue")
var cmd="-a foo -c \"lorem ipsum dolor\" -g"

val res=for(m<-cliRegex findAllMatchIn(cmd)) yield {
	val value=if ((m group "quotedValue" )!=null){m group "quotedValue"} else{ m group "value"}
	(m group "command",value)
}
for(r<-res){
	println(r)
}