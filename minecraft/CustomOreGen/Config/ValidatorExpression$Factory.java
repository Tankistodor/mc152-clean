package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorExpression$Factory implements ValidatorNode$IValidatorFactory
{
    private final ExpressionEvaluator _evaluator;

    public ValidatorExpression$Factory(ExpressionEvaluator var1)
    {
        this._evaluator = var1;
    }

    public ValidatorExpression createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorExpression(var1, var2, this._evaluator);
    }

   /* public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
