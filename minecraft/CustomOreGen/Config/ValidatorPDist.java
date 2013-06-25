package CustomOreGen.Config;

import CustomOreGen.Server.IOreDistribution;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import org.w3c.dom.Node;

public class ValidatorPDist extends ValidatorNode
{
    private final IOreDistribution _parentDist;
    public String name = null;
    public PDist pdist = null;

    protected ValidatorPDist(ValidatorNode var1, Node var2, IOreDistribution var3)
    {
        super(var1, var2);
        this._parentDist = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        this.name = (String)this.validateRequiredAttribute(String.class, "Name", true);

        if (this._parentDist == null)
        {
            this.pdist = new PDist();
        }
        else
        {
            try
            {
                this.pdist = (PDist)this._parentDist.getDistributionSetting(this.name);
            }
            catch (ClassCastException var4)
            {
                throw new ParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode(), var4);
            }

            if (this.pdist == null)
            {
                throw new ParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode());
            }
        }

        this.pdist.mean = ((Float)this.validateNamedAttribute(Float.class, "Avg", Float.valueOf(this.pdist.mean), true)).floatValue();
        this.pdist.range = ((Float)this.validateNamedAttribute(Float.class, "Range", Float.valueOf(this.pdist.range), true)).floatValue();
        this.pdist.type = (PDist$Type)this.validateNamedAttribute(PDist$Type.class, "Type", this.pdist.type, true);

        if (this._parentDist != null)
        {
            try
            {
                this._parentDist.setDistributionSetting(this.name, this.pdist);
            }
            catch (IllegalAccessException var2)
            {
                throw new ParserException("Setting \'" + this.name + "\' is not configurable.", this.getNode(), var2);
            }
            catch (IllegalArgumentException var3)
            {
                throw new ParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode(), var3);
            }
        }

        return true;
    }
}
