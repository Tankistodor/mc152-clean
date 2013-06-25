package CustomOreGen.Server;

import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiGroupPanel$GuiGroupButton;
import CustomOreGen.Server.GuiCustomOreGenSettings$IOptionControl;
import java.util.Iterator;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiCustomOreGenSettings$GuiGroupPanel
{
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected int _scrollHInset;
    protected int _scrollOffsetX;
    protected int _scrollOffsetMax;
    protected int mouseX;
    protected int mouseY;
    protected final Vector _groupButtons;
    protected GuiButton _groupScrollLButton;
    protected GuiButton _groupScrollRButton;
    protected GuiCustomOreGenSettings$IOptionControl _currentGroup;
    protected GuiButton _currentButton;

    final GuiCustomOreGenSettings this$0;

    public GuiCustomOreGenSettings$GuiGroupPanel(GuiCustomOreGenSettings var1, int var2, int var3, int var4, int var5, ConfigOption$DisplayGroup var6, Vector var7)
    {
        this.this$0 = var1;
        this.posX = 0;
        this.posY = 0;
        this.width = 0;
        this.height = 0;
        this._scrollHInset = 0;
        this._scrollOffsetX = 0;
        this._scrollOffsetMax = 0;
        this.mouseX = 0;
        this.mouseY = 0;
        this._groupButtons = new Vector();
        this._groupScrollLButton = null;
        this._groupScrollRButton = null;
        this._currentGroup = null;
        this._currentButton = null;
        this.posX = var2;
        this.posY = var3;
        this.width = var4;
        this.height = var5;
        int var8 = 0;
        int var9 = 0;

        for (int var10 = -1; !var7.isEmpty() && var10 < var7.size(); ++var10)
        {
            ConfigOption$DisplayGroup var11 = var10 < 0 ? null : (ConfigOption$DisplayGroup)var7.get(var10);
            String var12 = var10 < 0 ? "[ All ]" : var11.getDisplayName();
            int var13 = GuiCustomOreGenSettings.access$800(var1).getStringWidth(var12) + 10;
            GuiCustomOreGenSettings$GuiGroupPanel$GuiGroupButton var14 = new GuiCustomOreGenSettings$GuiGroupPanel$GuiGroupButton(this, var10 + 1, var8, 0, var13, var5, var12, var11);
            this._groupButtons.add(var14);

            if (var11 == var6)
            {
                var9 = (var4 - var13) / 2 - var8;
                this._currentGroup = var14;
            }

            var8 += var13;
        }

        this._scrollHInset = 4;
        this._scrollOffsetMax = var4 - 2 * this._scrollHInset - var8;

        if (this._scrollOffsetMax < 0)
        {
            this._groupScrollLButton = new GuiButton(0, var2 + 4, var3, 20, var5, "<");
            this._groupScrollRButton = new GuiButton(0, var2 + var4 - 24, var3, 20, var5, ">");
            this._scrollHInset = 26;
            this._scrollOffsetMax = var4 - 2 * this._scrollHInset - var8;
            this._scrollOffsetX = Math.min(Math.max(var9 - this._scrollHInset, this._scrollOffsetMax), 0);
        }
        else
        {
            this._scrollOffsetX = this._scrollOffsetMax / 2;
        }
    }

    public ConfigOption$DisplayGroup getSelectedGroup()
    {
        return this._currentGroup == null ? null : (ConfigOption$DisplayGroup)this._currentGroup.getOption();
    }

    public int getScrollPos()
    {
        return this._scrollOffsetX;
    }

    public boolean isInScrollArea(int var1, int var2)
    {
        int var3 = var1 - this.posX;
        int var4 = var2 - this.posY;
        return var3 >= this._scrollHInset && var3 <= this.width - this._scrollHInset && var4 > 0 && var4 < this.height;
    }

    public void drawScreen(int var1, int var2, float var3)
    {
        this.mouseX = var1;
        this.mouseY = var2;
        Iterator var4 = this._groupButtons.iterator();

        while (var4.hasNext())
        {
            GuiCustomOreGenSettings$IOptionControl var5 = (GuiCustomOreGenSettings$IOptionControl)var4.next();
            var5.getControl().drawButton(GuiCustomOreGenSettings.access$900(this.this$0), var1, var2);
        }

        Tessellator var7 = Tessellator.instance;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GuiCustomOreGenSettings.access$1000(this.this$0).renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        double var8 = 32.0D;
        var7.startDrawingQuads();
        var7.setColorRGBA_I(4473924, 255);
        var7.addVertexWithUV((double)this.posX, (double)(this.posY + this.height), 0.0D, 0.0D, (double)this.height / var8);
        var7.addVertexWithUV((double)(this.posX + this._scrollHInset), (double)(this.posY + this.height), 0.0D, (double)this._scrollHInset / var8, (double)this.height / var8);
        var7.addVertexWithUV((double)(this.posX + this._scrollHInset), (double)this.posY, 0.0D, (double)this._scrollHInset / var8, 0.0D);
        var7.addVertexWithUV((double)this.posX, (double)this.posY, 0.0D, 0.0D, 0.0D);
        var7.addVertexWithUV((double)(this.posX + this.width - this._scrollHInset), (double)(this.posY + this.height), 0.0D, 0.0D, (double)this.height / var8);
        var7.addVertexWithUV((double)(this.posX + this.width), (double)(this.posY + this.height), 0.0D, (double)this._scrollHInset / var8, (double)this.height / var8);
        var7.addVertexWithUV((double)(this.posX + this.width), (double)this.posY, 0.0D, (double)this._scrollHInset / var8, 0.0D);
        var7.addVertexWithUV((double)(this.posX + this.width - this._scrollHInset), (double)this.posY, 0.0D, 0.0D, 0.0D);
        var7.draw();

        if (this._groupScrollLButton != null)
        {
            this._groupScrollLButton.drawButton(GuiCustomOreGenSettings.access$1100(this.this$0), var1, var2);

            if (this._groupScrollLButton == this._currentButton)
            {
                this._scrollOffsetX = Math.min(0, this._scrollOffsetX + 1);
            }
        }

        if (this._groupScrollRButton != null)
        {
            this._groupScrollRButton.drawButton(GuiCustomOreGenSettings.access$1200(this.this$0), var1, var2);

            if (this._groupScrollRButton == this._currentButton)
            {
                this._scrollOffsetX = Math.max(this._scrollOffsetMax, this._scrollOffsetX - 1);
            }
        }

        if (this._currentButton != null && !Mouse.isButtonDown(0))
        {
            this._currentButton = null;
        }
    }

    public void mouseClicked(int var1, int var2, int var3)
    {
        this.mouseX = var1;
        this.mouseY = var2;

        if (var3 == 0)
        {
            this._currentButton = null;
            Iterator var4 = this._groupButtons.iterator();

            while (var4.hasNext())
            {
                GuiCustomOreGenSettings$IOptionControl var5 = (GuiCustomOreGenSettings$IOptionControl)var4.next();

                if (var5.getControl().mousePressed(GuiCustomOreGenSettings.access$1300(this.this$0), var1, var2))
                {
                    this._currentButton = var5.getControl();
                    GuiCustomOreGenSettings.access$1400(this.this$0).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    break;
                }
            }

            if (this._groupScrollLButton != null && this._groupScrollLButton.mousePressed(GuiCustomOreGenSettings.access$1500(this.this$0), var1, var2))
            {
                this._currentButton = this._groupScrollLButton;
            }
            else if (this._groupScrollRButton != null && this._groupScrollRButton.mousePressed(GuiCustomOreGenSettings.access$1600(this.this$0), var1, var2))
            {
                this._currentButton = this._groupScrollRButton;
            }
        }
    }
}
