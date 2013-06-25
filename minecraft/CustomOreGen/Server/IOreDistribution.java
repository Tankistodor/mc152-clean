package CustomOreGen.Server;

import CustomOreGen.Util.GeometryStream;
import java.util.Map;
import net.minecraft.world.World;

public interface IOreDistribution
{
    Map getDistributionSettings();

    Object getDistributionSetting(String var1);

    void setDistributionSetting(String var1, Object var2) throws IllegalArgumentException, IllegalAccessException;

    void generate(World var1, int var2, int var3);

    void populate(World var1, int var2, int var3);

    void cull();

    void clear();

    GeometryStream getDebuggingGeometry(World var1, int var2, int var3);

    boolean validate() throws IllegalStateException;

    void inheritFrom(IOreDistribution var1) throws IllegalArgumentException;
}
