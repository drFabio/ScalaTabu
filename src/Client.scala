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
  	protected var _connId:Int=_
	_currentRole=new GameHallPlayerCommand(this)
	_currentRole.start
	def sendMessage(str:String){
		this.sendMessage(_currentRole.asInstanceOf[CLICommandActor].handleMessage(str))
	}
	override def handleCommand(input:AbstractCommand)={
		input match{
			case wh:setup.WhoAreYou=>{
				_connId=wh.connId
			}
		}
	} 
	def getConnId()={
		_connId
	}

 }
abstract class PlayerCLICommandActor(val pa:PlayerActor) extends CLICommandActor{
	def createMessage(mess:String):Message={
		new Message(mess,Some(pa.name),Some(pa.getConnId))
	}
 	def help(){
 		
 	}
}
/**
 * Papeis do jogador quando ele está no gameHall
 * @type {[type]}
 */
 class GameHallPlayerCommand( pa:PlayerActor) extends PlayerCLICommandActor(pa){
	
 	def interpretCommand(cmd:List[(String,Option[String])]):AbstractCommand={
 		cmd match{
 				case (h,_)::_ if (h=="h")=>new Help()
 				case (l,_)::_ if (l=="l")=>new gameHall.List()
 				case (c,_)::_ if (c=="c")=> gameHall.CreateGame.factory(cmd,pa.name,pa.getConnId)
 				case (j,gameId:Some[String])::_ if (j=="j")=> new gameHall.JoinGame(gameId.get.toInt,pa.name,pa.getConnId)
 		}
 	}

	def executeCommand(cmd:AbstractCommand){
		cmd match{
			case wh:setup.WhoAreYou=>{
				sender ! (new setup.WhoAreYou(wh.connId) with InternalCommand)
				sender ! new Reply(new setup.IAm(pa.name))
			}
			case c:gameHall.CreatedGame=>{
				display(c)
				sender ! new GamePlayerCommand(pa)
				sender ! new Reply(new game.IAmReady(pa.getConnId))
			} 
			case c:gameHall.JoinedGame=>{
				display(c)
				sender ! new GamePlayerCommand(pa)
				sender ! new Reply(new game.IAmReady(pa.getConnId))

			}
			case m:Message=>{
				dísplay(m)
			}
		
		}
		
	}
 }
/**
 * Papeis do jogador quando ele está no jogo em si
 * @type {[type]}
 */
class GamePlayerCommand( pa:PlayerActor) extends PlayerCLICommandActor(pa){
	def interpretCommand(cmd:List[(String,Option[String])]):AbstractCommand={
		cmd match{
			case (h,_)::_ if (h=="h")=>new Help()
		}

	}
	def executeCommand(cmd:AbstractCommand){
		cmd match{
			case m:Message=>{
				dísplay(m)
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