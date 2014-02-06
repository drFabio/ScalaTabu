package game.basics
import game.io.CommandActor
import scala.actors.Actor
import scala.actors.Actor._
import game.io.commands._

/**
* Salaão de jogos
*/
class GameHall(val size:Int) extends CommandActor{
	def help(){
		sender ! new Reply(new Message("Digite -l para listar \n -j [Numero do jogo] para entrar num jogo \n -c [Nome do jogo] (-n [Numero de times] (-p [Pontos para vencer]))"))
	}
	def executeCommand(cmd:AbstractCommand){
		cmd match {
			case c:gameHall.List=>{
				sender ! new Reply(new Message(list getOrElse("Não existem jogos, crie um novo")))
			}
	

			case c:gameHall.CreateGame => {

				val g:Option[Game]=createGame(sender.asInstanceOf[Actor],c.gameName,c.numTeams,c.maxScore,c.creatorName,c.connId)
				if(g.isEmpty){
					sender ! new Reply(new ErrorMessage("Não foi possível criar seu jogo, tente novamente mais tarde"))
				}
				else{
					val ga=g.get
					ga.start
					sender ! ga
					sender ! new Reply(new gameHall.CreatedGame("Jogo criado com sucesso!"))
				}
			}
			case c:gameHall.JoinGame=>{
				if(_gameBuffer(c.gameId)==null){
					throw new IllegalArgumentException("O jogo numero "+c.gameId+" não existe mais")
				}
				
				_gameBuffer(c.gameId).join(new Player(sender.asInstanceOf[Actor],c.playerName,c.connId))
				sender ! _gameBuffer(c.gameId)
				sender ! new Reply(new gameHall.JoinedGame("Entrou no jogo "+_gameBuffer(c.gameId).name+" com sucesso!"))

			}
			case _=>{

			}

		}
	}
	/**
	 * Jogos que serão criados
	 * @type {[type]}
	 */
	protected val _gameBuffer:Array[Game]=new Array[Game](size)
	protected var _gamesOn=0

	println("Salao de jogos inciado com uma capacidade para "+size+" jogos")
	def this()=this(10)

	/**
	* Lista as Salas
	*/
	def list():Option[String]={
		var ret:String=""
		for(g<-_gameBuffer if g !=null){
			if(ret!=""){
				ret+="\n"
			}
			ret+=g
		}
		return if(ret==""){
			None
		}
		else{
			Some(ret)
		}
	}
	/**
	 * Cria um novo jogo com o numero de times e o score desejado
	 * @type {[type]}
	 */
	def createGame(creatorActor:Actor,name:String, numTeams:Int, maxScore:Int,creatorName:String,creatorConnId:Int):Option[Game]={
		if(_gamesOn==this.size){
			return None
		}
		for((x,i) <- _gameBuffer.view.zipWithIndex if x==null){
			
			_gamesOn+=1
			_gameBuffer(i)=new Game(new Player(creatorActor,creatorName,creatorConnId),i,name,numTeams,maxScore)
			return Some(_gameBuffer(i))
		
		}
		return None
	}
	
	

}
