package CustomOreGen;

import CustomOreGen.Client.ClientState;
import CustomOreGen.Server.ServerState;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;

public class ForgeInterface
{
    public static ForgeInterface createAndRegister()
    {
        CustomOreGenBase.log.finer("Registering Forge interface ...");
        ForgeInterface var0 = new ForgeInterface();
        MinecraftForge.EVENT_BUS.register(var0);
        return var0;
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent var1)
    {
        ClientState.onRenderWorld(Minecraft.getMinecraft().renderViewEntity, (double)var1.partialTicks);
    }

    @ForgeSubscribe
    public void onLoadWorld(Load var1)
    {
        if (var1.world instanceof WorldServer)
        {
            ServerState.checkIfServerChanged(MinecraftServer.getServer(), var1.world.getWorldInfo());
            ServerState.getWorldConfig(var1.world);
        }
        else if (var1.world instanceof WorldClient && ClientState.hasWorldChanged(var1.world))
        {
            ClientState.onWorldChanged(var1.world);
        }
    }

    public static String getWorldDimensionFolder(World var0)
    {
        return var0.provider.getSaveFolder();
    }
}
