import java.net._
import java.io._
import scala.actors.Actor
import scala.actors.Actor._
import scala.io._
//import  game.basics._
/**
* Ator que cuida da obtenção de comandos vindo do socket
*/
class SocketActor(protected val sock:Socket,protected val connId:Int) extends Actor{
	protected val in:BufferedReader=new BufferedReader(new InputStreamReader(sock.getInputStream()))
	protected val out:PrintWriter= new PrintWriter(sock.getOutputStream(), true)
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
object TabuServer{
	
	def main(args:Array[String])={
		try{
			val port=1337
			println("Iniciando servidor na porta "+port)
		//	val gh=new GameHall
			val server = new ServerSocket(port)
			var connId=0
			while (true) {
			    val s = server.accept()
			    println("Recebido conexão")
			    var a =new SocketActor(s,connId)
			    a.start()
			    connId+=1
			}
		}
		catch{
			case e:Throwable=>{
				e.printStackTrace()
			}
		}
	}
}