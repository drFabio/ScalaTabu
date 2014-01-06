import java.net._
import java.io._
import scala.io._
import game.io._
//import  game.basics._
/**
* Ator que cuida da obtenção de comandos vindo do socket
*/
class PlayerActor(protected val sock:Socket) extends {

}
 with SocketActor
object TabuClient{
	def main(args:Array[String]){
		try{
			println("Inicializado um cliente")
			val port=1337
			val s = new Socket(InetAddress.getByName("localhost"), port)
			val out = new PrintStream(s.getOutputStream())
			//Inicializa o ator que cuida dos sockets
			val a=new PlayerActor(s)
			a.start()
			//Le indefinidamente do input
			for (line <- io.Source.stdin.getLines){
				a.sendMessage(line)
			}
			s.close()
		}
		catch{
			case e:Throwable=>{
				e.printStackTrace()
			}
		}
	}	
}