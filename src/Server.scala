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
class receiverActor(gh:GameHall,protected val sock:Socket,val connId:Int)  extends {} with SocketActor{
 	protected var _name:String=null
	_currentRole=gh
	/**
	*Envia a mensagem de boas vindas 
	*/
 	def sendHello(name:String){
		
		this.sendMessage(new commands.Message("Bem vindo "+name+"! voce esta conectado , digite -h para ajuda"))
	}
	override def handleInput(input:commands.AbstractCommand)={
		input match {
			case c:commands.setup.IAm=>{
				sendHello(c.name)
			}
			case _=>{
				super.handleInput(input)
			}
		}
	}
	this.sendMessage(new commands.setup.WhoAreYou(connId))
 }
/**
 * Servidor de tabu criando um receiverActor para cada jogador
 */
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