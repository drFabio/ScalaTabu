package game.basics
import scala.actors.Actor
class Player(val actor:Actor,val name:String,val connId:Int){
	protected var _teamId:Option[Int]=None
	protected var _isReady:Boolean=false
	override def toString()={
		name
	}
	def teamId=_teamId

	def teamId_= (value:Int):Unit =	_teamId=Some(value)

	def isReady=_isReady
	def isReady_=(value:Boolean):Unit=_isReady=value
}