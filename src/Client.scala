import java.net._
import java.io._
import scala.io._
import game.io._
import game.io.commands._
//import  game.basics._
/**
* Ator que cuida da obtenção de comandos vindo do socket
*/
class PlayerActor(protected val sock:Socket,val name:String) extends{

}
  with SocketActor{
	_currentRole=new GameHallPlayerCommand(name)
	_currentRole.start
	def sendMessage(str:String){
		this.sendMessage(_currentRole.asInstanceOf[CLICommandActor].handleMessage(str))
	}

 }
 class GameHallPlayerCommand(val name:String) extends CLICommandActor{
 	def interpretCommand(cmd:List[(String,Option[String])]):AbstractCommand={
 		cmd match{
 				case (h,_)::_ if (h=="h")=>new Help()
 				case (l,_)::_ if (l=="l")=>new gameHall.List()
 				case (c,_)::_ if (c=="c")=> gameHall.CreateGame.factory(("cn",Some(name))::cmd)
 				case (j,gameId:Some[String])::_ if (j=="j")=> new gameHall.JoinGame(gameId.get.toInt,name)
 		}
 	}
 	def help(){
 		
 	}
	def executeCommand(cmd:AbstractCommand){
		cmd match{
			case wh:setup.WhoAreYou=>{
				sender ! new Reply(new setup.IAm(name))
			}
			case _=>{
				println("RECEBI OUTRA COISA!")
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
			for (line <- io.Source.stdin.getLines if line!=null){
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