package CustomOreGen.Config;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ParserException extends SAXException
{
    public final Node node;
    public int lineNumber;

    public ParserException(String var1, Node var2, int var3, Exception var4)
    {
        super(var1, var4);
        this.node = var2;
        this.lineNumber = var3;
    }

    public ParserException(String var1, Node var2, int var3)
    {
        super(var1);
        this.node = var2;
        this.lineNumber = var3;
    }

    public ParserException(String var1, Node var2, Exception var3)
    {
        this(var1, var2, -1, var3);
    }

    public ParserException(String var1, Node var2)
    {
        this(var1, var2, -1);
    }

    public ParserException(String var1, int var2, Exception var3)
    {
        this(var1, (Node)null, var2, var3);
    }

    public ParserException(String var1, int var2)
    {
        this(var1, (Node)null, var2);
    }

    public ParserException(String var1, Exception var2)
    {
        this(var1, (Node)null, -1, var2);
    }

    public ParserException(String var1)
    {
        this(var1, (Node)null, -1);
    }

    public ParserException(Exception var1)
    {
        super(var1);
        this.node = null;
        this.lineNumber = -1;
    }

    public String getMessage()
    {
        String var1 = "CustomOreGen Config Error";
        return this.node == null ? (this.lineNumber < 0 ? var1 + ": " + super.getMessage() : var1 + " at [line " + this.lineNumber + "]: " + super.getMessage()) : (this.lineNumber < 0 ? var1 + " at " + formatNode(this.node, 16) + ": " + super.getMessage() : var1 + " at " + formatNode(this.node, 0) + " [line " + this.lineNumber + "]: " + super.getMessage());
    }

    public String toString()
    {
        StringBuilder var1 = new StringBuilder(this.getMessage());

        if (this.node != null)
        {
            Object var2 = this.node;
            Object var3 = null;

            while (var2 != null)
            {
                var1.append("\n  in ");
                var1.append(formatNode((Node)var2, 17));
                Node var4 = (Node)((Node)var2).getUserData("hidden-parent");

                if (var4 != null)
                {
                    if (var3 == null)
                    {
                        var3 = var2;
                    }

                    var2 = var4;
                }
                else
                {
                    if (var3 != null)
                    {
                        var2 = var3;
                        var3 = null;
                    }

                    if (((Node)var2).getNodeType() == 2)
                    {
                        var2 = ((Attr)var2).getOwnerElement();
                    }
                    else
                    {
                        var2 = ((Node)var2).getParentNode();
                    }
                }
            }
        }

        return var1.toString();
    }

    public static String formatNode(Node var0, int var1)
    {
        StringBuilder var2 = new StringBuilder();
        String var3 = null;

        switch (var0.getNodeType())
        {
            case 1:
                var2.append("Element");
                var3 = "<%s>";
                break;

            case 2:
                var2.append("Attribute");
                var3 = "\'%s\'";
                break;

            case 3:
                var2.append("Text");
                break;

            case 4:
                var2.append("CData");
                break;

            case 5:
                var2.append("EntityRef");
                var3 = "\'%s\'";
                break;

            case 6:
                var2.append("Entity");
                var3 = "\'%s\'";
                break;

            case 7:
                var2.append("Instruction");
                var3 = "\'%s\'";
                break;

            case 8:
                var2.append("Comment");
                break;

            case 9:
                var2.append("Document");
                break;

            case 10:
                var2.append("DocType");
                var3 = "\'%s\'";
                break;

            case 11:
                var2.append("Fragment");
                break;

            case 12:
                var2.append("Notation");
                var3 = "\'%s\'";
        }

        Object var4;

        if ((var1 & 2) != 0)
        {
            var4 = var0.getUserData("validated");

            if (var4 == null)
            {
                var2.append(" ?");
            }
            else if (!(var4 instanceof Boolean))
            {
                var2.append(" (valid=" + var4 + ")");
            }
            else if (((Boolean)var4).booleanValue())
            {
                var2.append(" +");
            }
            else
            {
                var2.append(" -");
            }
        }

        String var5;
        String var7;

        if (var3 != null)
        {
            var2.append(" ");
            var7 = var0.getPrefix();

            if (var7 == null)
            {
                var7 = "";
            }
            else
            {
                var7 = var7 + ":";
            }

            var5 = var0.getLocalName();

            if (var5 == null)
            {
                var5 = var0.getNodeName();
            }

            var2.append(String.format(var3, new Object[] {var7 + var5}));
        }

        if ((var1 & 16) != 0)
        {
            var4 = var0.getUserData("line-number");

            if (var4 != null)
            {
                var2.append(" [line " + var4 + "]");
            }
        }

        if ((var1 & 4) != 0)
        {
            var7 = var0.getNamespaceURI();

            if (var7 != null)
            {
                var2.append(" (xmlns=" + var7 + ")");
            }
        }

        if ((var1 & 8) != 0)
        {
            var2.append(" {class=" + var0.getClass().getSimpleName() + "}");
        }

        if ((var1 & 1) != 0)
        {
            var4 = var0.getUserData("value");

            if (var4 == null)
            {
                var5 = var0.getNodeValue();

                if (var5 != null)
                {
                    var4 = "\'" + var5.trim() + "\'";
                }
            }

            if (var4 != null)
            {
                var2.append(" = " + var4);
            }
        }

        if ((var1 & 4096) != 0)
        {
            NamedNodeMap var9 = var0.getAttributes();
            String var6;

            for (int var8 = 0; var9 != null && var8 < var9.getLength(); ++var8)
            {
                var6 = "\n" + formatNode(var9.item(var8), var1);
                var2.append(var6.replace("\n", "\n  "));
            }

            for (Node var10 = var0.getFirstChild(); var10 != null; var10 = var10.getNextSibling())
            {
                var6 = "\n" + formatNode(var10, var1);
                var2.append(var6.replace("\n", "\n  "));
            }
        }

        return var2.toString();
    }
}
