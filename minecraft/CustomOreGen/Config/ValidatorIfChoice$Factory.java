package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorIfChoice$Factory implements ValidatorNode$IValidatorFactory
{
    private final boolean _invert;

    public ValidatorIfChoice$Factory(boolean var1)
    {
        this._invert = var1;
    }

    public ValidatorIfChoice createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorIfChoice(var1, var2, this._invert);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
