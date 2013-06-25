package CustomOreGen.Config;

import CustomOreGen.Server.IOreDistribution;
import CustomOreGen.Server.IOreDistribution$IDistributionFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConfigParser$StdDistFactory implements IOreDistribution$IDistributionFactory
{
    protected Constructor _ctor;
    protected boolean _canGen;

    public ConfigParser$StdDistFactory(Class var1, boolean var2)
    {
        try
        {
            this._ctor = var1.getConstructor(new Class[] {Integer.TYPE, Boolean.TYPE});
        }
        catch (NoSuchMethodException var4)
        {
            throw new IllegalArgumentException(var4);
        }

        this._canGen = var2;
    }

    public IOreDistribution createDistribution(int var1)
    {
        try
        {
            return (IOreDistribution)this._ctor.newInstance(new Object[] {Integer.valueOf(var1), Boolean.valueOf(this._canGen)});
        }
        catch (InvocationTargetException var3)
        {
            throw new IllegalArgumentException(var3);
        }
        catch (IllegalAccessException var4)
        {
            throw new IllegalArgumentException(var4);
        }
        catch (InstantiationException var5)
        {
            throw new IllegalArgumentException(var5);
        }
    }
}
