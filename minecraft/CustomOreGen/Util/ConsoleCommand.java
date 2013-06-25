package CustomOreGen.Util;

import CustomOreGen.CustomPacketPayload;
import CustomOreGen.Config.ConfigParser;
import CustomOreGen.Server.ServerState;
import CustomOreGen.Util.ConsoleCommand$ArgName;
import CustomOreGen.Util.ConsoleCommand$ArgOptional;
import CustomOreGen.Util.ConsoleCommand$CommandDelegate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ConsoleCommand extends CommandBase
{
    private final Object _obj;
    private final Method _method;

    public static void sendText(ICommandSender var0, String var1)
    {
        if (var1 != null && var0 != null)
        {
            if (var0 instanceof EntityPlayerMP)
            {
                NetServerHandler var2 = ((EntityPlayerMP)var0).playerNetServerHandler;
                (new CustomPacketPayload(CustomPacketPayload.PayloadType.CommandResponse, var1)).sendToClient(var2);
            }
            else
            {
                var0.sendChatToPlayer(var1);
            }
        }
    }

    public static WorldServer getSenderWorld(ICommandSender var0)
    {
        World var1 = null;

        if (var0 instanceof Entity)
        {
            var1 = ((Entity)var0).worldObj;
        }
        else
        {
            if (!(var0 instanceof TileEntity))
            {
                return null;
            }

            var1 = ((TileEntity)var0).worldObj;
        }

        if (var1 == null)
        {
            return null;
        }
        else
        {
            int var2 = var1.provider.dimensionId;
            return MinecraftServer.getServer().worldServerForDimension(var2);
        }
    }

    public ConsoleCommand(Object var1, Method var2)
    {
        if ((var2.getModifiers() & 8) == 0 && var1 == null)
        {
            throw new RuntimeException("Method \'" + var2.getName() + "\' for class " + var2.getDeclaringClass().getSimpleName() + " requires an object instance");
        }
        else
        {
            this._obj = var1;
            this._method = var2;
        }
    }

    public ConsoleCommand(Method var1)
    {
        this((Object)null, var1);
    }

    public String getCommandName()
    {
        ConsoleCommand$CommandDelegate var1 = (ConsoleCommand$CommandDelegate)this._method.getAnnotation(ConsoleCommand$CommandDelegate.class);
        return var1 != null && var1.names() != null && var1.names().length > 0 ? var1.names()[0] : this._method.getName();
    }

    public List getCommandAliases()
    {
        ConsoleCommand$CommandDelegate var1 = (ConsoleCommand$CommandDelegate)this._method.getAnnotation(ConsoleCommand$CommandDelegate.class);
        return var1 != null && var1.names() != null && var1.names().length > 1 ? Arrays.asList(Arrays.copyOfRange(var1.names(), 1, var1.names().length)) : null;
    }

    public String getCommandUsage(ICommandSender var1)
    {
        return this.getCommandHelp(var1, false);
    }

    public String getCommandHelp(ICommandSender var1, boolean var2)
    {
        StringBuilder var3 = new StringBuilder("/" + this.getCommandName());
        Class[] var4 = this._method.getParameterTypes();
        Annotation[][] var5 = this._method.getParameterAnnotations();

        for (int var6 = 0; var6 < var4.length; ++var6)
        {
            Class var7 = var4[var6];
            String var8 = "arg" + var6 + ":" + var7.getSimpleName();
            boolean var9 = true;

            if (var5 != null && var6 < var5.length && var5[var6] != null)
            {
                Annotation[] var10 = var5[var6];
                int var11 = var10.length;

                for (int var12 = 0; var12 < var11; ++var12)
                {
                    Annotation var13 = var10[var12];

                    if (var13 instanceof ConsoleCommand$ArgName)
                    {
                        var8 = ((ConsoleCommand$ArgName)var13).name();
                    }
                    else if (var13 instanceof ConsoleCommand$ArgOptional)
                    {
                        var9 = false;
                    }
                }
            }

            if (!var7.isAssignableFrom(ICommandSender.class))
            {
                if (var7.isAssignableFrom(WorldServer.class))
                {
                    WorldServer var15 = getSenderWorld(var1);

                    if (var15 != null)
                    {
                        continue;
                    }

                    var7 = Integer.class;
                }
                else if (var7.isArray() && var6 == var4.length - 1 && this._method.isVarArgs())
                {
                    var8 = " ... ";
                    var9 = false;
                }

                var3.append(' ');

                if (var9)
                {
                    var3.append('<');
                }
                else
                {
                    var3.append('[');
                }

                var3.append(var8);

                if (var9)
                {
                    var3.append('>');
                }
                else
                {
                    var3.append(']');
                }
            }
        }

        ConsoleCommand$CommandDelegate var14 = (ConsoleCommand$CommandDelegate)this._method.getAnnotation(ConsoleCommand$CommandDelegate.class);

        if (var2 && var14 != null && var14.desc() != null && !var14.desc().isEmpty())
        {
            var3.append("\n  ");
            var3.append(var14.desc());
        }

        return var3.toString();
    }

    public void processCommand(ICommandSender var1, String[] var2)
    {
        Class[] var3 = this._method.getParameterTypes();
        Annotation[][] var4 = this._method.getParameterAnnotations();
        Object[] var5 = new Object[var3.length];

        try
        {
            int var6 = 0;

            for (int var7 = 0; var7 < var3.length; ++var7)
            {
                Class var8 = var3[var7];
                String var9 = "arg" + var7 + ":" + var8.getSimpleName();
                String var10 = null;
                boolean var11 = true;

                if (var4 != null && var7 < var4.length && var4[var7] != null)
                {
                    Annotation[] var12 = var4[var7];
                    int var13 = var12.length;

                    for (int var14 = 0; var14 < var13; ++var14)
                    {
                        Annotation var15 = var12[var14];

                        if (var15 instanceof ConsoleCommand$ArgName)
                        {
                            var9 = ((ConsoleCommand$ArgName)var15).name();
                        }
                        else if (var15 instanceof ConsoleCommand$ArgOptional)
                        {
                            var11 = false;
                            var10 = ((ConsoleCommand$ArgOptional)var15).defValue();

                            if (var10.isEmpty())
                            {
                                var10 = null;
                            }
                        }
                    }
                }

                if (var8.isAssignableFrom(ICommandSender.class))
                {
                    var5[var7] = var1;
                }
                else if (var8.isAssignableFrom(WorldServer.class))
                {
                    var5[var7] = getSenderWorld(var1);

                    if (var5[var7] == null)
                    {
                        Integer var20 = (Integer)ConfigParser.parseString(Integer.class, var6 < var2.length ? var2[var6++] : var10);
                        var5[var7] = var20 == null ? null : MinecraftServer.getServer().worldServerForDimension(var20.intValue());

                        if (var5[var7] == null && var11)
                        {
                            throw new SyntaxErrorException("Missing or invalid dimension ID for required argument \'" + var9 + "\'", new Object[0]);
                        }
                    }
                }
                else if (var8.isArray() && var7 == var3.length - 1 && this._method.isVarArgs())
                {
                    var5[var7] = Array.newInstance(var8.getComponentType(), var2.length - var6);

                    for (int var21 = 0; var21 < Array.getLength(var5[var7]); ++var21)
                    {
                        Object var22 = ConfigParser.parseString(var8.getComponentType(), var2[var6++]);
                        Array.set(var5[var7], var21, var22);
                    }
                }
                else
                {
                    var5[var7] = ConfigParser.parseString(var8, var6 < var2.length ? var2[var6++] : var10);

                    if (var5[var7] == null && var11)
                    {
                        throw new SyntaxErrorException("Missing required argument \'" + var9 + "\'", new Object[0]);
                    }
                }
            }

            if (var6 < var2.length)
            {
                throw new SyntaxErrorException("Too many arguments", new Object[0]);
            }
        }
        catch (IllegalArgumentException var18)
        {
            throw new SyntaxErrorException(var18.getMessage(), new Object[0]);
        }

        try
        {
            Object var19 = this._method.invoke(this._obj, var5);

            if (var19 != null)
            {
                sendText(var1, var19.toString());
            }
        }
        catch (InvocationTargetException var16)
        {
            throw new CommandException("Error: " + var16.getCause().getMessage(), new Object[0]);
        }
        catch (Exception var17)
        {
            throw new CommandException("Unkown Error: " + var17.getMessage(), new Object[0]);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        ConsoleCommand$CommandDelegate var2 = (ConsoleCommand$CommandDelegate)this._method.getAnnotation(ConsoleCommand$CommandDelegate.class);

        if (var2 == null || var2.isDebugging())
        {
            WorldServer var3 = getSenderWorld(var1);

            if (var3 != null)
            {
                ServerState.checkIfServerChanged(MinecraftServer.getServer(), var3.getWorldInfo());

                if (!ServerState.getWorldConfig(var3).debuggingMode)
                {
                    return false;
                }
            }
        }

        return super.canCommandSenderUseCommand(var1);
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        ConsoleCommand$CommandDelegate var1 = (ConsoleCommand$CommandDelegate)this._method.getAnnotation(ConsoleCommand$CommandDelegate.class);
        return var1 != null && var1.isCheat() ? 2 : 0;
    }
}
