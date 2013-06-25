package CustomOreGen.Util;

import CustomOreGen.Server.DistributionSettingMap$Copyable;
import CustomOreGen.Util.PDist$1;
import CustomOreGen.Util.PDist$Type;
import java.util.Random;

public class PDist implements DistributionSettingMap$Copyable
{
    public float mean;
    public float range;
    public PDist$Type type;

    public PDist(float var1, float var2, PDist$Type var3)
    {
        this.set(var1, var2, var3);
    }

    public PDist(float var1, float var2)
    {
        this.set(var1, var2, PDist$Type.uniform);
    }

    public PDist()
    {
        this.set(0.0F, 0.0F, PDist$Type.uniform);
    }

    public void copyFrom(PDist var1)
    {
        this.mean = var1.mean;
        this.range = var1.range;
        this.type = var1.type;
    }

    public PDist set(float var1, float var2, PDist$Type var3)
    {
        this.mean = var1;
        this.range = var2 >= 0.0F ? var2 : -var2;
        this.type = var3;
        return this;
    }

    public float getMax()
    {
        return this.mean + this.range;
    }

    public float getMin()
    {
        return this.mean - this.range;
    }

    public float getValue(Random var1)
    {
        if (this.range == 0.0F)
        {
            return this.mean;
        }
        else
        {
            switch (PDist$1.$SwitchMap$net$minecraft$src$CustomOreGen$Util$PDist$Type[this.type.ordinal()])
            {
                case 1:
                    return (var1.nextFloat() * 2.0F - 1.0F) * this.range + this.mean;

                case 2:
                    float var2 = (float)var1.nextGaussian() / 2.5F;

                    if (var2 < -1.0F)
                    {
                        var2 = -1.0F;
                    }
                    else if (var2 > 1.0F)
                    {
                        var2 = 1.0F;
                    }

                    return var2 * this.range + this.mean;

                default:
                    return 0.0F;
            }
        }
    }

    public int getIntValue(Random var1)
    {
        float var2 = this.getValue(var1);
        int var3 = (int)var2;
        var2 -= (float)var3;

        if (var2 > 0.0F && var2 > var1.nextFloat())
        {
            ++var3;
        }
        else if (var2 < 0.0F && -var2 > var1.nextFloat())
        {
            --var3;
        }

        return var3;
    }

    public String toString()
    {
        return String.format("%f +- %f %s", new Object[] {Float.valueOf(this.mean), Float.valueOf(this.range), this.type.name()});
    }

    public void copyFrom(Object var1)
    {
        this.copyFrom((PDist)var1);
    }
}
