package game.io
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
				case cmd:AbstractCommand => executeCommand(cmd)
			}
		}
	}

}
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

/**
 * Lida com o recebimento e envio de mensagens de texto, parseando comandos e mensagens
 */
trait SocketActor extends Actor{
	/**
	 * @note Usando def em trait para inicialização tardia
	 */
	protected def sock:Socket
	protected var handler:CommandActor=_

	protected lazy val oin:ObjectInputStream=new ObjectInputStream(this.sock.getInputStream())
	protected lazy val oout:ObjectOutputStream = new ObjectOutputStream (this.sock.getOutputStream())
	// protected val in:BufferedReader=new BufferedReader(new InputStreamReader(this.sock.getInputStream()))
	// protected val out:PrintWriter= new PrintWriter(this.sock.getOutputStream(), true)
	/**
	 * Ação principal desse socket actor, simplesmente le inputs eternamente e lida com eles
	 */
	def act(){
		try{
			
			var ok=true
			while(ok){
				// var input=in.readLine
				var input=oin.readObject
				if(input==null){ //fim da stream ou erro provavelmente
					ok=false
				}
				else{
					try{
						input match{
							case m:AbstractCommand=>handleInput(m)
					 	}
					 	// handleInput(input)
						
					}
					catch{
						case e:IllegalArgumentException=>{
							sendMessage(new ErrorMessage(e))
						}
						
					}
				}
			}
		}
		catch{
			
			case e:Throwable=>{
				println("Exceção cliente finalizado")
				e.printStackTrace
				sock.close()
			}
		}

	}
	/**
	 * @todo mudar para objetos depois
	 * Lida com um input
	 */
	def handleInput(input:AbstractCommand){
		input match{
			case rp:Reply=>{
				sendMessage(rp.cmd)
			}
			case mess:Message=>{
				display(mess)
			}
			case cmd:Command=>{
				actor{
					handler ! cmd
					self.react{
						case am:AbstractCommand=>handleInput(am)
						case ca:CommandActor=>{
							handler=ca
						}
					}
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
	def sendMessage(mess:AbstractCommand){
		oout.writeObject(mess)
	}
	
}
