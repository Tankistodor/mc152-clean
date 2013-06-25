package CustomOreGen.Config;

import CustomOreGen.Config.ConfigParser$ConfigExpressionEvaluator;
import CustomOreGen.Config.ConfigParser$StdDistFactory;
import CustomOreGen.Config.ValidatorAnnotation.Factory;
import CustomOreGen.Config.ValidatorDistribution$Factory;
import CustomOreGen.Config.ValidatorExpression$Factory;
import CustomOreGen.Config.ValidatorIfChoice$Factory;
import CustomOreGen.Config.ValidatorIfCondition$Factory;
import CustomOreGen.Config.ValidatorIfModInstalled$Factory;
import CustomOreGen.Config.ValidatorImport$Factory;
import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import CustomOreGen.Config.ValidatorOption$Factory;
import CustomOreGen.Config.ValidatorRefOption$Factory;
import CustomOreGen.Config.ValidatorRoot$Factory;
import CustomOreGen.Config.ValidatorSection$Factory;
import CustomOreGen.Server.ChoiceOption;
import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.IOreDistribution$IDistributionFactory;
import CustomOreGen.Server.MapGenCloud;
import CustomOreGen.Server.MapGenClusters;
import CustomOreGen.Server.MapGenVeins;
import CustomOreGen.Server.NumericOption;
import CustomOreGen.Server.WorldConfig;
import CustomOreGen.Server.WorldGenSubstitution;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class ConfigParser
{
    public final WorldConfig target;
    public final ConfigParser$ConfigExpressionEvaluator defaultEvaluator = new ConfigParser$ConfigExpressionEvaluator(this);
    protected Random rng = null;
    protected final DocumentBuilder domBuilder;
    protected final SAXParser saxParser;
    private static final Map distributionValidators = new HashMap();

    public boolean blockExists(String var1)
    {
        return (new BlockDescriptor(var1)).getTotalMatchWeight() > 0.0F;
    }

    public boolean biomeExists(String var1)
    {
        return (new BiomeDescriptor(var1)).getTotalMatchWeight() > 0.0F;
    }

    public float nextRandom()
    {
        return this.rng == null ? 0.0F : this.rng.nextFloat();
    }

    public ConfigParser(WorldConfig var1) throws ParserConfigurationException, SAXException
    {
        this.target = var1;
        DocumentBuilderFactory var2 = DocumentBuilderFactory.newInstance();
        var2.setNamespaceAware(true);
        var2.setIgnoringComments(true);
        var2.setIgnoringElementContentWhitespace(true);
        var2.setExpandEntityReferences(true);
        this.domBuilder = var2.newDocumentBuilder();
        SAXParserFactory var3 = SAXParserFactory.newInstance();
        var3.setNamespaceAware(true);
        this.saxParser = var3.newSAXParser();
    }

    public void parseFile(File var1) throws ParserConfigurationException, SAXException, IOException
    {
        Document var2 = this.domBuilder.newDocument();
        var2.setUserData("value", var1, (UserDataHandler)null);
        this.saxParser.parse(var1, new LineAwareSAXHandler(var2));
        ValidatorNode var3 = new ValidatorNode(this, var2);
        Vector var4 = new Vector();
        var3.addGlobalValidator((short)1, "Import", new ValidatorImport$Factory(true));
        var3.addGlobalValidator((short)1, "OptionalImport", new ValidatorImport$Factory(false));
        var3.addGlobalValidator((short)1, "Description", new ValidatorAnnotation.Factory());
        var3.addGlobalValidator((short)2, "Description", new ValidatorAnnotation.Factory());
        var3.addGlobalValidator((short)1, "Comment", new ValidatorAnnotation.Factory());
        var3.addGlobalValidator((short)1, "ConfigSection", new ValidatorSection$Factory());
        var3.addGlobalValidator((short)1, "IfCondition", new ValidatorIfCondition$Factory());
        var3.addGlobalValidator((short)1, "IfChoice", new ValidatorIfChoice$Factory(false));
        var3.addGlobalValidator((short)1, "IfNotChoice", new ValidatorIfChoice$Factory(true));
        var3.addGlobalValidator((short)1, "IfModInstalled", new ValidatorIfModInstalled$Factory(false));
        var3.addGlobalValidator((short)1, "IfNotModInstalled", new ValidatorIfModInstalled$Factory(true));
        var3.addGlobalValidator((short)1, "GetOption", new ValidatorRefOption$Factory());
        var3.addGlobalValidator((short)1, "Expression", new ValidatorExpression$Factory(this.defaultEvaluator));
        var4.add("OptionDisplayGroup");
        var3.addGlobalValidator((short)1, "OptionDisplayGroup", new ValidatorOption$Factory(ConfigOption$DisplayGroup.class));
        var4.add("OptionChoice");
        var3.addGlobalValidator((short)1, "OptionChoice", new ValidatorOption$Factory(ChoiceOption.class));
        var4.add("OptionNumeric");
        var3.addGlobalValidator((short)1, "OptionNumeric", new ValidatorOption$Factory(NumericOption.class));
        var4.add("MystcraftSymbol");
        //var3.addGlobalValidator((short)1, "MystcraftSymbol", new ValidatorMystcraftSymbol$Factory());
        Iterator var5 = distributionValidators.entrySet().iterator();

        while (var5.hasNext())
        {
            Entry var6 = (Entry)var5.next();
            var3.addGlobalValidator((short)1, (String)var6.getKey(), (ValidatorNode$IValidatorFactory)var6.getValue());
            var4.add(var6.getKey());
        }

        var3.addGlobalValidator((short)1, "Config", new ValidatorRoot$Factory(var4));

        if (this.target.worldInfo == null)
        {
            this.rng = null;
        }
        else
        {
            this.rng = new Random(this.target.worldInfo.getSeed());
            this.rng.nextInt();
        }

        var3.validate();
    }

    public static Object parseString(Class var0, String var1) throws IllegalArgumentException
    {
        if (var0 != null && var1 != null)
        {
            if (var0.isAssignableFrom(String.class))
            {
                return var1;
            }
            else if (var0.isEnum())
            {
                Enum[] var2 = (Enum[])((Enum[])var0.getEnumConstants());
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    Enum var5 = var2[var4];

                    if (var5.name().equalsIgnoreCase(var1))
                    {
                        return var5;
                    }
                }

                throw new IllegalArgumentException("Invalid enumeration value \'" + var1 + "\'");
            }
            else if (!var0.isAssignableFrom(Character.TYPE) && !var0.isAssignableFrom(Character.class))
            {
                if (!var0.isAssignableFrom(Boolean.TYPE) && !var0.isAssignableFrom(Boolean.class))
                {
                    try
                    {
                        if (!var0.isAssignableFrom(Byte.TYPE) && !var0.isAssignableFrom(Byte.class))
                        {
                            if (!var0.isAssignableFrom(Short.TYPE) && !var0.isAssignableFrom(Short.class))
                            {
                                if (!var0.isAssignableFrom(Integer.TYPE) && !var0.isAssignableFrom(Integer.class))
                                {
                                    if (!var0.isAssignableFrom(Long.TYPE) && !var0.isAssignableFrom(Long.class))
                                    {
                                        if (!var0.isAssignableFrom(Float.TYPE) && !var0.isAssignableFrom(Float.class))
                                        {
                                            if (!var0.isAssignableFrom(Double.TYPE) && !var0.isAssignableFrom(Double.class))
                                            {
                                                throw new IllegalArgumentException("Type \'" + var0.getSimpleName() + "\' is not a string, enumeration, or primitve type.");
                                            }
                                            else
                                            {
                                                return Double.valueOf(Double.parseDouble(var1));
                                            }
                                        }
                                        else
                                        {
                                            return Float.valueOf(Float.parseFloat(var1));
                                        }
                                    }
                                    else
                                    {
                                        return Long.decode(var1);
                                    }
                                }
                                else
                                {
                                    return Integer.decode(var1);
                                }
                            }
                            else
                            {
                                return Short.decode(var1);
                            }
                        }
                        else
                        {
                            return Byte.decode(var1);
                        }
                    }
                    catch (NumberFormatException var6)
                    {
                        throw new IllegalArgumentException("Invalid numerical value \'" + var1 + "\'", var6);
                    }
                }
                else
                {
                    return Boolean.valueOf(Boolean.parseBoolean(var1));
                }
            }
            else
            {
                return var1.length() == 0 ? Character.valueOf('\u0000') : Character.valueOf(var1.charAt(0));
            }
        }
        else
        {
            return null;
        }
    }

    public static Number convertNumber(Class var0, Number var1) throws IllegalArgumentException
    {
        if (var0 != null && var1 != null)
        {
            if (!var0.isAssignableFrom(Byte.TYPE) && !var0.isAssignableFrom(Byte.class))
            {
                if (!var0.isAssignableFrom(Short.TYPE) && !var0.isAssignableFrom(Short.class))
                {
                    if (!var0.isAssignableFrom(Integer.TYPE) && !var0.isAssignableFrom(Integer.class))
                    {
                        if (!var0.isAssignableFrom(Long.TYPE) && !var0.isAssignableFrom(Long.class))
                        {
                            if (!var0.isAssignableFrom(Float.TYPE) && !var0.isAssignableFrom(Float.class))
                            {
                                if (!var0.isAssignableFrom(Double.TYPE) && !var0.isAssignableFrom(Double.class))
                                {
                                    throw new IllegalArgumentException("Type \'" + var0.getSimpleName() + "\' is not a numeric type.");
                                }
                                else
                                {
                                    return Double.valueOf(var1.doubleValue());
                                }
                            }
                            else
                            {
                                return Float.valueOf(var1.floatValue());
                            }
                        }
                        else
                        {
                            return Long.valueOf(var1.longValue());
                        }
                    }
                    else
                    {
                        return Integer.valueOf(var1.intValue());
                    }
                }
                else
                {
                    return Short.valueOf(var1.shortValue());
                }
            }
            else
            {
                return Byte.valueOf(var1.byteValue());
            }
        }
        else
        {
            return null;
        }
    }

    public static void addDistributionType(String var0, ValidatorNode$IValidatorFactory var1)
    {
        if (distributionValidators.containsKey(var0))
        {
            throw new IllegalArgumentException("A distribution with the name \'" + var0 + "\' already exists.");
        }
        else
        {
            distributionValidators.put(var0, var1);
        }
    }

    public static void addDistributionType(String var0, IOreDistribution$IDistributionFactory var1)
    {
        addDistributionType(var0, (ValidatorNode$IValidatorFactory)(new ValidatorDistribution$Factory(var1)));
    }

    public static void addDistributionType(String var0, Class var1, boolean var2)
    {
        addDistributionType(var0, (IOreDistribution$IDistributionFactory)(new ConfigParser$StdDistFactory(var1, var2)));
    }

    static
    {
        addDistributionType("StandardGen", MapGenClusters.class, true);
        addDistributionType("StandardGenPreset", MapGenClusters.class, false);
        addDistributionType("Veins", MapGenVeins.class, true);
        addDistributionType("VeinsPreset", MapGenVeins.class, false);
        addDistributionType("Cloud", MapGenCloud.class, true);
        addDistributionType("CloudPreset", MapGenCloud.class, false);
        addDistributionType("Substitute", WorldGenSubstitution.class, true);
        addDistributionType("SubstitutePreset", WorldGenSubstitution.class, false);
    }
}
