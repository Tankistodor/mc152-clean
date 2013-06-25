package CustomOreGen.Server;

import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.IGeometryBuilder;
import CustomOreGen.Util.Transform;
import CustomOreGen.Util.WireframeShapes;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class MapGenOreDistribution$Component extends StructureComponent
{
    public final MapGenOreDistribution$StructureGroup structureGroup;
    public long populatedBlocks;
    public long placedBlocks;

    final MapGenOreDistribution this$0;

    public MapGenOreDistribution$Component(MapGenOreDistribution var1, MapGenOreDistribution$StructureGroup var2)
    {
        super(0);
        this.this$0 = var1;
        this.populatedBlocks = 0L;
        this.placedBlocks = 0L;
        this.structureGroup = var2;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        int var4 = Math.min(var3.maxX, this.boundingBox.maxX) - Math.max(var3.minX, this.boundingBox.minX) + 1;
        int var5 = Math.min(var3.maxY, this.boundingBox.maxY) - Math.max(var3.minY, this.boundingBox.minY) + 1;
        int var6 = Math.min(var3.maxZ, this.boundingBox.maxZ) - Math.max(var3.minZ, this.boundingBox.minZ) + 1;

        if (var4 > 0 && var5 > 0 && var6 > 0)
        {
            this.populatedBlocks += (long)(var4 * var5 * var6);
            var4 = this.boundingBox.maxX - this.boundingBox.minX + 1;
            var5 = Math.min(var1.getHeight() - 1, this.boundingBox.maxY) - Math.max(0, this.boundingBox.minY) + 1;
            var6 = this.boundingBox.maxZ - this.boundingBox.minZ + 1;
            long var7 = (long)(var4 * var5 * var6);

            if (this.populatedBlocks == var7 && this.structureGroup != null)
            {
                ++this.structureGroup.completeComponents;
                this.structureGroup.completeComponentBlocks += this.placedBlocks;
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    public boolean attemptPlaceBlock(World var1, Random var2, int var3, int var4, int var5, StructureBoundingBox var6)
    {
        if (!var6.isVecInside(var3, var4, var5))
        {
            return false;
        }
        else
        {
            Chunk var7 = var1.getChunkFromBlockCoords(var3, var5);
            int var8 = var3 & 15;
            int var9 = var5 & 15;
            int var10 = var7.getBlockID(var8, var4, var9);
            int var11 = this.this$0.replaceableBlocks.matchesBlock_fast(var10);

            if (var11 == 0)
            {
                return false;
            }
            else if (var11 == -1 && !this.this$0.replaceableBlocks.matchesBlock(var10, var7.getBlockMetadata(var8, var4, var9), var2))
            {
                return false;
            }
            else
            {
                int var12 = this.this$0.oreBlock.getMatchingBlock(var2);

                if (var12 == -1)
                {
                    return false;
                }
                else
                {
                    boolean var13 = var7.setBlockIDWithMetadata(var8, var4, var9, var12 >>> 16, var12 & 65535);

                    if (var13)
                    {
                        ++this.placedBlocks;
                        ++this.this$0.placedBlocks;
                        var1.markBlockForUpdate(var3, var4, var5);
                    }

                    return var13;
                }
            }
        }
    }

    public void setParent(MapGenOreDistribution$Component var1)
    {
        if (var1 != null)
        {
            this.componentType = var1.componentType + 1;
        }
        else
        {
            this.componentType = 0;
        }
    }

    public void setChild(MapGenOreDistribution$Component var1)
    {
        if (var1 != null)
        {
            var1.componentType = this.componentType + 1;
        }
    }

    public void buildWireframe(IGeometryBuilder var1)
    {
        float[] var2 = new float[4];

        if (this.this$0.wfHasBB)
        {
            var2[3] = (float)(this.this$0.wfBBColor >>> 24 & 255L) / 255.0F;
            var2[0] = (float)(this.this$0.wfBBColor >>> 16 & 255L) / 255.0F;
            var2[1] = (float)(this.this$0.wfBBColor >>> 8 & 255L) / 255.0F;
            var2[2] = (float)(this.this$0.wfBBColor & 255L) / 255.0F;
            var1.setColor(var2);
            StructureBoundingBox var3 = this.getBoundingBox();
            Transform var4 = new Transform();
            var4.scale(0.5F, 0.5F, 0.5F);
            var4.translate((float)(var3.maxX + var3.minX), (float)(var3.maxY + var3.minY), (float)(var3.maxZ + var3.minZ));
            var4.scale((float)(var3.maxX - var3.minX), (float)(var3.maxY - var3.minY), (float)(var3.maxZ - var3.minZ));
            var1.setPositionTransform(var4);
            WireframeShapes.addUnitWireCube(var1);
        }

        if (this.this$0.wfHasWireframe)
        {
            var2[3] = (float)(this.this$0.wfWireframeColor >>> 24 & 255L) / 255.0F;
            var2[0] = (float)(this.this$0.wfWireframeColor >>> 16 & 255L) / 255.0F;
            var2[1] = (float)(this.this$0.wfWireframeColor >>> 8 & 255L) / 255.0F;
            var2[2] = (float)(this.this$0.wfWireframeColor & 255L) / 255.0F;
            var1.setColor(var2);
        }
    }
}
