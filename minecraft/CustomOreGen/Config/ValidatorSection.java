package CustomOreGen.Config;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorSection extends ValidatorNode
{
    protected ValidatorSection(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.replaceWithNodeContents(new Node[] {this.getNode()});
        return false;
    }
}
