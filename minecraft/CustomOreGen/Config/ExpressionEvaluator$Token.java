package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$TokenType;

public class ExpressionEvaluator$Token
{
    public final String source;
    public final int start;
    public final int end;
    public final ExpressionEvaluator$TokenType type;
    public final Object data;
    private String _str;

    public String toString()
    {
        if (this._str == null)
        {
            this._str = this.source.substring(this.start, this.end);
        }

        return this._str;
    }

    protected ExpressionEvaluator$Token(String var1, int var2, int var3, ExpressionEvaluator$TokenType var4, Object var5)
    {
        this._str = null;
        this.source = var1;
        this.start = var2;
        this.end = var3;
        this.type = var4;
        this.data = var5;
    }

    protected ExpressionEvaluator$Token(String var1, int var2, int var3, ExpressionEvaluator$TokenType var4)
    {
        this(var1, var2, var3, var4, var4.data);
    }

    protected ExpressionEvaluator$Token(String var1, int var2, int var3)
    {
        this(var1, var2, var3, (ExpressionEvaluator$TokenType)null, (Object)null);
    }
}
