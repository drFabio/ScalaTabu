package game.basics
import game.io.commands._


/**
*@fixme remover listbuffer e resolver só com list?
*/
import scala.collection.mutable.ListBuffer
class Game(val creator:Player,val index:Int,val name:String,val numTeams:Int,val maxScore:Int) extends _root_.game.io.CommandActor{
	def help(){
	}
	def executeCommand(cmd:AbstractCommand){
	}
	protected val _teamList=Array.fill(numTeams){new Team()}
	protected var _joinCounter=0
	protected var _roundCounter=0
	protected var _currentTeam:Int=0
	//this.join(this.creator)
	/**
	* Jogador entra 
	*/
	def join(p:Player){
		p.teamId=Some(this._joinCounter)
		this._teamList(this._joinCounter)+p
		this._joinCounter=(this._joinCounter+1)%this._teamList.length
	}


	/**
	* Jogador sai
	*/
	def leave(p:Player){
		this._teamList(p.teamId.get)-p
	}
	/**
	 * Inicia a proxima rodada
	 */
	protected def _nextRound(){
		this._currentTeam=this._roundCounter
		val p:Player=_teamList(this._currentTeam).getNextPlayer()
		//Pega o próximo jogador a falar a palavra
		this._roundCounter=(this._roundCounter+1)%this.numTeams

	}
	/**
	 * Achou uma palavra com sucesso definitivamente
	 */
	def foundWord(){
		if(_teamList(this._currentTeam).score()==this.maxScore){
			this._endGame(this._currentTeam)
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
		for(i<-0 until this.numTeams){
			if(this._teamList(i).score()==this.maxScore){
				this._endGame(i)
			}
			else{
				this._nextRound
				
			}

		}
	}
	override def toString()={
		index+" - "+name
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
		def score():Int={
			this._points+=1
			return this._points
		}
		def getNextPlayer():Player={
			val pos=this.position
			this.position=(this.position+1)%this.players.length
			return this.players(pos)
		}
	}
	
}	

