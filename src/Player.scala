package game.basics
class Player(val actor:game.io.SocketActor,val name:String){
	var teamId:Option[Int]=None
}