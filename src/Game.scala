package game.basics
/**
*@fixme remover listbuffer e resolver só com list?
*/
import scala.collection.mutable.ListBuffer
class Game(val name:String,val numTeams:Int){
	def this(name:String)=this(name,2)
	protected val teamList=Array.fill(numTeams){new Team()}
	protected var joinCounter=0
	protected var roundCounter=0
	/**
	* Jogador entra 
	*/
	def join(p:Player){
		p.teamId=Some(this.joinCounter)
		this.teamList(this.joinCounter)+p
		this.joinCounter=(this.joinCounter+1)%this.teamList.length
	}
	/**
	* Jogador sai
	*/
	def leave(p:Player){
		this.teamList(p.teamId.get)-p
	}

	def nextRound(){
		val playingTeam=this.roundCounter
		val p:Player=teamList(playingTeam).getNextPlayer()
		//Pega o próximo jogador a falar a palavra
		this.roundCounter=(this.roundCounter+1)%this.numTeams

	}

	class Team{
		var points:Int=0
		var players:ListBuffer[Player]=ListBuffer()
		var position:Int=0
		def +(p:Player){
			this.players+=p
		}
		def -(p:Player){

			val index=this.players.indexOf(p)
			if(this.position>index){
				this.position-=1
			}	
			this.players.remove(index)
		}

		def getNextPlayer():Player={
			val pos=this.position
			this.position=(this.position+1)%this.players.length
			return this.players(pos)
		}
	}

	
}	

