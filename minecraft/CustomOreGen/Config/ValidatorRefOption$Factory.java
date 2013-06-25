package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorRefOption$Factory implements ValidatorNode$IValidatorFactory
{
    public ValidatorRefOption createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorRefOption(var1, var2);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
