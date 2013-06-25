package CustomOreGen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class GeometryRequestData implements Serializable
{
    public transient World world;
    public int dimensionID;
    public int chunkX;
    public int chunkZ;
    public int batchID;
    private static final long serialVersionUID = 2L;

    public GeometryRequestData() {}

    public GeometryRequestData(World var1, int var2, int var3, int var4)
    {
        this.world = var1;
        this.dimensionID = var1 == null ? 0 : var1.provider.dimensionId;
        this.chunkX = var2;
        this.chunkZ = var3;
        this.batchID = var4;
    }

    private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException
    {
        var1.defaultReadObject();
        MinecraftServer var2 = MinecraftServer.getServer();

        if (var2 != null && var2.isServerRunning())
        {
            this.world = MinecraftServer.getServer().worldServerForDimension(this.dimensionID);
        }
    }
}
