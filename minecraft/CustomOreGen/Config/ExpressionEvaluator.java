package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$1;
import CustomOreGen.Config.ExpressionEvaluator$Assoc;
import CustomOreGen.Config.ExpressionEvaluator$EvaluationDelegate;
import CustomOreGen.Config.ExpressionEvaluator$EvaluatorException;
import CustomOreGen.Config.ExpressionEvaluator$Token;
import CustomOreGen.Config.ExpressionEvaluator$TokenType;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionEvaluator
{
    protected static Map _symbolMap = new HashMap();

    protected Object getIdentifierValue(String var1)
    {
        return null;
    }

    public Object evaluate(String var1) throws ExpressionEvaluator$EvaluatorException
    {
        Stack var2 = this.parse(var1);

        if (var2.isEmpty())
        {
            return null;
        }
        else
        {
            Object var3 = null;

            try
            {
                var3 = evaluate(var2);
            }
            catch (EmptyStackException var5)
            {
                throw new ExpressionEvaluator$EvaluatorException("Incomplete expression.", new ExpressionEvaluator$Token(var1, var1.length(), var1.length()));
            }

            if (!var2.isEmpty())
            {
                throw new ExpressionEvaluator$EvaluatorException("Expression contains too many values.", (ExpressionEvaluator$Token)var2.peek());
            }
            else
            {
                return var3;
            }
        }
    }

    protected static Object evaluate(Stack var0) throws ExpressionEvaluator$EvaluatorException, EmptyStackException
    {
        ExpressionEvaluator$Token var1 = (ExpressionEvaluator$Token)var0.pop();

        if (!var1.type.retain)
        {
            throw new ExpressionEvaluator$EvaluatorException("Un-evaluatable token.", var1);
        }
        else
        {
            boolean var2;
            boolean var3;
            Object var6;
            double var7;
            Object var8;
            String var9;
            String var10;

            switch (ExpressionEvaluator$1.$SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[var1.type.ordinal()])
            {
                case 1:
                    var7 = evaluateNumber(var0);
                    return Double.valueOf(evaluateNumber(var0) * Math.pow(10.0D, var7));

                case 2:
                    return evaluate(var0).toString();

                case 3:
                    var6 = evaluate(var0);

                    if (var6 instanceof Number)
                    {
                        return (Number)var6;
                    }
                    else if (var6 instanceof Boolean)
                    {
                        return Long.valueOf(((Boolean)var6).booleanValue() ? 1L : 0L);
                    }
                    else
                    {
                        if (var6 instanceof String)
                        {
                            try
                            {
                                if (var6.toString().contains("."))
                                {
                                    return Double.valueOf(Double.parseDouble(var6.toString()));
                                }

                                return Long.decode(var6.toString());
                            }
                            catch (NumberFormatException var5)
                            {
                                ;
                            }
                        }

                        throw new ExpressionEvaluator$EvaluatorException("Cannot reduce \'" + var6 + "\' to a numerical value.", var1);
                    }

                case 4:
                    var6 = evaluate(var0);

                    if (var6 instanceof Boolean)
                    {
                        return (Boolean)var6;
                    }
                    else if (var6 instanceof Number)
                    {
                        return Boolean.valueOf(((Number)var6).doubleValue() != 0.0D);
                    }
                    else
                    {
                        if (var6 instanceof String)
                        {
                            if (var6.toString().equalsIgnoreCase("true"))
                            {
                                return Boolean.TRUE;
                            }

                            if (var6.toString().equalsIgnoreCase("false"))
                            {
                                return Boolean.FALSE;
                            }
                        }

                        throw new ExpressionEvaluator$EvaluatorException("Cannot reduce \'" + var6 + "\' to a boolean value.", var1);
                    }

                case 5:
                    return Boolean.valueOf(!evaluateBoolean(var0));

                case 6:
                    return Double.valueOf(-evaluateNumber(var0));

                case 7:
                    return Double.valueOf(evaluateNumber(var0));

                case 8:
                    var6 = evaluate(var0);
                    var8 = evaluate(var0);
                    return evaluateBoolean(var0) ? var8 : var6;

                case 9:
                    var10 = evaluateString(var0);
                    var9 = evaluateString(var0);
                    return Boolean.valueOf(var9.matches(var10));

                case 10:
                    var10 = evaluateString(var0);
                    var9 = evaluateString(var0);
                    String var11 = evaluateString(var0);
                    return var11.replaceAll(var9, var10);

                case 11:
                    return Double.valueOf(evaluateNumber(var0) * evaluateNumber(var0));

                case 12:
                    var7 = evaluateNumber(var0);
                    return Double.valueOf(evaluateNumber(var0) / var7);

                case 13:
                    var7 = evaluateNumber(var0);
                    return Double.valueOf(evaluateNumber(var0) % var7);

                case 14:
                    return Double.valueOf(evaluateNumber(var0) + evaluateNumber(var0));

                case 15:
                    var7 = evaluateNumber(var0);
                    return Double.valueOf(evaluateNumber(var0) - var7);

                case 16:
                    var10 = evaluateString(var0);
                    var9 = evaluateString(var0);
                    return var9 + var10;

                case 17:
                    var7 = evaluateNumber(var0);
                    return Boolean.valueOf(evaluateNumber(var0) < var7);

                case 18:
                    var7 = evaluateNumber(var0);
                    return Boolean.valueOf(evaluateNumber(var0) <= var7);

                case 19:
                    var7 = evaluateNumber(var0);
                    return Boolean.valueOf(evaluateNumber(var0) > var7);

                case 20:
                    var7 = evaluateNumber(var0);
                    return Boolean.valueOf(evaluateNumber(var0) >= var7);

                case 21:
                case 22:
                    var6 = evaluate(var0);
                    var8 = evaluate(var0);
                    boolean var4 = false;

                    if (var8 instanceof Number && var6 instanceof Number)
                    {
                        var4 = ((Number)var8).doubleValue() == ((Number)var6).doubleValue();
                    }
                    else
                    {
                        if (!var8.getClass().isAssignableFrom(var6.getClass()))
                        {
                            throw new ExpressionEvaluator$EvaluatorException("Cannot compare equality of \'" + var8.getClass().getSimpleName() + "\' and \'" + var6.getClass().getSimpleName() + "\'.", var1);
                        }

                        var4 = var6.equals(var8);
                    }

                    return Boolean.valueOf(var1.type == ExpressionEvaluator$TokenType.EQUAL ? var4 : !var4);

                case 23:
                    var2 = evaluateBoolean(var0);
                    var3 = evaluateBoolean(var0);
                    return Boolean.valueOf(var3 && var2);

                case 24:
                    var2 = evaluateBoolean(var0);
                    var3 = evaluateBoolean(var0);
                    return Boolean.valueOf(var3 || var2);

                default:
                    if (var1.data == null)
                    {
                        throw new ExpressionEvaluator$EvaluatorException("Un-evaluatable token.", var1);
                    }
                    else if (var1.data instanceof ExpressionEvaluator$EvaluationDelegate)
                    {
                        return ((ExpressionEvaluator$EvaluationDelegate)var1.data).evaluate(var1, var0);
                    }
                    else
                    {
                        return var1.data;
                    }
            }
        }
    }

    protected static String evaluateString(Stack var0) throws ExpressionEvaluator$EvaluatorException, EmptyStackException
    {
        ExpressionEvaluator$Token var1 = (ExpressionEvaluator$Token)var0.peek();
        Object var2 = evaluate(var0);

        try
        {
            return (String)var2;
        }
        catch (ClassCastException var4)
        {
            throw new ExpressionEvaluator$EvaluatorException("Expected a string value.", var1, var4);
        }
    }

    protected static double evaluateNumber(Stack var0) throws ExpressionEvaluator$EvaluatorException, EmptyStackException
    {
        ExpressionEvaluator$Token var1 = (ExpressionEvaluator$Token)var0.peek();
        Object var2 = evaluate(var0);

        try
        {
            return ((Number)var2).doubleValue();
        }
        catch (ClassCastException var4)
        {
            throw new ExpressionEvaluator$EvaluatorException("Expected a numeric value.", var1, var4);
        }
    }

    protected static boolean evaluateBoolean(Stack var0) throws ExpressionEvaluator$EvaluatorException, EmptyStackException
    {
        ExpressionEvaluator$Token var1 = (ExpressionEvaluator$Token)var0.peek();
        Object var2 = evaluate(var0);

        try
        {
            return ((Boolean)var2).booleanValue();
        }
        catch (ClassCastException var4)
        {
            throw new ExpressionEvaluator$EvaluatorException("Expected a boolean value.", var1, var4);
        }
    }

    public Stack parse(String var1) throws ExpressionEvaluator$EvaluatorException
    {
        Stack var2 = new Stack();
        Stack var3 = new Stack();
        ExpressionEvaluator$Token var4 = null;
        ExpressionEvaluator$Assoc var5 = ExpressionEvaluator$Assoc.RIGHT;
        ExpressionEvaluator$Token var6;

        for (var6 = this.getToken(var1, 0); var6 != null; var6 = this.getToken(var1, var6.end))
        {
            ExpressionEvaluator$Assoc var7 = var6.type.associativity;

            if (var7 == ExpressionEvaluator$Assoc.BRACKET_OPEN)
            {
                var7 = ExpressionEvaluator$Assoc.RIGHT;
            }
            else if (var7 == ExpressionEvaluator$Assoc.BRACKET_CLOSE)
            {
                var7 = ExpressionEvaluator$Assoc.LEFT;
            }

            if (var7 == ExpressionEvaluator$Assoc.LEFT != (var5 == ExpressionEvaluator$Assoc.NONE))
            {
                if (var6.type == ExpressionEvaluator$TokenType.ADD)
                {
                    var6 = new ExpressionEvaluator$Token(var6.source, var6.start, var6.end, ExpressionEvaluator$TokenType.UNARY_PLUS);
                }
                else
                {
                    if (var6.type != ExpressionEvaluator$TokenType.SUBTRACT)
                    {
                        throw new ExpressionEvaluator$EvaluatorException("\'" + var6 + "\' cannot follow \'" + var4 + "\'.", var6);
                    }

                    var6 = new ExpressionEvaluator$Token(var6.source, var6.start, var6.end, ExpressionEvaluator$TokenType.UNARY_MINUS);
                }
            }

            var5 = var7;
            var4 = var6;
            ExpressionEvaluator$Token var8;

            switch (ExpressionEvaluator$1.$SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[var6.type.associativity.ordinal()])
            {
                case 1:
                    if (var6.type.retain)
                    {
                        var2.push(var6);
                    }

                    break;

                case 2:
                    var3.push(var6);
                    break;

                case 3:
                    var8 = null;

                    while (var8 == null && !var3.isEmpty())
                    {
                        ExpressionEvaluator$Token var9 = (ExpressionEvaluator$Token)var3.pop();

                        if (var9.type.associativity == ExpressionEvaluator$Assoc.BRACKET_OPEN)
                        {
                            if (!var9.toString().equals(var6.data))
                            {
                                throw new ExpressionEvaluator$EvaluatorException("Missing corresponding " + var9.data + ".", var9);
                            }

                            var8 = var9;
                        }
                        else if (var9.type.retain)
                        {
                            var2.push(var9);
                        }
                    }

                    if (var8 == null)
                    {
                        throw new ExpressionEvaluator$EvaluatorException("Missing corresponding " + var6.data + ".", var6);
                    }

                    if (var6.type.retain)
                    {
                        var2.push(var8);
                        var2.push(var6);
                    }

                    var5 = ExpressionEvaluator$Assoc.NONE;
                    break;

                case 4:
                case 5:
                    while (!var3.isEmpty())
                    {
                        var8 = (ExpressionEvaluator$Token)var3.peek();

                        if (var8.type.associativity != ExpressionEvaluator$Assoc.LEFT && var8.type.associativity != ExpressionEvaluator$Assoc.RIGHT || var6.type.associativity == ExpressionEvaluator$Assoc.RIGHT && var6.type.precedence == var8.type.precedence || var6.type.precedence < var8.type.precedence)
                        {
                            break;
                        }

                        var3.pop();

                        if (var8.type.retain)
                        {
                            var2.push(var8);
                        }
                    }

                    var3.push(var6);
            }
        }

        if (!var2.isEmpty() && var5 != ExpressionEvaluator$Assoc.NONE)
        {
            throw new ExpressionEvaluator$EvaluatorException("Incomplete expression.", new ExpressionEvaluator$Token(var1, var1.length(), var1.length()));
        }
        else
        {
            while (!var3.isEmpty())
            {
                var6 = (ExpressionEvaluator$Token)var3.pop();

                if (var6.type.associativity == ExpressionEvaluator$Assoc.BRACKET_OPEN)
                {
                    throw new ExpressionEvaluator$EvaluatorException("Missing corresponding " + var6.data + ".", var6);
                }

                if (var6.type.retain)
                {
                    var2.push(var6);
                }
            }

            return var2;
        }
    }

    public ExpressionEvaluator$Token getToken(String var1, int var2) throws ExpressionEvaluator$EvaluatorException
    {
        if (var1 != null && !var1.isEmpty())
        {
            while (var1.length() > var2 && Character.isWhitespace(var1.charAt(var2)))
            {
                ++var2;
            }

            if (var1.length() <= var2)
            {
                return null;
            }
            else
            {
                char var3 = var1.charAt(var2);
                int var4 = var2 + 1;

                if (Character.isDigit(var3))
                {
                    boolean var9 = false;
                    char var6;

                    if (var3 == 48 && var1.length() > var4 && Character.toLowerCase(var1.charAt(var4)) == 120)
                    {
                        ++var4;

                        while (var1.length() > var4)
                        {
                            var6 = var1.charAt(var4);

                            if (!Character.isDigit(var6) && (var6 < 97 || var6 > 102) && (var6 < 65 || var6 > 70))
                            {
                                break;
                            }

                            ++var4;
                        }
                    }
                    else
                    {
                        while (var1.length() > var4)
                        {
                            var6 = var1.charAt(var4);

                            if (Character.isDigit(var6))
                            {
                                ++var4;
                            }
                            else
                            {
                                if (var6 != 46)
                                {
                                    break;
                                }

                                ++var4;
                                var9 = true;
                            }
                        }
                    }

                    try
                    {
                        return var9 ? new ExpressionEvaluator$Token(var1, var2, var4, ExpressionEvaluator$TokenType.NUMBER, Double.valueOf(Double.parseDouble(var1.substring(var2, var4)))) : new ExpressionEvaluator$Token(var1, var2, var4, ExpressionEvaluator$TokenType.NUMBER, Long.decode(var1.substring(var2, var4)));
                    }
                    catch (NumberFormatException var7)
                    {
                        throw new ExpressionEvaluator$EvaluatorException("Invalid number.", new ExpressionEvaluator$Token(var1, var2, var4));
                    }
                }
                else
                {
                    if (!Character.isLetter(var3) && var3 != 95)
                    {
                        if (var3 == 34 || var3 == 39)
                        {
                            var4 = var1.indexOf(var3, var4);

                            if (var4 < 0)
                            {
                                throw new ExpressionEvaluator$EvaluatorException("Unmatched string delimeter " + var3 + ".", new ExpressionEvaluator$Token(var1, var2, var2 + 1));
                            }

                            ++var4;
                            return new ExpressionEvaluator$Token(var1, var2, var4, ExpressionEvaluator$TokenType.STRING, var1.substring(var2 + 1, var4 - 1));
                        }

                        if ((var3 == 60 || var3 == 62 || var3 == 33) && var1.length() > var4 && var1.charAt(var4) == 61)
                        {
                            ++var4;
                        }
                    }
                    else
                    {
                        while (var1.length() > var4)
                        {
                            char var5 = var1.charAt(var4);

                            if (!Character.isLetterOrDigit(var5) && var5 != 95 && var5 != 46)
                            {
                                break;
                            }

                            ++var4;
                        }

                        Object var8 = this.getIdentifierValue(var1.substring(var2, var4));

                        if (var8 != null)
                        {
                            if (var8 instanceof ExpressionEvaluator$EvaluationDelegate && ((ExpressionEvaluator$EvaluationDelegate)var8).explicitArguments() > 0)
                            {
                                return new ExpressionEvaluator$Token(var1, var2, var4, ExpressionEvaluator$TokenType.FUNCTION, var8);
                            }

                            return new ExpressionEvaluator$Token(var1, var2, var4, ExpressionEvaluator$TokenType.VARIABLE, var8);
                        }
                    }

                    ExpressionEvaluator$TokenType var10 = (ExpressionEvaluator$TokenType)_symbolMap.get(var1.substring(var2, var4).toLowerCase());

                    if (var10 != null)
                    {
                        return new ExpressionEvaluator$Token(var1, var2, var4, var10);
                    }
                    else
                    {
                        throw new ExpressionEvaluator$EvaluatorException("Unexpected token.", new ExpressionEvaluator$Token(var1, var2, var4));
                    }
                }
            }
        }
        else
        {
            return null;
        }
    }

    static
    {
        ExpressionEvaluator$TokenType[] var0 = ExpressionEvaluator$TokenType.values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            ExpressionEvaluator$TokenType var3 = var0[var2];

            if (var3.symbol != null)
            {
                _symbolMap.put(var3.symbol, var3);
            }
        }
    }
}
