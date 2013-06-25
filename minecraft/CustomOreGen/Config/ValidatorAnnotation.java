package CustomOreGen.Config;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorAnnotation extends ValidatorSimpleNode {
	protected ValidatorAnnotation(ValidatorNode var1, Node var2) {
		super(var1, var2, String.class, (ExpressionEvaluator) null);
	}

	protected boolean validateChildren() throws ParserException {
		super.validateChildren();
		this.getNode().setUserData("validated", Boolean.valueOf(true),
				(UserDataHandler) null);
		return false;
	}

	public static class Factory implements ValidatorNode$IValidatorFactory {

		public ValidatorAnnotation createValidator(ValidatorNode parent,
				Node node) {
			return new ValidatorAnnotation(parent, node);
		}

		/*public volatile ValidatorNode createValidator1(ValidatorNode x0, Node x1) {
			return createValidator(x0, x1);
		}*/

		public Factory() {
		}
	}

}
