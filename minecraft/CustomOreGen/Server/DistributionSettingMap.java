package CustomOreGen.Server;

import CustomOreGen.Config.ConfigParser;
import CustomOreGen.Server.DistributionSettingMap$Copyable;
import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Util.CIStringMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DistributionSettingMap
{
    private final Map _settingMap = new CIStringMap(new LinkedHashMap());

    public DistributionSettingMap(Class var1)
    {
        Field[] var2 = var1.getFields();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            Field var5 = var2[var4];
            DistributionSettingMap$DistributionSetting var6 = (DistributionSettingMap$DistributionSetting)var5.getAnnotation(DistributionSettingMap$DistributionSetting.class);

            if (var6 != null)
            {
                this._settingMap.put(var6.name(), new Object[] {var5, var6});
            }
        }
    }

    public Map getDescriptions()
    {
        CIStringMap var1 = new CIStringMap(new LinkedHashMap());
        Iterator var2 = this._settingMap.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();
            DistributionSettingMap$DistributionSetting var4 = (DistributionSettingMap$DistributionSetting)((Object[])var3.getValue())[1];
            var1.put(var4.name(), var4.info());
        }

        return var1;
    }

    public String getDescription(String var1)
    {
        Object[] var2 = (Object[])this._settingMap.get(var1);
        return var2 == null ? null : ((DistributionSettingMap$DistributionSetting)var2[1]).info();
    }

    public Object get(IOreDistribution var1, String var2)
    {
        if (var1 == null)
        {
            return null;
        }
        else
        {
            Object[] var3 = (Object[])this._settingMap.get(var2);

            if (var3 == null)
            {
                return null;
            }
            else
            {
                Field var4 = (Field)var3[0];

                if (var4 == null)
                {
                    return null;
                }
                else
                {
                    try
                    {
                        return var4.get(var1);
                    }
                    catch (IllegalAccessException var6)
                    {
                        return null;
                    }
                    catch (ClassCastException var7)
                    {
                        return null;
                    }
                }
            }
        }
    }

    public void set(IOreDistribution var1, String var2, Object var3) throws IllegalArgumentException, IllegalAccessException
    {
        if (var1 != null)
        {
            Object[] var4 = (Object[])this._settingMap.get(var2);

            if (var4 == null)
            {
                throw new IllegalArgumentException("Setting \'" + var2 + "\' is not supported by distribution \'" + var1 + "\'");
            }
            else
            {
                Field var5 = (Field)var4[0];
                DistributionSettingMap$DistributionSetting var6 = (DistributionSettingMap$DistributionSetting)var4[1];

                if (var3 != null && var3 instanceof String)
                {
                    var3 = ConfigParser.parseString(var5.getType(), (String)var3);
                }

                if (Modifier.isFinal(var5.getModifiers()))
                {
                    try
                    {
                        Object var7 = var5.get(var1);

                        if (!DistributionSettingMap$Copyable.class.isAssignableFrom(var5.getType()))
                        {
                            throw new IllegalStateException("Setting is final and does not support copying");
                        }

                        if (var7 == null || var3 == null)
                        {
                            throw new IllegalStateException("Setting is final and null");
                        }

                        ((DistributionSettingMap$Copyable)var7).copyFrom(var3);
                    }
                    catch (Exception var8)
                    {
                        throw new IllegalArgumentException("Failed to copy setting \'" + var6.name() + "\' for distribution \'" + var1 + "\'", var8);
                    }
                }
                else
                {
                    var5.set(var1, var3);
                }
            }
        }
    }

    public void inheritAll(IOreDistribution var1, IOreDistribution var2)
    {
        Iterator var3 = this._settingMap.entrySet().iterator();

        while (var3.hasNext())
        {
            Entry var4 = (Entry)var3.next();
            Field var5 = (Field)((Object[])var4.getValue())[0];
            DistributionSettingMap$DistributionSetting var6 = (DistributionSettingMap$DistributionSetting)((Object[])var4.getValue())[1];

            try
            {
                if (var6.inherited())
                {
                    Object var7 = var5.get(var1);

                    if (Modifier.isFinal(var5.getModifiers()))
                    {
                        Object var8 = var5.get(var2);

                        if (!DistributionSettingMap$Copyable.class.isAssignableFrom(var5.getType()))
                        {
                            throw new IllegalStateException("Setting is final and does not support copying");
                        }

                        if (var7 == null || var8 == null)
                        {
                            throw new IllegalStateException("Setting is null");
                        }

                        ((DistributionSettingMap$Copyable)var8).copyFrom(var7);
                    }
                    else
                    {
                        var5.set(var2, var7);
                    }
                }
            }
            catch (Exception var9)
            {
                ;
            }
        }
    }
}
