package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.GeometryStream;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSubstitution extends WorldGenerator implements IOreDistribution
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
    public final BlockDescriptor oreBlock;
    @DistributionSettingMap$DistributionSetting(
            name = "ReplaceableBlock",
            info = "List of replaceable blocks"
    )
    public final BlockDescriptor replaceableBlocks;
    @DistributionSettingMap$DistributionSetting(
            name = "TargetBiome",
            info = "List of valid target biomes"
    )
    public final BiomeDescriptor biomes;
    @DistributionSettingMap$DistributionSetting(
            name = "additionalRange",
            info = "Distance outside of current chunk to scan in every pass, in meters"
    )
    public int additionalRange;
    @DistributionSettingMap$DistributionSetting(
            name = "minHeight",
            info = "Minimum substitution height"
    )
    public int minHeight;
    @DistributionSettingMap$DistributionSetting(
            name = "maxHeight",
            info = "Maximum substitution height"
    )
    public int maxHeight;
    @DistributionSettingMap$DistributionSetting(
            name = "populatedChunks",
            info = "Chunks populated during current game session."
    )
    public int populatedChunks;
    @DistributionSettingMap$DistributionSetting(
            name = "placedBlocks",
            info = "Blocks placed during current game session."
    )
    public long placedBlocks;
    protected boolean _valid;
    protected final boolean _canGenerate;
    protected static final DistributionSettingMap settingMap = new DistributionSettingMap(WorldGenSubstitution.class);

    public WorldGenSubstitution(int var1, boolean var2)
    {
        this.oreBlock = new BlockDescriptor(Integer.toString(Block.stone.blockID));
        this.replaceableBlocks = new BlockDescriptor();
        this.biomes = new BiomeDescriptor(".*");
        this.additionalRange = 0;
        this.minHeight = Integer.MIN_VALUE;
        this.maxHeight = Integer.MAX_VALUE;
        this.populatedChunks = 0;
        this.placedBlocks = 0L;
        this._valid = false;
        this.name = "Substitute_" + var1;
        this.seed = (new Random((long)var1)).nextLong();
        this._canGenerate = var2;
    }

    public void inheritFrom(IOreDistribution var1) throws IllegalArgumentException
    {
        if (var1 != null && var1 instanceof WorldGenSubstitution)
        {
            settingMap.inheritAll((WorldGenSubstitution)var1, this);
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
            this.generate(var1, var4, var2 * 16, 0, var3 * 16);
        }
    }

    public void cull() {}

    public void clear()
    {
        this.populatedChunks = 0;
        this.placedBlocks = 0L;
    }

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

        float var2 = this.replaceableBlocks.getTotalMatchWeight();

        if (var2 <= 0.0F)
        {
            ;
        }

        float var3 = this.biomes.getTotalMatchWeight();

        if (var3 <= 0.0F)
        {
            ;
        }

        if (this.additionalRange < 0)
        {
            this._valid = false;
            throw new IllegalStateException("Invalid additional scan range \'" + this.additionalRange + "\' for " + this);
        }
        else if (this.minHeight > this.maxHeight)
        {
            this._valid = false;
            throw new IllegalStateException("Invalid height range [" + this.minHeight + "," + this.maxHeight + "] for " + this);
        }
        else
        {
            return this._valid && this._canGenerate;
        }
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (this._canGenerate && this._valid && this.oreBlock != null)
        {
            int var6 = var3 / 16;
            int var7 = var5 / 16;
            int var8 = (this.additionalRange + 15) / 16;
            int var9 = (this.additionalRange + 7) / 8;
            int var10 = Math.max(0, this.minHeight);
            int var11 = Math.min(var1.getHeight() - 1, this.maxHeight);

            for (int var12 = -var8; var12 <= var8; ++var12)
            {
                for (int var13 = -var8; var13 <= var8; ++var13)
                {
                    int var14 = var6 + var12;
                    int var15 = var7 + var13;

                    if (var1.blockExists(var14 * 16, 0, var15 * 16))
                    {
                        Chunk var16 = var1.getChunkFromChunkCoords(var14, var15);
                        int var17 = var12 < 0 && -var12 * 2 > var9 ? 8 : 0;
                        int var18 = var13 < 0 && -var13 * 2 > var9 ? 8 : 0;
                        int var19 = var12 > 0 && var12 * 2 > var9 ? 8 : 16;
                        int var20 = var13 > 0 && var13 * 2 > var9 ? 8 : 16;

                        for (int var21 = var17; var21 < var19; ++var21)
                        {
                            for (int var22 = var18; var22 < var20; ++var22)
                            {
                                BiomeGenBase var23 = var16.getBiomeGenForWorldCoords(var21, var22, var1.provider.worldChunkMgr);

                                if (var23 == null || this.biomes.getWeight(var23) > 0.5F)
                                {
                                    for (int var24 = var10; var24 <= var11; ++var24)
                                    {
                                        int var25 = var16.getBlockID(var21, var24, var22);
                                        int var26 = this.replaceableBlocks.matchesBlock_fast(var25);

                                        if (var26 != 0 && (var26 != -1 || this.replaceableBlocks.matchesBlock(var25, var16.getBlockMetadata(var21, var24, var22), var2)))
                                        {
                                            int var27 = this.oreBlock.getMatchingBlock(var2);

                                            if (var27 != -1 && var16.setBlockIDWithMetadata(var21, var24, var22, var27 >>> 16, var27 & 65535))
                                            {
                                                ++this.placedBlocks;
                                                var1.markBlockForUpdate(var14 * 16 + var21, var24, var15 * 16 + var22);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ++this.populatedChunks;
            return true;
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
