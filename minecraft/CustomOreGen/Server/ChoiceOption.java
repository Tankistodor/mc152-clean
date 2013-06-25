package CustomOreGen.Server;

import CustomOreGen.Util.CIStringMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ChoiceOption extends ConfigOption
{
    private String _value = null;
    private CIStringMap _valueMap = new CIStringMap(new LinkedHashMap());

    public ChoiceOption(String var1)
    {
        super(var1);
    }

    public Object getValue()
    {
        return this._value;
    }

    public boolean setValue(Object var1)
    {
        if (var1 == null)
        {
            return false;
        }
        else if (!(var1 instanceof String))
        {
            return false;
        }
        else
        {
            String var2 = this._valueMap.getCanonicalKey((String)var1);

            if (var2 == null)
            {
                return false;
            }
            else
            {
                this._value = var2;
                return true;
            }
        }
    }

    public void addPossibleValue(String var1, String var2, String var3)
    {
        if (var1 != null)
        {
            this._valueMap.put(var1, new String[] {var2, var3});

            if (this._value == null)
            {
                this._value = var1;
            }
        }
    }

    public void removePossibleValue(String var1)
    {
        if (var1 != null)
        {
            if (this._value == var1)
            {
                this._value = this.nextPossibleValue();

                if (this._value == var1)
                {
                    this._value = null;
                }
            }

            this._valueMap.remove(var1);
        }
    }

    public String nextPossibleValue()
    {
        boolean var1 = false;
        String var2 = null;
        Iterator var3 = this._valueMap.keySet().iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();

            if (var2 == null)
            {
                var2 = var4;
            }

            if (var1)
            {
                return var4;
            }

            if (var4.equals(this._value))
            {
                var1 = true;
            }
        }

        if (var1)
        {
            return var2;
        }
        else
        {
            return null;
        }
    }

    public String getDisplayValue()
    {
        String[] var1 = (String[])this._valueMap.get(this._value);
        return var1 != null && var1[0] != null ? var1[0] : this._value;
    }

    public String getValueDescription()
    {
        String[] var1 = (String[])this._valueMap.get(this._value);
        return var1 == null ? null : var1[1];
    }
}
