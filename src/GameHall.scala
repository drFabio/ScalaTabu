package game.basics
import game.io.CommandActor
import scala.actors.Actor
import scala.actors.Actor._
import game.io.commands._

/**
* Holds the several players 
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
			case c:gameHall.IAm =>{
				println("Recebi o nome " +c.name)
			}

			case c:gameHall.CreateGame => {

				val g:Option[Game]=createGame(c.gameName,c.numTeams,c.maxScore)
				if(g.isEmpty){
					sender ! new Reply(new ErrorMessage("Não foi possível criar seu jogo, tente novamente mais tarde"))
				}
				else{
					/*val ga=g.get
					ga.start
					sender ! ga*/
					sender ! new Reply(new Message("Jogo criado com sucesso!"))
				}
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
	def createGame( name:String, numTeams:Int, maxScore:Int):Option[Game]={
		if(_gamesOn==this.size){
			println("SALA LOTADA")
			return None
		}
		for((x,i) <- _gameBuffer.view.zipWithIndex if x==null){
			
			_gamesOn+=1
			_gameBuffer(i)=new Game(i,name,numTeams,maxScore)
			return Some(_gameBuffer(i))
		
		}
		return None
	}
	
	def createGame(name:String):Option[Game]={
		createGame(name,2,10)
	}
	/**
	 * @todo finalizar jogo
	 */

}
	
object GameHall{

}