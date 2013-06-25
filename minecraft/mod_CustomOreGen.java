import CustomOreGen.CustomOreGenBase;
import CustomOreGen.CustomPacketPayload;
import CustomOreGen.CustomPacketPayload.PayloadType;
import CustomOreGen.GeometryData;
import CustomOreGen.GeometryRequestData;
import CustomOreGen.Client.ClientState;
import CustomOreGen.Client.ClientState$WireframeRenderMode;
import CustomOreGen.Server.ServerState;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class mod_CustomOreGen extends BaseMod
{
    public String getVersion()
    {
        return "[1.5.2]tankemod";
    }

    public String getPriorities()
    {
        return "after:*;";
    }

    public void load()
    {
        if (!CustomOreGenBase.hasFML())
        {
            CustomOreGenBase.log = ModLoader.getLogger();
        }

        try
        {
            File var1 = null;
            MinecraftServer var2 = MinecraftServer.getServer();

            if (var2 != null)
            {
                var1 = var2.getFile("");
            }
            else
            {
                Minecraft.getMinecraft();
                var1 = Minecraft.getMinecraftDir();
            }

            CustomOreGenBase.mcPath = var1.getCanonicalPath();
        }
        catch (Throwable var3)
        {
            ;
        }

        if (!CustomOreGenBase.hasFML())
        {
            ModLoader.setInGameHook(this, true, false);
        }

        CustomPacketPayload.registerChannels(this);
    }

    public void modsLoaded()
    {
        CustomOreGenBase.onModPostLoad();
        boolean var1 = false;
        String var2 = null;
        Iterator var3 = ModLoader.getLoadedMods().iterator();

        while (var3.hasNext())
        {
            BaseMod var4 = (BaseMod)var3.next();

            if (var4 == this)
            {
                var1 = true;
            }
            else if (var1 && var4 != null)
            {
                var2 = (var2 == null ? "" : var2 + ", ") + var4.getName();
            }
        }

        if (var2 == null)
        {
            CustomOreGenBase.log.finer("Confirmed that CustomOreGen has precedence during world generation");
        }
        else
        {
            CustomOreGenBase.log.warning("The following mods force ModLoader to load them *after* CustomOreGen: " + var2 + ".  Distributions may not behave as expected if they (1) target custom biomes from or (2) replace ores placed by these mods.");
        }
    }

    public void generateSurface(World var1, Random var2, int var3, int var4)
    {
        if (!CustomOreGenBase.hasFML())
        {
            ServerState.checkIfServerChanged(MinecraftServer.getServer(), var1.getWorldInfo());
            ServerState.onPopulateChunk(var1, var2, var3 / 16, var4 / 16);
        }
    }

    public void generateNether(World var1, Random var2, int var3, int var4)
    {
        this.generateSurface(var1, var2, var3, var4);
    }

    @SideOnly(Side.CLIENT)
    public boolean onTickInGame(float var1, Minecraft var2)
    {
        if (CustomOreGenBase.hasFML())
        {
            return false;
        }
        else
        {
            Minecraft var3 = Minecraft.getMinecraft();

            if (var3.isSingleplayer())
            {
                ServerState.checkIfServerChanged(MinecraftServer.getServer(), (WorldInfo)null);
            }

            if (var3.theWorld != null && ClientState.hasWorldChanged(var3.theWorld))
            {
                ClientState.onWorldChanged(var3.theWorld);
            }

            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2)
    {
        Minecraft var3 = Minecraft.getMinecraft();

        if (var3.theWorld != null && ClientState.hasWorldChanged(var3.theWorld))
        {
            ClientState.onWorldChanged(var3.theWorld);
        }

        CustomPacketPayload var4 = CustomPacketPayload.decodePacket(var2);

        if (var4 != null)
        {
            switch (mod_CustomOreGen$1.$SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[var4.type.ordinal()])
            {
                case 1:
                    ClientState.addDebuggingGeometry((GeometryData)var4.data);
                    break;

                case 2:
                    String var5 = (String)var4.data;

                    if ("_DISABLE_".equals(var5))
                    {
                        ClientState.dgEnabled = false;
                        return;
                    }

                    if (!CustomOreGenBase.hasForge())
                    {
                        var1.handleChat(new Packet3Chat("\u00a7cWarning: Minecraft Forge must be installed to view wireframes."));
                        return;
                    }

                    if (var5 != null)
                    {
                        ClientState$WireframeRenderMode var6 = null;
                        ClientState$WireframeRenderMode[] var7 = ClientState$WireframeRenderMode.values();
                        int var8 = var7.length;

                        for (int var9 = 0; var9 < var8; ++var9)
                        {
                            ClientState$WireframeRenderMode var10 = var7[var9];

                            if (var10.name().equalsIgnoreCase(var5))
                            {
                                var6 = var10;
                                break;
                            }
                        }

                        if (var6 != null)
                        {
                            ClientState.dgRenderingMode = var6;
                        }
                        else
                        {
                            var1.handleChat(new Packet3Chat("\u00a7cError: Invalid wireframe mode \'" + var5 + "\'"));
                        }
                    }
                    else
                    {
                        int var11 = ClientState.dgRenderingMode == null ? 0 : ClientState.dgRenderingMode.ordinal();
                        var11 = (var11 + 1) % ClientState$WireframeRenderMode.values().length;
                        ClientState.dgRenderingMode = ClientState$WireframeRenderMode.values()[var11];
                    }

                    var1.handleChat(new Packet3Chat("COG Client wireframe mode: " + ClientState.dgRenderingMode.name()));
                    break;

                case 3:
                    ClientState.clearDebuggingGeometry();
                    break;

                case 4:
                    if (!var3.isSingleplayer())
                    {
                        //ClientState.addMystcraftSymbol((MystcraftSymbolData)var4.data);
                    }

                    break;

                case 5:
                    var3.ingameGUI.getChatGUI().printChatMessage((String)var4.data);
                    break;

                default:
                    throw new RuntimeException("Unhandled client packet type " + var4.type);
            }
        }
    }

    public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2)
    {
        World var3 = var1.playerEntity == null ? null : var1.playerEntity.worldObj;
        ServerState.checkIfServerChanged(MinecraftServer.getServer(), var3 == null ? null : var3.getWorldInfo());
        CustomPacketPayload var4 = CustomPacketPayload.decodePacket(var2);

        if (var4 != null)
        {
            switch (mod_CustomOreGen$1.$SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[var4.type.ordinal()])
            {
                case 6:
                    GeometryData var5 = null;

                    if (var1.getPlayer().mcServer.getConfigurationManager().areCommandsAllowed(var1.getPlayer().username))
                    {
                        var5 = ServerState.getDebuggingGeometryData((GeometryRequestData)var4.data);
                    }

                    if (var5 == null)
                    {
                        (new CustomPacketPayload(CustomPacketPayload.PayloadType.DebuggingGeometryRenderMode, "_DISABLE_")).sendToClient(var1);
                    }
                    else
                    {
                        (new CustomPacketPayload(CustomPacketPayload.PayloadType.DebuggingGeometryData, var5)).sendToClient(var1);
                    }

                    break;

                default:
                    throw new RuntimeException("Unhandled server packet type " + var4.type);
            }
        }
    }

    public void onClientLogin(EntityPlayer var1)
    {
        World var2 = var1 == null ? null : var1.worldObj;
        ServerState.checkIfServerChanged(MinecraftServer.getServer(), var2 == null ? null : var2.getWorldInfo());

        if (var1 != null)
        {
            ServerState.onClientLogin((EntityPlayerMP)var1);
        }
    }
    
    //################################
    
    static class mod_CustomOreGen$1
{
    static final int[] $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType = new int[CustomPacketPayload.PayloadType.values().length];

    static
    {
        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload.PayloadType.DebuggingGeometryData.ordinal()] = 1;
        }
        catch (NoSuchFieldError var6)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload.PayloadType.DebuggingGeometryRenderMode.ordinal()] = 2;
        }
        catch (NoSuchFieldError var5)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload.PayloadType.DebuggingGeometryReset.ordinal()] = 3;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            //$SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload$PayloadType.MystcraftSymbolData.ordinal()] = 4;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload.PayloadType.CommandResponse.ordinal()] = 5;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$CustomPacketPayload$PayloadType[CustomPacketPayload.PayloadType.DebuggingGeometryRequest.ordinal()] = 6;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
    
}
