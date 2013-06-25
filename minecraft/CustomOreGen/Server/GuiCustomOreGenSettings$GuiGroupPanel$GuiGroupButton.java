package CustomOreGen.Server;

import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiGroupPanel;
import CustomOreGen.Server.GuiCustomOreGenSettings$IOptionControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

class GuiCustomOreGenSettings$GuiGroupPanel$GuiGroupButton extends GuiButton implements GuiCustomOreGenSettings$IOptionControl
{
    protected final ConfigOption$DisplayGroup _group;
    private final int _relX;
    private final int _relY;

    final GuiCustomOreGenSettings$GuiGroupPanel this$1;

    public GuiCustomOreGenSettings$GuiGroupPanel$GuiGroupButton(GuiCustomOreGenSettings$GuiGroupPanel var1, int var2, int var3, int var4, int var5, int var6, String var7, ConfigOption$DisplayGroup var8)
    {
        super(var2, var3, var4, var5, var6, var7);
        this.this$1 = var1;
        this._group = var8;
        this._relX = var3;
        this._relY = var4;
    }

    public ConfigOption getOption()
    {
        return this._group;
    }

    public GuiButton getControl()
    {
        return this;
    }

    private boolean isButtonVisible()
    {
        this.xPosition = this.this$1.posX + this.this$1._scrollHInset + this.this$1._scrollOffsetX + this._relX;
        this.yPosition = this.this$1.posY + this._relY;
        return !this.this$1.isInScrollArea(this.xPosition, this.yPosition) && !this.this$1.isInScrollArea(this.xPosition + this.width, this.yPosition + this.height);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (this.isButtonVisible() && this.func_82252_a() && super.mousePressed(var1, var2, var3))
        {
            this.this$1._currentGroup = this;
            this.this$1.this$0.refreshGui = 1;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    public int getHoverState(boolean var1)
    {
        if (this.this$1.isInScrollArea(this.this$1.mouseX, this.this$1.mouseY))
        {
            return super.getHoverState(var1);
        }
        else
        {
            this.field_82253_i = false;
            return 1;
        }
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft var1, int var2, int var3)
    {
        if (this.isButtonVisible() && this.drawButton)
        {
            this.field_82253_i = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
            int var4 = this.getHoverState(this.field_82253_i);
            this.mouseDragged(var1, var2, var3);

            if (this.this$1._currentGroup == this)
            {
                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -1610612736);
            }
            else
            {
                if (this._group == null)
                {
                    drawRect(this.xPosition, this.yPosition, this.xPosition + 1, this.yPosition + this.height, -16777216);
                }

                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + 1, -16777216);
                drawRect(this.xPosition + this.width - 1, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
            }

            FontRenderer var5 = var1.fontRenderer;
            int var6 = this.enabled ? (this.field_82253_i ? 16777120 : 14737632) : 10526880;
            this.drawCenteredString(var5, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);

            if (this.func_82252_a() && this._group != null)
            {
                this.this$1.this$0._toolTip = this._group.getDescription();
            }
        }
    }
}
