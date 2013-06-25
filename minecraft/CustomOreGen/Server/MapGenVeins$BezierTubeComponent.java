package CustomOreGen.Server;

import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Server.MapGenVeins$BezierTubeComponent$interpolationContext;
import CustomOreGen.Util.IGeometryBuilder;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import CustomOreGen.Util.Transform;
import CustomOreGen.Util.WireframeShapes;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

class MapGenVeins$BezierTubeComponent extends MapGenOreDistribution$Component
{
    protected float[] mid;
    protected float[] end;
    protected final float rad;
    protected MapGenVeins$BezierTubeComponent prev;
    protected MapGenVeins$BezierTubeComponent next;
    protected final MapGenVeins$BezierTubeComponent$interpolationContext context;
    protected final Transform mat;

    final MapGenVeins this$0;

    public MapGenVeins$BezierTubeComponent(MapGenVeins var1, MapGenOreDistribution$StructureGroup var2, Transform var3)
    {
        super(var1, var2);
        this.this$0 = var1;
        this.mid = new float[] {0.0F, 0.0F, 0.0F};
        var3.transformVector(this.mid);
        this.end = new float[] {0.0F, 0.0F, 1.0F};
        var3.transformVector(this.end);
        float[] var4 = new float[] {1.0F, 0.0F, 0.0F, 0.0F};
        var3.transformVector(var4);
        this.rad = MathHelper.sqrt_float(var4[0] * var4[0] + var4[1] * var4[1] + var4[2] * var4[2]);
        float var5 = this.rad * var1.orRadiusMult.getMax();

        if (var5 < 0.0F)
        {
            var5 = 0.0F;
        }

        float[] var6 = new float[] { -var5, -var5, -1.0F, var5, var5, 1.0F};
        var3.transformBB(var6);
        this.boundingBox = new StructureBoundingBox(MathHelper.floor_float(var6[0]), MathHelper.floor_float(var6[1]), MathHelper.floor_float(var6[2]), MathHelper.floor_float(var6[3]) + 1, MathHelper.floor_float(var6[4]) + 1, MathHelper.floor_float(var6[5]) + 1);
        this.context = new MapGenVeins$BezierTubeComponent$interpolationContext(this);
        this.mat = var3.identity();
    }

    public void setChild(MapGenOreDistribution$Component var1)
    {
        super.setChild(var1);
        this.next = var1 instanceof MapGenVeins$BezierTubeComponent ? (MapGenVeins$BezierTubeComponent)var1 : null;

        if (this.next != null)
        {
            float var2 = this.interpolateRadius(0.5F) * this.this$0.orRadiusMult.getMax();

            if (var2 < 0.0F)
            {
                var2 = 0.0F;
            }

            float[] var3 = new float[3];
            this.interpolatePosition(var3, 0.5F);
            StructureBoundingBox var4 = new StructureBoundingBox(MathHelper.floor_float(var3[0] - var2), MathHelper.floor_float(var3[1] - var2), MathHelper.floor_float(var3[2] - var2), MathHelper.floor_float(var3[0] + var2) + 1, MathHelper.floor_float(var3[1] + var2) + 1, MathHelper.floor_float(var3[2] + var2) + 1);
            this.boundingBox.expandTo(var4);
        }
    }

    public void setParent(MapGenOreDistribution$Component var1)
    {
        super.setParent(var1);
        this.prev = var1 instanceof MapGenVeins$BezierTubeComponent ? (MapGenVeins$BezierTubeComponent)var1 : null;

        if (this.prev != null)
        {
            float var2 = this.prev.next == this ? -0.5F : -1.0F;
            float var3 = this.interpolateRadius(var2) * this.this$0.orRadiusMult.getMax();

            if (var3 < 0.0F)
            {
                var3 = 0.0F;
            }

            float[] var4 = new float[3];
            this.interpolatePosition(var4, var2);
            StructureBoundingBox var5 = new StructureBoundingBox(MathHelper.floor_float(var4[0] - var3), MathHelper.floor_float(var4[1] - var3), MathHelper.floor_float(var4[2] - var3), MathHelper.floor_float(var4[0] + var3) + 1, MathHelper.floor_float(var4[1] + var3) + 1, MathHelper.floor_float(var4[2] + var3) + 1);
            this.boundingBox.expandTo(var5);
        }
    }

