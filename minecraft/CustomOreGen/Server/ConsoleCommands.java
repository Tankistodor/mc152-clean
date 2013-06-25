package CustomOreGen.Server;

import CustomOreGen.CustomOreGenBase;
import CustomOreGen.CustomPacketPayload;
import CustomOreGen.CustomPacketPayload.PayloadType;
import CustomOreGen.Server.IOreDistribution$StandardSettings;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.ConsoleCommand;
import CustomOreGen.Util.ConsoleCommand$ArgName;
import CustomOreGen.Util.ConsoleCommand$ArgOptional;
import CustomOreGen.Util.ConsoleCommand$CommandDelegate;
import CustomOreGen.Util.PDist;
import CustomOreGen.Util.PDist$Type;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;

public class ConsoleCommands
{
    public static ConsoleCommands createAndRegister()
    {
        CustomOreGenBase.log.finer("Registering Console command interface ...");
        ConsoleCommands var0 = new ConsoleCommands();
        Method[] var1 = ConsoleCommands.class.getMethods();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            Method var4 = var1[var3];

            if (var4.getAnnotation(ConsoleCommand$CommandDelegate.class) != null)
            {
                ModLoader.addCommand(new ConsoleCommand(var0, var4));
            }
        }

        return var0;
    }

    private static void resetClientGeometryCache()
    {
        (new CustomPacketPayload(PayloadType.DebuggingGeometryReset, (Serializable)null)).sendToAllClients();
    }

    private static void buildFieldValue(StringBuilder var0, String var1, String var2, String var3, Object var4)
    {
        var0.append('\n');
        var0.append(var1);
        var0.append(var2);
        var0.append(" = ");

        if (var4 != null)
        {
            var0.append('[');
            var0.append(var4.getClass().getSimpleName());
            var0.append("] ");
            var0.append(var4);

            if (var3 != null)
            {
                String[] var5;
                int var6;
                int var7;
                String var8;

                if (var4 instanceof BlockDescriptor)
                {
                    var5 = ((BlockDescriptor)var4).toDetailedString();
                    var6 = var5.length;

                    for (var7 = 0; var7 < var6; ++var7)
                    {
                        var8 = var5[var7];
                        var0.append('\n');
                        var0.append(var1);
                        var0.append("  ");
                        var0.append(var8);
                    }
                }
                else if (var4 instanceof BiomeDescriptor)
                {
                    var5 = ((BiomeDescriptor)var4).toDetailedString();
                    var6 = var5.length;

                    for (var7 = 0; var7 < var6; ++var7)
                    {
                        var8 = var5[var7];
                        var0.append('\n');
                        var0.append(var1);
                        var0.append("  ");
                        var0.append(var8);
                    }
                }
            }
        }
        else
        {
            var0.append("[null]");
        }

        if (var3 != null)
        {
            var0.append('\n');
            var0.append(var1);
            var0.append("  ");
            var0.append("\u00a77");
            var0.append(var3);
            var0.append("\u00a7r");
        }
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Dumps a summary of a distribution or a specific distribution setting to the console."
    )
    public String cogInfo(
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var1,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) @ConsoleCommand$ArgOptional String var2,
            @ConsoleCommand$ArgName(
                    name = "setting"
            ) @ConsoleCommand$ArgOptional String var3,
            @ConsoleCommand$ArgName(
                    name = "detail"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "false"
            ) boolean var4)
    {
        StringBuilder var5 = new StringBuilder();
        WorldConfig var6 = ServerState.getWorldConfig(var1);
        Collection var7 = var6.getOreDistributions();
        Collection var8 = var2 != null && !var2.isEmpty() ? var6.getOreDistributions(var2) : null;
        var5.append("CustomOreGen [1.4.6]v2 (");

        if (var6.world != null)
        {
            var5.append("Dim " + var6.world.provider.dimensionId + ", ");
        }

        if (var8 != null)
        {
            var5.append(var8.size() + "/");
        }

        var5.append(var7.size() + " distributions");
        var5.append(')');

        if (var8 != null && var8.size() > 0)
        {
            var5.append(':');
            Pattern var9 = var3 != null && !var3.isEmpty() ? Pattern.compile(var3, 2) : null;
            Iterator var10 = var8.iterator();

            while (var10.hasNext())
            {
                IOreDistribution var11 = (IOreDistribution)var10.next();
                var5.append("\n " + var11);

                if (var9 != null)
                {
                    Map var12 = var11.getDistributionSettings();
                    LinkedHashMap var13 = new LinkedHashMap();
                    Iterator var14 = var12.entrySet().iterator();
                    Entry var15;

                    while (var14.hasNext())
                    {
                        var15 = (Entry)var14.next();

                        if (var9.matcher((CharSequence)var15.getKey()).matches())
                        {
                            Object var16 = var11.getDistributionSetting((String)var15.getKey());
                            var13.put(var15.getKey(), var16);
                        }
                    }

                    var5.append(" (" + var13.size() + "/" + var12.size() + " settings)");

                    if (var13.size() > 0)
                    {
                        var5.append(':');
                    }

                    var14 = var13.entrySet().iterator();

                    while (var14.hasNext())
                    {
                        var15 = (Entry)var14.next();
                        buildFieldValue(var5, "  ", (String)var15.getKey(), var4 ? (String)var12.get(var15.getKey()) : null, var15.getValue());
                    }
                }
            }
        }

        CustomOreGenBase.log.finer(var5.toString());
        return var5.toString();
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets the global wireframe rendering mode.  Omit mode to cycle through modes."
    )
    public void cogWireframeMode(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "None|Polygon|Wireframe|WireframeOverlay"
            ) @ConsoleCommand$ArgOptional String var2)
    {
        if (var1 instanceof EntityPlayerMP)
        {
            NetServerHandler var3 = ((EntityPlayerMP)var1).playerNetServerHandler;
            (new CustomPacketPayload(PayloadType.DebuggingGeometryRenderMode, var2)).sendToClient(var3);
        }
        else
        {
            throw new IllegalArgumentException("/cogWireframeMode is a client-side command and may only be used by a player.");
        }
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Clears cached structure information.  Omit distribution name to clear all distributions."
    )
    public String cogClear(
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var1,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = ".*"
            ) String var2)
    {
        int var3 = 0;
        Iterator var4 = ServerState.getWorldConfig(var1).getOreDistributions(var2).iterator();

        while (var4.hasNext())
        {
            IOreDistribution var5 = (IOreDistribution)var4.next();
            ++var3;
            var5.clear();
            var5.validate();
        }

        resetClientGeometryCache();
        return "Cleared " + var3 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Repopulates chunks in the specified range around the player.  Omit distribution name to repopulate all distributions."
    )
    public String cogPopulate(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "chunkRange"
            ) int var3,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = ".*"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "centerX"
            ) @ConsoleCommand$ArgOptional Integer var5,
            @ConsoleCommand$ArgName(
                    name = "centerZ"
            ) @ConsoleCommand$ArgOptional Integer var6)
    {
        WorldConfig var7 = ServerState.getWorldConfig(var2);
        Collection var8 = var7.getOreDistributions(var4);
        ChunkCoordinates var9 = var1.getPlayerCoordinates();
        int var10 = var5 == null ? var9.posX : var5.intValue();
        int var11 = var6 == null ? var9.posZ : var6.intValue();

        for (int var12 = (var10 >> 4) - var3; var12 <= (var10 >> 4) + var3; ++var12)
        {
            for (int var13 = (var11 >> 4) - var3; var13 <= (var11 >> 4) + var3; ++var13)
            {
                var7.world.getChunkFromChunkCoords(var12, var13);
                ServerState.populateDistributions(var8, var7.world, var12, var13);
            }
        }

        return "Populated " + var8.size() + " distributions in " + (2 * var3 + 1) * (2 * var3 + 1) + " chunk(s) around (" + var10 + ",0," + var11 + ")";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets the parent distribution.  Omit parent name to clear parent distribution."
    )
    public String cogParent(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "parent"
            ) @ConsoleCommand$ArgOptional String var4)
    {
        WorldConfig var5 = ServerState.getWorldConfig(var2);
        IOreDistribution var6 = null;

        if (var4 != null)
        {
            Collection var7 = var5.getOreDistributions(var4);

            if (var7.isEmpty())
            {
                throw new IllegalArgumentException("Parent name \'" + var4 + "\' does not match any distributions.");
            }

            if (var7.size() > 1)
            {
                throw new IllegalArgumentException("Parent name \'" + var4 + "\' is ambiguous (matches " + var7.size() + " distributions).");
            }

            var6 = (IOreDistribution)var7.iterator().next();
        }

        int var12 = 0;
        Iterator var8 = var5.getOreDistributions(var3).iterator();

        while (var8.hasNext())
        {
            IOreDistribution var9 = (IOreDistribution)var8.next();

            try
            {
                var9.setDistributionSetting(IOreDistribution$StandardSettings.Parent.name(), var6);
                ++var12;
                var9.clear();
                var9.validate();
            }
            catch (Exception var11)
            {
                ConsoleCommand.sendText(var1, "\u00a7c" + var11.getMessage());
            }
        }

        resetClientGeometryCache();
        return "Changed parent for " + var12 + " distributions";
    }

    private static int changeDescriptor(String var0, ICommandSender var1, WorldServer var2, String var3, String var4, float var5, boolean var6)
    {
        int var7 = 0;
        Iterator var8 = ServerState.getWorldConfig(var2).getOreDistributions(var3).iterator();

        while (var8.hasNext())
        {
            IOreDistribution var9 = (IOreDistribution)var8.next();

            try
            {
                Object var10 = var9.getDistributionSetting(var0);

                if (var10 == null)
                {
                    throw new IllegalArgumentException("Distribution \'" + var9 + "\' does not support descriptor " + var0 + ".");
                }

                if (var10 instanceof BlockDescriptor)
                {
                    BlockDescriptor var11 = (BlockDescriptor)var10;

                    if (var6)
                    {
                        var11.clear();
                    }

                    var11.add(var4, var5);
                    ++var7;
                }
                else
                {
                    if (!(var10 instanceof BiomeDescriptor))
                    {
                        throw new IllegalArgumentException("Setting " + var0 + " on Distribution \'" + var9 + "\' is not a descriptor.");
                    }

                    BiomeDescriptor var13 = (BiomeDescriptor)var10;

                    if (var6)
                    {
                        var13.clear();
                    }

                    var13.add(var4, var5);
                    ++var7;
                }

                var9.clear();
                var9.validate();
            }
            catch (Exception var12)
            {
                ConsoleCommand.sendText(var1, "\u00a7c" + var12.getMessage());
            }
        }

        resetClientGeometryCache();
        return var7;
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Adds an ore block."
    )
    public String cogAddOreBlock(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "block"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.OreBlock.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, false);
        return "Added ore block for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets the ore block (clearing any previous ore blocks)."
    )
    public String cogSetOreBlock(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "block"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.OreBlock.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, true);
        return "Set ore block for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Adds a replaceable block."
    )
    public String cogAddReplaceable(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "block"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.ReplaceableBlock.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, false);
        return "Added replaceable block for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets the replaceable block (clearing any previous replaceable blocks)."
    )
    public String cogSetReplaceable(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "block"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.ReplaceableBlock.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, true);
        return "Set replaceable block for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Adds a target biome."
    )
    public String cogAddBiome(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "biome"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.TargetBiome.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, false);
        return "Added biome for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets the target biome (clearing any previous biomes)."
    )
    public String cogSetBiome(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "biome"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "weight"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "1"
            ) float var5)
    {
        String var6 = IOreDistribution$StandardSettings.TargetBiome.name();
        int var7 = changeDescriptor(var6, var1, var2, var3, var4, var5, true);
        return "Set biome for " + var7 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets a distribution setting.  Setting names are the same as the <Setting> names in the config file.  Range and Type are optional (default to 0 and \'uniform\', respectively)."
    )
    public String cogSetting(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "setting"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "average"
            ) float var5,
            @ConsoleCommand$ArgName(
                    name = "range"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "0"
            ) float var6,
            @ConsoleCommand$ArgName(
                    name = "type"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "uniform"
            ) PDist$Type var7)
    {
        int var8 = 0;
        Iterator var9 = ServerState.getWorldConfig(var2).getOreDistributions(var3).iterator();

        while (var9.hasNext())
        {
            IOreDistribution var10 = (IOreDistribution)var9.next();

            try
            {
                var10.setDistributionSetting(var4, new PDist(var5, var6, var7));
                ++var8;
                var10.clear();
                var10.validate();
            }
            catch (Exception var12)
            {
                ConsoleCommand.sendText(var1, "\u00a7c" + var12.getMessage());
            }
        }

        resetClientGeometryCache();
        return "Changed \'" + var4 + "\' for " + var8 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Sets a simple numeric/boolean/string/enum setting.  Setting names are the same as the corresponding attributes in the config file."
    )
    public String cogSettingEx(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "distribution"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "setting"
            ) String var4,
            @ConsoleCommand$ArgName(
                    name = "value"
            ) String var5)
    {
        int var6 = 0;
        Iterator var7 = ServerState.getWorldConfig(var2).getOreDistributions(var3).iterator();

        while (var7.hasNext())
        {
            IOreDistribution var8 = (IOreDistribution)var7.next();

            try
            {
                var8.setDistributionSetting(var4, var5);
                ++var6;
                var8.clear();
                var8.validate();
            }
            catch (Exception var10)
            {
                ConsoleCommand.sendText(var1, "\u00a7c" + var10.getMessage());
            }
        }

        resetClientGeometryCache();
        return "Changed \'" + var4 + "\' for " + var6 + " distributions";
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Dumps a summary of an Option to the console."
    )
    public String cogOptionInfo(
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var1,
            @ConsoleCommand$ArgName(
                    name = "option"
            ) @ConsoleCommand$ArgOptional String var2,
            @ConsoleCommand$ArgName(
                    name = "detail"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "false"
            ) boolean var3)
    {
        StringBuilder var4 = new StringBuilder();
        WorldConfig var5 = ServerState.getWorldConfig(var1);
        Collection var6 = var5.getConfigOptions();
        Collection var7 = var2 != null && !var2.isEmpty() ? var5.getConfigOptions(var2) : null;
        var4.append("CustomOreGen [1.4.6]v2 (");

        if (var5.world != null)
        {
            var4.append("Dim " + var5.world.provider.dimensionId + ", ");
        }

        if (var7 != null)
        {
            var4.append(var7.size() + "/");
        }

        var4.append(var6.size() + " options");
        var4.append(')');

        if (var7 != null && var7.size() > 0)
        {
            var4.append(':');
            Iterator var8 = var7.iterator();

            while (var8.hasNext())
            {
                ConfigOption var9 = (ConfigOption)var8.next();
                buildFieldValue(var4, "  ", var9.getName(), var3 ? var9.getDescription() : null, var9.getValue());
            }
        }

        CustomOreGenBase.log.finer(var4.toString());
        return var4.toString();
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Set an option value for the current dimension.  The change lasts until the world configuration is reloaded."
    )
    public String cogOption(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "option"
            ) String var3,
            @ConsoleCommand$ArgName(
                    name = "value"
            ) String var4)
    {
        WorldConfig var5 = ServerState.getWorldConfig(var2);
        Collection var6 = var5.getConfigOptions(var3);
        int var7 = 0;
        Iterator var8 = var6.iterator();

        while (var8.hasNext())
        {
            ConfigOption var9 = (ConfigOption)var8.next();

            if (var9.setValue(var4))
            {
                ++var7;
            }
            else
            {
                ConsoleCommand.sendText(var1, "\u00a7cInvalid value \'" + var4 + "\' for Option \'" + var9.getName() + "\'");
            }
        }

        WorldConfig.loadedOptionOverrides[2] = var5.getConfigOptions();
        ServerState.clearWorldConfig(var2);
        var5 = ServerState.getWorldConfig(var2);
        String var10 = var5.dimensionDir.toString();
        WorldConfig.loadedOptionOverrides[2] = null;
        resetClientGeometryCache();
        return "Changed " + var7 + " options for " + var10;
    }

    @ConsoleCommand$CommandDelegate(
            isDebugging = false,
            desc = "Enabled or disable debugging mode for the current dimension."
    )
    public String cogEnableDebugging(ICommandSender var1,
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var2,
            @ConsoleCommand$ArgName(
                    name = "enable"
            )
            @ConsoleCommand$ArgOptional(
                    defValue = "true"
            ) boolean var3)
    {
        return this.cogOption(var1, var2, "debugMode", Boolean.toString(var3));
    }

    @ConsoleCommand$CommandDelegate(
            desc = "Reloads the world configuration from disk.  This will reset any changes made via console commands."
    )
    public String cogLoadConfig(
            @ConsoleCommand$ArgName(
                    name = "dimension"
            ) WorldServer var1)
    {
        ServerState.clearWorldConfig(var1);
        String var2 = ServerState.getWorldConfig(var1).dimensionDir.toString();
        resetClientGeometryCache();
        return "Reloaded config data for " + var2;
    }
}
