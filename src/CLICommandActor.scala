package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import game.io.commands._
 /**
  * Lida com o recebimento de comandos pela interface da command line
  */
abstract class CLICommandActor extends CommandActor{
	/**
	 * Intepreta uma mensagem no formato string
	 * @type {[type]}
	 */
	def handleMessage(mess:String):AbstractCommand={
		if(mess.startsWith("-")){
			this.interpretCommand(CLICommandActor.parseCommand(mess))
		}
		else{
			new Message(mess)
		}
	}
	/**
	 * Interpreta um comando vindo do parser
	 * @type {[type]}
	 */
	def interpretCommand(cmd:List[(String,Option[String])]):AbstractCommand
}

object CLICommandActor{
	val cliRegex=new scala.util.matching.Regex("""-(\w+)(\s+(\"([\w ]+)\"|\w+))?""","cmd","dumb","value","quotedValue")
	/**
	 * Interpreta linhas de comando
	 * @type {[type]}
	 */
	def parseCommand(cmd:String):List[(String,Option[String])]={
		var cmdList:Iterator[(String,Option[String])]=for(m<-cliRegex findAllMatchIn(cmd)) yield {
			val value=if ((m group "quotedValue" )!=null){m group "quotedValue"} else{ m group "value"}
			(m group "cmd",if(value==null){None} else {Some(value)})
		}
		if(cmdList.isEmpty){
			throw new IllegalArgumentException("ERRO:Por favor envie comandos no formaro -Comando Argumento -COmando2 Argumento .. -ComandoN argumento")
			
		}
		cmdList.toList
	}
}