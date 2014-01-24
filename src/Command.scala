import java.util.Calendar
import game.basics.cards.TabuCard
/**
 * Comandos enviados via socket e entre os atores
 */
package game.io.commands{
	abstract class AbstractCommand() extends Serializable{
		val now=Calendar.getInstance

	}
	/**
	 * Uma mensagem de texto , opcionalmente com rementente
	 * @type {[type]}
	 */
	class Message(val content:String,val senderName:Option[String],val senderConnId:Option[Int]) extends AbstractCommand{
		override def toString()={

			val mess=if(senderName.isEmpty){
				content
			}
			else{
				senderName.get+" :"+content
			}
			now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+" - "+mess
		}
		def this(content:String)=this(content,None,None)
	}

	class ErrorMessage(content:String) extends Message(content){
		def this(e:Exception)=this(e.getMessage)
	}
	trait Command extends AbstractCommand{

	}

	class Help() extends Command{
		
	}
	/**
	 * Devolve para a parte que enviou o comando recebido (cliente ou servidor)
	 */
	class Reply(val cmd:AbstractCommand) extends AbstractCommand{

	}
	trait InternalCommand extends AbstractCommand{

	}

	package setup{
		class WhoAreYou(val connId:Int) extends Command{
			
		}
		class IAm(val name:String) extends Command{
			
		}
	}
	package gameHall{
		class List extends Command{

		}

		class CreateGame(val gameName:String,val numTeams:Int,val maxScore:Int,val creatorName:String,val connId:Int) extends Command{
		
		}
		class JoinGame(val gameId:Int,val playerName:String,val connId:Int) extends Command{
			
		}
		class CreatedGame(message:String) extends Message(message) with Command{

		}
		class JoinedGame(message:String) extends Message(message) with Command{

		}

		object CreateGame{
			val defaultTeam:Int=2
			val defaultScore:Int=10

			def factory(cmd:_root_.scala.collection.immutable.List[(String,Option[String])],creatorName:String,connId:Int):CreateGame={
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
				return new CreateGame(name,teams,score,creatorName,connId)
			}
		}
	
	}
	package game{
		/**
		 * Jogador está pronto
		 * @type {[type]}
		 */
		class IAmReady(val connId:Int) extends Command{

		}
		/**
		 * Alguem do seu time acertou!!
		 */
		class Correct extends Command{

		}
		/**
		 * Pessoa do time falou a palavra tabu
		 */
		class Tabu extends Command{

		}
		/**
		 * Vez de voce perceber se o cara falou a palavra tabu ou não
		 */
		class GuessWord extends Command{

		}
		/**
		 * Vez do jogador
		 * @type {[type]}
		 */
		class YourTurn(tc:TabuCard) extends Command{

		}
		/**
		 * Time oposto jogando pessoas devem reparar na carta
		 * 
		 * @type {[type]}
		 */
		class PayAtention(tc:TabuCard) extends Command{

		}


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
					case cmd:Command =>{
						try{
							executeCommand(cmd)
						}
						catch{
							case e:IllegalArgumentException=>{
								sender ! new Reply(new ErrorMessage(e))
							}
						}

					} 
					case m:Message=>{
						display(m)
					}
				}
			}
		}
		/**run
		 * Mostra algum resultado para o usuario
		 */
		def display(mess:Message){
			println(mess)
		}

	}

}