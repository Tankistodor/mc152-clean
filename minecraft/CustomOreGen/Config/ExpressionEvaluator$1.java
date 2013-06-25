package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$Assoc;
import CustomOreGen.Config.ExpressionEvaluator$TokenType;

class ExpressionEvaluator$1
{
    static final int[] $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType;

    static final int[] $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc = new int[ExpressionEvaluator$Assoc.values().length];

    static
    {
        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[ExpressionEvaluator$Assoc.NONE.ordinal()] = 1;
        }
        catch (NoSuchFieldError var29)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[ExpressionEvaluator$Assoc.BRACKET_OPEN.ordinal()] = 2;
        }
        catch (NoSuchFieldError var28)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[ExpressionEvaluator$Assoc.BRACKET_CLOSE.ordinal()] = 3;
        }
        catch (NoSuchFieldError var27)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[ExpressionEvaluator$Assoc.LEFT.ordinal()] = 4;
        }
        catch (NoSuchFieldError var26)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$Assoc[ExpressionEvaluator$Assoc.RIGHT.ordinal()] = 5;
        }
        catch (NoSuchFieldError var25)
        {
            ;
        }

        $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType = new int[ExpressionEvaluator$TokenType.values().length];

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.SCIENTIFIC.ordinal()] = 1;
        }
        catch (NoSuchFieldError var24)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.TO_STRING.ordinal()] = 2;
        }
        catch (NoSuchFieldError var23)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.TO_NUMBER.ordinal()] = 3;
        }
        catch (NoSuchFieldError var22)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.TO_BOOLEAN.ordinal()] = 4;
        }
        catch (NoSuchFieldError var21)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.LOGICAL_NOT.ordinal()] = 5;
        }
        catch (NoSuchFieldError var20)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.UNARY_MINUS.ordinal()] = 6;
        }
        catch (NoSuchFieldError var19)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.UNARY_PLUS.ordinal()] = 7;
        }
        catch (NoSuchFieldError var18)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.IF.ordinal()] = 8;
        }
        catch (NoSuchFieldError var17)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.MATCH.ordinal()] = 9;
        }
        catch (NoSuchFieldError var16)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.REPLACE.ordinal()] = 10;
        }
        catch (NoSuchFieldError var15)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.MULTIPLY.ordinal()] = 11;
        }
        catch (NoSuchFieldError var14)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.DIVIDE.ordinal()] = 12;
        }
        catch (NoSuchFieldError var13)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.MODULUS.ordinal()] = 13;
        }
        catch (NoSuchFieldError var12)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.ADD.ordinal()] = 14;
        }
        catch (NoSuchFieldError var11)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.SUBTRACT.ordinal()] = 15;
        }
        catch (NoSuchFieldError var10)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.CONCATENATE.ordinal()] = 16;
        }
        catch (NoSuchFieldError var9)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.LESS.ordinal()] = 17;
        }
        catch (NoSuchFieldError var8)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.LESS_EQUAL.ordinal()] = 18;
        }
        catch (NoSuchFieldError var7)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.GREATER.ordinal()] = 19;
        }
        catch (NoSuchFieldError var6)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.GREATER_EQUAL.ordinal()] = 20;
        }
        catch (NoSuchFieldError var5)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.EQUAL.ordinal()] = 21;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.NOT_EQUAL.ordinal()] = 22;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.AND.ordinal()] = 23;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Config$ExpressionEvaluator$TokenType[ExpressionEvaluator$TokenType.OR.ordinal()] = 24;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
