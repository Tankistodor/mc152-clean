package CustomOreGen.Config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public abstract class ValidatorCondition extends ValidatorNode
{
    protected boolean invert = false;

    protected ValidatorCondition(ValidatorNode var1, Node var2, boolean var3)
    {
        super(var1, var2);
        this.invert = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        Element var1 = null;
        Element var2 = null;

        for (Node var3 = this.getNode().getFirstChild(); var3 != null; var3 = var3.getNextSibling())
        {
            if (var3.getNodeType() == 1)
            {
                if (var3.getNodeName().equalsIgnoreCase("Then"))
                {
                    var1 = (Element)var3;
                }
                else if (var3.getNodeName().equalsIgnoreCase("Else"))
                {
                    var2 = (Element)var3;
                }
            }
        }

        if (var1 == null)
        {
            if (var2 != null)
            {
                throw new ParserException("Cannot have Else without Then", var2);
            }

            var1 = this.getNode().getOwnerDocument().createElement("Then");

            while (this.getNode().hasChildNodes())
            {
                var1.appendChild(this.getNode().getFirstChild());
            }

            this.getNode().appendChild(var1);
        }

        var1.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);

        if (var2 != null)
        {
            var2.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        }

        super.validateChildren();
        boolean var5 = this.evaluateCondition();

        if (this.invert)
        {
            var5 = !var5;
        }

        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.checkChildrenValid();
        Element var4 = var5 ? var1 : var2;

        if (var4 != null)
        {
            (new ValidatorUnchecked(this, var4)).validate();
        }

        this.replaceWithNodeContents(new Node[] {var4});
        return false;
    }

    protected abstract boolean evaluateCondition() throws ParserException;
}
