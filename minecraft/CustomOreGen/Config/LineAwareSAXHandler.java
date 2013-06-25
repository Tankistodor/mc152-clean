package CustomOreGen.Config;

import java.util.Stack;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class LineAwareSAXHandler extends DefaultHandler
{
    private Locator locator = null;
    private final Stack nodeStack = new Stack();

    public LineAwareSAXHandler(Node var1)
    {
        this.nodeStack.push(var1);
    }

    public void setDocumentLocator(Locator var1)
    {
        this.locator = var1;
    }

    public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException
    {
        Node var5 = (Node)this.nodeStack.peek();
        Document var6 = var5.getNodeType() == 9 ? (Document)var5 : var5.getOwnerDocument();
        int var7 = this.locator.getLineNumber();
        Element var8 = var6.createElementNS(var1, var3);
        var8.setUserData("line-number", Integer.valueOf(var7), (UserDataHandler)null);
        var5.appendChild(var8);

        for (int var9 = 0; var9 < var4.getLength(); ++var9)
        {
            Attr var10 = var6.createAttributeNS(var4.getURI(var9), var4.getQName(var9));
            var10.setValue(var4.getValue(var9));
            var10.setUserData("line-number", Integer.valueOf(var7), (UserDataHandler)null);
            var8.setAttributeNodeNS(var10);
        }

        this.nodeStack.push(var8);
    }

    public void endElement(String var1, String var2, String var3)
    {
        Node var4 = (Node)this.nodeStack.pop();
    }

    public void characters(char[] var1, int var2, int var3) throws SAXException
    {
        Node var4 = (Node)this.nodeStack.peek();
        Document var5 = var4.getNodeType() == 9 ? (Document)var4 : var4.getOwnerDocument();
        Text var6 = var5.createTextNode(new String(var1, var2, var3));
        var6.setUserData("line-number", Integer.valueOf(this.locator.getLineNumber()), (UserDataHandler)null);
        var4.appendChild(var6);
    }

    public void fatalError(SAXParseException var1) throws ParserException
    {
        throw new ParserException(var1.getMessage(), (Node)this.nodeStack.peek(), this.locator.getLineNumber(), var1);
    }
}
