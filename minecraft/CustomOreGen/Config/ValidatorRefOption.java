package CustomOreGen.Config;

import CustomOreGen.Server.ConfigOption;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorRefOption extends ValidatorNode
{
    public ValidatorRefOption(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        String var1 = (String)this.validateRequiredAttribute(String.class, "name", true);
        ConfigOption var2 = this.getParser().target.getConfigOption(var1);

        if (var2 == null)
        {
            throw new ParserException("Option \'" + var1 + "\' is not a recognized option.", this.getNode());
        }
        else
        {
            this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
            this.checkChildrenValid();
            Object var3 = var2.getValue();
            this.replaceWithNode(new Node[] {var3 == null ? null : this.getNode().getOwnerDocument().createTextNode(var3.toString())});
            return false;
        }
    }
}
