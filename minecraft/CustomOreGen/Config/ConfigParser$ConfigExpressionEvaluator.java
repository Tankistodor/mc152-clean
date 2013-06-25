package CustomOreGen.Config;

import CustomOreGen.Config.ExpressionEvaluator$EvaluationDelegate;
import CustomOreGen.Server.ConfigOption;
import CustomOreGen.Util.CIStringMap;
import java.util.Map;

import net.minecraft.src.ModLoader;

public class ConfigParser$ConfigExpressionEvaluator extends ExpressionEvaluator
{
    private Map localIdentifiers;

    final ConfigParser this$0;

    public ConfigParser$ConfigExpressionEvaluator(ConfigParser var1)
    {
        this.this$0 = var1;
        this.localIdentifiers = new CIStringMap();
        this.localIdentifiers.put("isModInstalled", new ExpressionEvaluator$EvaluationDelegate(false, ModLoader.class, "isModLoaded", new Class[] {String.class}));
        this.localIdentifiers.put("blockExists", new ExpressionEvaluator$EvaluationDelegate(false, var1, "blockExists", new Class[] {String.class}));
        this.localIdentifiers.put("biomeExists", new ExpressionEvaluator$EvaluationDelegate(false, var1, "biomeExists", new Class[] {String.class}));
        this.localIdentifiers.put("world.nextRandom", new ExpressionEvaluator$EvaluationDelegate(false, var1, "nextRandom", new Class[0]));
    }

    public ConfigParser$ConfigExpressionEvaluator(ConfigParser var1, Object var2)
    {
        this.this$0 = var1;
        this.localIdentifiers = new CIStringMap();
        this.localIdentifiers.put("_default_", var2);
    }

    protected Object getIdentifierValue(String var1)
    {
        String var2 = var1.toLowerCase();
        ConfigOption var3 = this.this$0.target.getConfigOption(var1);

        if (var3 != null)
        {
            return var3.getValue();
        }
        else
        {
            Object var4 = this.this$0.target.getWorldProperty(var1);

            if (var4 != null)
            {
                return var4;
            }
            else
            {
                Object var5 = this.localIdentifiers.get(var1);
                return var5 != null ? var5 : (var2.startsWith("age.") ? Integer.valueOf(0) : super.getIdentifierValue(var1));
            }
        }
    }
}
