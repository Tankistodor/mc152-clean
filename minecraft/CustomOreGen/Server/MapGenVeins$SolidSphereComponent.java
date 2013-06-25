package CustomOreGen.Server;

import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.IGeometryBuilder;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import CustomOreGen.Util.Transform;
import CustomOreGen.Util.WireframeShapes;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

class MapGenVeins$SolidSphereComponent extends MapGenOreDistribution$Component
{
    protected final Transform mat;
    protected final Transform invMat;

    final MapGenVeins this$0;

    public MapGenVeins$SolidSphereComponent(MapGenVeins var1, MapGenOreDistribution$StructureGroup var2, Transform var3)
    {
        super(var1, var2);
        this.this$0 = var1;
        float var4 = var1.orRadiusMult.getMax();

        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        float[] var5 = new float[] { -var4, -var4, -var4, var4, var4, var4};
        var3.transformBB(var5);
        this.boundingBox = new StructureBoundingBox(MathHelper.floor_float(var5[0]), MathHelper.floor_float(var5[1]), MathHelper.floor_float(var5[2]), MathHelper.floor_float(var5[3]) + 1, MathHelper.floor_float(var5[4]) + 1, MathHelper.floor_float(var5[5]) + 1);
        this.mat = var3.clone();

        if (var3.determinant() != 0.0F)
        {
            this.invMat = var3.inverse();
        }
        else
        {
            this.invMat = null;
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        if (this.invMat == null)
        {
            return true;
        }
        else
        {
            float var4 = this.this$0.orRadiusMult.getMax();

            if (var4 < 0.0F)
            {
                var4 = 0.0F;
            }

            var4 *= var4;
            float var5 = this.this$0.orRadiusMult.getMin();

            if (var5 < 0.0F)
            {
                var5 = 0.0F;
            }

            var5 *= var5;
            float[] var6 = new float[3];

            for (int var7 = Math.max(this.boundingBox.minX, var3.minX); var7 <= Math.min(this.boundingBox.maxX, var3.maxX); ++var7)
            {
                for (int var8 = Math.max(this.boundingBox.minY, var3.minY); var8 <= Math.min(this.boundingBox.maxY, var3.maxY); ++var8)
                {
                    for (int var9 = Math.max(this.boundingBox.minZ, var3.minZ); var9 <= Math.min(this.boundingBox.maxZ, var3.maxZ); ++var9)
                    {
                        var6[0] = (float)var7 + 0.5F;
                        var6[1] = (float)var8 + 0.5F;
                        var6[2] = (float)var9 + 0.5F;
                        this.invMat.transformVector(var6);
                        float var10 = var6[0] * var6[0] + var6[1] * var6[1] + var6[2] * var6[2];

                        if (var10 <= var4)
                        {
                            if (var10 > var5)
                            {
                                float var11 = this.this$0.orRadiusMult.getValue(var2);

                                if (var10 > var11 * var11)
                                {
                                    continue;
                                }
                            }

                            if (this.this$0.orDensity.getIntValue(var2) >= 1)
                            {
                                this.attemptPlaceBlock(var1, var2, var7, var8, var9, var3);
                            }
                        }
                    }
                }
            }

            super.addComponentParts(var1, var2, var3);
            return true;
        }
    }

    public void buildWireframe(IGeometryBuilder var1)
    {
        super.buildWireframe(var1);

        if (this.this$0.wfHasWireframe)
        {
            var1.setPositionTransform(this.mat);
            WireframeShapes.addUnitWireSphere(var1, 8, 8);
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.LINE, new int[0]);
            var1.addVertex(new float[] {0.0F, 0.0F, -1.0F});
            var1.addVertex(new float[] {0.0F, 0.0F, 1.0F});
        }
    }
}
