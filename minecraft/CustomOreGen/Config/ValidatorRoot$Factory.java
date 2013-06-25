package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import java.util.Collection;
import org.w3c.dom.Node;

public class ValidatorRoot$Factory implements ValidatorNode$IValidatorFactory
{
    private final Collection _topLevelNodes;

    public ValidatorRoot$Factory(Collection var1)
    {
        this._topLevelNodes = var1;
    }

    public ValidatorRoot createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorRoot(var1, var2, this._topLevelNodes);
    }

    /*public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
