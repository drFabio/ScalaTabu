package game.basics.cards
//Representa uma carta de tabu
class TabuCard(val word:String,val tabuWords:List[String]) extends Serializable{
	override def toString()={
		" Palavara ="+word+"\n Tabus "+tabuWords.mkString("\n - ")
	}
}
//Representa um baralho de Tabu
object TabuDeck{
	/**
	 * Pega um barulho de tabu
	 */
	def getCards():List[TabuCard]={
		List(new TabuCard("água",List("líquido","azul","bebida","piscina")),
		new TabuCard("cerveja",List("líquido","beber","alcool","brejas")),
		new TabuCard("azul",List("cores","céu","água","mar")),
		new TabuCard("vento",List("ar","brisa","tornado","elemento")),
		new TabuCard("chocolate",List("cacau","leite","doce","bombom")))
	}
}