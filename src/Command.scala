package game.io

class Command(cmd:String){
	/**
	 * The name of the command
	 */
	val name:String
	for(i<-cmd.split(' ')){
		println(i)
	}
}