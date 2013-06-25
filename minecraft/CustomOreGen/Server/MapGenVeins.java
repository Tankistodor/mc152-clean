package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Server.MapGenVeins$1;
import CustomOreGen.Server.MapGenVeins$BezierTubeComponent;
import CustomOreGen.Server.MapGenVeins$BranchType;
import CustomOreGen.Server.MapGenVeins$SolidSphereComponent;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import CustomOreGen.Util.Transform;
import java.util.Random;
import net.minecraft.util.MathHelper;

public class MapGenVeins extends MapGenOreDistribution
{
    @DistributionSettingMap$DistributionSetting(
            name = "branchType",
            info = "Vein branch type (Bezier or Ellipsoid)"
    )
    public MapGenVeins$BranchType brType;
    @DistributionSettingMap$DistributionSetting(
            name = "MotherlodeFrequency",
            info = "Number of motherlodes per 16x16 chunk"
    )
    public final PDist mlFrequency;
    @DistributionSettingMap$DistributionSetting(
            name = "MotherlodeRangeLimit",
            info = "Max horizontal distance that a motherlode may be from the parent distribution, in meters"
    )
    public final PDist mlRangeLimit;
    @DistributionSettingMap$DistributionSetting(
            name = "MotherlodeSize",
            info = "Motherlode size (radius), in meters"
    )
    public final PDist mlSize;
    @DistributionSettingMap$DistributionSetting(
            name = "MotherlodeHeight",
            info = "Height of motherlode, in meters"
    )
    public final PDist mlHeight;
    @DistributionSettingMap$DistributionSetting(
            name = "BranchFrequency",
            info = "Number of branches per motherlode"
    )
    public final PDist brFrequency;
    @DistributionSettingMap$DistributionSetting(
            name = "BranchInclination",
            info = "Branch angle from horizontal plane, in radians"
    )
    public final PDist brInclination;
    @DistributionSettingMap$DistributionSetting(
            name = "BranchLength",
            info = "Length of branches, in meters"
    )
    public final PDist brLength;
    @DistributionSettingMap$DistributionSetting(
            name = "BranchHeightLimit",
            info = "Max vertical distance that a branch may go above/below motherlode, in meters"
    )
    public final PDist brHeightLimit;
    @DistributionSettingMap$DistributionSetting(
            name = "SegmentForkFrequency",
            info = "Forking rate of each segment"
    )
    public final PDist sgForkFrequency;
    @DistributionSettingMap$DistributionSetting(
            name = "SegmentForkLengthMult",
            info = "Multiplier to remaining branch length for each fork"
    )
    public final PDist sgForkLenMult;
    @DistributionSettingMap$DistributionSetting(
            name = "SegmentLength",
            info = "Length of branch segments, in meters"
    )
    public final PDist sgLength;
    @DistributionSettingMap$DistributionSetting(
            name = "SegmentAngle",
            info = "Angle at which each segment diverges from the previous segment, in radians"
    )
    public final PDist sgAngle;
    @DistributionSettingMap$DistributionSetting(
            name = "SegmentRadius",
            info = "Cross-section radius of branch segments, in meters"
    )
    public final PDist sgRadius;
    @DistributionSettingMap$DistributionSetting(
            name = "OreDensity",
            info = "Density multiplier for individual ore blocks"
    )
    public final PDist orDensity;
    @DistributionSettingMap$DistributionSetting(
            name = "OreRadiusMult",
            info = "Radius multiplier for individual ore blocks"
    )
    public final PDist orRadiusMult;
    protected static final DistributionSettingMap _veinsSettingMap = new DistributionSettingMap(MapGenVeins.class);

    public MapGenVeins(int var1, boolean var2)
    {
        super(_veinsSettingMap, var1, var2);
        this.brType = MapGenVeins$BranchType.Bezier;
        this.mlFrequency = this.frequency;
        this.mlRangeLimit = this.parentRangeLimit;
        this.mlSize = new PDist(2.5F, 1.0F);
        this.mlHeight = new PDist(32.0F, 16.0F, PDist$Type.normal);
        this.brFrequency = new PDist(3.0F, 2.0F);
        this.brInclination = new PDist(0.0F, 0.55F);
        this.brLength = new PDist(120.0F, 60.0F);
        this.brHeightLimit = new PDist(16.0F, 0.0F);
        this.sgForkFrequency = new PDist(0.2F, 0.0F);
        this.sgForkLenMult = new PDist(0.75F, 0.25F);
        this.sgLength = new PDist(15.0F, 6.0F);
        this.sgAngle = new PDist(0.5F, 0.5F);
        this.sgRadius = new PDist(0.5F, 0.3F);
        this.orDensity = new PDist(1.0F, 0.0F);
        this.orRadiusMult = new PDist(1.0F, 0.1F);
        this.name = "Veins_" + var1;
        this.frequency.set(0.025F, 0.0F, PDist$Type.uniform);
    }

