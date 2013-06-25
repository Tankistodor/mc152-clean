package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorBiomeDescriptor$Factory;
import CustomOreGen.Config.ValidatorBlockDescriptor$Factory;
import CustomOreGen.Config.ValidatorPDist$Factory;
import CustomOreGen.Server.IOreDistribution;
import CustomOreGen.Server.IOreDistribution$IDistributionFactory;
import CustomOreGen.Server.IOreDistribution$StandardSettings;
import CustomOreGen.Util.BiomeDescriptor;
import CustomOreGen.Util.BlockDescriptor;
import CustomOreGen.Util.PDist;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorDistribution extends ValidatorNode
{
    private final IOreDistribution$IDistributionFactory _distributionFactory;
    public IOreDistribution distribution = null;

    protected ValidatorDistribution(ValidatorNode var1, Node var2, IOreDistribution$IDistributionFactory var3)
    {
        super(var1, var2);
        this._distributionFactory = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        try
        {
            this.distribution = this._distributionFactory.createDistribution(this.getParser().target.getOreDistributions().size());
        }
        catch (Exception var7)
        {
            throw new ParserException("Failed to create distribution using \'" + this._distributionFactory + "\'.", this.getNode(), var7);
        }

        this.getParser().target.getOreDistributions().add(this.distribution);
        this.getNode().setUserData("value", this.distribution, (UserDataHandler)null);
        super.validateChildren();
        String var1 = (String)this.validateNamedAttribute(String.class, "Inherits", (Object)null, true);

        if (var1 != null)
        {
            Collection var2 = this.getParser().target.getOreDistributions(var1);

            if (var2.isEmpty())
            {
                throw new ParserException("Cannot inherit settings (\'" + var1 + "\' is not a loaded distribution).", this.getNode());
            }

            if (var2.size() > 1)
            {
                throw new ParserException("Cannot inherit settings (\'" + var1 + "\' is ambiguous; matching " + var2.size() + " loaded distributions).", this.getNode());
            }

            try
            {
                this.distribution.inheritFrom((IOreDistribution)var2.iterator().next());
            }
            catch (IllegalArgumentException var6)
            {
                throw new ParserException("Cannot inherit settings (" + var6.getMessage() + ").", this.getNode(), var6);
            }
        }

        HashSet var10 = new HashSet(this.distribution.getDistributionSettings().keySet());
        String var3 = IOreDistribution$StandardSettings.Name.name();

        if (var10.contains(var3))
        {
            String var4 = (String)this.validateNamedAttribute(String.class, var3, (Object)null, true);

            try
            {
                if (var4 != null)
                {
                    this.distribution.setDistributionSetting(var3, var4);
                }
            }
            catch (IllegalAccessException var8)
            {
                throw new ParserException("Attribute \'" + var3 + "\' is not configurable.", this.getNode(), var8);
            }
            catch (IllegalArgumentException var9)
            {
                throw new ParserException("Attribute \'" + var3 + "\' cannot be set (" + var9.getMessage() + ").", this.getNode(), var9);
            }

            var10.remove(var3);
        }

        this.validateDistributionSettings(var10);
        return true;
    }

    public void validateDistributionSettings(Set var1) throws ParserException
    {
        String var2 = IOreDistribution$StandardSettings.Parent.name();

        if (var1.contains(var2))
        {
            for (Node var3 = this.getNode().getParentNode(); var3 != null; var3 = var3.getParentNode())
            {
                Object var4 = var3.getUserData("value");

                if (var4 != null && var4 instanceof IOreDistribution)
                {
                    try
                    {
                        this.distribution.setDistributionSetting(var2, var4);
                    }
                    catch (IllegalAccessException var18)
                    {
                        throw new ParserException("Parent distribution is not configurable.", this.getNode(), var18);
                    }
                    catch (IllegalArgumentException var19)
                    {
                        throw new ParserException("Invalid parent distribution.", this.getNode(), var19);
                    }

                    this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
                    break;
                }
            }

            var1.remove(var2);
        }

        String var20 = IOreDistribution$StandardSettings.OreBlock.name();
        String var5;
        Iterator var6;
        ValidatorBlockDescriptor var7;

        if (var1.contains(var20))
        {
            BlockDescriptor var21 = new BlockDescriptor();
            var5 = (String)this.validateNamedAttribute(String.class, "Block", (Object)null, true);

            if (var5 != null)
            {
                var21.add(var5);
            }

            var6 = this.validateNamedChildren(2, "OreBlock", new ValidatorBlockDescriptor$Factory()).iterator();

            while (var6.hasNext())
            {
                var7 = (ValidatorBlockDescriptor)var6.next();
                var21.add(var7.blocks, var7.weight);
            }

            if (!var21.getDescriptors().isEmpty())
            {
                try
                {
                    this.distribution.setDistributionSetting(var20, var21);
                }
                catch (IllegalAccessException var16)
                {
                    throw new ParserException("Target ore blocks are not configurable.", this.getNode(), var16);
                }
                catch (IllegalArgumentException var17)
                {
                    throw new ParserException("Target ore blocks are not supported by this distribution.", this.getNode(), var17);
                }
            }

            var1.remove(var20);
        }

        String var22 = IOreDistribution$StandardSettings.ReplaceableBlock.name();

        if (var1.contains(var22))
        {
            BlockDescriptor var23 = new BlockDescriptor();
            var6 = this.validateNamedChildren(2, "Replaces", new ValidatorBlockDescriptor$Factory()).iterator();

            while (var6.hasNext())
            {
                var7 = (ValidatorBlockDescriptor)var6.next();
                var23.add(var7.blocks, var7.weight);
            }

            if (!var23.getDescriptors().isEmpty())
            {
                try
                {
                    this.distribution.setDistributionSetting(var22, var23);
                }
                catch (IllegalAccessException var14)
                {
                    throw new ParserException("Replaceable blocks are not configurable.", this.getNode(), var14);
                }
                catch (IllegalArgumentException var15)
                {
                    throw new ParserException("Replaceable blocks are not supported by this distribution.", this.getNode(), var15);
                }
            }

            var1.remove(var22);
        }

        var5 = IOreDistribution$StandardSettings.TargetBiome.name();

        if (var1.contains(var5))
        {
            BiomeDescriptor var24 = new BiomeDescriptor();
            Iterator var26 = this.validateNamedChildren(2, "Biome", new ValidatorBiomeDescriptor$Factory()).iterator();

            while (var26.hasNext())
            {
                ValidatorBiomeDescriptor var8 = (ValidatorBiomeDescriptor)var26.next();
                var24.add(var8.biome, var8.weight);
            }

            if (!var24.getDescriptors().isEmpty())
            {
                try
                {
                    this.distribution.setDistributionSetting(var5, var24);
                }
                catch (IllegalAccessException var12)
                {
                    throw new ParserException("Biomes are not configurable.", this.getNode(), var12);
                }
                catch (IllegalArgumentException var13)
                {
                    throw new ParserException("Biomes are not supported by this distribution.", this.getNode(), var13);
                }
            }

            var1.remove(var5);
        }

        ValidatorPDist var25;

        for (var6 = this.validateNamedChildren(2, "Setting", new ValidatorPDist$Factory(this.distribution)).iterator(); var6.hasNext(); var25 = (ValidatorPDist)var6.next())
        {
            ;
        }

        var6 = var1.iterator();

        while (var6.hasNext())
        {
            String var27 = (String)var6.next();
            Object var28 = this.distribution.getDistributionSetting(var27);

            if (var28 != null)
            {
                if (var28 instanceof PDist)
                {
                    continue;
                }

                var28 = this.validateNamedAttribute(var28.getClass(), var27, var28, true);
            }
            else
            {
                var28 = this.validateNamedAttribute(String.class, var27, (Object)null, true);
            }

            try
            {
                if (var28 != null)
                {
                    this.distribution.setDistributionSetting(var27, var28);
                }
            }
            catch (IllegalAccessException var10)
            {
                throw new ParserException("Attribute \'" + var27 + "\' is not configurable.", this.getNode(), var10);
            }
            catch (IllegalArgumentException var11)
            {
                throw new ParserException("Attribute \'" + var27 + "\' cannot be set (" + var11.getMessage() + ").", this.getNode(), var11);
            }
        }
    }
}
