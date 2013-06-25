package CustomOreGen.Server;

import CustomOreGen.CustomOreGenBase;
import CustomOreGen.CustomPacketPayload;
import CustomOreGen.CustomPacketPayload.PayloadType;
import CustomOreGen.GeometryData;
import CustomOreGen.GeometryRequestData;
import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOpenMenuButton;
import CustomOreGen.Util.GeometryStream;
import CustomOreGen.Util.SimpleProfiler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Frame;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;

public class ServerState
{
    private static MinecraftServer _server = null;
    private static Map _worldConfigs = new HashMap();
    private static Map _populatedChunks = new HashMap();
    private static Object _optionsGuiButton = null;

    private static boolean isChunkSavedPopulated(World var0, int var1, int var2)
    {
        File var3 = getWorldConfig(var0).dimensionDir;
        DataInputStream var4 = RegionFileCache.getChunkInputStream(var3, var1, var2);

        if (var4 != null)
        {
            try
            {
                NBTTagCompound var5 = CompressedStreamTools.read(var4);

                if (var5.hasKey("Level") && var5.getCompoundTag("Level").getBoolean("TerrainPopulated"))
                {
                    return true;
                }
            }
            catch (IOException var6)
            {
                ;
            }
        }

        return false;
    }

    private static void patchBiomeDecorator(BiomeDecorator var0)
    {
        try
        {
            WorldGenerator var1 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 10);
            WorldGenerator var2 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 11);
            WorldGenerator var3 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 12);
            WorldGenerator var4 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 13);
            WorldGenerator var5 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 14);
            WorldGenerator var6 = (WorldGenerator)ModLoader.getPrivateValue(BiomeDecorator.class, var0, 15);
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 10, new WorldGenEmpty(var1));
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 11, new WorldGenEmpty(var2));
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 12, new WorldGenEmpty(var3));
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 13, new WorldGenEmpty(var4));
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 14, new WorldGenEmpty(var5));
            ModLoader.setPrivateValue(BiomeDecorator.class, var0, 15, new WorldGenEmpty(var6));
        }
        catch (Exception var7)
        {
            CustomOreGenBase.log.throwing("CustomOreGenBase", "patchBiomeDecorator", var7);
        }
    }

    public static WorldConfig getWorldConfig(World var0)
    {
        WorldConfig var1 = (WorldConfig)_worldConfigs.get(var0);

        while (var1 == null)
        {
            try
            {
                var1 = new WorldConfig(var0);
                validateOptions(var1.getConfigOptions(), true);
                validateDistributions(var1.getOreDistributions(), true);

                /*if (CustomOreGenBase.hasMystcraft())
                {
                    Iterator var2 = var1.getMystcraftSymbols().iterator();

                    while (var2.hasNext())
                    {
                        MystcraftSymbolData var3 = (MystcraftSymbolData)var2.next();
                        MystcraftInterface.appyAgeSpecificCOGSymbol(var3);
                    }
                }*/
            }
            catch (Exception var4)
            {
                if (onConfigError(var4))
                {
                    var1 = null;
                    continue;
                }

                var1 = WorldConfig.createEmptyConfig();
            }

            _worldConfigs.put(var0, var1);
        }

        return var1;
    }

    public static void clearWorldConfig(World var0)
    {
        _worldConfigs.remove(var0);
    }

    public static boolean onConfigError(Throwable var0)
    {
        CustomOreGenBase.log.throwing("CustomOreGen.ServerState", "loadWorldConfig", var0);
        Frame[] var1 = Frame.getFrames();

        if (var1 != null && var1.length > 0)
        {
            switch ((new ConfigErrorDialog()).showDialog(var1[0], var0))
            {
                case 1:
                    return true;

                case 2:
                    return false;
            }
        }

        ModLoader.throwException((String)null, var0);
        return false;
    }

    public static void validateDistributions(Collection var0, boolean var1) throws IllegalStateException
    {
        Iterator var2 = var0.iterator();

        while (var2.hasNext())
        {
            IOreDistribution var3 = (IOreDistribution)var2.next();

            if (!var3.validate() && var1)
            {
                var2.remove();
            }
        }
    }

    public static void validateOptions(Collection var0, boolean var1)
    {
        Iterator var2 = var0.iterator();

        while (var2.hasNext())
        {
            ConfigOption var3 = (ConfigOption)var2.next();

            if (var1 && var3 instanceof ConfigOption$DisplayGroup)
            {
                var2.remove();
            }
        }
    }

    public static void populateDistributions(Collection var0, World var1, int var2, int var3)
    {
        SimpleProfiler.globalProfiler.startSection("Populate");
        BlockSand.fallInstantly = true;
        var1.scheduledUpdatesAreImmediate = true;
        Iterator var4 = var0.iterator();

        while (var4.hasNext())
        {
            IOreDistribution var5 = (IOreDistribution)var4.next();
            var5.generate(var1, var2, var3);
            var5.populate(var1, var2, var3);
            var5.cull();
        }

        var1.scheduledUpdatesAreImmediate = false;
        BlockSand.fallInstantly = false;
        SimpleProfiler.globalProfiler.endSection();
    }

    public static GeometryData getDebuggingGeometryData(GeometryRequestData var0)
    {
        if (_server == null)
        {
            return null;
        }
        else if (var0.world == null)
        {
            return null;
        }
        else
        {
            WorldConfig var1 = getWorldConfig(var0.world);

            if (!var1.debuggingMode)
            {
                return null;
            }
            else
            {
                int var2 = 0;
                LinkedList var3 = new LinkedList();
                IOreDistribution var5;

                for (Iterator var4 = var1.getOreDistributions().iterator(); var4.hasNext(); var5.cull())
                {
                    var5 = (IOreDistribution)var4.next();
                    var5.generate(var0.world, var0.chunkX, var0.chunkZ);
                    GeometryStream var6 = var5.getDebuggingGeometry(var0.world, var0.chunkX, var0.chunkZ);

                    if (var6 != null)
                    {
                        var3.add(var6);
                        var2 += var6.getStreamDataSize();
                    }
                }

                return new GeometryData(var0, var3);
            }
        }
    }

    public static void onPopulateChunk(World var0, Random var1, int var2, int var3)
    {
        WorldConfig var4 = getWorldConfig(var0);
        Object var5 = null;
        Integer var6 = Integer.valueOf(var0.provider.dimensionId);
        var5 = (Map)_populatedChunks.get(var6);

        if (var5 == null)
        {
            var5 = new HashMap();
            _populatedChunks.put(var6, var5);
        }

        ChunkCoordIntPair var7 = new ChunkCoordIntPair(var2 >>> 4, var3 >>> 4);
        int[] var8 = (int[])((Map)var5).get(var7);

        if (var8 == null)
        {
            var8 = new int[16];
            ((Map)var5).put(var7, var8);
        }

        var8[var2 & 15] |= 65537 << (var3 & 15);
        int var16 = (var4.deferredPopulationRange + 15) / 16;
        int var17 = 4 * var16 * (var16 + 1) + 1;

        for (int var18 = var2 - var16; var18 <= var2 + var16; ++var18)
        {
            for (int var9 = var3 - var16; var9 <= var3 + var16; ++var9)
            {
                int var10 = 0;

                for (int var11 = var18 - var16; var11 <= var18 + var16; ++var11)
                {
                    for (int var12 = var9 - var16; var12 <= var9 + var16; ++var12)
                    {
                        ChunkCoordIntPair var13 = new ChunkCoordIntPair(var11 >>> 4, var12 >>> 4);
                        int[] var14 = (int[])((Map)var5).get(var13);

                        if (var14 == null)
                        {
                            var14 = new int[16];
                            ((Map)var5).put(var13, var14);
                        }

                        if ((var14[var11 & 15] >>> (var12 & 15) & 65536) == 0)
                        {
                            boolean var15 = isChunkSavedPopulated(var0, var11, var12);
                            var14[var11 & 15] |= (var15 ? 65537 : 65536) << (var12 & 15);
                        }

                        if ((var14[var11 & 15] >>> (var12 & 15) & 1) != 0)
                        {
                            ++var10;
                        }
                    }
                }

                if (var10 == var17)
                {
                    populateDistributions(var4.getOreDistributions(), var0, var18, var9);
                }
            }
        }
    }

    public static boolean checkIfServerChanged(MinecraftServer var0, WorldInfo var1)
    {
        if (_server == var0)
        {
            return false;
        }
        else
        {
            if (var0 != null && var1 == null)
            {
                if (var0.worldServers == null)
                {
                    return false;
                }

                WorldServer[] var2 = var0.worldServers;
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    WorldServer var5 = var2[var4];

                    if (var5 != null)
                    {
                        var1 = var5.getWorldInfo();
                    }

                    if (var1 != null)
                    {
                        break;
                    }
                }

                if (var1 == null)
                {
                    return false;
                }
            }

            onServerChanged(var0, var1);
            return true;
        }
    }

    public static void onServerChanged(MinecraftServer var0, WorldInfo var1)
    {
        _worldConfigs.clear();
        WorldConfig.loadedOptionOverrides[1] = WorldConfig.loadedOptionOverrides[2] = null;
        _populatedChunks.clear();

        if (CustomOreGenBase.hasMystcraft())
        {
            //MystcraftInterface.clearCOGSymbols();
        }

        _server = var0;
        CustomOreGenBase.log.finer("Server world changed to " + var1.getWorldName());
        BiomeGenBase[] var2 = BiomeGenBase.biomeList;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            BiomeGenBase var5 = var2[var4];

            if (var5 != null && var5.theBiomeDecorator != null)
            {
                patchBiomeDecorator(var5.theBiomeDecorator);
            }
        }

        File var8 = null;
        ISaveFormat var9 = _server.getActiveAnvilConverter();

        if (var9 != null && var9 instanceof SaveFormatOld)
        {
            //var8 = (File)ModLoader.getPrivateValue(SaveFormatOld.class, var9, 0);
            
        }

        var8 = new File(var8, _server.getFolderName());
        WorldConfig var10 = null;

        while (var10 == null)
        {
            try
            {
                var10 = new WorldConfig(var1, var8);
                validateOptions(var10.getConfigOptions(), false);
                validateDistributions(var10.getOreDistributions(), false);

                /*if (CustomOreGenBase.hasMystcraft())
                {
                    Iterator var11 = var10.getMystcraftSymbols().iterator();

                    while (var11.hasNext())
                    {
                        MystcraftSymbolData var6 = (MystcraftSymbolData)var11.next();
                        MystcraftInterface.addCOGSymbol(var6);
                    }
                }*/
            }
            catch (Exception var7)
            {
                if (!onConfigError(var7))
                {
                    break;
                }

                var10 = null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void onWorldCreationMenuTick(GuiCreateWorld var0)
    {
        if (var0 == null)
        {
            _optionsGuiButton = null;
        }
        else
        {
            if (_optionsGuiButton == null)
            {
                WorldConfig.loadedOptionOverrides[0] = null;
                GuiCustomOreGenSettings var1 = new GuiCustomOreGenSettings(var0);
                _optionsGuiButton = new GuiCustomOreGenSettings$GuiOpenMenuButton(var0, 99, 0, 0, 150, 20, "Custom Ore Generation...", var1);
            }

            GuiCustomOreGenSettings$GuiOpenMenuButton var3 = (GuiCustomOreGenSettings$GuiOpenMenuButton)_optionsGuiButton;
            Collection var2 = (Collection)ModLoader.getPrivateValue(GuiScreen.class, var0, 4);

            if (!var2.contains(var3))
            {
                var3.xPosition = (var0.width - var3.getWidth()) / 2;
                var3.yPosition = 165;
                var2.add(var3);
            }

            var3.drawButton = !((Boolean)ModLoader.getPrivateValue(GuiCreateWorld.class, var0, 11)).booleanValue();
        }
    }

    public static void onClientLogin(EntityPlayerMP var0)
    {
        /*if (var0.worldObj != null && CustomOreGenBase.hasMystcraft())
        {
            Iterator var1 = getWorldConfig(var0.worldObj).getMystcraftSymbols().iterator();

            while (var1.hasNext())
            {
                MystcraftSymbolData var2 = (MystcraftSymbolData)var1.next();
                (new CustomPacketPayload(CustomPacketPayload$PayloadType.MystcraftSymbolData, var2)).sendToClient(var0.playerNetServerHandler);
            }
        }*/
    }
}
