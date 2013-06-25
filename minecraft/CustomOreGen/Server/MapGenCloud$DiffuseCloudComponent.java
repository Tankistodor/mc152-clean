package CustomOreGen.Server;

import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.IGeometryBuilder;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import CustomOreGen.Util.NoiseGenerator;
import CustomOreGen.Util.Transform;
import CustomOreGen.Util.WireframeShapes;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

class MapGenCloud$DiffuseCloudComponent extends MapGenOreDistribution$Component
{
    protected final Transform mat;
    protected final Transform invMat;
    protected final NoiseGenerator noiseGen;
    protected final float sizeNoiseMagnitude;
    protected final int noiseLevels;

    final MapGenCloud this$0;

    public MapGenCloud$DiffuseCloudComponent(MapGenCloud var1, MapGenOreDistribution$StructureGroup var2, Transform var3, Random var4)
    {
        super(var1, var2);
        this.this$0 = var1;
        this.noiseGen = new NoiseGenerator(var4);
        this.sizeNoiseMagnitude = Math.abs(var1.clSizeNoise.getValue(var4));
        float var5 = (1.0F + this.sizeNoiseMagnitude * 2.0F) * var1.orRadiusMult.getMax();

        if (var5 < 0.0F)
        {
            var5 = 0.0F;
        }

        float[] var6 = new float[] { -var5, -var5, -var5, var5, var5, var5};
        var3.transformBB(var6);
        this.boundingBox = new StructureBoundingBox(MathHelper.floor_float(var6[0]), MathHelper.floor_float(var6[1]), MathHelper.floor_float(var6[2]), MathHelper.floor_float(var6[3]) + 1, MathHelper.floor_float(var6[4]) + 1, MathHelper.floor_float(var6[5]) + 1);
        float var7 = (float)Math.max(this.boundingBox.getXSize(), Math.max(this.boundingBox.getYSize(), this.boundingBox.getZSize())) * 0.2F;
        this.noiseLevels = var7 <= 1.0F ? 0 : (int)(Math.log((double)var7) / Math.log(2.0D) + 0.5D);
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

    public float getNoise(float var1, float var2, float var3)
    {
        double var4 = 0.0D;

        for (int var6 = 0; var6 < this.noiseLevels; ++var6)
        {
            float var7 = (float)(1 << var6);
            var4 += (double)(1.0F / var7) * this.noiseGen.noise((double)(var1 * var7), (double)(var2 * var7), (double)(var3 * var7));
        }

        return (float)var4;
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
            float var4 = Math.max(this.this$0.orRadiusMult.getMax(), 0.0F);
            float var5 = Math.max(this.this$0.orRadiusMult.getMin(), 0.0F);
            float var6 = var4 * (1.0F + this.sizeNoiseMagnitude * 2.0F);
            float var7 = var5 * (1.0F - this.sizeNoiseMagnitude * 2.0F);
            var6 *= var6;
            var7 *= var7;
            float[] var8 = new float[3];

            for (int var9 = Math.max(this.boundingBox.minX, var3.minX); var9 <= Math.min(this.boundingBox.maxX, var3.maxX); ++var9)
            {
                for (int var10 = Math.max(this.boundingBox.minY, var3.minY); var10 <= Math.min(this.boundingBox.maxY, var3.maxY); ++var10)
                {
                    for (int var11 = Math.max(this.boundingBox.minZ, var3.minZ); var11 <= Math.min(this.boundingBox.maxZ, var3.maxZ); ++var11)
                    {
                        var8[0] = (float)var9 + 0.5F;
                        var8[1] = (float)var10 + 0.5F;
                        var8[2] = (float)var11 + 0.5F;
                        this.invMat.transformVector(var8);
                        float var12 = var8[0] * var8[0] + var8[1] * var8[1] + var8[2] * var8[2];

                        if (var12 <= var6)
                        {
                            if (var12 > var7)
                            {
                                float var13 = MathHelper.sqrt_float(var12);
                                float var14 = 1.0F;

                                if (var13 > 0.0F)
                                {
                                    var14 += this.sizeNoiseMagnitude * this.getNoise(var8[0] / var13, var8[1] / var13, var8[2] / var13);
                                }
                                else
                                {
                                    var14 += this.sizeNoiseMagnitude * this.getNoise(0.0F, 0.0F, 0.0F);
                                }

                                if (var14 <= 0.0F)
                                {
                                    continue;
                                }

                                var13 /= var14;

                                if (var13 > var4 || var13 > var5 && var13 > this.this$0.orRadiusMult.getValue(var2))
                                {
                                    continue;
                                }
                            }

                            if (this.this$0.orVolumeNoiseCutoff.getMin() <= 1.0F && (this.this$0.orVolumeNoiseCutoff.getMax() <= 0.0F || (this.getNoise(var8[0], var8[1], var8[2]) + 1.0F) / 2.0F >= this.this$0.orVolumeNoiseCutoff.getValue(var2)) && this.this$0.orDensity.getIntValue(var2) >= 1)
                            {
                                this.attemptPlaceBlock(var1, var2, var9, var10, var11, var3);
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

        if (this.this$0.wfHasWireframe && this.mat != null)
        {
            int var2 = Math.max(8, (1 << this.noiseLevels) * 4);
            int var3 = var2;
            var1.setPositionTransform(this.mat);
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {var2 + 1, var2 + 2, 1});
            float[][] var4 = WireframeShapes.getCirclePoints(var2, (float[][])null);
            float[][] var5 = WireframeShapes.getCirclePoints(2 * var2, (float[][])null);
            float[] var6 = new float[3];
            int var7;
            int var8;
            float var9;

            for (var7 = 1; var7 < var3; ++var7)
            {
                for (var8 = 0; var8 < var2; ++var8)
                {
                    var6[0] = var5[var7][1] * var4[var8][0];
                    var6[1] = var5[var7][1] * var4[var8][1];
                    var6[2] = var5[var7][0];
                    var9 = 1.0F + this.sizeNoiseMagnitude * this.getNoise(var6[0], var6[1], var6[2]);
                    var6[0] *= var9;
                    var6[1] *= var9;
                    var6[2] *= var9;
                    var1.addVertex(var6);
                }

                var1.addVertexRef(var2);
            }

            var1.setVertexMode(IGeometryBuilder$PrimitiveType.TRIANGLE, new int[] {1});
            var7 = (var2 + 1) * (var3 - 2) + 1;
            var1.addVertexRef(var7);

            for (var8 = 1; var8 <= var2; ++var8)
            {
                if (var8 == 1)
                {
                    var6[0] = 0.0F;
                    var6[1] = 0.0F;
                    var6[2] = 1.0F;
                    var9 = 1.0F + this.sizeNoiseMagnitude * this.getNoise(var6[0], var6[1], var6[2]);
                    var6[0] *= var9;
                    var6[1] *= var9;
                    var6[2] *= var9;
                    var1.addVertex(var6);
                }
                else
                {
                    var1.addVertexRef(2);
                }

                var1.addVertexRef(var7 + 3 * var8);
            }

            var1.setVertexMode(IGeometryBuilder$PrimitiveType.TRIANGLE, new int[] {1});
            var7 = 3 * var2 + 2;
            var1.addVertexRef(var7);

            for (var8 = 1; var8 <= var2; ++var8)
            {
                if (var8 == 1)
                {
                    var6[0] = 0.0F;
                    var6[1] = 0.0F;
                    var6[2] = -1.0F;
                    var9 = 1.0F + this.sizeNoiseMagnitude * this.getNoise(var6[0], var6[1], var6[2]);
                    var6[0] *= var9;
                    var6[1] *= var9;
                    var6[2] *= var9;
                    var1.addVertex(var6);
                }
                else
                {
                    var1.addVertexRef(2);
                }

                var1.addVertexRef(var7 + var8);
            }
        }
    }
}
