package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Server.WorldGenMinableCustom$1;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.GeometryStream;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

@Deprecated
public class WorldGenMinableCustom extends WorldGenerator implements IOreDistribution
{
    @DistributionSettingMap$DistributionSetting(
            name = "Name",
            inherited = false,
            info = "Descriptive distribution name."
    )
    public String name;
    @DistributionSettingMap$DistributionSetting(
            name = "Seed",
            inherited = false,
            info = "Distribution random number seed."
    )
    public long seed;
    @DistributionSettingMap$DistributionSetting(
            name = "OreBlock",
            info = "Ore block(s) - total weight must not be more than 100%"
    )
    public final BlockDescriptor oreBlock = new BlockDescriptor();
    @DistributionSettingMap$DistributionSetting(
            name = "Size",
            info = "Roughly the number of blocks in every deposit.  No range."
    )
    public final PDist numberOfBlocks = new PDist(8.0F, 0.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "Frequency",
            info = "Number of deposits per 16x16 chunk.  No range."
    )
    public final PDist freq = new PDist(20.0F, 0.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "Height",
            info = "Vertical height of the deposits, relative to 256m world.  Normal distributions are approximated."
    )
    public final PDist height;
    protected boolean _valid;
    protected final boolean _canGenerate;
    protected static final DistributionSettingMap settingMap = new DistributionSettingMap(WorldGenMinableCustom.class);

    public WorldGenMinableCustom(int var1, boolean var2)
    {
        this.height = new PDist(64.0F, 64.0F, PDist$Type.uniform);
        this._valid = false;
        this.name = "StandardGen_" + var1;
        this.seed = (new Random((long)var1)).nextLong();
        this._canGenerate = var2;
    }

    public void inheritFrom(IOreDistribution var1) throws IllegalArgumentException
    {
        if (var1 != null && var1 instanceof WorldGenMinableCustom)
        {
            settingMap.inheritAll((WorldGenMinableCustom)var1, this);
            this._valid = false;
        }
        else
        {
            throw new IllegalArgumentException("Invalid source distribution \'" + var1 + "\'");
        }
    }

    public Map getDistributionSettings()
    {
        return settingMap.getDescriptions();
    }

    public Object getDistributionSetting(String var1)
    {
        return settingMap.get(this, var1);
    }

    public void setDistributionSetting(String var1, Object var2) throws IllegalArgumentException, IllegalAccessException
    {
        settingMap.set(this, var1, var2);
    }

    public void generate(World var1, int var2, int var3) {}

    public void populate(World var1, int var2, int var3)
    {
        if (this._canGenerate && this._valid && this.oreBlock != null)
        {
            Random var4 = new Random(var1.getSeed());
            long var5 = var4.nextLong() >> 3;
            long var7 = var4.nextLong() >> 3;
            var4.setSeed(var5 * (long)var2 + var7 * (long)var3 ^ var1.getSeed() ^ this.seed);
            int var9 = (int)this.height.mean;
            int var10 = (int)this.height.range;
            int var11 = (int)(this.height.mean - this.height.range);
            int var12 = (int)(this.height.mean + this.height.range);
            int var13 = this.freq.getIntValue(var4);

            for (int var14 = 0; var14 < var13; ++var14)
            {
                int var15 = var2 * 16 + var4.nextInt(16);
                boolean var16 = false;
                int var18;

                switch (WorldGenMinableCustom$1.$SwitchMap$net$minecraft$src$CustomOreGen$Util$PDist$Type[this.height.type.ordinal()])
                {
                    case 1:
                        var18 = var4.nextInt(var10) + var4.nextInt(var10) + (var9 - var10);
                        break;

                    default:
                        var18 = var4.nextInt(var12 - var11) + var11;
                }

                int var17 = var3 * 16 + var4.nextInt(16);
                this.generate(var1, var4, var15, var18, var17);
            }
        }
    }

    public void cull() {}

    public void clear() {}

