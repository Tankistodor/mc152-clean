package CustomOreGen.Config;

import CustomOreGen.Config.ValidatorImport$WildcardFileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class ValidatorImport extends ValidatorNode
{
    protected boolean required = true;

    protected ValidatorImport(ValidatorNode var1, Node var2, boolean var3)
    {
        super(var1, var2);
        this.required = var3;
    }

    protected boolean validateChildren() throws ParserException
    {
        super.validateChildren();
        File var1 = (File)this.getNode().getOwnerDocument().getUserData("value");
        File var2 = var1.getParentFile();
        String var3 = (String)this.validateRequiredAttribute(String.class, "file", true);
        List var4 = getMatchingFiles(var2, var3);
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.checkChildrenValid();

        if (var4.isEmpty())
        {
            if (this.required)
            {
                throw new ParserException("No files found matching \'" + var3 + "\'.", this.getNode());
            }

            this.replaceWithNode(new Node[0]);
        }
        else
        {
            ArrayList var5 = new ArrayList(var4.size());
            Iterator var6 = var4.iterator();

            while (var6.hasNext())
            {
                File var7 = (File)var6.next();
                Element var8 = this.getNode().getOwnerDocument().createElement("ImportedDoc");
                this.getNode().appendChild(var8);
                var8.setUserData("value", var7, (UserDataHandler)null);

                try
                {
                    this.getParser().saxParser.parse(var7, new LineAwareSAXHandler(var8));
                }
                catch (Exception var10)
                {
                    throw new ParserException(var10.getMessage(), this.getNode(), var10);
                }

                (new ValidatorUnchecked(this, var8)).validate();
                var5.add(var8);
            }

            this.replaceWithNodeContents((Node[])var5.toArray(new Node[var5.size()]));
        }

        return false;
    }

    private static List getMatchingFiles(File var0, String var1)
    {
        Object var2 = Arrays.asList(new File[] {(var0 == null ? new File("") : var0).getAbsoluteFile()});
        Stack var3 = new Stack();

        for (File var4 = new File(var1); var4 != null; var4 = var4.getParentFile())
        {
            var3.push(var4.getName());
        }

        while (!var3.isEmpty())
        {
            String var12 = (String)var3.pop();
            LinkedList var5 = new LinkedList();
            Iterator var6 = ((List)var2).iterator();

            while (var6.hasNext())
            {
                File var7 = (File)var6.next();

                if (!var12.contains("*") && !var12.contains("?"))
                {
                    File var13 = new File(var7, var12);

                    if (var13.exists())
                    {
                        var5.add(var13);
                    }
                }
                else
                {
                    File[] var8 = var7.listFiles(new ValidatorImport$WildcardFileFilter(var12));
                    int var9 = var8.length;

                    for (int var10 = 0; var10 < var9; ++var10)
                    {
                        File var11 = var8[var10];

                        if (var11.exists())
                        {
                            var5.add(var11);
                        }
                    }
                }
            }

            var2 = var5;
        }

        return (List)var2;
    }
}
