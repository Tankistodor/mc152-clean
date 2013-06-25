package CustomOreGen.Server;

import CustomOreGen.CustomOreGenBase;
import CustomOreGen.ForgeInterface;
import CustomOreGen.Config.ConfigParser;
import CustomOreGen.Config.PropertyIO;
import CustomOreGen.Util.CIStringMap;
import CustomOreGen.Util.MapCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;

import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.xml.sax.SAXException;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class WorldConfig
{
    public static Collection[] loadedOptionOverrides = new Collection[3];
    public final World world;
    public final WorldInfo worldInfo;
    public final File globalConfigDir;
    public final File worldBaseDir;
    public final File dimensionDir;
    public int deferredPopulationRange;
    public boolean debuggingMode;
    public boolean vanillaOreGen;
    private Collection oreDistributions;
    private Map configOptions;
    private Map loadedOptions;
    private Map worldProperties;
    private Map cogSymbolData;

    public static WorldConfig createEmptyConfig()
    {
        try
        {
            return new WorldConfig((File)null, (WorldInfo)null, (File)null, (World)null, (File)null);
        }
        catch (Exception var1)
        {
            throw new RuntimeException(var1);
        }
    }

    public WorldConfig() throws IOException, SAXException, ParserConfigurationException
    {
        this(new File(CustomOreGenBase.mcPath, "config"), (WorldInfo)null, (File)null, (World)null, (File)null);
    }

    public WorldConfig(WorldInfo var1, File var2) throws IOException, SAXException, ParserConfigurationException
    {
        this(new File(CustomOreGenBase.mcPath, "config"), var1, var2, (World)null, (File)null);
    }

    public WorldConfig(World var1) throws IOException, SAXException, ParserConfigurationException
    {
        this(new File(CustomOreGenBase.mcPath, "config"), (WorldInfo)null, (File)null, var1, (File)null);
    }

    private WorldConfig(File var1, WorldInfo var2, File var3, World var4, File var5) throws IOException, SAXException, ParserConfigurationException
    {
        this.deferredPopulationRange = 0;
        this.debuggingMode = false;
        this.vanillaOreGen = false;
        this.oreDistributions = new LinkedList();
        this.configOptions = new CIStringMap(new LinkedHashMap());
        this.loadedOptions = new CIStringMap(new LinkedHashMap());
        this.worldProperties = new CIStringMap(new LinkedHashMap());
        this.cogSymbolData = new CIStringMap(new LinkedHashMap());
        String var6;

        if (var4 != null)
        {
            /*if (var4.getSaveHandler() != null && var4.getSaveHandler() instanceof SaveHandler)
            {
            	var3 = (File)ModLoader.getPrivateValue(SaveHandler.class, (SaveHandler) var4.getSaveHandler(), 1);
            }
            else
            {
                var3 = null;
            }*/
        	
        	if (CustomOreGenBase.hasForge()) {
        	if (ForgeInterface.getWorldDimensionFolder(var4) != null) {
        		var3 = new File(CustomOreGenBase.mcPath+"/saves/"+var4.getWorldInfo().getWorldName()+"/"+ForgeInterface.getWorldDimensionFolder(var4));
        	} else {
        		var3 = new File(CustomOreGenBase.mcPath+"/saves/"+var4.getWorldInfo().getWorldName());
        	}}

            var6 = var4.provider.dimensionId == 0 ? null : "DIM" + var4.provider.dimensionId;

            if (CustomOreGenBase.hasForge())
            {
                var6 = ForgeInterface.getWorldDimensionFolder(var4);
            }

            if (var6 == null)
            {
                var5 = var3;
            }
            else if (var3 == null)
            {
                var5 = new File(var6);
            }
            else
            {
                var5 = new File(var3, var6);
            }

            var2 = var4.getWorldInfo();
        }

        this.world = var4;
        this.worldInfo = var2;
        populateWorldProperties(this.worldProperties, var4, var2);
        this.worldBaseDir = var3;
        this.dimensionDir = var5;
        this.globalConfigDir = var1;

        if (var5 != null)
        {
            CustomOreGenBase.log.finer("Loading config data for dimension \'" + var5 + "\' ...");
        }
        else if (var3 != null)
        {
            CustomOreGenBase.log.finer("Loading config data for world \'" + var3 + "\' ...");
        }
        else
        {
            if (var1 == null)
            {
                return;
            }

            CustomOreGenBase.log.finer("Loading global config \'" + var1 + "\' ...");
        }

        var6 = null;
        File[] var7 = new File[3];
        int var8 = this.buildFileList("CustomOreGen_Config.xml", var7);

        if (var8 < 0)
        {
            if (var5 != null)
            {
                CustomOreGenBase.log.warning("No config file found for dimension \'" + var5 + "\' at any scope!");
            }
            else if (var3 != null)
            {
                CustomOreGenBase.log.finer("No config file found for world \'" + var3 + "\' at any scope.");
            }
            else
            {
                CustomOreGenBase.log.finer("No global config file found.");
            }
        }
        else
        {
            File var16 = var7[var8];
            File[] var9 = new File[3];
            int var10 = this.buildFileList("CustomOreGen_Options.txt", var9);
            File var11 = var9[Math.max(Math.max(1, var8), var10)];
            ConfigOption var14;

            for (int var12 = var8; var12 < var9.length; ++var12)
            {
                if (var9[var12] != null && var9[var12].exists())
                {
                    PropertyIO.load(this.loadedOptions, new FileInputStream(var9[var12]));
                }

                if (loadedOptionOverrides[var12] != null)
                {
                    Iterator var13 = loadedOptionOverrides[var12].iterator();

                    while (var13.hasNext())
                    {
                        var14 = (ConfigOption)var13.next();

                        if (var14.getValue() != null)
                        {
                            this.loadedOptions.put(var14.getName(), var14.getValue().toString());
                        }
                    }
                }
            }

            (new ConfigParser(this)).parseFile(var16);
            ConfigOption var20;

            if (var11 != null && !var11.exists())
            {
                Iterator var17 = this.configOptions.values().iterator();

                while (var17.hasNext())
                {
                    var20 = (ConfigOption)var17.next();

                    if (var20.getValue() != null)
                    {
                        this.loadedOptions.put(var20.getName(), var20.getValue().toString());
                    }
                }

                if ((var3 != null) && var3.exists()) { var11.createNewFile(); 
                String var19 = "CustomOreGen [1.5.2]tankemod Config Options";
                PropertyIO.save(this.loadedOptions, new FileOutputStream(var11), var19); }
            }

            ConfigOption var21 = (ConfigOption)this.configOptions.get("deferredPopulationRange");

            if (var21 != null && var21 instanceof NumericOption)
            {
                Double var18 = (Double)var21.getValue();
                this.deferredPopulationRange = var18 != null && var18.doubleValue() > 0.0D ? (int)Math.ceil(var18.doubleValue()) : 0;
            }
            else
            {
                CustomOreGenBase.log.warning("Numeric Option \'" + var21 + "\' not found in config file - defaulting to \'" + this.deferredPopulationRange + "\'.");
            }

            var20 = (ConfigOption)this.configOptions.get("debugMode");

            if (var20 != null && var20 instanceof ChoiceOption)
            {
                String var22 = (String)var20.getValue();
                this.debuggingMode = var22 == null ? false : var22.equalsIgnoreCase("on") || var22.equalsIgnoreCase("true");
            }
            else
            {
                CustomOreGenBase.log.warning("Choice Option \'" + var20 + "\' not found in config file - defaulting to \'" + this.debuggingMode + "\'.");
            }

            var14 = (ConfigOption)this.configOptions.get("vanillaOreGen");

            if (var14 != null && var14 instanceof ChoiceOption)
            {
                String var15 = (String)var14.getValue();
                this.vanillaOreGen = var15 == null ? false : var15.equalsIgnoreCase("on") || var15.equalsIgnoreCase("true");
            }
            else
            {
                CustomOreGenBase.log.warning("Choice Option \'" + var14 + "\' not found in config file - defaulting to \'" + this.vanillaOreGen + "\'.");
            }
        }
    }

    private int buildFileList(String var1, File[] var2)
    {
        if (var2 == null)
        {
            var2 = new File[3];
        }

        if (this.globalConfigDir != null)
        {
            var2[0] = new File(this.globalConfigDir, var1);
        }

        if (this.worldBaseDir != null)
        {
            var2[1] = new File(this.worldBaseDir, var1);
        }

        if (this.dimensionDir != null)
        {
            var2[2] = new File(this.dimensionDir, var1);
        }

        for (int var3 = var2.length - 1; var3 >= 0; --var3)
        {
            if (var2[var3] != null && var2[var3].exists())
            {
                return var3;
            }
        }

        return -1;
    }

    private void loadOptions(File var1, boolean var2) throws IOException
    {
        if (var1 != null)
        {
            Properties var3 = new Properties();

            if (var1.exists())
            {
                var3.load(new FileInputStream(var1));
                this.loadedOptions.putAll(var3);
            }
            else if (var2)
            {
                var1.createNewFile();
                var3.putAll(this.loadedOptions);
                String var4 = "CustomOreGen [1.4.6]v2 Config Options";
                var3.store(new FileOutputStream(var1), var4);
            }
        }
    }

    private static void populateWorldProperties(Map var0, World var1, WorldInfo var2)
    {
        var0.put("world", var2 == null ? "" : var2.getWorldName());
        var0.put("world.seed", Long.valueOf(var2 == null ? 0L : var2.getSeed()));
        var0.put("world.version", Integer.valueOf(var2 == null ? 0 : var2.getSaveVersion()));
        var0.put("world.isHardcore", Boolean.valueOf(var2 == null ? false : var2.isHardcoreModeEnabled()));
        var0.put("world.hasFeatures", Boolean.valueOf(var2 == null ? false : var2.isMapFeaturesEnabled()));
        var0.put("world.hasCheats", Boolean.valueOf(var2 == null ? false : var2.areCommandsAllowed()));
        var0.put("world.gameMode", var2 == null ? "" : var2.getGameType().getName());
        var0.put("world.gameMode.id", Integer.valueOf(var2 == null ? 0 : var2.getGameType().getID()));
        var0.put("world.type", var2 == null ? "" : var2.getTerrainType().getWorldTypeName());
        var0.put("world.type.version", Integer.valueOf(var2 == null ? 0 : var2.getTerrainType().getGeneratorVersion()));
        String var3 = "RandomLevelSource";
        String var4 = "ChunkProviderGenerate";

        if (var1 != null)
        {
            IChunkProvider var5 = var1.provider.createChunkGenerator();
            var3 = var5.makeString();
            var4 = var5.getClass().getSimpleName();

            if (var5 instanceof ChunkProviderGenerate)
            {
                var4 = "ChunkProviderGenerate";
            }
            else if (var5 instanceof ChunkProviderFlat)
            {
                var4 = "ChunkProviderFlat";
            }
            else if (var5 instanceof ChunkProviderHell)
            {
                var4 = "ChunkProviderHell";
            }
            else if (var5 instanceof ChunkProviderEnd)
            {
                var3 = "EndRandomLevelSource";
                var4 = "ChunkProviderEnd";
            }
        }

        var0.put("dimension.generator", var3);
        var0.put("dimension.generator.class", var4);
        var0.put("dimension", var1 == null ? "" : var1.provider.getDimensionName());
        var0.put("dimension.id", Integer.valueOf(var1 == null ? 0 : var1.provider.dimensionId));
        var0.put("dimension.isSurface", Boolean.valueOf(var1 == null ? false : var1.provider.isSurfaceWorld()));
        var0.put("dimension.groundLevel", Integer.valueOf(var1 == null ? 0 : var1.provider.getAverageGroundLevel()));
        var0.put("dimension.height", Integer.valueOf(var1 == null ? 0 : var1.getHeight()));
        var0.put("age", Boolean.FALSE);

        /*if (CustomOreGenBase.hasMystcraft())
        {
            MystcraftInterface.populateAgePropertyMap(var1, var0);
        }*/
    }

    public Collection getOreDistributions()
    {
        return this.oreDistributions;
    }

    public Collection getOreDistributions(String var1)
    {
        LinkedList var2 = new LinkedList();

        if (var1 != null)
        {
            Pattern var3 = Pattern.compile(var1, 2);
            Matcher var4 = var3.matcher("");
            Iterator var5 = this.oreDistributions.iterator();

            while (var5.hasNext())
            {
                IOreDistribution var6 = (IOreDistribution)var5.next();
                var4.reset(var6.toString());

                if (var4.matches())
                {
                    var2.add(var6);
                }
            }
        }

        return Collections.unmodifiableCollection(var2);
    }

    public ConfigOption getConfigOption(String var1)
    {
        return (ConfigOption)this.configOptions.get(var1);
    }

    public Collection getConfigOptions()
    {
        return new WorldConfig$1(this, this.configOptions);
    }

    public Collection getConfigOptions(String var1)
    {
        LinkedList var2 = new LinkedList();

        if (var1 != null)
        {
            Pattern var3 = Pattern.compile(var1, 2);
            Matcher var4 = var3.matcher("");
            Iterator var5 = this.configOptions.values().iterator();

            while (var5.hasNext())
            {
                ConfigOption var6 = (ConfigOption)var5.next();
                var4.reset(var6.getName());

                if (var4.matches())
                {
                    var2.add(var6);
                }
            }
        }

        return Collections.unmodifiableCollection(var2);
    }

    public String loadConfigOption(String var1)
    {
        return (String)this.loadedOptions.get(var1);
    }

    public Object getWorldProperty(String var1)
    {
        return this.worldProperties.get(var1);
    }

    /*public MystcraftSymbolData getMystcraftSymbol(String var1)
    {
        //return (MystcraftSymbolData)this.cogSymbolData.get(var1);
    	return null;
    }

    public Collection getMystcraftSymbols()
    {
        return new WorldConfig$2(this, this.cogSymbolData);
    }*/

    static Map access$000(WorldConfig var0)
    {
        return var0.worldProperties;
    }
    
    
    class WorldConfig$1 extends MapCollection
    {
        final WorldConfig this$0;

        WorldConfig$1(WorldConfig var1, Map var2)
        {
            super(var2);
            this.this$0 = var1;
        }

        protected String getKey(ConfigOption var1)
        {
            return var1.getName();
        }

        protected Object getKey(Object var1)
        {
            return this.getKey((ConfigOption)var1);
        }
    }

    class WorldConfig$2 extends MapCollection
    {
        final WorldConfig this$0;

        WorldConfig$2(WorldConfig var1, Map var2)
        {
            super(var2);
            this.this$0 = var1;
        }

		@Override
		protected Object getKey(Object var1) {
			// TODO Auto-generated method stub
			return null;
		}

    }
    
    
}
