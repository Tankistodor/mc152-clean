package CustomOreGen.Config;

import CustomOreGen.Server.ChoiceOption;
import CustomOreGen.Server.ConfigOption;
import org.w3c.dom.Node;

public class ValidatorIfChoice extends ValidatorCondition
{
    protected ValidatorIfChoice(ValidatorNode var1, Node var2, boolean var3)
    {
        super(var1, var2, var3);
    }

    protected boolean evaluateCondition() throws ParserException
    {
        String var1 = (String)this.validateRequiredAttribute(String.class, "name", true);
        String var2 = (String)this.validateNamedAttribute(String.class, "value", (Object)null, true);
        ConfigOption var3 = this.getParser().target.getConfigOption(var1);
        boolean var4 = var3 != null && var3 instanceof ChoiceOption;

        if (var2 == null)
        {
            return var4;
        }
        else if (var4)
        {
            return var2.equalsIgnoreCase((String)var3.getValue());
        }
        else
        {
            throw new ParserException("Option \'" + var1 + "\' is not a recognized Choice option.", this.getNode());
        }
    }
}
