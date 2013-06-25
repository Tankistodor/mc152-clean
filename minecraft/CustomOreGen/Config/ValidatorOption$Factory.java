package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorOption$Factory implements ValidatorNode$IValidatorFactory
{
    private final Class _type;

    public ValidatorOption$Factory(Class var1)
    {
        this._type = var1;
    }

    public ValidatorOption createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorOption(var1, var2, this._type);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
