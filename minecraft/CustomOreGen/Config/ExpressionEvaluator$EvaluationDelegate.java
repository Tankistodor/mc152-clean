package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$EvaluatorException;
import CustomOreGen.Config.ExpressionEvaluator$Token;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EmptyStackException;
import java.util.Stack;

public class ExpressionEvaluator$EvaluationDelegate
{
    private final Object _obj;
    private final Method _method;
    private final boolean _passToken;

    public ExpressionEvaluator$EvaluationDelegate(boolean var1, Object var2, String var3, Class ... var4)
    {
        this(var1, var2, var2.getClass(), var3, var4);
    }

    public ExpressionEvaluator$EvaluationDelegate(boolean var1, Class var2, String var3, Class ... var4)
    {
        this(var1, (Object)null, var2, var3, var4);
    }

    private ExpressionEvaluator$EvaluationDelegate(boolean var1, Object var2, Class var3, String var4, Class ... var5)
    {
        Method var6 = null;

        if (var5 != null)
        {
            try
            {
                var6 = var3.getMethod(var4, var5);
            }
            catch (NoSuchMethodException var11)
            {
                ;
            }
        }
        else
        {
            Method[] var7 = var3.getMethods();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9)
            {
                Method var10 = var7[var9];

                if (var10.getName().equals(var4))
                {
                    if (var6 != null)
                    {
                        throw new IllegalArgumentException("Method \'" + var4 + "\' is ambiguous for class " + var3.getSimpleName());
                    }

                    var6 = var10;
                }
            }
        }

        if (var6 == null)
        {
            throw new IllegalArgumentException("Method \'" + var4 + "\' is not defined for class " + var3.getSimpleName());
        }
        else if ((var6.getModifiers() & 8) == 0 && (var2 == null || !var3.isInstance(var2)))
        {
            throw new IllegalArgumentException("Method \'" + var4 + "\' for class " + var3.getSimpleName() + " requires an object instance");
        }
        else
        {
            if (var1)
            {
                boolean var12 = false;

                if (var6.getParameterTypes().length > 0)
                {
                    Class var13 = var6.getParameterTypes()[0];

                    if (var13.isAssignableFrom(String.class) || var13.isAssignableFrom(ExpressionEvaluator$Token.class))
                    {
                        var12 = true;
                    }
                }

                if (!var12)
                {
                    throw new IllegalArgumentException("Method \'" + var4 + "\' for class " + var3.getSimpleName() + " must take a String or Token as the first parameter");
                }
            }

            this._obj = var2;
            this._method = var6;
            this._passToken = var1;
        }
    }

    protected Object evaluate(ExpressionEvaluator$Token var1, Stack var2) throws ExpressionEvaluator$EvaluatorException, EmptyStackException
    {
        Class[] var3 = this._method.getParameterTypes();
        Object[] var4 = new Object[var3.length];

        for (int var5 = var4.length - 1; var5 >= 0; --var5)
        {
            if (var5 == 0 && this._passToken)
            {
                if (var3[0].isAssignableFrom(ExpressionEvaluator$Token.class))
                {
                    var4[0] = var1;
                }
                else
                {
                    var4[0] = var1.toString();
                }
            }
            else if (var3[var5].isAssignableFrom(String.class))
            {
                var4[var5] = ExpressionEvaluator.evaluateString(var2);
            }
            else if (!var3[var5].isAssignableFrom(Double.class) && !var3[var5].isAssignableFrom(Double.TYPE))
            {
                if (var3[var5].isAssignableFrom(Boolean.class))
                {
                    var4[var5] = Boolean.valueOf(ExpressionEvaluator.evaluateBoolean(var2));
                }
                else
                {
                    var4[var5] = ExpressionEvaluator.evaluate(var2);
                }
            }
            else
            {
                var4[var5] = Double.valueOf(ExpressionEvaluator.evaluateNumber(var2));
            }
        }

        try
        {
            Object var9 = this._method.invoke(this._obj, var4);

            if (var9 == null)
            {
                throw new ExpressionEvaluator$EvaluatorException("Delegate evaluation produced null value.", var1);
            }
            else
            {
                return var9;
            }
        }
        catch (IllegalAccessException var6)
        {
            throw new ExpressionEvaluator$EvaluatorException("Cannot evaluate delegate.", var1, var6);
        }
        catch (IllegalArgumentException var7)
        {
            throw new ExpressionEvaluator$EvaluatorException(var7.getMessage(), var1, var7);
        }
        catch (InvocationTargetException var8)
        {
            throw new ExpressionEvaluator$EvaluatorException(var8.getCause().getMessage(), var1, var8);
        }
    }

    public int explicitArguments()
    {
        return this._method.getParameterTypes().length - (this._passToken ? 1 : 0);
    }

    public String toString()
    {
        String var1 = this._method.getDeclaringClass().getSimpleName() + "." + this._method.getName();
        return this._obj != null ? var1 + "(" + this._obj + ")" : var1;
    }
}
