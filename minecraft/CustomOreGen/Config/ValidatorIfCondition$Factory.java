package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorIfCondition$Factory implements ValidatorNode$IValidatorFactory
{
    public ValidatorIfCondition createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorIfCondition(var1, var2);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