    public GeometryStream getDebuggingGeometry(World var1, int var2, int var3)
    {
        return null;
    }

    public boolean validate() throws IllegalStateException
    {
        this._valid = true;
        float var1 = this.oreBlock.getTotalMatchWeight();

        if (var1 <= 0.0F)
        {
            if (this._canGenerate)
            {
                this._valid = false;
                throw new IllegalStateException("Ore block descriptor for " + this + " is empty or does not match any registered blocks.");
            }
        }
        else if (var1 > 1.0F)
        {
            this._valid = false;
            throw new IllegalStateException("Ore block descriptor for " + this + " is overspecified with a total match weight of " + var1 * 100.0F + "%.");
        }

        return this._valid && this._canGenerate;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (this._canGenerate && this._valid && this.oreBlock != null)
        {
            int var6 = this.oreBlock.getMatchingBlock(var2);

            if (var6 == -1)
            {
                return false;
            }
            else
            {
                int var7 = var6 >>> 16;
                int var8 = var6 & 65535;
                int var9 = this.numberOfBlocks.getIntValue(var2);
                float var10 = var2.nextFloat() * (float)Math.PI;
                double var11 = (double)((float)(var3 + 8) + MathHelper.sin(var10) * (float)var9 / 8.0F);
                double var13 = (double)((float)(var3 + 8) - MathHelper.sin(var10) * (float)var9 / 8.0F);
                double var15 = (double)((float)(var5 + 8) + MathHelper.cos(var10) * (float)var9 / 8.0F);
                double var17 = (double)((float)(var5 + 8) - MathHelper.cos(var10) * (float)var9 / 8.0F);
                double var19 = (double)(var4 + var2.nextInt(3) - 2);
                double var21 = (double)(var4 + var2.nextInt(3) - 2);

                for (int var23 = 0; var23 <= var9; ++var23)
                {
                    double var24 = var11 + (var13 - var11) * (double)var23 / (double)var9;
                    double var26 = var19 + (var21 - var19) * (double)var23 / (double)var9;
                    double var28 = var15 + (var17 - var15) * (double)var23 / (double)var9;
                    double var30 = var2.nextDouble() * (double)var9 / 16.0D;
                    double var32 = (double)(MathHelper.sin((float)var23 * (float)Math.PI / (float)var9) + 1.0F) * var30 + 1.0D;
                    double var34 = (double)(MathHelper.sin((float)var23 * (float)Math.PI / (float)var9) + 1.0F) * var30 + 1.0D;
                    int var36 = MathHelper.floor_double(var24 - var32 / 2.0D);
                    int var37 = MathHelper.floor_double(var26 - var34 / 2.0D);
                    int var38 = MathHelper.floor_double(var28 - var32 / 2.0D);
                    int var39 = MathHelper.floor_double(var24 + var32 / 2.0D);
                    int var40 = MathHelper.floor_double(var26 + var34 / 2.0D);
                    int var41 = MathHelper.floor_double(var28 + var32 / 2.0D);

                    for (int var42 = var36; var42 <= var39; ++var42)
                    {
                        double var43 = ((double)var42 + 0.5D - var24) / (var32 / 2.0D);

                        if (var43 * var43 < 1.0D)
                        {
                            for (int var45 = var37; var45 <= var40; ++var45)
                            {
                                double var46 = ((double)var45 + 0.5D - var26) / (var34 / 2.0D);

                                if (var43 * var43 + var46 * var46 < 1.0D)
                                {
                                    for (int var48 = var38; var48 <= var41; ++var48)
                                    {
                                        double var49 = ((double)var48 + 0.5D - var28) / (var32 / 2.0D);

                                        if (var43 * var43 + var46 * var46 + var49 * var49 < 1.0D && var1.getBlockId(var42, var45, var48) == Block.stone.blockID)
                                        {
                                        	var1.setBlockMetadataWithNotify(var42, var45, var48, var7, var8);
                                            //var1.setBlockAndMetadata(var42, var45, var48, var7, var8);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return this.name;
    }
}
