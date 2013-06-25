package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import java.util.Collection;
import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorRoot extends ValidatorNode
{
    private final Collection _topLevelNodes;

    protected ValidatorRoot(ValidatorNode var1, Node var2, Collection var3)
    {
        super(var1, var2);
        this._topLevelNodes = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        Node var1 = this.getNode().getParentNode();

        if (var1 != null && var1.getNodeType() == 9)
        {
            this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);

            if (this._topLevelNodes != null)
            {
                Iterator var2 = this._topLevelNodes.iterator();

                while (var2.hasNext())
                {
                    String var3 = (String)var2.next();
                    this.validateNamedChildren(2, var3, (ValidatorNode$IValidatorFactory)null);
                }
            }
        }

        return true;
    }
}
