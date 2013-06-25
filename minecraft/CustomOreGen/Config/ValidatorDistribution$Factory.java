package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import CustomOreGen.Server.IOreDistribution$IDistributionFactory;
import org.w3c.dom.Node;

public class ValidatorDistribution$Factory implements ValidatorNode$IValidatorFactory
{
    private final IOreDistribution$IDistributionFactory _distributionFactory;

    public ValidatorDistribution$Factory(IOreDistribution$IDistributionFactory var1)
    {
        this._distributionFactory = var1;
    }

    public ValidatorDistribution createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorDistribution(var1, var2, this._distributionFactory);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
