package game.basics
/**
*@fixme remover listbuffer e resolver só com list?
*/
import scala.collection.mutable.ListBuffer
class Game(val name:String,val numTeams:Int,val maxScore:Int){
	protected val teamList=Array.fill(numTeams){new Team()}
	protected var joinCounter=0
	protected var roundCounter=0
	protected var currentTeam:Int=0
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
	/**
	 * Inicia a proxima rodada
	 */
	protected def _nextRound(){
		this.currentTeam=this.roundCounter
		val p:Player=teamList(this.currentTeam).getNextPlayer()
		//Pega o próximo jogador a falar a palavra
		this.roundCounter=(this.roundCounter+1)%this.numTeams

	}
	/**
	 * Achou uma palavra com sucesso definitivamente
	 */
	def foundWord(){
		if(teamList(this.currentTeam).score()==this.max){
			this._endGame(this.currentTeam)
		}
		else{
			this._nextRound
			
		}

	}
	/**
	 * @todo implementar
	 */
	def _endGame(winnerIndex:Int){

	}
	/**
	 * Falou a palavra Tabu TODOS outros times acertam
	 */
	def tabu(){
		for(i<-0 until this.numTeams;){
			if(this.teamList(i).score()==this.max){
				this._endGame(i)
			}
			else{
				this._nextRound
				
			}

		}
	}

	class Team{
		protected var _points:Int=0
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
		def score()={
			this._points+=1
		}
		def getNextPlayer():Player={
			val pos=this.position
			this.position=(this.position+1)%this.players.length
			return this.players(pos)
		}
	}

	
}	

