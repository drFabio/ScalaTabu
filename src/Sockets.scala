package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import java.util.Date;

/**
 * Lida com o recebimento e envio de mensagens de texto, parseando comandos e mensagens
 */
trait SocketActor extends Actor{
	/**
	 * @note Usando def em trait para inicialização tardia
	 */
	protected def sock:Socket

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
				sock.close()
			}
		}

	}
	/**
	 * Parseia um comando
	 */
	def parseCommand(cmd:String){
		println("Recebido "+cmd)
		var isCmd=true
		var currentCmd:Option[String]=None
		var cmdList:List[(String,Option[String])]=Nil
		for(i<-cmd.split(' ')){
			if(i.startsWith("-") !=isCmd ){
				throw new IllegalArgumentException("ERRO:Por favor envie comandos no formaro -Comando Argumento -COmando2 Argumento .. -ComandoN argumento")
			}
			if(isCmd){
				currentCmd=i.substring(1)
			}
			else{
				cmdList=(currentCmd,Some(i))::cmdList
				currentCmd=None
			}
			isCmd=(!isCmd)
		}
		if(!currentCmd.isEmpty){
			currentCmd=None
			cmdList=(currentCmd,None)::cmdList
		}
		println(cmdList)
	}
	def executeCommand(cmdList:List[(String,Option[String])]){

	}
	/**
	 * Lida com um input
	 */
	def handleInput(input:String){
		input match{
			case str if str.startsWith("-")=>  parseCommand(str)
			case str=> display(str)
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

