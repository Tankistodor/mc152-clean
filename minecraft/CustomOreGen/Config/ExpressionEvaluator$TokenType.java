package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$Assoc;
import CustomOreGen.Config.ExpressionEvaluator$EvaluationDelegate;

public enum ExpressionEvaluator$TokenType
{
    TRUE("true", 0, ExpressionEvaluator$Assoc.NONE, true, Boolean.valueOf(true)),
    FALSE("false", 0, ExpressionEvaluator$Assoc.NONE, true, Boolean.valueOf(false)),
    NUMBER((String)null, 0, ExpressionEvaluator$Assoc.NONE, true, (Object)null),
    HEXNUMBER((String)null, 0, ExpressionEvaluator$Assoc.NONE, true, (Object)null),
    STRING((String)null, 0, ExpressionEvaluator$Assoc.NONE, true, (Object)null),
    VARIABLE((String)null, 0, ExpressionEvaluator$Assoc.NONE, true, (Object)null),
    PAREN_OPEN("(", 10, ExpressionEvaluator$Assoc.BRACKET_OPEN, false, ")"),
    PAREN_CLOSE(")", 10, ExpressionEvaluator$Assoc.BRACKET_CLOSE, false, "("),
    BRACKET_OPEN("[", 10, ExpressionEvaluator$Assoc.BRACKET_OPEN, false, "]"),
    BRACKET_CLOSE("]", 10, ExpressionEvaluator$Assoc.BRACKET_CLOSE, false, "["),
    BRACE_OPEN("{", 10, ExpressionEvaluator$Assoc.BRACKET_OPEN, false, "}"),
    BRACE_CLOSE("}", 10, ExpressionEvaluator$Assoc.BRACKET_CLOSE, false, "{"),
    EXPONENT("^", 20, ExpressionEvaluator$Assoc.LEFT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "pow", new Class[]{Double.TYPE, Double.TYPE})),
    SCIENTIFIC("e", 20, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    TO_STRING("$", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    TO_NUMBER("#", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    TO_BOOLEAN("?", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    LOGICAL_NOT("!", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    UNARY_MINUS((String)null, 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    UNARY_PLUS((String)null, 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    IF("if", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    ABS("abs", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "abs", new Class[]{Double.TYPE})),
    SIGN("sign", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "signum", new Class[]{Double.TYPE})),
    MIN("min", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "min", new Class[]{Double.TYPE, Double.TYPE})),
    MAX("max", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "max", new Class[]{Double.TYPE, Double.TYPE})),
    FLOOR("floor", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "floor", new Class[]{Double.TYPE})),
    CEILING("ceil", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "ceil", new Class[]{Double.TYPE})),
    ROUND("round", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "round", new Class[]{Double.TYPE})),
    EXP("exp", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "exp", new Class[]{Double.TYPE})),
    LOG("log", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "log", new Class[]{Double.TYPE})),
    SINE("sin", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "sin", new Class[]{Double.TYPE})),
    COSINE("cos", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "cos", new Class[]{Double.TYPE})),
    TANGENT("tan", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "tan", new Class[]{Double.TYPE})),
    ARCSINE("asin", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "asin", new Class[]{Double.TYPE})),
    ARCCOSINE("acos", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "acos", new Class[]{Double.TYPE})),
    ARCTANGENT("atan", 20, ExpressionEvaluator$Assoc.RIGHT, true, new ExpressionEvaluator$EvaluationDelegate(false, Math.class, "atan", new Class[]{Double.TYPE})),
    MATCH("match", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    REPLACE("replace", 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    FUNCTION((String)null, 20, ExpressionEvaluator$Assoc.RIGHT, true, (Object)null),
    MULTIPLY("*", 30, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    DIVIDE("/", 30, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    MODULUS("%", 30, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    ADD("+", 40, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    SUBTRACT("-", 40, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    CONCATENATE("~", 40, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    LESS("<", 50, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    LESS_EQUAL("<=", 50, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    GREATER(">", 50, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    GREATER_EQUAL(">=", 50, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    EQUAL("=", 60, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    NOT_EQUAL("!=", 60, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    AND("&", 70, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    OR("|", 75, ExpressionEvaluator$Assoc.LEFT, true, (Object)null),
    COMMA(",", 80, ExpressionEvaluator$Assoc.LEFT, false, (Object)null),
    SEMICOLON(";", 80, ExpressionEvaluator$Assoc.LEFT, false, (Object)null),
    COLON(":", 80, ExpressionEvaluator$Assoc.LEFT, false, (Object)null);
    public final String symbol;
    public final int precedence;
    public final ExpressionEvaluator$Assoc associativity;
    public final boolean retain;
    public final Object data;

    private ExpressionEvaluator$TokenType(String var3, int var4, ExpressionEvaluator$Assoc var5, boolean var6, Object var7)
    {
        this.symbol = var3;
        this.precedence = var4;
        this.associativity = var5;
        this.retain = var6;
        this.data = var7;
    }
}
