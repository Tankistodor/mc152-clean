package CustomOreGen;

import CustomOreGen.Server.ConsoleCommands;
import CustomOreGen.Server.ServerState;
import CustomOreGen.Server.WorldConfig;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import net.minecraft.src.ModLoader;

public class CustomOreGenBase
{
    public static final String version = "[1.4.6]v2";
    public static final String mcVersion = "[1.4.6,1.4.7]";
    public static String mcPath = "";
    public static Logger log = Logger.getLogger("STDOUT");
    private static int _hasFML = 0;
    private static int _hasForge = 0;
    private static int _hasMystcraft = 0;

    public static boolean isClassLoaded(String var0)
    {
        try
        {
            CustomOreGenBase.class.getClassLoader().loadClass(var0);
            return true;
        }
        catch (ClassNotFoundException var2)
        {
            return false;
        }
    }

    public static void onModPostLoad()
    {
        ConsoleCommands.createAndRegister();
        File var0 = new File(mcPath, "config/");

        if (unpackResourceFile("CustomOreGen_Config.xml", new File(var0, "CustomOreGen_Config.xml")))
        {
            File var1 = new File(var0, "CustomOreGen Standard Modules");
            var1.mkdir();
            String[] var2 = new String[] {"ExtraCaves.xml", "MinecraftOres.xml", "IndustrialCraft2.xml", "Forestry.xml", "Redpower2.xml"};
            String[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                String var6 = var3[var5];
                unpackResourceFile("CustomOreGen Standard Modules/" + var6, new File(var1, var6));
            }

            File var9 = new File(var0, "CustomOreGen Extra Modules");
            var9.mkdir();
        }

        hasMystcraft();
        WorldConfig var8 = null;

        while (var8 == null)
        {
            try
            {
                var8 = new WorldConfig();
            }
            catch (Exception var7)
            {
                if (!ServerState.onConfigError(var7))
                {
                    break;
                }

                var8 = null;
            }
        }
    }

    public static File unpackStandardModule(String var0)
    {
        File var1 = new File(mcPath, "config/CustomOreGen Standard Modules" + var0);

        if (!var1.exists())
        {
            unpackResourceFile("CustomOreGen Standard Modules/" + var0, var1);
        }

        return var1;
    }

    public static boolean unpackResourceFile(String var0, File var1)
    {
        if (var1.exists())
        {
            return false;
        }
        else
        {
            try
            {
                log.fine("Unpacking \'" + var0 + "\' ...");
                InputStream var2 = CustomOreGenBase.class.getClassLoader().getResourceAsStream(var0);
                BufferedOutputStream var3 = new BufferedOutputStream(new FileOutputStream(var1));
                byte[] var4 = new byte[1024];
                boolean var5 = false;
                int var7;

                while ((var7 = var2.read(var4)) >= 0)
                {
                    var3.write(var4, 0, var7);
                }

                var2.close();
                var3.close();
                return true;
            }
            catch (Exception var6)
            {
                throw new RuntimeException("Failed to unpack resource \'" + var0 + "\'", var6);
            }
        }
    }

    public static boolean hasFML()
    {
        if (_hasFML == 0)
        {
            _hasFML = isClassLoaded("cpw.mods.fml.common.FMLCommonHandler") ? 1 : -1;
        }

        return _hasFML == 1;
    }

    public static boolean hasForge()
    {
        if (_hasForge == 0)
        {
            _hasForge = isClassLoaded("net.minecraftforge.common.MinecraftForge") ? 1 : -1;
        }

        return _hasForge == 1;
    }

    public static boolean hasMystcraft()
    {
        /*if (_hasMystcraft == 0)
        {
            try
            {
                _hasMystcraft = -1;

                if (ModLoader.isModLoaded("Mystcraft"))
                {
                    MystcraftInterface.init();
                    _hasMystcraft = 1;
                }
            }
            catch (Throwable var1)
            {
                log.severe("COG Mystcraft interface appears to be incompatible with the installed version of Mystcraft.");
                log.throwing("MystcraftInterface", "checkInterface", var1);
            }
        }
         */
        return _hasMystcraft == 1;
    }
}
