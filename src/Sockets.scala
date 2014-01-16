package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import java.util.Date;

abstract class CommandActor extends Actor{
	def executeCommand(cmdList:List[(String,Option[String])])
	def help()
	def act{
		loop{
			react{
				case (h:String,_)::_ if h=="h" => help
				case cmdList:List[(String,Option[String])] => executeCommand(cmdList)
				case _ =>println("SEI LÁ O QUE RECEBI")
			}
		}
	}
}
object CommandActor{
	def parseCommand(cmd:String):List[(String,Option[String])]={
		var isCmd=true
		var currentCmd:Option[String]=None
		var cmdList:List[(String,Option[String])]=Nil
		for(i<-cmd.split(' ')){
			if(i.startsWith("-") !=isCmd ){
				throw new IllegalArgumentException("ERRO:Por favor envie comandos no formaro -Comando Argumento -COmando2 Argumento .. -ComandoN argumento")
			}
			if(isCmd){
				currentCmd=Some(i.substring(1))
			}
			else{
				cmdList=(currentCmd.get,Some(i))::cmdList
				currentCmd=None
			}
			isCmd=(!isCmd)
		}
		if(!currentCmd.isEmpty){
			cmdList=(currentCmd.get,None)::cmdList
			currentCmd=None
		}
		return cmdList
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

	// protected val oin:ObjectInputStream=new ObjectInputStream(this.sock.getInputStream())
	// protected val oout:ObjectOutputStream = new ObjectOutputStream (this.sock.getOutputStream())
	protected val in:BufferedReader=new BufferedReader(new InputStreamReader(this.sock.getInputStream()))
	protected val out:PrintWriter= new PrintWriter(this.sock.getOutputStream(), true)
	/**
	 * Ação principal desse socket actor, simplesmente le inputs eternamente e lida com eles
	 */
	def act(){
		try{
			var ok=true
			while(ok){
				var input=in.readLine()
				if(input==null){ //fim da stream ou erro provavelmente
					ok=false
				}
				else{
					try{
						handleInput(input)
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
				println("AQUI!!!!!!")
				sendMessage(str.substring(1))
			}
			case str:String if str.startsWith("-")=>{
				var list= CommandActor.parseCommand(str)
				println("RECEBI "+list)
				val sActor=this
				actor{
					sActor.handler ! list
					self.react{
						case s:String=>sendMessage(s)
					}
				}

			}
			case str:String=> display(str)
			case _ => println("Não sei o que recebi aquii")
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
		//this.oout.writeObject((Message.MESS,mess))
		out.println(mess)
	}
}