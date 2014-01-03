package game.tabu

/**
* Holds the several players 
*/
class GameHall(size:Int){
	protected val gameBuffer:Array[Game]=new Array[Game](size)
	println("Salao de jogos inciado com uma capacidade para "+size+" jogos")
	def this()=this(10)

	/**
	* Lista as Salas
	*/
	def list()={
		
	}
}
	
object GameHall{

}