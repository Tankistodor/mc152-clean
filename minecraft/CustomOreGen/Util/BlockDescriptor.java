package CustomOreGen.Util;

import CustomOreGen.Server.DistributionSettingMap$Copyable;
import CustomOreGen.Util.BlockDescriptor$Descriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.block.Block;

public class BlockDescriptor implements DistributionSettingMap$Copyable
{
    protected LinkedList _descriptors = new LinkedList();
    protected Map _matches = new Hashtable();
    protected boolean _compiled = false;
    protected float[] _fastMatch = new float[256];

    public BlockDescriptor()
    {
        this.clear();
    }

    public BlockDescriptor(String var1)
    {
        this.set(var1);
    }

    public void copyFrom(BlockDescriptor var1)
    {
        this._descriptors = new LinkedList(var1._descriptors);
        this._matches = new Hashtable(var1._matches);
        this._compiled = var1._compiled;
        this._fastMatch = (float[])var1._fastMatch.clone();
    }

    public BlockDescriptor set(String var1)
    {
        this.clear();

        if (var1 != null)
        {
            this._descriptors.add(new BlockDescriptor$Descriptor(var1, 1.0F));
        }

        return this;
    }

    public BlockDescriptor add(String var1)
    {
        return this.add(var1, 1.0F);
    }

    public BlockDescriptor add(String var1, float var2)
    {
        if (var1 != null && var2 != 0.0F)
        {
            this._compiled = false;
            this._descriptors.add(new BlockDescriptor$Descriptor(var1, var2));
        }

        return this;
    }

    public BlockDescriptor clear()
    {
        this._compiled = false;
        this._descriptors.clear();
        return this;
    }

    public List getDescriptors()
    {
        return Collections.unmodifiableList(this._descriptors);
    }

    protected void add(int var1, int var2, float var3)
    {
        if (var3 != 0.0F)
        {
            Integer var4 = Integer.valueOf(var1 << 16 | var2 & 65535);
            Float var5 = (Float)this._matches.get(var4);

            if (var5 != null)
            {
                var3 += var5.floatValue();
            }

            this._matches.put(var4, Float.valueOf(var3));

            if (var1 >= 0 && var1 < this._fastMatch.length)
            {
                if (var2 == -1 && !Float.isNaN(this._fastMatch[var1]))
                {
                    this._fastMatch[var1] += var3;
                }
                else
                {
                    this._fastMatch[var1] = Float.NaN;
                }
            }
        }
    }

    protected float[] regexMatch(String var1, String var2)
    {
        float[] var3 = new float[17];
        Iterator var4 = this._descriptors.iterator();

        while (var4.hasNext())
        {
            BlockDescriptor$Descriptor var5 = (BlockDescriptor$Descriptor)var4.next();

            if ((var1 == null || !var5.getPattern().matcher(var1).matches()) && (var2 == null || !var5.getPattern().matcher(var2).matches()))
            {
                for (int var6 = 0; var6 < 16; ++var6)
                {
                    if (var1 != null && var5.getPattern().matcher(var1 + ":" + var6).matches() || var2 != null && var5.getPattern().matcher(var2 + ":" + var6).matches())
                    {
                        ++var5.matches;
                        var3[var6] += var5.weight;
                    }
                }
            }
            else
            {
                ++var5.matches;
                var3[16] += var5.weight;
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
            Arrays.fill(this._fastMatch, 0.0F);
            BlockDescriptor$Descriptor var2;

            for (Iterator var1 = this._descriptors.iterator(); var1.hasNext(); var2.matches = 0)
            {
                var2 = (BlockDescriptor$Descriptor)var1.next();
            }

            float[] var10 = this.regexMatch("0", "air");
            this.add(0, -1, var10[16]);
            Block[] var11 = Block.blocksList;
            int var3 = var11.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                Block var5 = var11[var4];

                if (var5 != null && var5.blockID != 0)
                {
                    String var6 = Integer.toString(var5.blockID);
                    String var7 = var5.getUnlocalizedName() == null ? null : var5.getUnlocalizedName().replace("tile.", "");
                    float[] var8 = this.regexMatch(var6, var7);
                    this.add(var5.blockID, -1, var8[16]);

                    for (int var9 = 0; var9 < 16; ++var9)
                    {
                        this.add(var5.blockID, var9, var8[var9]);
                    }
                }
            }
        }
    }

