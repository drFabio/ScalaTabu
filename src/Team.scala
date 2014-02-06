package game.basics
import scala.collection.mutable.ListBuffer
/**
 * Classe que representa um time de infinitos jogadores
 */
class Team{

	protected var _points:Int=0
	protected var _players:ListBuffer[Player]=ListBuffer()
	var position:Int=0
	//Adiciona um jogador
	def +(p:Player){
		this._players+=p
	}
	//Remove um jogador
	def -(p:Player){

		val index=this._players.indexOf(p)
		if(this.position>index){
			this.position-=1
		}	
		this._players.remove(index)
	}
	//Pontua
	def score():Int={
		this._points+=1
		return this._points
	}
	//Pega o proximo jogador a jogar
	def getNextPlayer():Player={
		val pos=this.position
		this.position=(this.position+1)%this._players.length
		return this._players(pos)
	}
	//Pega a quantidade de jogadores
	def getNumPlayers():Int={
		this._players.length
	}
	//Lista de jogaodres
	def getPlayers():List[Player]={
		return _players.toList
	}
	//Lista os jogadores retornando uma string
	def list():String={
		var ret=""
		for(p<-_players){
			ret+="\n\t"+p.name
		}
		ret
	}
}