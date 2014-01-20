package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import game.io.commands._

abstract class CommandActor extends Actor{
	def executeCommand(cmdList:List[(String,Option[String])])
	def help()
	def act{
		loop{
			react{
				case (h:String,_)::_ if h=="h" => help
				case cmdList:List[(String,Option[String])] => executeCommand(cmdList)
			}
		}
	}
}
object CommandActor{
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
							case s:String=>handleInput(s)
							case m:Message=>handleInput(m.toString())
					 	}
					 	// handleInput(input)
						
					}
					catch{
						case e:IllegalArgumentException=>{
							sendMessage(e.getMessage)
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
	def handleInput(input:String){
		input match{
			case str:String if str.startsWith("!")=>{
				
				sendMessage(str.substring(1))
			}
			case str:String if str.startsWith("-")=>{
				println("RECEBI O COMANDO "+str)
				actor{
					handler !  CommandActor.parseCommand(str)
					self.react{
						case s:String=>handleInput(s)
						case  ca:CommandActor=>{
							handler=ca
						}
					}
				}

			}
			case str:String=> display(str)
		}
	}
	
	/**run
	 * Mostra algum resultado para o usuario
	 */
	def display(str:String){
		println("OUT:  "+str)
	}
	/**
	 * Envia uma mensagem atraves do socket
	 */
	def sendMessage(mess:String){
		oout.writeObject(mess)
		// out.println(mess)
	}
	def sendMessage(mess:AbstractMessage){
		oout.writeObject(mess)
	}
}
