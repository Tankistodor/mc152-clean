package CustomOreGen.Config;

import net.minecraft.src.ModLoader;

import org.w3c.dom.Node;

public class ValidatorIfModInstalled extends ValidatorCondition
{
    protected ValidatorIfModInstalled(ValidatorNode var1, Node var2, boolean var3)
    {
        super(var1, var2, var3);
    }

    protected boolean evaluateCondition() throws ParserException
    {
        String var1 = (String)this.validateRequiredAttribute(String.class, "name", true);
        return ModLoader.isModLoaded(var1);
    }
}
