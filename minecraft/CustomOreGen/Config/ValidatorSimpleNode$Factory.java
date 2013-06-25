package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import org.w3c.dom.Node;

public class ValidatorSimpleNode$Factory implements ValidatorNode$IValidatorFactory
{
    private final Class _targetClass;
    private final ExpressionEvaluator _evaluator;

    public ValidatorSimpleNode$Factory(Class var1, ExpressionEvaluator var2)
    {
        this._targetClass = var1;
        this._evaluator = var2;
    }

    public ValidatorSimpleNode$Factory(Class var1)
    {
        this._targetClass = var1;
        this._evaluator = null;
    }

	@Override
	public ValidatorNode createValidator(ValidatorNode var1, Node var2) {
		// TODO Auto-generated method stub
		return new ValidatorSimpleNode(var1, var2, this._targetClass, this._evaluator);
	}

    /*public ValidatorSimpleNode createValidator(ValidatorNode var1, Node var2)
    {
        return new ValidatorSimpleNode(var1, var2, this._targetClass, this._evaluator);
    }

    public ValidatorNode createValidator(ValidatorNode var1, Node var2)
    {
        return this.createValidator(var1, var2);
    }*/
}
