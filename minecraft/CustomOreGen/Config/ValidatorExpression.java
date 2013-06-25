package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$EvaluatorException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorExpression extends ValidatorSimpleNode
{
    protected ValidatorExpression(ValidatorNode var1, Node var2, ExpressionEvaluator var3)
    {
        super(var1, var2, String.class, var3);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.checkChildrenValid();
        Object var1 = null;

        try
        {
            var1 = this.evaluator.evaluate((String)this.content);
        }
        catch (ExpressionEvaluator$EvaluatorException var3)
        {
            throw new ParserException(var3.getMessage(), this.getNode(), var3);
        }

        this.replaceWithNode(new Node[] {var1 == null ? null : this.getNode().getOwnerDocument().createTextNode(var1.toString())});
        return false;
    }
}
