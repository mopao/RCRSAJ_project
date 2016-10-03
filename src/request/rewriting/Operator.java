package request.rewriting;

public enum Operator {
EQUALS("=");
private final String symbol;       

private Operator(String s) {
    symbol = s;
}
public String toString() {
    return this.symbol;
 }
}