    public void interpolatePosition(float[] var1, float var2)
    {
        float var3;

        if (var2 > 0.0F && this.next != null)
        {
            var3 = 1.0F - var2;
            var1[0] = var3 * var3 * this.mid[0] + 2.0F * var2 * var3 * this.end[0] + var2 * var2 * this.next.mid[0];
            var1[1] = var3 * var3 * this.mid[1] + 2.0F * var2 * var3 * this.end[1] + var2 * var2 * this.next.mid[1];
            var1[2] = var3 * var3 * this.mid[2] + 2.0F * var2 * var3 * this.end[2] + var2 * var2 * this.next.mid[2];
        }
        else if (var2 < 0.0F && this.prev != null)
        {
            var3 = 1.0F + var2;
            var1[0] = var3 * var3 * this.mid[0] - 2.0F * var2 * var3 * this.prev.end[0] + var2 * var2 * this.prev.mid[0];
            var1[1] = var3 * var3 * this.mid[1] - 2.0F * var2 * var3 * this.prev.end[1] + var2 * var2 * this.prev.mid[1];
            var1[2] = var3 * var3 * this.mid[2] - 2.0F * var2 * var3 * this.prev.end[2] + var2 * var2 * this.prev.mid[2];
        }
        else
        {
            var3 = 1.0F - 2.0F * var2;
            var1[0] = var3 * this.mid[0] + 2.0F * var2 * this.end[0];
            var1[1] = var3 * this.mid[1] + 2.0F * var2 * this.end[1];
            var1[2] = var3 * this.mid[2] + 2.0F * var2 * this.end[2];
        }
    }

    public void interpolateDerivative(float[] var1, float var2)
    {
        if (var2 > 0.0F && this.next != null)
        {
            var1[0] = 2.0F * ((1.0F - var2) * (this.end[0] - this.mid[0]) + var2 * (this.next.mid[0] - this.end[0]));
            var1[1] = 2.0F * ((1.0F - var2) * (this.end[1] - this.mid[1]) + var2 * (this.next.mid[1] - this.end[1]));
            var1[2] = 2.0F * ((1.0F - var2) * (this.end[2] - this.mid[2]) + var2 * (this.next.mid[2] - this.end[2]));
        }
        else if (var2 < 0.0F && this.prev != null)
        {
            var1[0] = 2.0F * ((1.0F + var2) * (this.mid[0] - this.prev.end[0]) - var2 * (this.prev.end[0] - this.prev.mid[0]));
            var1[1] = 2.0F * ((1.0F + var2) * (this.mid[1] - this.prev.end[1]) - var2 * (this.prev.end[1] - this.prev.mid[1]));
            var1[2] = 2.0F * ((1.0F + var2) * (this.mid[2] - this.prev.end[2]) - var2 * (this.prev.end[2] - this.prev.mid[2]));
        }
        else
        {
            var1[0] = 2.0F * (this.end[0] - this.mid[0]);
            var1[1] = 2.0F * (this.end[1] - this.mid[1]);
            var1[2] = 2.0F * (this.end[2] - this.mid[2]);
        }
    }

