package game.io

import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
import java.util.Date;

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
					display(input)
				}
				/*
				in.readObject() match{
					case (Message.MESS,_)=>this.display(_)
					case _=>println("???")
				}*/
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
	 * Mostra algum resultado para o usuario
	 * @type {[type]}
	 */
	def display(str:String){
		println("OUT:  "+str)
	}
	/**
	 * Envia uma mensagem atraves do socket
	 * @type String mensagem
	 */
	def sendMessage(mess:String){
		//this.out.writeObject((Message.MESS,mess))
		out.println(mess)
	}
	object Message extends Enumeration{
		val COMM,MESS,COMM_SETUP= Value
	}
}