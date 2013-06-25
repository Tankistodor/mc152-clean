package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorIfModInstalled$Factory implements ValidatorNode$IValidatorFactory
{
    private final boolean _invert;

    public ValidatorIfModInstalled$Factory(boolean var1)
    {
        this._invert = var1;
    }

    public ValidatorIfModInstalled createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorIfModInstalled(var1, var2, this._invert);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
