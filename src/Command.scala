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

	package setup{
		class WhoAreYou extends Command{
			
		}
		class IAm(val name:String) extends Command{
			
		}
	}
	package gameHall{
		class List extends Command{

		}

		class CreateGame(val gameName:String,val numTeams:Int,val maxScore:Int,val creatorName:String) extends Command{
		
		}
		class JoinGame(val gameId:Int,val playerName:String) extends Command{
			override def toString()={
				"JOIN!!"+gameId+" "+playerName
			}
		}
		object CreateGame{
			val defaultTeam:Int=2
			val defaultScore:Int=10

			def factory(cmd:_root_.scala.collection.immutable.List[(String,Option[String])]):CreateGame={
				var score:Int=defaultScore
				var teams:Int=defaultTeam
				var name:String=null
				var creatorName:String=null
				for(c<-cmd){
					c._1 match{
						case p:String if p=="c"=>name=c._2.get
						case p:String if p=="n"=>teams=c._2.get.toInt
						case p:String if p=="p"=>score=c._2.get.toInt
						case p:String if p=="cn"=>creatorName=c._2.get
						case _=>{}
					}
				}

				if(score<=0){
					score=CreateGame.defaultScore
				}
				if(teams<=2){
					teams=CreateGame.defaultTeam
				}
				return new CreateGame(name,teams,score,creatorName)
			}
		}
	
	}
	package game{

	}
}


package game.io {
	import scala.actors.Actor
	import scala.actors.Actor._
	import java.net._
	import java.io._
	import scala.io._
	import game.io.commands._

	/**
	 * Classe que lida com recebimento de commandos
	 */
	abstract class CommandActor extends Actor{
		/**
		 * Executa um comando recebido
		 * @type {[type]}
		 */
		def executeCommand(cmd:AbstractCommand)
		def help()
		def act{
			loop{
				react{
					case h:Help=>help()
					case cmd:AbstractCommand =>{
						try{

							executeCommand(cmd)
						}
						catch{
							case e:IllegalArgumentException=>{
								sender ! new Reply(new ErrorMessage(e))
							}
						}

					} 
				}
			}
		}

	}
}