    public Map getMatches()
    {
        this.compileMatches();
        return Collections.unmodifiableMap(this._matches);
    }

    public float getWeight_fast(int var1)
    {
        this.compileMatches();
        return var1 >= 0 && var1 < this._fastMatch.length ? this._fastMatch[var1] : Float.NaN;
    }

    public float getWeight(int var1, int var2)
    {
        this.compileMatches();
        float var3 = 0.0F;
        Float var4 = (Float)this._matches.get(Integer.valueOf(var1 << 16 | 65535));

        if (var4 != null)
        {
            var3 = var4.floatValue();
        }

        if (var2 >= 0)
        {
            Float var5 = (Float)this._matches.get(Integer.valueOf(var1 << 16 | var2 & 65535));

            if (var5 != null)
            {
                var3 += var5.floatValue();
            }
        }

        return var3;
    }

    public int matchesBlock_fast(int var1)
    {
        float var2 = this.getWeight_fast(var1);
        return Float.isNaN(var2) ? -1 : (var2 <= 0.0F ? 0 : (var2 < 1.0F ? -1 : 1));
    }

    public boolean matchesBlock(int var1, int var2, Random var3)
    {
        float var4 = this.getWeight(var1, var2);

        if (var4 <= 0.0F)
        {
            return false;
        }
        else if (var4 < 1.0F)
        {
            if (var3 == null)
            {
                var3 = new Random();
            }

            return var3.nextFloat() < var4;
        }
        else
        {
            return true;
        }
    }

    public int getMatchingBlock(Random var1)
    {
        this.compileMatches();
        float var2 = -1.0F;
        Iterator var3 = this._matches.entrySet().iterator();

        while (var3.hasNext())
        {
            Entry var4 = (Entry)var3.next();
            float var5 = ((Float)var4.getValue()).floatValue();
            int var6 = ((Integer)var4.getKey()).intValue() >>> 16;
            int var7 = ((Integer)var4.getKey()).intValue() & 65535;

            if (var7 >= 32768)
            {
                var7 = 0;
            }

            if (var5 > 0.0F)
            {
                if (var5 >= 1.0F)
                {
                    return var6 << 16 | var7;
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
                    return var6 << 16 | var7;
                }
            }
        }

        return -1;
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
                return "[no blocks]";

            case 1:
                return ((BlockDescriptor$Descriptor)this._descriptors.get(0)).toString();

            default:
                return this._descriptors.toString();
        }
    }

    public String[] toDetailedString()
    {
        this.compileMatches();
        String[] var1 = new String[this._matches.size() + 1];
        var1[0] = this._matches.size() + " block matches";

        if (this._matches.size() > 0)
        {
            var1[0] = var1[0] + ':';
        }

        int var2 = 1;

        for (Iterator var3 = this._matches.entrySet().iterator(); var3.hasNext(); ++var2)
        {
            Entry var4 = (Entry)var3.next();
            float var5 = ((Float)var4.getValue()).floatValue();
            int var6 = ((Integer)var4.getKey()).intValue() >>> 16;
            int var7 = ((Integer)var4.getKey()).intValue() & 65535;
            Block var8 = Block.blocksList[var6];

            if (var8 == null)
            {
                var1[var2] = var6 == 0 ? "Air" : "[??]";
            }
            else
            {
                //var1[var2] = var8.translateBlockName();
            	var1[var2] = var8.getLocalizedName();
            }

            var1[var2] = var1[var2] + " (" + var6;

            if (var7 < 32768)
            {
                var1[var2] = var1[var2] + ":" + var7;
            }

            var1[var2] = var1[var2] + ") - " + var5;
        }

        return var1;
    }

    public void copyFrom(Object var1)
    {
        this.copyFrom((BlockDescriptor)var1);
    }
}
