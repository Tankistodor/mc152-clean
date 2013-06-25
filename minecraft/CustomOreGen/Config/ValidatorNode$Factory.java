package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorNode$Factory implements ValidatorNode$IValidatorFactory
{
    public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorNode(var1, var2);
    }
}
