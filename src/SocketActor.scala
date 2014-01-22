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
	/**
	 * @note Usando def em trait para inicialização tardia
	 */
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
				case mess:Message=>{
					display(mess)
				}
				case cmd:Command=>{
					_currentRole ! cmd
				}
				case ca:CommandActor=>{
					changeHandler(ca)
				}
			
			}
		}
	}
	internalHandler.start()


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

	def changeHandler(ca:CommandActor){
		_currentRole=ca
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
	/**
	 * Lida com o input recebifo via socket
	 * @type {[type]}
	 */
	def handleInput(input:AbstractCommand)={
		internalHandler ! input
	}
}
