package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import CustomOreGen.Server.IOreDistribution;
import org.w3c.dom.Node;

public class ValidatorPDist$Factory implements ValidatorNode$IValidatorFactory
{
    private final IOreDistribution _parentDist;

    public ValidatorPDist$Factory(IOreDistribution var1)
    {
        this._parentDist = var1;
    }

    public ValidatorPDist createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorPDist(var1, var2, this._parentDist);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
