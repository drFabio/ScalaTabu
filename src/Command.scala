import java.util.Date

package game.io.commands{
	abstract class AbstractCommand() extends Serializable{
		val now=new Date()

	}
	class Message(val content:String) extends AbstractCommand{
		override def toString()={
			"OUT: "+this.content
		}
	}
	class ErrorMessage(e:Exception) extends Message(e.getMessage){

	}
	class Command() extends AbstractCommand{

	}
	class Help() extends Command{
		
	}
	/**
	 * Devolve para a parte que enviou o comando recebido (cliente ou servidor)
	 */
	class Reply(val cmd:AbstractCommand) extends AbstractCommand{

	}
	package gameHall{
		class List extends Command{

		}
		class WhoAreYou extends Command{
			
		}
		class IAm(val name:String) extends Command{
			
		}
		class CreateGame(val gameName:String,val numTeams:Int,val maxScore:Int) extends Command{
			def this( gameName:String, numTeams:Int)=this(gameName,numTeams,10)
			def this( gameName:String)=this(gameName,2,10)
		}
		class JoinGame(val gameId:Int) extends Command{

		}
	}
	package game{

	}
}