    public float interpolateRadius(float var1)
    {
        return var1 > 0.0F && this.next != null ? (1.0F - var1) * this.rad + var1 * this.next.rad : (var1 < 0.0F && this.prev != null ? (1.0F + var1) * this.rad - var1 * this.prev.rad : (var1 <= 0.0F && var1 > -1.0F ? this.rad : (var1 > 0.0F && var1 < 1.0F ? this.rad * MathHelper.sqrt_float(1.0F - 4.0F * var1 * var1) : 0.0F)));
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        float var4 = this.this$0.orRadiusMult.getMax();

        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        float var5 = var4 * var4;
        float var6 = this.this$0.orRadiusMult.getMin();

        if (var6 < 0.0F)
        {
            var6 = 0.0F;
        }

        float var7 = var6 * var6;
        float[] var8 = new float[3];
        float[] var9 = new float[6];
        boolean var10 = true;
        this.context.init(0.0F, true);
        int var24;

        do
        {
            var24 = (int)this.context.radius / 4 + 1;

            if (this.context.radius > 0.0F)
            {
                float var11 = 0.7F * (float)var24 / this.context.radius;
                int var12 = (int)(var4 / var11) + 1;
                boolean var13 = this.context.radius * var4 < 0.25F;
                this.mat.identity();
                this.mat.translate(this.context.pos[0], this.context.pos[1], this.context.pos[2]);
                this.mat.rotateZInto(this.context.der[0], this.context.der[1], this.context.der[2]);
                this.mat.scale(this.context.radius, this.context.radius, (float)var24);
                var9[0] = -var4;
                var9[1] = -var4;
                var9[2] = -1.0F;
                var9[3] = var4;
                var9[4] = var4;
                var9[5] = 1.0F;
                this.mat.transformBB(var9);
                boolean var14 = var9[3] >= (float)var3.minX && var9[0] <= (float)var3.maxX && var9[5] >= (float)var3.minZ && var9[2] <= (float)var3.maxZ && var9[4] >= (float)var3.minY && var9[1] <= (float)var3.maxY;

                if (var14)
                {
                    for (int var15 = -var12; var15 < var12; ++var15)
                    {
                        for (int var16 = -var12; var16 < var12; ++var16)
                        {
                            var8[0] = (float)var15 * var11;
                            var8[1] = (float)var16 * var11;
                            var8[2] = 0.0F;
                            float var17 = var8[0] * var8[0] + var8[1] * var8[1];

                            if (var17 <= var5)
                            {
                                if (var17 > var7)
                                {
                                    float var18 = this.this$0.orRadiusMult.getValue(var2);

                                    if (var17 > var18 * var18)
                                    {
                                        continue;
                                    }
                                }

                                if (!var13 || this.context.radius * var4 * 4.0F >= var2.nextFloat())
                                {
                                    this.mat.transformVector(var8);
                                    int var25 = MathHelper.floor_float(var8[0]) - var24 / 2;
                                    int var19 = MathHelper.floor_float(var8[1]) - var24 / 2;
                                    int var20 = MathHelper.floor_float(var8[2]) - var24 / 2;

                                    for (int var21 = var25; var21 < var24 + var25; ++var21)
                                    {
                                        for (int var22 = var19; var22 < var24 + var19; ++var22)
                                        {
                                            for (int var23 = var20; var23 < var24 + var20; ++var23)
                                            {
                                                if (this.this$0.orDensity.getIntValue(var2) >= 1)
                                                {
                                                    this.attemptPlaceBlock(var1, var2, var21, var22, var23, var3);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        while (this.context.advance(0.7F * (float)var24));

        super.addComponentParts(var1, var2, var3);
        return true;
    }

    public void buildWireframe(IGeometryBuilder var1)
    {
        super.buildWireframe(var1);

        if (this.this$0.wfHasWireframe)
        {
            var1.setPositionTransform((Transform)null);
            this.context.pos[0] = 2.0F * this.mid[0] - this.end[0];
            this.context.pos[1] = 2.0F * this.mid[1] - this.end[1];
            this.context.pos[2] = 2.0F * this.mid[2] - this.end[2];
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.LINE, new int[0]);
            var1.addVertex(this.context.pos);
            var1.addVertex(this.end);
            byte var2 = 10;
            var1.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {var2 + 1, var2 + 2, 1});
            this.context.init(0.05F, true);

            do
            {
                this.mat.identity();
                this.mat.translate(this.context.pos[0], this.context.pos[1], this.context.pos[2]);
                this.mat.rotateZInto(this.context.der[0], this.context.der[1], this.context.der[2]);
                this.mat.scale(this.context.radius, this.context.radius, 0.0F);
                var1.setPositionTransform(this.mat);
                float[][] var3 = WireframeShapes.getCirclePoints(var2, (float[][])null);

                for (int var4 = 0; var4 < var2; ++var4)
                {
                    var1.addVertex(var3[var4], var3[var4], (float[])null, (float[])null);
                }

                var1.addVertexRef(var2);
            }
            while (this.context.advance(2.0F));
        }
    }
}
