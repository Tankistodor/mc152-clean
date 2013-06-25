package CustomOreGen.Server;

import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Util.GeometryStream;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenOreDistribution$StructureGroup extends StructureStart
{
    public final int structureCount;
    public int completeComponents;
    public long completeComponentBlocks;
    public final int chunkX;
    public final int chunkZ;
    private MapGenOreDistribution$StructureGroup newerGroup;
    private MapGenOreDistribution$StructureGroup olderGroup;

    final MapGenOreDistribution this$0;

    public MapGenOreDistribution$StructureGroup(MapGenOreDistribution var1, int var2, int var3, int var4)
    {
        this.this$0 = var1;
        this.completeComponents = 0;
        this.completeComponentBlocks = 0L;
        this.chunkX = var2;
        this.chunkZ = var3;
        int var5 = 0;

        for (int var6 = 0; var6 < var4; ++var6)
        {
            Random var7 = new Random(MapGenOreDistribution.access$200(var1).nextLong());

            if (var1.generateStructure(this, var7) != null)
            {
                ++var5;
            }
        }

        this.structureCount = var5;
        this.updateBoundingBox();

        if (ServerState.getWorldConfig(MapGenOreDistribution.access$300(var1)).debuggingMode && (var1.wfHasBB || var1.wfHasWireframe))
        {
            this.buildWireframes();
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isSizeableStructure()
    {
        return true;
    }

    public void addComponent(MapGenOreDistribution$Component var1, MapGenOreDistribution$Component var2)
    {
        this.components.add(var1);
        var1.setParent(var2);

        if (var2 != null)
        {
            var2.setChild(var1);
        }
    }

    public boolean canPlaceComponentAt(int var1, float var2, float var3, float var4, Random var5)
    {
        int var6 = MathHelper.floor_float(var2);
        int var7 = MathHelper.floor_float(var3);
        int var8 = MathHelper.floor_float(var4);

        if (var1 == 0)
        {
            BiomeGenBase var9 = MapGenOreDistribution.access$400(this.this$0).getBiomeGenForCoords(var6, var8);

            if (var9 != null && !this.this$0.biomes.matchesBiome(var9, var5))
            {
                return false;
            }
        }

        if (var1 == 0)
        {
            float var13 = this.this$0.parentRangeLimit.getValue(var5);

            if (this.this$0.parent != null)
            {
                if (var13 < 0.0F)
                {
                    return false;
                }

                ChunkPosition var10 = this.this$0.parent.getNearestStructure(MapGenOreDistribution.access$500(this.this$0), var6, 0, var8);

                if (var10 == null)
                {
                    return false;
                }

                float var11 = (float)(var10.x - var6);
                float var12 = (float)(var10.z - var8);

                if (var11 * var11 + var12 * var12 > var13 * var13)
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
     */
    public void generateStructure(World var1, Random var2, StructureBoundingBox var3)
    {
        int var4 = this.completeComponents;
        super.generateStructure(var1, var2, var3);

        if (var4 != this.completeComponents && this.completeComponents == this.components.size())
        {
            this.this$0.completedStructures += this.structureCount;
            this.this$0.completedStructureBlocks += this.completeComponentBlocks;
        }
    }

    public void buildWireframes()
    {
        MapGenOreDistribution$Component var2;
        GeometryStream var8;

        for (Iterator var1 = this.getComponents().iterator(); var1.hasNext(); var2.buildWireframe(var8))
        {
            var2 = (MapGenOreDistribution$Component)var1.next();
            StructureBoundingBox var3 = var2.getBoundingBox();
            int var4 = var3.getCenterX() / 16;
            int var5 = var3.getCenterZ() / 16;
            long var6 = (long)var4 << 32 | (long)var5 & 4294967295L;
            var8 = (GeometryStream)this.this$0.debuggingGeometryMap.get(Long.valueOf(var6));

            if (var8 == null)
            {
                var8 = new GeometryStream();
                this.this$0.debuggingGeometryMap.put(Long.valueOf(var6), var8);
            }
        }
    }

    static MapGenOreDistribution$StructureGroup access$000(MapGenOreDistribution$StructureGroup var0)
    {
        return var0.olderGroup;
    }

    static MapGenOreDistribution$StructureGroup access$100(MapGenOreDistribution$StructureGroup var0)
    {
        return var0.newerGroup;
    }

    static MapGenOreDistribution$StructureGroup access$002(MapGenOreDistribution$StructureGroup var0, MapGenOreDistribution$StructureGroup var1)
    {
        return var0.olderGroup = var1;
    }

    static MapGenOreDistribution$StructureGroup access$102(MapGenOreDistribution$StructureGroup var0, MapGenOreDistribution$StructureGroup var1)
    {
        return var0.newerGroup = var1;
    }
}
