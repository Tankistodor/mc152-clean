package CustomOreGen.Config;

import org.w3c.dom.Node;

public class ValidatorUnchecked extends ValidatorNode
{
    protected ValidatorUnchecked(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        return false;
    }
}
