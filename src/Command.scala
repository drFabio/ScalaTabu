import java.util.Date

package game.io.commands{
	abstract class AbstractMessage() extends Serializable{
		val now=new Date()

	}
	class Message(val content:String) extends AbstractMessage{
		override def toString()={
			"OUT: "+this.content
		}
	}

	class Command() extends AbstractMessage{

	}
	class Help() extends Command{
		
	}
	package gameHall{
		class List extends Command{

		}
		class CreateGame(val gameName:String,val numTeams:Int,val pointsToWin:Int) extends Command{
			def this( gameName:String, numTeams:Int)=this(gameName,numTeams,10)
			def this( gameName:String)=this(gameName,2,10)
		}
		class JoinGame(val gameId:Int) extends Command{

		}
	}
	package game{

	}
}