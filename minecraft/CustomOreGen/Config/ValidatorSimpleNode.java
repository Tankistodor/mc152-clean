package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$EvaluatorException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorSimpleNode extends ValidatorNode
{
    public Object content = null;
    public ExpressionEvaluator evaluator;
    private final Class _targetClass;

    protected ValidatorSimpleNode(ValidatorNode var1, Node var2, Class var3, ExpressionEvaluator var4)
    {
        super(var1, var2);
        this._targetClass = var3;
        this.evaluator = (ExpressionEvaluator)(var4 == null ? this.getParser().defaultEvaluator : var4);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        StringBuilder var1 = new StringBuilder();

        for (Node var2 = this.getNode().getFirstChild(); var2 != null; var2 = var2.getNextSibling())
        {
            if (var2.getNodeType() == 3)
            {
                var2.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
                var1.append(var2.getNodeValue());
            }
        }

        String var6 = var1.toString().trim();

        try
        {
            if (var6.startsWith(":="))
            {
                Object var3 = this.evaluator.evaluate(var6.substring(2));

                if (var3 == null)
                {
                    this.content = null;
                }
                else if (this._targetClass.isInstance(var3))
                {
                    this.content = var3;
                }
                else if (var3 instanceof Number && Number.class.isAssignableFrom(this._targetClass))
                {
                    this.getParser();
                    this.content = ConfigParser.convertNumber(this._targetClass, (Number)var3);
                }
                else
                {
                    this.content = ConfigParser.parseString(this._targetClass, var3.toString());
                }
            }
            else
            {
                this.content = ConfigParser.parseString(this._targetClass, var6);
            }

            return true;
        }
        catch (IllegalArgumentException var4)
        {
            throw new ParserException(var4.getMessage(), this.getNode(), var4);
        }
        catch (ExpressionEvaluator$EvaluatorException var5)
        {
            throw new ParserException(var5.getMessage(), this.getNode(), var5);
        }
    }
}
