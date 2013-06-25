package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorImport$Factory implements ValidatorNode$IValidatorFactory
{
    private final boolean _required;

    public ValidatorImport$Factory(boolean var1)
    {
        this._required = var1;
    }

    public ValidatorImport createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorImport(var1, var2, this._required);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
