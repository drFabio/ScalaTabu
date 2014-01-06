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

	// protected val in:ObjectInputStream=new ObjectInputStream(this.sock.getInputStream())
	// protected val out:ObjectOutputStream = new ObjectOutputStream (this.sock.getOutputStream())
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
	def display(mess:string){
		println("OUT:  "+input)
	}
	/**
	 * Envia uma mensagem atraves do socket
	 * @type {[type]}
	 */
	def sendMessage(mess:String){
		this.out.println(mess)
	}
	class Message extends Enumeration{
		val COMM=Value("command")
		val MES=Value("message")
		val now=new Date()
	}
}