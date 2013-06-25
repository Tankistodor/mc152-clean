package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Server.MapGenCloud$DiffuseCloudComponent;
import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import CustomOreGen.Util.Transform;
import java.util.Random;

public class MapGenCloud extends MapGenOreDistribution
{
    @DistributionSettingMap$DistributionSetting(
            name = "CloudRadius",
            info = "Cloud horizontal radius, in meters"
    )
    public final PDist clRadius = new PDist(25.0F, 10.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "CloudThickness",
            info = "Cloud thickness (vertical radius), in meters"
    )
    public final PDist clThickness = new PDist(14.0F, 6.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "CloudSizeNoise",
            info = "Noise level added to cloud radius and thickness"
    )
    public final PDist clSizeNoise = new PDist(0.2F, 0.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "CloudHeight",
            info = "Height of cloud, in meters"
    )
    public final PDist clHeight;
    @DistributionSettingMap$DistributionSetting(
            name = "CloudInclination",
            info = "Cloud angle from horizontal plane, in radians"
    )
    public final PDist clInclination;
    @DistributionSettingMap$DistributionSetting(
            name = "OreRadiusMult",
            info = "Radius multiplier for individual ore blocks"
    )
    public final PDist orRadiusMult;
    @DistributionSettingMap$DistributionSetting(
            name = "OreDensity",
            info = "Density multiplier for individual ore blocks"
    )
    public final PDist orDensity;
    @DistributionSettingMap$DistributionSetting(
            name = "OreVolumeNoiseCutoff",
            info = "Minimum threshold for density noise on individual ore blocks"
    )
    public final PDist orVolumeNoiseCutoff;
    protected static final DistributionSettingMap _cloudSettingsMap = new DistributionSettingMap(MapGenCloud.class);

    public MapGenCloud(int var1, boolean var2)
    {
        super(_cloudSettingsMap, var1, var2);
        this.clHeight = new PDist(32.0F, 16.0F, PDist$Type.normal);
        this.clInclination = new PDist(0.0F, 0.35F);
        this.orRadiusMult = new PDist(1.0F, 0.1F);
        this.orDensity = new PDist(0.1F, 0.0F);
        this.orVolumeNoiseCutoff = new PDist(0.5F, 0.0F);
        this.name = "Cloud_" + var1;
        this.frequency.set(0.001F, 0.0F, PDist$Type.uniform);
    }

    public boolean validate() throws IllegalStateException
    {
        float var1 = Math.max(this.clRadius.getMax(), this.clThickness.getMax());
        var1 *= 1.0F + this.clSizeNoise.getMax() * 2.0F;
        var1 *= this.orRadiusMult.getMax();
        this.range = (int)(var1 + 15.9999F) / 16;
        return super.validate();
    }

    public MapGenOreDistribution$Component generateStructure(MapGenOreDistribution$StructureGroup var1, Random var2)
    {
        float var3 = (var2.nextFloat() + (float)var1.chunkX) * 16.0F;
        float var4 = this.clHeight.getValue(var2);
        float var5 = (var2.nextFloat() + (float)var1.chunkZ) * 16.0F;

        if (!var1.canPlaceComponentAt(0, var3, var4, var5, var2))
        {
            return null;
        }
        else
        {
            Transform var6 = new Transform();
            var6.translate(var3, var4, var5);
            var6.rotateZInto(0.0F, 1.0F, 0.0F);
            var6.rotateZ(var2.nextFloat() * ((float)Math.PI * 2F));
            var6.rotateY(this.clInclination.getValue(var2));
            var6.scale(this.clRadius.getValue(var2), this.clRadius.getValue(var2), this.clThickness.getValue(var2));
            MapGenCloud$DiffuseCloudComponent var7 = new MapGenCloud$DiffuseCloudComponent(this, var1, var6, var2);
            var1.addComponent(var7, (MapGenOreDistribution$Component)null);
            return var7;
        }
    }
}
