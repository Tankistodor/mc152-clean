package CustomOreGen.Config;

import org.w3c.dom.Node;

public class ValidatorIfCondition extends ValidatorCondition
{
    protected ValidatorIfCondition(ValidatorNode var1, Node var2)
    {
        super(var1, var2, false);
    }

    protected boolean evaluateCondition() throws ParserException
    {
        return ((Boolean)this.validateRequiredAttribute(Boolean.class, "Condition", true)).booleanValue();
    }
}
