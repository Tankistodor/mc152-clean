package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.GeometryStream;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public abstract class MapGenOreDistribution extends MapGenStructure implements IOreDistribution
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
            name = "DistributionFrequency",
            info = "Number of distribution structures per 16x16 chunk"
    )
    public final PDist frequency;
    @DistributionSettingMap$DistributionSetting(
            name = "Parent",
            info = "The parent distribution, or null if no parent"
    )
    public MapGenOreDistribution parent;
    @DistributionSettingMap$DistributionSetting(
            name = "ParentRangeLimit",
            info = "Max horizontal distance to a parent distribution, in meters"
    )
    public final PDist parentRangeLimit;
    @DistributionSettingMap$DistributionSetting(
            name = "drawBoundBox",
            info = "Whether bounding boxes are drawn for components"
    )
    public boolean wfHasBB;
    @DistributionSettingMap$DistributionSetting(
            name = "boundBoxColor",
            info = "Color of bounding boxes for components"
    )
    public long wfBBColor;
    @DistributionSettingMap$DistributionSetting(
            name = "drawWireframe",
            info = "Whether wireframes are drawn for components"
    )
    public boolean wfHasWireframe;
    @DistributionSettingMap$DistributionSetting(
            name = "wireframeColor",
            info = "Color of wireframes for components"
    )
    public long wfWireframeColor;
    @DistributionSettingMap$DistributionSetting(
            name = "completedStructures",
            info = "Structures completed during current game session."
    )
    public int completedStructures;
    @DistributionSettingMap$DistributionSetting(
            name = "completedStructureBlocks",
            info = "Blocks placed in structures completed during current game session."
    )
    public long completedStructureBlocks;
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
    protected Map debuggingGeometryMap;
    protected boolean _valid;
    protected final boolean _canGenerate;
    private MapGenOreDistribution$StructureGroup newestGroup;
    private MapGenOreDistribution$StructureGroup oldestGroup;
    protected final DistributionSettingMap _settingMap;

    public MapGenOreDistribution(DistributionSettingMap var1, int var2, boolean var3)
    {
        this.replaceableBlocks = new BlockDescriptor(Integer.toString(Block.stone.blockID));
        this.biomes = new BiomeDescriptor(".*");
        this.frequency = new PDist(0.025F, 0.0F);
        this.parent = null;
        this.parentRangeLimit = new PDist(32.0F, 32.0F, PDist$Type.normal);
        this.wfHasBB = false;
        this.wfBBColor = -2147483648L;
        this.wfHasWireframe = false;
        this.wfWireframeColor = -15294967L;
        this.completedStructures = 0;
        this.completedStructureBlocks = 0L;
        this.populatedChunks = 0;
        this.placedBlocks = 0L;
        this.debuggingGeometryMap = new HashMap();
        this._valid = false;
        this.newestGroup = null;
        this.oldestGroup = null;
        this.name = "Distribution_" + var2;
        this.seed = (new Random((long)var2)).nextLong();
        this._canGenerate = var3;
        this._settingMap = var1;
    }

    public void inheritFrom(IOreDistribution var1) throws IllegalArgumentException
    {
        if (var1 != null && this.getClass().isInstance(var1))
        {
            this._settingMap.inheritAll(var1, this);
            this._valid = false;
        }
        else
        {
            throw new IllegalArgumentException("Invalid source distribution \'" + var1 + "\'");
        }
    }

    public Map getDistributionSettings()
    {
        return this._settingMap.getDescriptions();
    }

    public Object getDistributionSetting(String var1)
    {
        return this._settingMap.get(this, var1);
    }

    public void setDistributionSetting(String var1, Object var2) throws IllegalArgumentException, IllegalAccessException
    {
        this._settingMap.set(this, var1, var2);
    }

    public void generate(World var1, int var2, int var3)
    {
        if (this._canGenerate && this._valid)
        {
            if (var1 != this.worldObj)
            {
                this.clear();
            }

            this.generate(var1.getChunkProvider(), var1, var2, var3, (byte[])null);
        }
    }

    public void populate(World var1, int var2, int var3)
    {
        if (this._canGenerate && this._valid)
        {
            Random var4 = new Random(var1.getSeed());
            long var5 = var4.nextLong() >> 3;
            long var7 = var4.nextLong() >> 3;
            var4.setSeed(var5 * (long)var2 + var7 * (long)var3 ^ var1.getSeed() ^ this.seed);
            this.generateStructuresInChunk(var1, var4, var2, var3);
        }
    }

    public void cull()
    {
        if (this._canGenerate)
        {
            int var1 = (int)(6.0F * Math.min(1.0F, this.frequency.getMax()) * (float)(2 * this.range + 1) * (float)(2 * this.range + 1));

            if (this.structureMap.size() > var1 * 3)
            {
                MapGenOreDistribution$StructureGroup var2;

                for (var2 = this.newestGroup; var2 != null && var1 > 0; --var1)
                {
                    var2 = MapGenOreDistribution$StructureGroup.access$000(var2);
                }

                if (var2 != null)
                {
                    if (MapGenOreDistribution$StructureGroup.access$100(var2) == null)
                    {
                        this.newestGroup = null;
                    }
                    else
                    {
                        MapGenOreDistribution$StructureGroup.access$002(MapGenOreDistribution$StructureGroup.access$100(var2), (MapGenOreDistribution$StructureGroup)null);
                    }

                    this.oldestGroup = MapGenOreDistribution$StructureGroup.access$100(var2);
                    MapGenOreDistribution$StructureGroup.access$102(var2, (MapGenOreDistribution$StructureGroup)null);

                    while (var2 != null)
                    {
                        Long var3 = Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2.chunkX, var2.chunkZ));
                        this.structureMap.remove(var3);
                        var2 = MapGenOreDistribution$StructureGroup.access$000(var2);
                    }
                }
            }
        }
    }

    public void clear()
    {
        if (this._canGenerate)
        {
            this.structureMap.clear();
            this.newestGroup = this.oldestGroup = null;
            this.debuggingGeometryMap.clear();
            this.completedStructures = this.populatedChunks = 0;
            this.completedStructureBlocks = this.placedBlocks = 0L;
        }
    }

    public GeometryStream getDebuggingGeometry(World var1, int var2, int var3)
    {
        if (this._canGenerate && this._valid)
        {
            if (var1 != this.worldObj)
            {
                return null;
            }
            else
            {
                long var4 = (long)var2 << 32 | (long)var3 & 4294967295L;
                return (GeometryStream)this.debuggingGeometryMap.get(Long.valueOf(var4));
            }
        }
        else
        {
            return null;
        }
    }

    public boolean validate() throws IllegalStateException
    {
        this.clear();
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

        return this._valid && this._canGenerate;
    }

    protected MapGenOreDistribution$StructureGroup getCachedStructureGroup(int var1, int var2)
    {
        Long var3 = Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
        MapGenOreDistribution$StructureGroup var4 = (MapGenOreDistribution$StructureGroup)this.structureMap.get(var3);

        if (var4 != null)
        {
            MapGenOreDistribution$StructureGroup var5 = MapGenOreDistribution$StructureGroup.access$000(var4);
            MapGenOreDistribution$StructureGroup var6 = MapGenOreDistribution$StructureGroup.access$100(var4);

            if (var5 == null)
            {
                this.oldestGroup = var6;
            }
            else
            {
                MapGenOreDistribution$StructureGroup.access$102(var5, var6);
            }

            if (var6 == null)
            {
                this.newestGroup = var5;
            }
            else
            {
                MapGenOreDistribution$StructureGroup.access$002(var6, var5);
            }

            MapGenOreDistribution$StructureGroup.access$102(var4, (MapGenOreDistribution$StructureGroup)null);
            MapGenOreDistribution$StructureGroup.access$002(var4, this.newestGroup);

            if (this.newestGroup == null)
            {
                this.oldestGroup = var4;
            }
            else
            {
                MapGenOreDistribution$StructureGroup.access$102(this.newestGroup, var4);
            }

            this.newestGroup = var4;
        }

        return var4;
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6)
    {
        if (this.parent != null)
        {
            int var7 = this.parent.range;
            this.parent.range = ((int)this.parentRangeLimit.getMax() + 15) / 16;
            this.parent.generate(var1, var2, var3);
            this.parent.range = var7;
        }

        this.rand.setSeed((long)this.rand.nextInt() ^ this.seed);
        this.rand.nextInt();
        MapGenOreDistribution$StructureGroup var9 = this.getCachedStructureGroup(var2, var3);

        if (var9 == null)
        {
            this.rand.nextInt();

            if (this.canSpawnStructureAtCoords(var2, var3))
            {
                var9 = (MapGenOreDistribution$StructureGroup)this.getStructureStart(var2, var3);
                Long var8 = Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2, var3));
                this.structureMap.put(var8, var9);
            }
        }
    }

    protected boolean canSpawnStructureAtCoords(int var1, int var2)
    {
        return this._canGenerate && this._valid ? (this.frequency.getMax() >= 1.0F ? true : this.frequency.getIntValue(this.rand) == 1) : false;
    }

    protected StructureStart getStructureStart(int var1, int var2)
    {
        int var3 = this.frequency.getMax() >= 1.0F ? this.frequency.getIntValue(this.rand) : 1;
        MapGenOreDistribution$StructureGroup var4 = new MapGenOreDistribution$StructureGroup(this, var1, var2, var3);
        MapGenOreDistribution$StructureGroup.access$102(var4, (MapGenOreDistribution$StructureGroup)null);
        MapGenOreDistribution$StructureGroup.access$002(var4, this.newestGroup);

        if (this.newestGroup == null)
        {
            this.oldestGroup = var4;
        }
        else
        {
            MapGenOreDistribution$StructureGroup.access$102(this.newestGroup, var4);
        }

        this.newestGroup = var4;
        return var4;
    }

    public abstract MapGenOreDistribution$Component generateStructure(MapGenOreDistribution$StructureGroup var1, Random var2);

    /**
     * Generates structures in specified chunk next to existing structures. Does *not* generate StructureStarts.
     */
    public boolean generateStructuresInChunk(World var1, Random var2, int var3, int var4)
    {
        if (this._canGenerate && this._valid)
        {
            int var5 = var3 << 4;
            int var6 = var4 << 4;
            StructureBoundingBox var7 = new StructureBoundingBox(var5, 0, var6, var5 + 15, var1.getHeight(), var6 + 15);
            boolean var8 = false;

            for (int var9 = var3 - this.range; var9 <= var3 + this.range; ++var9)
            {
                for (int var10 = var4 - this.range; var10 <= var4 + this.range; ++var10)
                {
                    MapGenOreDistribution$StructureGroup var11 = this.getCachedStructureGroup(var9, var10);

                    if (var11 != null && var11.isSizeableStructure() && var11.getBoundingBox().intersectsWith(var7))
                    {
                        var11.generateStructure(var1, var2, var7);
                        var8 = true;
                    }
                }
            }

            ++this.populatedChunks;
            return var8;
        }
        else
        {
            return false;
        }
    }

    public ChunkPosition getNearestStructure(World var1, int var2, int var3, int var4)
    {
        if (this._canGenerate && this._valid)
        {
            ChunkPosition var5 = null;
            int var6 = Integer.MAX_VALUE;
            StructureBoundingBox var7 = new StructureBoundingBox(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            Iterator var8 = this.structureMap.values().iterator();

            while (var8.hasNext())
            {
                MapGenOreDistribution$StructureGroup var9 = (MapGenOreDistribution$StructureGroup)var8.next();

                if (var9.getBoundingBox().intersectsWith(var7))
                {
                    Iterator var10 = var9.getComponents().iterator();

                    while (var10.hasNext())
                    {
                        MapGenOreDistribution$Component var11 = (MapGenOreDistribution$Component)var10.next();

                        if (var11.getComponentType() == 0)
                        {
                            ChunkPosition var12 = var11.getCenter();
                            int var13 = (var12.x - var2) * (var12.x - var2) + (var12.z - var4) * (var12.z - var4);

                            if (var13 < var6)
                            {
                                var5 = var12;
                                var6 = var13;
                                int var14 = (int)Math.sqrt((double)var13) + 1;
                                var7.minX = var2 - var14;
                                var7.minZ = var4 - var14;
                                var7.maxX = var2 + var14;
                                var7.maxZ = var4 + var14;
                            }
                        }
                    }
                }
            }

            return var5;
        }
        else
        {
            return null;
        }
    }

    public String toString()
    {
        return this.name;
    }

    static Random access$200(MapGenOreDistribution var0)
    {
        return var0.rand;
    }

    static World access$300(MapGenOreDistribution var0)
    {
        return var0.worldObj;
    }

    static World access$400(MapGenOreDistribution var0)
    {
        return var0.worldObj;
    }

    static World access$500(MapGenOreDistribution var0)
    {
        return var0.worldObj;
    }
}
