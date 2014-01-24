package game.basics
import scala.collection.mutable.ListBuffer
/**
 * Classe que representa um time de infinitos jogadores
 */
class Team{
	protected var _points:Int=0
	protected var _players:ListBuffer[Player]=ListBuffer()
	var position:Int=0
	def +(p:Player){
		this._players+=p
	}
	def -(p:Player){

		val index=this._players.indexOf(p)
		if(this.position>index){
			this.position-=1
		}	
		this._players.remove(index)
	}
	def score():Int={
		this._points+=1
		return this._points
	}
	def getNextPlayer():Player={
		val pos=this.position
		this.position=(this.position+1)%this._players.length
		return this._players(pos)
	}
	def getNumPlayers():Int={
		this._players.length
	}
	def getPlayers():List[Player]={
		return _players.toList
	}

	def list():String={
		var ret=""
		for(p<-_players){
			ret+="\n\t"+p.name
		}
		ret
	}
}