package game.basics
import game.io.CommandActor
import scala.actors.Actor
import scala.actors.Actor._

/**
* Holds the several players 
*/
class GameHall(val size:Int) extends CommandActor{
	override def help(){
		println("HELP OVERRADADO HUEHUHEUHEUHEUH")
	}
	def executeCommand(cmdList:List[(String,Option[String])]){
		println("NO execute command do gameHall")
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
	def list()={
		
	}
	/**
	 * Cria um novo jogo com o numero de times e o score desejado
	 * @type {[type]}
	 */
	def createGame( name:String, numTeams:Int, maxScore:Int):Option[Game]={
		if(_gamesOn==this.size){
			return None
		}
		for((x,i) <- _gameBuffer.view.zipWithIndex){
			if(i==null){
				_gamesOn+=1
				_gameBuffer(i)=new Game(i,name,numTeams,maxScore)
				return Some(_gameBuffer(i))
			}
		}
		return None
	}
	def createGame( name:String, numTeams:Int):Option[Game]={
		createGame(name,numTeams,10)
	}
	def createGame(name:String):Option[Game]={
		createGame(name)
	}
	/**
	 * @todo finalizar jogo
	 */

}
	
object GameHall{

}