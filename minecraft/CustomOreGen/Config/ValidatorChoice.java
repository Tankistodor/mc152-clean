package CustomOreGen.Config;

import org.w3c.dom.Node;

public class ValidatorChoice extends ValidatorNode
{
    public String value = null;
    public String displayValue = null;
    public String description = null;

    protected ValidatorChoice(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.value = (String)this.validateRequiredAttribute(String.class, "Value", true);
        this.displayValue = (String)this.validateNamedAttribute(String.class, "DisplayValue", this.displayValue, true);
        this.description = (String)this.validateNamedAttribute(String.class, "Description", (Object)null, true);
        return true;
    }
}
