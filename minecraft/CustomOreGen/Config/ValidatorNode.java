package CustomOreGen.Config;

import CustomOreGen.Config.ConfigParser$ConfigExpressionEvaluator;
import CustomOreGen.Config.ValidatorNode$IValidatorFactory;
import CustomOreGen.Config.ValidatorSimpleNode$Factory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class ValidatorNode
{
    private ConfigParser _parser = null;
    private Node _node = null;
    private Hashtable _validatorMap = null;
    private boolean _validatorMapShared = false;

    public ValidatorNode(ConfigParser var1, Node var2)
    {
        this._parser = var1;
        this._node = var2;
        var2.setUserData("validator", this, (UserDataHandler)null);
    }

    protected ValidatorNode(ValidatorNode var1, Node var2)
    {
        if (var1._validatorMap != null)
        {
            this._validatorMap = var1._validatorMap;
            this._validatorMapShared = var1._validatorMapShared = true;
        }

        this._parser = var1._parser;
        this._node = var2;
        var2.setUserData("validator", this, (UserDataHandler)null);
    }

    public final Node getNode()
    {
        return this._node;
    }

    public final ConfigParser getParser()
    {
        return this._parser;
    }

    public final void addGlobalValidator(short var1, String var2, ValidatorNode$IValidatorFactory var3)
    {
        List var4 = Arrays.asList(new Object[] {Short.valueOf(var1), var2.toLowerCase()});

        if (this._validatorMap == null)
        {
            this._validatorMap = new Hashtable();
            this._validatorMapShared = false;
        }
        else if (this._validatorMapShared)
        {
            this._validatorMap = new Hashtable(this._validatorMap);
            this._validatorMapShared = false;
        }

        this._validatorMap.put(var4, var3);
    }

    public final void validate() throws ParserException
    {
        if (this.validateChildren())
        {
            this.checkChildrenValid();
        }
    }

    protected boolean validateChildren() throws ParserException
    {
        if (this._validatorMap != null)
        {
            LinkedList var1 = new LinkedList();
            NamedNodeMap var2 = this._node.getAttributes();

            for (int var3 = 0; var2 != null && var3 < var2.getLength(); ++var3)
            {
                var1.addLast(var2.item(var3));
            }

            NodeList var9 = this._node.getChildNodes();

            for (int var4 = 0; var9 != null && var4 < var9.getLength(); ++var4)
            {
                var1.addLast(var9.item(var4));
            }

            Iterator var10 = var1.iterator();

            while (var10.hasNext())
            {
                Node var5 = (Node)var10.next();
                List var6 = Arrays.asList(new Object[] {Short.valueOf(var5.getNodeType()), var5.getNodeName().toLowerCase()});
                ValidatorNode$IValidatorFactory var7 = (ValidatorNode$IValidatorFactory)this._validatorMap.get(var6);

                if (var7 != null)
                {
                    ValidatorNode var8 = var7.createValidator(this, var5);

                    if (var8 != null)
                    {
                        var8.validate();
                    }
                }
            }
        }

        return true;
    }

    protected void checkChildrenValid() throws ParserException
    {
        NamedNodeMap var1 = this._node.getAttributes();

        for (int var2 = 0; var1 != null && var2 < var1.getLength(); ++var2)
        {
            Node var3 = var1.item(var2);

            if (var3.getUserData("validated") == null)
            {
                throw new ParserException("Unexpected at this location.", var3);
            }
        }

        NodeList var5 = this._node.getChildNodes();

        for (int var6 = 0; var5 != null && var6 < var5.getLength(); ++var6)
        {
            Node var4 = var5.item(var6);

            switch (var4.getNodeType())
            {
                case 3:
                    if (var4.getNodeValue() == null || var4.getNodeValue().trim().isEmpty())
                    {
                        break;
                    }

                case 8:
                case 9:
                    break;

                default:
                    if (var4.getUserData("validated") == null)
                    {
                        throw new ParserException("Unexpected at this location.", var4);
                    }
            }
        }
    }

    protected final LinkedList validateNamedChildren(int var1, String var2, ValidatorNode$IValidatorFactory var3) throws ParserException
    {
        LinkedList var4 = new LinkedList();
        NamedNodeMap var5 = this._node.getAttributes();
        NodeList var6 = this._node.getChildNodes();
        int var7 = var5 == null ? 0 : var5.getLength();
        int var8 = var6 == null ? 0 : var6.getLength();

        for (int var9 = -var7; var9 < var8; ++var9)
        {
            Node var10 = var9 < 0 ? var5.item(-var9 - 1) : var6.item(var9);

            if ((var1 >>> var10.getNodeType()) % 2 != 0 && (var2 == null || var10.getNodeName().equalsIgnoreCase(var2)))
            {
                var10.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);

                if (var3 != null)
                {
                    var4.addLast(var3.createValidator(this, var10));
                }
            }
        }

        Iterator var12 = var4.iterator();

        while (var12.hasNext())
        {
            ValidatorNode var11 = (ValidatorNode)var12.next();
            var11.validate();
        }

        return var4;
    }

    protected final Object validateNamedAttribute(Class var1, String var2, Object var3, boolean var4) throws ParserException
    {
        ConfigParser$ConfigExpressionEvaluator var10000;

        if (var3 == null)
        {
            var10000 = null;
        }
        else
        {
            //var10000 = new ConfigParser$ConfigExpressionEvaluator;
            ConfigParser var10002 = this.getParser();
            var10002.getClass();
            var10000 = new ConfigParser$ConfigExpressionEvaluator(var10002, var3);
            //var10000.<init>(var10002, var3);
        }

        ConfigParser$ConfigExpressionEvaluator var5 = var10000;
        Object var6 = null;
        int var7 = 4;

        if (var4)
        {
            var7 |= 2;
        }

        LinkedList var8 = this.validateNamedChildren(var7, var2, new ValidatorSimpleNode$Factory(var1, var5));

        if (!var8.isEmpty())
        {
            var6 = ((ValidatorSimpleNode)var8.getLast()).content;
        }

        return var6 == null ? var3 : var6;
    }

    protected final Object validateRequiredAttribute(Class var1, String var2, boolean var3) throws ParserException
    {
        Object var4 = this.validateNamedAttribute(var1, var2, (Object)null, var3);

        if (var4 != null)
        {
            return var4;
        }
        else
        {
            throw new ParserException("Required attribute \'" + var2 + "\' not found.", this.getNode());
        }
    }

    protected final void replaceWithNode(Node ... var1) throws ParserException
    {
        Node var2 = this.getNode().getParentNode();

        if (var2 != null)
        {
            if (var1 != null && var1.length > 0)
            {
                Node[] var3 = var1;
                int var4 = var1.length;

                for (int var5 = 0; var5 < var4; ++var5)
                {
                    Node var6 = var3[var5];

                    if (var6 != null)
                    {
                        Node var7 = var6;

                        while (true)
                        {
                            Node var8 = (Node)var7.getUserData("hidden-parent");

                            if (var8 == null)
                            {
                                var7.setUserData("hidden-parent", this.getNode(), (UserDataHandler)null);
                                break;
                            }

                            if (var8 == this.getNode())
                            {
                                break;
                            }

                            var7 = var8;
                        }

                        if (var6.getNodeType() == 2)
                        {
                            if (var2.getNodeType() != 1)
                            {
                                throw new ParserException("Attempting to merge attribute to non-element node.", var6);
                            }

                            Attr var9 = (Attr)var6;
                            var9.getOwnerElement().removeAttributeNode(var9);
                            ((Element)var2).setAttributeNode(var9);
                        }
                        else
                        {
                            var2.insertBefore(var6, this.getNode());
                        }
                    }
                }
            }

            var2.removeChild(this.getNode());
        }
    }

    protected final void replaceWithNodeContents(Node ... var1) throws ParserException
    {
        Node var2 = this.getNode().getParentNode();

        if (var2 != null)
        {
            if (var1 != null && var1.length > 0)
            {
                ArrayList var3 = new ArrayList();
                Node[] var4 = var1;
                int var5 = var1.length;

                for (int var6 = 0; var6 < var5; ++var6)
                {
                    Node var7 = var4[var6];

                    if (var7 != null)
                    {
                        Node var8;
                        Node var9;

                        if (var7 != this.getNode())
                        {
                            var8 = var7;

                            while (true)
                            {
                                var9 = (Node)var8.getUserData("hidden-parent");

                                if (var9 == null)
                                {
                                    var8.setUserData("hidden-parent", this.getNode(), (UserDataHandler)null);
                                    break;
                                }

                                if (var9 == this.getNode())
                                {
                                    break;
                                }

                                var8 = var9;
                            }
                        }

                        Node var10;

                        if (var2.getNodeType() == 1)
                        {
                            NamedNodeMap var13 = var7.getAttributes();

                            for (int var14 = 0; var14 < var13.getLength(); ++var14)
                            {
                                var10 = var13.item(var14);
                                Node var11 = var10;

                                while (true)
                                {
                                    Node var12 = (Node)var11.getUserData("hidden-parent");

                                    if (var12 == null)
                                    {
                                        var11.setUserData("hidden-parent", var7, (UserDataHandler)null);
                                        break;
                                    }

                                    if (var12 == var7)
                                    {
                                        break;
                                    }

                                    var11 = var12;
                                }

                                var3.add(var13.item(var14));
                            }
                        }

                        for (var8 = var7.getFirstChild(); var8 != null; var8 = var8.getNextSibling())
                        {
                            var9 = var8;

                            while (true)
                            {
                                var10 = (Node)var9.getUserData("hidden-parent");

                                if (var10 == null)
                                {
                                    var9.setUserData("hidden-parent", var7, (UserDataHandler)null);
                                    break;
                                }

                                if (var10 == var7)
                                {
                                    break;
                                }

                                var9 = var10;
                            }

                            var3.add(var8);
                        }
                    }
                }

                this.replaceWithNode((Node[])var3.toArray(new Node[var3.size()]));
            }
            else
            {
                var2.removeChild(this.getNode());
            }
        }
    }
}
