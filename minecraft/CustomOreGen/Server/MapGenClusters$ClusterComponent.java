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

class MapGenClusters$ClusterComponent extends MapGenOreDistribution$Component
{
    protected final int size;
    protected final float[] ptA;
    protected final float[] ptB;
    protected final float[] rad;

    final MapGenClusters this$0;

    public MapGenClusters$ClusterComponent(MapGenClusters var1, MapGenOreDistribution$StructureGroup var2, float var3, float var4, float var5, Random var6)
    {
        super(var1, var2);
        this.this$0 = var1;
        this.size = var1.clSize.getIntValue(var6);
        double var7 = (double)var6.nextFloat() * Math.PI;
        this.ptA = new float[3];
        this.ptB = new float[3];
        float var9 = (float)Math.sin(var7) * (float)this.size / 8.0F;
        float var10 = (float)Math.cos(var7) * (float)this.size / 8.0F;
        this.ptA[0] = var3 + var9;
        this.ptB[0] = var3 - var9;
        this.ptA[2] = var5 + var10;
        this.ptB[2] = var5 - var10;
        this.ptA[1] = var4 + (float)var6.nextInt(3) - 2.0F;
        this.ptB[1] = var4 + (float)var6.nextInt(3) - 2.0F;
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        this.rad = new float[this.size + 1];

        for (int var11 = 0; var11 < this.rad.length; ++var11)
        {
            float var12 = (float)var11 / (float)(this.rad.length - 1);
            float var13 = (float)var6.nextDouble() * (float)this.size / 32.0F;
            this.rad[var11] = ((float)Math.sin((double)var12 * Math.PI) + 1.0F) * var13 + 0.5F;
            float var14 = this.ptA[0] + (this.ptB[0] - this.ptA[0]) * var12;
            float var15 = this.ptA[1] + (this.ptB[1] - this.ptA[1]) * var12;
            float var16 = this.ptA[2] + (this.ptB[2] - this.ptA[2]) * var12;
            this.boundingBox.minX = Math.min(this.boundingBox.minX, MathHelper.floor_float(var14 - this.rad[var11]));
            this.boundingBox.minY = Math.min(this.boundingBox.minY, MathHelper.floor_float(var15 - this.rad[var11]));
            this.boundingBox.minZ = Math.min(this.boundingBox.minZ, MathHelper.floor_float(var16 - this.rad[var11]));
            this.boundingBox.maxX = Math.max(this.boundingBox.maxX, MathHelper.ceiling_float_int(var14 + this.rad[var11]));
            this.boundingBox.maxY = Math.max(this.boundingBox.maxY, MathHelper.ceiling_float_int(var15 + this.rad[var11]));
            this.boundingBox.maxZ = Math.max(this.boundingBox.maxZ, MathHelper.ceiling_float_int(var16 + this.rad[var11]));
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        for (int var4 = 0; var4 < this.rad.length; ++var4)
        {
            float var5 = (float)var4 / (float)(this.rad.length - 1);
            float var6 = this.ptA[0] + (this.ptB[0] - this.ptA[0]) * var5;
            float var7 = this.ptA[1] + (this.ptB[1] - this.ptA[1]) * var5;
            float var8 = this.ptA[2] + (this.ptB[2] - this.ptA[2]) * var5;
            int var9 = Math.max(MathHelper.floor_float(var6 - this.rad[var4]), var3.minX);
            int var10 = Math.min(MathHelper.floor_float(var6 + this.rad[var4]), var3.maxX);
            int var11 = Math.max(MathHelper.floor_float(var7 - this.rad[var4]), var3.minY);
            int var12 = Math.min(MathHelper.ceiling_float_int(var7 + this.rad[var4]), var3.maxY);
            int var13 = Math.max(MathHelper.ceiling_float_int(var8 - this.rad[var4]), var3.minZ);
            int var14 = Math.min(MathHelper.ceiling_float_int(var8 + this.rad[var4]), var3.maxZ);

            for (int var15 = var9; var15 <= var10; ++var15)
            {
                double var16 = ((double)var15 + 0.5D - (double)var6) / (double)this.rad[var4];

                if (var16 * var16 < 1.0D)
                {
                    for (int var18 = var11; var18 <= var12; ++var18)
                    {
                        double var19 = ((double)var18 + 0.5D - (double)var7) / (double)this.rad[var4];

                        if (var16 * var16 + var19 * var19 < 1.0D)
                        {
                            for (int var21 = var13; var21 <= var14; ++var21)
                            {
                                double var22 = ((double)var21 + 0.5D - (double)var8) / (double)this.rad[var4];

                                if (var16 * var16 + var19 * var19 + var22 * var22 < 1.0D)
                                {
                                    this.attemptPlaceBlock(var1, var2, var15, var18, var21, var3);
                                }
                            }
                        }
                    }
                }
            }
        }

        super.addComponentParts(var1, var2, var3);
        return true;
    }

    public void buildWireframe(IGeometryBuilder var1)
    {
        super.buildWireframe(var1);

        if (this.this$0.wfHasWireframe)
        {
            var1.setPositionTransform((Transform)null);
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.LINE, new int[0]);
            var1.addVertex(this.ptA);
            var1.addVertex(this.ptB);
            float var2 = this.ptB[0] - this.ptA[0];
            float var3 = this.ptB[1] - this.ptA[1];
            float var4 = this.ptB[2] - this.ptA[2];
            float var5 = (float)Math.sqrt((double)(var2 * var2 + var3 * var3 + var4 * var4));
            int var6 = MathHelper.ceiling_float_int(var5);
            byte var7 = 8;
            float[][] var8 = WireframeShapes.getCirclePoints(var7, (float[][])null);
            float[] var9 = new float[3];
            Transform var10 = new Transform();
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {var7 + 1, var7 + 2, 1});

            for (int var11 = 0; var11 <= var6; ++var11)
            {
                if (var11 == 0)
                {
                    var10.translate(this.ptA[0], this.ptA[1], this.ptA[2]);
                    var10.rotateZInto(this.ptB[0] - this.ptA[0], this.ptB[1] - this.ptA[1], this.ptB[2] - this.ptA[2]);
                }
                else
                {
                    var10.translate(0.0F, 0.0F, 1.0F);
                }

                var1.setPositionTransform(var10);
                float var12 = ((float)Math.sin(Math.PI * (double)var11 / (double)((float)var6)) + 1.0F) * (float)this.size / 32.0F + 0.5F;

                for (int var13 = 0; var13 < var7; ++var13)
                {
                    var9[0] = var8[var13][0] * var12;
                    var9[1] = var8[var13][1] * var12;
                    var9[2] = var8[var13][2];
                    var1.addVertex(var9, var9, (float[])null, (float[])null);
                }

                var1.addVertexRef(var7);
            }
        }
    }
}