    public boolean validate() throws IllegalStateException
    {
        float var1 = this.mlSize.getMax() * this.orRadiusMult.getMax();

        if (this.brFrequency.getMax() > 0.0F)
        {
            var1 += this.brLength.getMax();
        }

        this.range = (int)(var1 + 15.9999F) / 16;
        return super.validate();
    }

    public MapGenOreDistribution$Component generateStructure(MapGenOreDistribution$StructureGroup var1, Random var2)
    {
        float var3 = (var2.nextFloat() + (float)var1.chunkX) * 16.0F;
        float var4 = this.mlHeight.getValue(var2);
        float var5 = (var2.nextFloat() + (float)var1.chunkZ) * 16.0F;

        if (!var1.canPlaceComponentAt(0, var3, var4, var5, var2))
        {
            return null;
        }
        else
        {
            Transform var6 = new Transform();
            var6.translate(var3, var4, var5);
            var6.rotateZ(var2.nextFloat() * ((float)Math.PI * 2F));
            var6.rotateY(var2.nextFloat() * ((float)Math.PI * 2F));
            var6.scale(this.mlSize.getValue(var2), this.mlSize.getValue(var2), this.mlSize.getValue(var2));
            MapGenVeins$SolidSphereComponent var7 = new MapGenVeins$SolidSphereComponent(this, var1, var6);
            var1.addComponent(var7, (MapGenOreDistribution$Component)null);

            for (int var8 = this.brFrequency.getIntValue(var2); var8 > 0; --var8)
            {
                Random var9 = new Random(var2.nextLong());
                Transform var10 = new Transform();
                var10.translate(var3, var4, var5);
                var10.rotateY(var9.nextFloat() * ((float)Math.PI * 2F));
                var10.rotateX(-this.brInclination.getValue(var9));
                float var11 = var4 + this.brHeightLimit.getValue(var9);
                float var12 = var4 - this.brHeightLimit.getValue(var9);
                this.generateBranch(var1, this.brLength.getValue(var9), var11, var12, var10, var7, var9);
            }

            return var7;
        }
    }

    public void generateBranch(MapGenOreDistribution$StructureGroup var1, float var2, float var3, float var4, Transform var5, MapGenOreDistribution$Component var6, Random var7)
    {
        float[] var8 = new float[3];

        while (var2 > 0.0F)
        {
            float var9 = this.sgLength.getValue(var7);

            if (var9 > var2)
            {
                var9 = var2;
            }

            var2 -= var9;
            var9 /= 2.0F;
            float var10 = this.sgRadius.getValue(var7);
            var5.translate(0.0F, 0.0F, var9);
            Transform var11 = var5.clone().scale(var10, var10, var9);
            MapGenOreDistribution$Component var12 = null;

            switch (MapGenVeins$1.$SwitchMap$net$minecraft$src$CustomOreGen$Server$MapGenVeins$BranchType[this.brType.ordinal()])
            {
                case 1:
                    var12 = new MapGenVeins$SolidSphereComponent(this, var1, var11);
                    break;

                case 2:
                    var12 = new MapGenVeins$BezierTubeComponent(this, var1, var11);
            }

            var1.addComponent((MapGenOreDistribution$Component)var12, (MapGenOreDistribution$Component)var6);
            var6 = var12;
            var5.translate(0.0F, 0.0F, var9);
            var8[0] = 0.0F;
            var8[1] = 0.0F;
            var8[2] = 0.0F;
            var5.transformVector(var8);

            if (var8[1] > var3 || var8[1] < var4)
            {
                return;
            }

            if (!var1.canPlaceComponentAt(((MapGenOreDistribution$Component)var12).getComponentType() + 1, var8[0], var8[1], var8[2], var7))
            {
                return;
            }

            if (var2 <= 0.0F)
            {
                return;
            }

            for (int var13 = this.sgForkFrequency.getIntValue(var7); var13 > 0; --var13)
            {
                Random var14 = new Random(var7.nextLong());
                Transform var15 = var5.clone();
                float var16 = var14.nextFloat() * ((float)Math.PI * 2F);
                var15.rotate(this.sgAngle.getValue(var14), MathHelper.cos(var16), MathHelper.sin(var16), 0.0F);
                float var17 = this.sgForkLenMult.getValue(var14);
                this.generateBranch(var1, var2 * (var17 > 1.0F ? 1.0F : var17), var3, var4, var15, (MapGenOreDistribution$Component)var12, var14);
            }

            float var18 = var7.nextFloat() * ((float)Math.PI * 2F);
            var5.rotate(this.sgAngle.getValue(var7), MathHelper.cos(var18), MathHelper.sin(var18), 0.0F);
        }
    }
}
