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
	class ErrorMessage(content:String) extends Message(content){
		def this(e:Exception)=this(e.getMessage)
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
			def this( gameName:String, numTeams:Int)=this(gameName,numTeams,CreateGame.defaultScore)
			def this( gameName:String)=this(gameName,CreateGame.defaultTeam,CreateGame.defaultScore)
		}
		object CreateGame{
			val defaultTeam:Int=2
			val defaultScore:Int=10

			def factory(cmd:_root_.scala.collection.immutable.List[(String,Option[String])]):CreateGame={
				var score:Int=defaultScore
				var teams:Int=defaultTeam
				var name:String=null
				for(c<-cmd){
					c._1 match{
						case p:String if p=="c"=>name=c._2.get
						case p:String if p=="n"=>teams=c._2.get.toInt
						case p:String if p=="p"=>score=c._2.get.toInt
						case _=>{}
					}
				}

				if(score<=0){
					score=CreateGame.defaultScore
				}
				if(teams<=2){
					teams=CreateGame.defaultTeam
				}
				return new CreateGame(name,teams,score)
			}
		}
		class JoinGame(val gameId:Int) extends Command{

		}
	}
	package game{

	}
}