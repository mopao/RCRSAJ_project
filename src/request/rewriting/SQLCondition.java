package request.rewriting;
/**
 * cette classe représente une condition en sql
 * @author franck
 *
 */
public class SQLCondition {
	private SQLOperand leftOprerand, rightOperand;
	private Operator operator ;
	
	public SQLOperand getLeftOprerand() {
		return leftOprerand;
	}

	public SQLOperand getRightOperand() {
		return rightOperand;
	}

	public SQLCondition(SQLOperand leftOprerand, SQLOperand rightOperand,
			Operator operator) {
		super();
		this.leftOprerand = leftOprerand;
		this.rightOperand = rightOperand;
		this.operator = operator;
	}
	
	/**
	 * renomme la condition
	 * @param number : numéro de renommage pour les deux opérandes
	 * @return condition renommée
	 */
	public SQLCondition getRenamedCondition(int number){
		return new SQLCondition(leftOprerand.getRenamedOperand(number),
				rightOperand.getRenamedOperand(number), operator);
		
	}
	
	/**
	 * renomme la condition
	 * @param leftnumber: numéro de renommage pour l'opérande de gauche
	 * @param rightnumber: numéro de renommage pour l'opérande de droite
	 * @return condition renommée
	 */
	public SQLCondition getRenamedCondition(int leftnumber, int rightnumber){
		return new SQLCondition(leftOprerand.getRenamedOperand(leftnumber),
				rightOperand.getRenamedOperand(rightnumber), operator);
		
	}
	
	public String toString(){
		return leftOprerand.toString()+operator.toString()+rightOperand.toString();
	}

}
