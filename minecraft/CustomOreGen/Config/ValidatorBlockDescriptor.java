package CustomOreGen.Config;

import org.w3c.dom.Node;

public class ValidatorBlockDescriptor extends ValidatorNode
{
    public String blocks = null;
    public float weight = 1.0F;

    protected ValidatorBlockDescriptor(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.blocks = (String)this.validateRequiredAttribute(String.class, "Block", true);
        this.weight = ((Float)this.validateNamedAttribute(Float.class, "Weight", Float.valueOf(this.weight), true)).floatValue();
        return true;
    }
}
