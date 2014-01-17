import java.net._
import java.io._
import scala.io._
import game.io._
import scala.actors.Actor
import scala.actors.Actor._
import game.basics._
/**
* Ator que cuida da obtenção de comandos vindo do socket
*/
class receiverActor(gh:GameHall,protected val sock:Socket,protected val connId:Int) extends {
	//Early initializer
}
 with SocketActor{
	handler=gh
 	def sendHello(){
		this.sendMessage("Bem vindo! voce esta conectado , digite -h para ajuda")
		this.sendMessage("-getName")
	}
	this.sendHello()
 }
object TabuServer{
	def main(args:Array[String])={
		/**
		 * @todo pegar argumento para numero de pessoas
		 */
		val gh:GameHall=new GameHall
		gh.start
		try{
			val port=1337
			println("Iniciando servidor na porta "+port)
		//	val gh=new GameHall
			val server = new ServerSocket(port)
			var connId=0
			while (true) {
			    val s = server.accept()
			    println("Recebido conexão")
			    val a =new receiverActor(gh,s,connId)
			    a.start()
			    connId+=1
			}
		}
		catch{
			case e:Throwable=>{
				e.printStackTrace()
			}
		}
	}
}