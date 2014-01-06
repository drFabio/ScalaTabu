import java.net._
import java.io._
import scala.io._

object TabuClient{
	def main(args:Array[String]){
		try{
			println("Inicializado um cliente")
			val port=1337
			val s = new Socket(InetAddress.getByName("localhost"), port)
			lazy val in = new BufferedSource(s.getInputStream()).getLines()
			val out = new PrintStream(s.getOutputStream())
			out.println("Hello, world")
			out.flush()
			println("Received: " + in.next())
			s.close()
		}
		catch{
			case e:Throwable=>{
				e.printStackTrace()
			}
		}
	}	
}