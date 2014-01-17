package game.basics
import game.io.CommandActor
import scala.actors.Actor
import scala.actors.Actor._

/**
* Holds the several players 
*/
class GameHall(val size:Int) extends CommandActor{
	def help(){
		sender ! ("!Digite -l para listar \n -j [Numero do jogo] para entrar num jogo \n -c [Nome do jogo] (-n [Numero de times] (-p [Pontos para vencer]))")
	}
	def executeCommand(cmdList:List[(String,Option[String])]){
		cmdList match {
			case (p,_)::_ if p=="l" => sender ! "!"+(list getOrElse("Não existem jogos, crie um novo"))
			case (p,name)::_ if p=="iAm" => sender ! println("RECEBI O NOME DO JAGUNCO E E "+name)

			case (p,name)::args if p=="c" => {

				val g:Option[Game]=if(args==Nil){
					createGame(name.get)
				}
				else{
					var numTeams=2
					var maxScore=10
					for(t<-args){
						if(t._1=='n'){
							numTeams=t._2.get.toInt
						}
						else if(t._1=='p'){
							maxScore=t._2.get.toInt
						}
					}
					createGame(name.get,numTeams,maxScore)
				}
				if(g.isEmpty){
					sender ! "!Não foi possível criar seu jogo, tente novamente mais tarde"
				}
				else{
					val ga=g.get
					ga.start
					sender ! ga
					sender ! "!-cg "+ga.index
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