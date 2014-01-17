import java.net._
import java.io._
import scala.io._
import game.io._
//import  game.basics._
/**
* Ator que cuida da obtenção de comandos vindo do socket
*/
class PlayerActor(protected val sock:Socket,val name:String) extends{
	
}
  with SocketActor{
	handler=new PlayerCommand(name)
	handler.start

 }
 class PlayerCommand(val name:String) extends CommandActor{
 	def help(){
 		
 	}
	def executeCommand(cmdList:List[(String,Option[String])]){
		cmdList match {
			case (p,_)::_ if (p=="getName")=>{
				sender ! "!-iAm "+name
			}

			case (p,index)::_ if (p=="cg")=>{
				println("Jogo criado com o n "+index.get)
				/**
				 * @todo e agora?
				 */
			}
		}
	}
 }
object TabuClient{
	def main(args:Array[String]){
		try{
			println("Por favor digite seu nome")
			val name=readLine()
			/**
			 * @todo pegar do argumento
			 */
			val port=1337
			val s = new Socket(InetAddress.getByName("localhost"), port)
			
			//Inicializa o ator que cuida dos sockets
			val a=new PlayerActor(s,name)
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