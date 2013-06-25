package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$Token;

public class ExpressionEvaluator$EvaluatorException extends Exception
{
    public final ExpressionEvaluator$Token token;

    public ExpressionEvaluator$EvaluatorException(String var1, ExpressionEvaluator$Token var2, Throwable var3)
    {
        super(var1, var3);
        this.token = var2;
    }

    public ExpressionEvaluator$EvaluatorException(String var1, ExpressionEvaluator$Token var2)
    {
        super(var1);
        this.token = var2;
    }

    public ExpressionEvaluator$EvaluatorException(String var1, Throwable var2)
    {
        this(var1, (ExpressionEvaluator$Token)null, var2);
    }

    public ExpressionEvaluator$EvaluatorException(String var1)
    {
        this(var1, (ExpressionEvaluator$Token)null);
    }

    public String getMessage()
    {
        if (this.token == null)
        {
            return super.getMessage();
        }
        else
        {
            String var1 = this.token.type == null ? "" : this.token.type.name();
            return String.format("At %s(%d-%d) %s : %s", new Object[] {var1, Integer.valueOf(this.token.start), Integer.valueOf(this.token.end), this.token, super.getMessage()});
        }
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder(this.getMessage());

        if (this.token != null && this.token.source != null && !this.token.source.isEmpty())
        {
            int var2 = (this.token.start + this.token.end - 60) / 2;
            String var3 = "... ";

            if (var2 < 0)
            {
                var3 = "    ";
                var2 = 0;
            }

            int var4 = (this.token.start + this.token.end + 60) / 2;
            String var5 = " ...";

            if (var4 > this.token.source.length())
            {
                var5 = "";
                var4 = this.token.source.length();
            }

            var1.append("\n  ");
            var1.append(var3);
            var1.append(this.token.source.substring(var2, var4).replace("\n", " "));
            var1.append(var5);
            var1.append("\n   at ");

            for (int var6 = var2; var6 < this.token.end; ++var6)
            {
                if (var6 != this.token.start && var6 != this.token.end - 1)
                {
                    var1.append('-');
                }
                else
                {
                    var1.append('^');
                }
            }
        }

        return var1.toString();
    }
}
