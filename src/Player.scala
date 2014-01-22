package game.basics
import scala.actors.Actor
class Player(val actor:Actor,val name:String){
	var teamId:Option[Int]=None
}