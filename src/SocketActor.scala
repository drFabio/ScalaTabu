package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import game.io.commands._



/**
 * Lida com o recebimento e envio de mensagens de texto, parseando comandos e mensagens
 */
trait SocketActor extends Actor{
	
	protected def sock:Socket
	/**
	 * Papel que estamos desenpenhando atualmente
	 * @type {[type]}
	 */
	protected var _currentRole:CommandActor=_
	protected lazy val oin:ObjectInputStream=new ObjectInputStream(this.sock.getInputStream())
	protected lazy val oout:ObjectOutputStream = new ObjectOutputStream (this.sock.getOutputStream())
	/**
	 * Comunicação entre o _currentRole e o socket
	 */
	protected lazy val internalHandler=actor{
		self.loop{
			self.react{
				case rp:Reply=>{
					sendMessage(rp.cmd)
				}
				case ic:InternalCommand=>{
					handleCommand(ic)
				}
				case ac:AbstractCommand=>{
					_currentRole ! ac
				}
				
				case ca:CommandActor=>{
					changeRole(ca)
				}
			
			}
		}
	}
	internalHandler.start()


	/**
	 * Le inputs vindo de um socket e lida com eles
	 */
	def act(){
		try{
			
			var ok=true
			while(ok){
				var input=oin.readObject
				if(input==null){ //fim da stream ou erro provavelmente
					ok=false
				}
				else{
					try{
						input match{
							case m:AbstractCommand=>handleInput(m)
					 	}

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
				sock.close()
			}
		}

	}
	//Muda o papel atual
	def changeRole(ca:CommandActor){
		_currentRole=ca
	}
	//Envia uma mensagem para o outro lado do socket
	def sendMessage(mess:AbstractCommand){
		oout.writeObject(mess)
	}
	/**
	 * Lida com o input recebifo via socket
	 */
	def handleInput(input:AbstractCommand)={
		internalHandler ! input
	}
	/**
	 * Lida com um comando vindo do papel atual
	 */
	def handleCommand(input:AbstractCommand)={

	}
}
