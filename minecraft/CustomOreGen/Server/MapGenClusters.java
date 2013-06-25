package CustomOreGen.Server;

import CustomOreGen.Server.DistributionSettingMap$DistributionSetting;
import CustomOreGen.Server.MapGenClusters$ClusterComponent;
import CustomOreGen.Server.MapGenOreDistribution$Component;
import CustomOreGen.Server.MapGenOreDistribution$StructureGroup;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import java.util.Random;

public class MapGenClusters extends MapGenOreDistribution
{
    @DistributionSettingMap$DistributionSetting(
            name = "Size",
            info = "Roughly the number of blocks in every deposit.  No range."
    )
    public final PDist clSize = new PDist(8.0F, 0.0F);
    @DistributionSettingMap$DistributionSetting(
            name = "Frequency",
            info = "Number of deposits per 16x16 chunk.  No range."
    )
    public final PDist clFreq;
    @DistributionSettingMap$DistributionSetting(
            name = "Height",
            info = "Vertical height of the deposits.  Normal distributions are approximated."
    )
    public final PDist clHeight;
    protected static final DistributionSettingMap _clusterSettingsMap = new DistributionSettingMap(MapGenClusters.class);

    public MapGenClusters(int var1, boolean var2)
    {
        super(_clusterSettingsMap, var1, var2);
        this.clFreq = this.frequency;
        this.clHeight = new PDist(64.0F, 64.0F, PDist$Type.uniform);
        this.name = "StandardGen_" + var1;
        this.frequency.set(20.0F, 0.0F, PDist$Type.uniform);
    }

    public boolean validate() throws IllegalStateException
    {
        int var1 = (int)Math.ceil((double)(this.clSize.getMax() / 4.0F));
        this.range = (var1 + 15) / 16;
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
            MapGenClusters$ClusterComponent var6 = new MapGenClusters$ClusterComponent(this, var1, var3, var4, var5, var2);
            var1.addComponent(var6, (MapGenOreDistribution$Component)null);
            return var6;
        }
    }
}
