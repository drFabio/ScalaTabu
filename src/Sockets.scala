package game.io
import scala.actors.Actor
import scala.actors.Actor._
import java.net._
import java.io._
import scala.io._
trait SocketActor extends Actor{
	protected def in:BufferedReader
	protected def out:PrintWriter
	protected def connId:Int
	protected def sock:Socket
	def act(){
		try{
			
			println("Usuario"+this.connId+" inicializado")
			var close=false
			while(!close){
				var input=in.readLine()
				if(input==null){ //fim da stream ?
					close=true
				}
				else{
					println("Recebido "+input)
				}
			}
			println("Cliente "+this.connId+" finalizado normalmente")
		}
		catch{
			case e:Throwable=>{
				println("Exceção cliente "+this.connId+" finalizado")
				sock.close()
			}
		}
	}
}