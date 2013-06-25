package CustomOreGen.Config;

import org.w3c.dom.Node;

public class ValidatorBiomeDescriptor extends ValidatorNode
{
    public String biome = null;
    public float weight = 1.0F;

    protected ValidatorBiomeDescriptor(ValidatorNode var1, Node var2)
    {
        super(var1, var2);
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.biome = (String)this.validateRequiredAttribute(String.class, "Name", true);
        this.weight = ((Float)this.validateNamedAttribute(Float.class, "Weight", Float.valueOf(this.weight), true)).floatValue();
        return true;
    }
}
