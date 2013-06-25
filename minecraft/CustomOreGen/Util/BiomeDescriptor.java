package CustomOreGen.Util;

import CustomOreGen.Server.DistributionSettingMap$Copyable;
import CustomOreGen.Util.BiomeDescriptor$Descriptor;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeDescriptor implements DistributionSettingMap$Copyable
{
    protected LinkedList _descriptors = new LinkedList();
    protected Map _matches = new Hashtable();
    protected boolean _compiled = false;

    public BiomeDescriptor()
    {
        this.clear();
    }

    public BiomeDescriptor(String var1)
    {
        this.set(var1);
    }

    public void copyFrom(BiomeDescriptor var1)
    {
        this._descriptors = new LinkedList(var1._descriptors);
        this._matches = new Hashtable(var1._matches);
        this._compiled = var1._compiled;
    }

    public BiomeDescriptor set(String var1)
    {
        this.clear();

        if (var1 != null)
        {
            this._descriptors.add(new BiomeDescriptor$Descriptor(var1, 1.0F));
        }

        return this;
    }

    public BiomeDescriptor add(String var1)
    {
        return this.add(var1, 1.0F);
    }

    public BiomeDescriptor add(String var1, float var2)
    {
        if (var1 != null && var2 != 0.0F)
        {
            this._compiled = false;
            this._descriptors.add(new BiomeDescriptor$Descriptor(var1, var2));
        }

        return this;
    }

    public BiomeDescriptor clear()
    {
        this._compiled = false;
        this._descriptors.clear();
        return this;
    }

    public List getDescriptors()
    {
        return Collections.unmodifiableList(this._descriptors);
    }

    protected void add(BiomeGenBase var1, float var2)
    {
        if (var1 != null && var2 != 0.0F)
        {
            Float var3 = (Float)this._matches.get(var1);

            if (var3 != null)
            {
                var2 += var3.floatValue();
            }

            this._matches.put(var1, Float.valueOf(var2));
        }
    }

    protected float regexMatch(String var1, String var2)
    {
        float var3 = 0.0F;
        Iterator var4 = this._descriptors.iterator();

        while (var4.hasNext())
        {
            BiomeDescriptor$Descriptor var5 = (BiomeDescriptor$Descriptor)var4.next();
            int var6 = var5.matches;
            Matcher var7;

            if (var1 != null)
            {
                var7 = var5.getPattern().matcher(var1);

                if (var7.matches())
                {
                    ++var5.matches;
                    var3 += var5.weight;
                }
            }

            if (var5.matches == var6 && var2 != null)
            {
                var7 = var5.getPattern().matcher(var2);

                if (var7.matches())
                {
                    ++var5.matches;
                    var3 += var5.weight;
                }
            }
        }

        return var3;
    }

    protected void compileMatches()
    {
        if (!this._compiled)
        {
            this._compiled = true;
            this._matches.clear();
            BiomeDescriptor$Descriptor var2;

            for (Iterator var1 = this._descriptors.iterator(); var1.hasNext(); var2.matches = 0)
            {
                var2 = (BiomeDescriptor$Descriptor)var1.next();
            }

            BiomeGenBase[] var7 = BiomeGenBase.biomeList;
            int var8 = var7.length;

            for (int var3 = 0; var3 < var8; ++var3)
            {
                BiomeGenBase var4 = var7[var3];

                if (var4 != null)
                {
                    String var5 = Integer.toString(var4.biomeID);
                    String var6 = var4.biomeName;
                    this.add(var4, this.regexMatch(var5, var6));
                }
            }
        }
    }

    public Map getMatches()
    {
        this.compileMatches();
        return Collections.unmodifiableMap(this._matches);
    }

    public float getWeight(BiomeGenBase var1)
    {
        this.compileMatches();
        Float var2 = (Float)this._matches.get(var1);
        return var2 == null ? 0.0F : var2.floatValue();
    }

    public boolean matchesBiome(BiomeGenBase var1, Random var2)
    {
        float var3 = this.getWeight(var1);

        if (var3 <= 0.0F)
        {
            return false;
        }
        else if (var3 < 1.0F)
        {
            if (var2 == null)
            {
                var2 = new Random();
            }

            return var2.nextFloat() < var3;
        }
        else
        {
            return true;
        }
    }

    public BiomeGenBase getMatchingBiome(Random var1)
    {
        this.compileMatches();
        float var2 = -1.0F;
        Iterator var3 = this._matches.entrySet().iterator();

        while (var3.hasNext())
        {
            Entry var4 = (Entry)var3.next();
            float var5 = ((Float)var4.getValue()).floatValue();
            BiomeGenBase var6 = (BiomeGenBase)var4.getKey();

            if (var5 > 0.0F)
            {
                if (var5 >= 1.0F)
                {
                    return var6;
                }

                if (var2 < 0.0F)
                {
                    if (var1 == null)
                    {
                        var1 = new Random();
                    }

                    var2 = var1.nextFloat();
                }

                var2 -= var5;

                if (var2 < 0.0F)
                {
                    return var6;
                }
            }
        }

        return null;
    }

    public float getTotalMatchWeight()
    {
        this.compileMatches();
        float var1 = 0.0F;
        Iterator var2 = this._matches.values().iterator();

        while (var2.hasNext())
        {
            Float var3 = (Float)var2.next();

            if (var3.floatValue() > 0.0F)
            {
                var1 += var3.floatValue();
            }
        }

        return var1;
    }

    public String toString()
    {
        switch (this._descriptors.size())
        {
            case 0:
                return "[no biomes]";

            case 1:
                return ((BiomeDescriptor$Descriptor)this._descriptors.get(0)).toString();

            default:
                return this._descriptors.toString();
        }
    }

    public String[] toDetailedString()
    {
        this.compileMatches();
        String[] var1 = new String[this._matches.size() + 1];
        var1[0] = this._matches.size() + " biome matches";

        if (this._matches.size() > 0)
        {
            var1[0] = var1[0] + ':';
        }

        int var2 = 1;

        for (Iterator var3 = this._matches.entrySet().iterator(); var3.hasNext(); ++var2)
        {
            Entry var4 = (Entry)var3.next();
            float var5 = ((Float)var4.getValue()).floatValue();
            BiomeGenBase var6 = (BiomeGenBase)var4.getKey();

            if (var6 == null)
            {
                var1[var2] = "[??]";
            }
            else
            {
                var1[var2] = var6.biomeName;
            }

            var1[var2] = var1[var2] + " - " + var5;
        }

        return var1;
    }

    public void copyFrom(Object var1)
    {
        this.copyFrom((BiomeDescriptor)var1);
    }
}
