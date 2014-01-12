package game.basics

/**
* Holds the several players 
*/
class GameHall(val size:Int){
	protected var gameBuffer:List=Nil
	println("Salao de jogos inciado com uma capacidade para "+size+" jogos")
	def this()=this(10)

	/**
	* Lista as Salas
	*/
	def list()={
		
	}
	def createGame(val name:String,val numTeams:Int,val maxScore:Int):Option={
		if(this.gameBuffer.length==this.size){
			return None
		}
		val g=new Game(name,numTeams,maxScore)
		this.gameBuffer=g::this.gameBuffer
		return Some(g)
	}
	def createGame(val name:String,val numTeams:Int)={
		createGame(name,numTeams,10)
	}
	def createGame(val name:String):Option={
		createGame(name)
	}

}
	
object GameHall{

}