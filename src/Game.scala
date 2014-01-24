package game.basics
import game.io.commands._
import  cards.TabuCard
import  cards.TabuDeck

class Game(val creator:Player,val index:Int,val name:String,val numTeams:Int,val maxScore:Int) extends _root_.game.io.CommandActor{
	protected val _teamList:Array[Team]=Array.fill(numTeams){new Team()}
	protected var _joinCounter=0
	protected var _roundCounter=0
	protected var _currentTeam:Int=0
	protected var _status=Status.WaitingPlayers
	protected var _cards:List[TabuCard]=Nil
	protected var _currentPlayer:Option[Player]=None
	protected var _currentCard:Option[TabuCard]=None
	protected var _connectionPlayerMap:Map[Int,Player]=Map()
	
	def help(){
		sender ! new Reply(new Message("Digite -l para listar times e jogadores \n -t na vez de outro time para declarar que foi uma palavra tabu \n -c Na sua vez de dizer as palavras para falar que o time acertou"))
	}
	def executeCommand(cmd:AbstractCommand){
		cmd match{
			case c:game.IAmReady=>{
				_connectionPlayerMap(c.connId).isReady=true
				if(	_status==Status.Started){
					_sendPlayerMessage(_connectionPlayerMap(c.connId))
					println("JA ESTAVA OMOCOADP")
				}
				else if(_status==Status.WaitingPlayers && _canGameStart()){
					_startGame()
					println("ERA P INICIAR")
				}
				else{
					println("N PODE INICIAR")
				}
			}
		}
	}

	this.join(this.creator)
	/**
	* Jogador entra 
	*/
	def join(p:Player){
		_connectionPlayerMap+=(p.connId->p)
		p.teamId=this._joinCounter
		this._teamList(this._joinCounter)+p
		this._joinCounter=(this._joinCounter+1)%this._teamList.length
		
		
	}
	/**
	 * Checa se o jogo pode iniciar
	 * Se todos times tem mais do que o minímo de jogadores
	 */
	protected def _canGameStart():Boolean={
		for(t<-this._teamList){
			if(t.getNumPlayers() < (Game.minPlayers)){
				return false
			}		
			var readyCount=0
			for(p<-t.getPlayers if p.isReady){
				readyCount+=1
			}
			if(readyCount<(Game.minPlayers)){
				return false
			}
		}
		return true
	}
	/**
	 * Inicia o jogo
	 */
	protected def _startGame(){
		_status=Status.Started
		_cards=TabuDeck.getCards
		this.display(new Message("Jogo iniciado!"))
		this._nextRound()
	}
	/**
	 * Pega a proxima carta do baralho, se o baralho acabou o reembaralha
	 */
	protected def _getNextCard():TabuCard={
		if(_cards==Nil){
			_cards=TabuDeck.getCards
		}
		val ret=_cards.head
		_cards=_cards.tail
		ret
	}
	/**
	 * Envia a mensagem adequada de acordo com o jogador corrente para o jogador X
	 * @type {[type]}
	 */
	protected def _sendPlayerMessage(p:Player,currentPlayer:Player){
		if(p.teamId.get==currentPlayer.teamId.get){
			p.actor ! new Reply(new game.PayAtention(_currentCard.get))
		}
		else{
			if(p==currentPlayer){
				p.actor ! new Reply(new game.YourTurn(_currentCard.get))
			}
			else{
				p.actor !  new Reply(new game.GuessWord)
			}
		}
	}
	protected def _sendPlayerMessage(p:Player){
		val cp:Player=_currentPlayer.get
		_sendPlayerMessage(p,cp)

	}
	/**
	 * Proxima jogada
	 */
	protected def _nextPlay(){
		_currentCard=Some(_getNextCard)
		
		val cp:Player=_currentPlayer.get
		//Envia o lookup MENOS para o time do jogador atual
		for(t<- _teamList){
			for(p<-t.getPlayers if p.isReady){
				_sendPlayerMessage(p)
			}
		}
	}
	/**
	* Jogador sai
	*/
	def leave(p:Player){
		_connectionPlayerMap-=p.connId
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
		this._currentPlayer=Some(p)
		/**
		 * @todo iniciar timer
		 */
		this._nextPlay()
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
	
	object Status extends Enumeration{
		val WaitingPlayers,Started=Value
	}

	override def display(mess:Message){
		var r=new Reply(mess)
		for(t<-_teamList){

			for(p<-t.getPlayers if mess.senderConnId.isEmpty || p.connId!=mess.senderConnId.get){
				p.actor ! r
			}
		}
	}
}	

object Game{
	val minPlayers=2
}