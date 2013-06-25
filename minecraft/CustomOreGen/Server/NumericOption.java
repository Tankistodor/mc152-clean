package CustomOreGen.Server;

public class NumericOption extends ConfigOption
{
    private double _value = 0.0D;
    private double _nvalue = 0.0D;
    private double _base = 0.0D;
    private double _range = 0.0D;
    private double _displayBase = Double.NaN;
    private double _displayRange = Double.NaN;
    private double _displayIncr = Double.NaN;

    public NumericOption(String var1)
    {
        super(var1);
    }

    public Object getValue()
    {
        return Double.valueOf(this._value);
    }

    public boolean setValue(Object var1)
    {
        double var2 = 0.0D;

        if (var1 == null)
        {
            return false;
        }
        else
        {
            if (!Double.TYPE.isInstance(var1) && !(var1 instanceof Double))
            {
                if (var1 instanceof Number)
                {
                    var2 = ((Number)var1).doubleValue();
                }
                else
                {
                    if (!(var1 instanceof String))
                    {
                        return false;
                    }

                    try
                    {
                        var2 = Double.parseDouble((String)var1);
                    }
                    catch (NumberFormatException var5)
                    {
                        return false;
                    }
                }
            }
            else
            {
                var2 = ((Double)var1).doubleValue();
            }

            if (var2 >= this.getMin() && var2 <= this.getMax())
            {
                this._value = var2;
                this._nvalue = this._range == 0.0D ? 0.0D : this.round((var2 - this._base) / this._range);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public double getMin()
    {
        return this._base;
    }

    public double getMax()
    {
        return this._base + this._range;
    }

    public boolean setLimits(double var1, double var3)
    {
        if (var3 < var1)
        {
            return false;
        }
        else
        {
            this._base = var1;
            this._range = var3 - var1;

            if (this._value < var1)
            {
                this._value = var1;
                this._nvalue = 0.0D;
            }
            else if (this._value > var3)
            {
                this._value = var3;
                this._nvalue = 1.0D;
            }

            return true;
        }
    }

    public double getDisplayMin()
    {
        return Double.isNaN(this._displayBase) ? this._base : this._displayBase;
    }

    public double getDisplayMax()
    {
        double var1 = Double.isNaN(this._displayRange) ? this._range : this._displayRange;
        return this.getDisplayMin() + var1;
    }

    public double getDisplayIncr()
    {
        if (Double.isNaN(this._displayIncr))
        {
            double var1 = Double.isNaN(this._displayRange) ? this._range : this._displayRange;
            return var1 / 100.0D;
        }
        else
        {
            return this._displayIncr;
        }
    }

    public boolean setDisplayLimits(double var1, double var3, double var5)
    {
        double var7 = var3 - var1;

        if (var7 < 0.0D)
        {
            return false;
        }
        else
        {
            double var9 = var7 / var5;

            if (!Double.isInfinite(var9) && var9 >= 1.0D)
            {
                this._displayBase = var1;
                this._displayRange = var7;
                this._displayIncr = var5;
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public double getDisplayValue()
    {
        double var1 = Double.isNaN(this._displayRange) ? this._range : this._displayRange;
        return this.round(this.getDisplayMin() + this.getNormalizedDisplayValue() * var1);
    }

    public double getNormalizedDisplayValue()
    {
        double var1 = Double.isNaN(this._displayRange) ? this._range : this._displayRange;

        if (var1 == 0.0D)
        {
            return 0.0D;
        }
        else
        {
            double var3 = Double.isNaN(this._displayIncr) ? 100.0D : var1 / this._displayIncr;
            return this.round(this._nvalue, var3);
        }
    }

    public boolean setNormalizedDisplayValue(double var1)
    {
        if (var1 >= 0.0D && var1 <= 1.0D)
        {
            this._nvalue = var1;
            this._nvalue = this.getNormalizedDisplayValue();
            this._value = this.round(this._base + this._nvalue * this._range);
            return true;
        }
        else
        {
            return false;
        }
    }

    private double round(double var1)
    {
        return (double)Math.round(var1 * 1000000.0D) / 1000000.0D;
    }

    private double round(double var1, double var3)
    {
        var3 = Math.min(Math.abs(var3), 1000000.0D);
        return (double)Math.round(var1 * var3) / var3;
    }
}
