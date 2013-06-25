package CustomOreGen.Server;

import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEmpty extends WorldGenerator
{
    public static final boolean cacheState = false;
    private final WorldGenerator delegateGenerator;
    private World _lastWorld = null;
    private boolean _lastEnabled = false;

    public WorldGenEmpty(WorldGenerator var1)
    {
        this.delegateGenerator = var1;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (var1 == this._lastWorld)
        {
            ;
        }

        ServerState.checkIfServerChanged(MinecraftServer.getServer(), var1.getWorldInfo());
        this._lastEnabled = ServerState.getWorldConfig(var1).vanillaOreGen;
        this._lastWorld = var1;
        return this._lastEnabled ? this.delegateGenerator.generate(var1, var2, var3, var4, var5) : false;
    }
}
