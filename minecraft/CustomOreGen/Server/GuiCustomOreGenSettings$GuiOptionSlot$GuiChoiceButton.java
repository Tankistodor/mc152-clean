package CustomOreGen.Server;

import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOptionSlot;
import CustomOreGen.Server.GuiCustomOreGenSettings$IOptionControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

class GuiCustomOreGenSettings$GuiOptionSlot$GuiChoiceButton extends GuiButton implements GuiCustomOreGenSettings$IOptionControl
{
    private final ChoiceOption _choice;
    private final int _maxWidth;
    private int mouseX;
    private int mouseY;

    final GuiCustomOreGenSettings$GuiOptionSlot this$1;

    public GuiCustomOreGenSettings$GuiOptionSlot$GuiChoiceButton(GuiCustomOreGenSettings$GuiOptionSlot var1, int var2, int var3, int var4, int var5, int var6, ChoiceOption var7)
    {
        super(var2, var3, var4, var5, var6, (String)null);
        this.this$1 = var1;
        this.mouseX = 0;
        this.mouseY = 0;
        this._choice = var7;
        this._maxWidth = var5;
        this.onValueChanged();
    }

    public ConfigOption getOption()
    {
        return this._choice;
    }

    public GuiButton getControl()
    {
        return this;
    }

    protected void onValueChanged()
    {
        this.displayString = this._choice.getDisplayValue();
        int var1 = this.displayString == null ? 0 : GuiCustomOreGenSettings.access$700(this.this$1.this$0).getStringWidth(this.displayString);
        this.width = Math.min(this._maxWidth, var1 + 10);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (super.mousePressed(var1, var2, var3))
        {
            this._choice.setValue(this._choice.nextPossibleValue());
            this.onValueChanged();
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
        if (this.this$1.isInBounds(this.mouseX, this.mouseY))
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
        super.drawButton(var1, var2, var3);

        if (this.func_82252_a())
        {
            this.this$1.this$0._toolTip = this._choice.getValueDescription();
        }
    }
}
