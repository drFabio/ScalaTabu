package game.basics

class Game(val name:String,val numTeams:Int){
	def this(name:String)=this(name,2)
	protected val teamList=Array.fill(numTeams){new Team()}
	protected var teamCounter=0
	
	def join(p:Player){
		p.teamId=Some(this.teamCounter)
		this.teamList(this.teamCounter)+p
		this.teamCounter=(this.teamCounter+1)%this.teamList.length
	}
	def leave(p:Player){
		this.teamList(p.teamId.get)-p
	}

	class Team{
		var points:Int=0
		var players:Map[Int,Player]=Map()
		def +(p:Player){
			this.players+=p.id->p
		}
		def -(p:Player){
			this.players-=p.id
		}
	}
	
}	

