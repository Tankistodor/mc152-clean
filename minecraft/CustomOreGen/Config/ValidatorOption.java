package CustomOreGen.Config;

import CustomOreGen.CustomOreGenBase;
import CustomOreGen.Config.ValidatorChoice$Factory;
import CustomOreGen.Server.ChoiceOption;
import CustomOreGen.Server.ConfigOption;
import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.ConfigOption$DisplayState;
import CustomOreGen.Server.NumericOption;
import java.util.Iterator;
import org.w3c.dom.Node;

public class ValidatorOption extends ValidatorNode
{
    private final Class _type;

    protected ValidatorOption(ValidatorNode var1, Node var2, Class var3)
    {
        super(var1, var2);
        this._type = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        String var1 = (String)this.validateRequiredAttribute(String.class, "name", true);
        Object var2 = null;
        Class var3 = null;

        if (this._type == ChoiceOption.class)
        {
            var3 = String.class;
            ChoiceOption var4 = new ChoiceOption(var1);
            var2 = var4;
            Iterator var5 = this.validateNamedChildren(2, "Choice", new ValidatorChoice$Factory()).iterator();

            while (var5.hasNext())
            {
                ValidatorChoice var6 = (ValidatorChoice)var5.next();
                var4.addPossibleValue(var6.value, var6.displayValue, var6.description);
            }

            if (var4.getValue() == null)
            {
                throw new ParserException("Choice option has no possible values.", this.getNode());
            }
        }
        else if (this._type == NumericOption.class)
        {
            var3 = Double.class;
            NumericOption var16 = new NumericOption(var1);
            var2 = var16;
            double var18 = ((Double)this.validateNamedAttribute(var3, "min", Double.valueOf(var16.getMin()), true)).doubleValue();
            double var7 = ((Double)this.validateNamedAttribute(var3, "max", Double.valueOf(var16.getMax()), true)).doubleValue();

            if (!var16.setLimits(var18, var7))
            {
                throw new ParserException("Numeric option value range [" + var18 + "," + var7 + "] is invalid.", this.getNode());
            }

            double var9 = ((Double)this.validateNamedAttribute(var3, "displayMin", Double.valueOf(var16.getMin()), true)).doubleValue();
            double var11 = ((Double)this.validateNamedAttribute(var3, "displayMax", Double.valueOf(var16.getMax()), true)).doubleValue();
            double var13 = ((Double)this.validateNamedAttribute(var3, "displayIncrement", Double.valueOf((var11 - var9) / 100.0D), true)).doubleValue();

            if (!var16.setDisplayLimits(var9, var11, var13))
            {
                throw new ParserException("Numeric option display range/increment [" + var9 + "," + var11 + "]/" + var13 + " is invalid.", this.getNode());
            }
        }
        else if (this._type == ConfigOption$DisplayGroup.class)
        {
            var2 = new ConfigOption$DisplayGroup(var1);
        }

        ((ConfigOption)var2).setDisplayState((ConfigOption$DisplayState)this.validateNamedAttribute(ConfigOption$DisplayState.class, "displayState", ((ConfigOption)var2).getDisplayState(), true));
        ((ConfigOption)var2).setDisplayName((String)this.validateNamedAttribute(String.class, "displayName", ((ConfigOption)var2).getDisplayName(), true));
        ((ConfigOption)var2).setDescription((String)this.validateNamedAttribute(String.class, "description", ((ConfigOption)var2).getDescription(), true));
        String var17 = (String)this.validateNamedAttribute(String.class, "displayGroup", (Object)null, true);

        if (var17 != null)
        {
            ConfigOption var20 = this.getParser().target.getConfigOption(var17);

            if (var20 == null || !(var20 instanceof ConfigOption$DisplayGroup))
            {
                throw new ParserException("Option \'" + var17 + "\' is not a recognized Display Group.", this.getNode());
            }

            ((ConfigOption)var2).setDisplayGroup((ConfigOption$DisplayGroup)var20);
        }

        Object var19 = var3 == null ? null : this.validateNamedAttribute(var3, "default", (Object)null, true);

        if (this.getParser().target.getConfigOption(((ConfigOption)var2).getName()) != null)
        {
            throw new ParserException("An Option named \'" + ((ConfigOption)var2).getName() + "\' already exists.", this.getNode());
        }
        else
        {
            this.getParser().target.getConfigOptions().add(var2);
            String var21 = this.getParser().target.loadConfigOption(((ConfigOption)var2).getName());

            if (var21 != null)
            {
                String var22 = null;

                try
                {
                    Object var8 = ConfigParser.parseString(var3, var21);

                    if (!((ConfigOption)var2).setValue(var8))
                    {
                        var22 = "";
                    }
                }
                catch (IllegalArgumentException var15)
                {
                    var22 = " (" + var15.getMessage() + ")";
                }

                if (var22 == null)
                {
                    return true;
                }

                CustomOreGenBase.log.warning("The saved value \'" + var21 + "\' for Config Option \'" + ((ConfigOption)var2).getName() + "\' is invalid" + var22 + ".  " + "The default value \'" + (var19 == null ? ((ConfigOption)var2).getValue() : var19) + "\' will be used instead.");
            }

            if (var19 != null && !((ConfigOption)var2).setValue(var19))
            {
                throw new ParserException("Invalid default value \'" + var19 + "\' for option \'" + ((ConfigOption)var2).getName() + "\'.", this.getNode());
            }
            else
            {
                return true;
            }
        }
    }
}
