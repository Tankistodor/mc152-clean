package CustomOreGen.Server;

import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOptionSlot;
import CustomOreGen.Server.GuiCustomOreGenSettings$IOptionControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.settings.EnumOptions;

class GuiCustomOreGenSettings$GuiOptionSlot$GuiNumericSlider extends GuiSlider implements GuiCustomOreGenSettings$IOptionControl
{
    private final NumericOption _numeric;

    final GuiCustomOreGenSettings$GuiOptionSlot this$1;

    public GuiCustomOreGenSettings$GuiOptionSlot$GuiNumericSlider(GuiCustomOreGenSettings$GuiOptionSlot var1, int var2, int var3, int var4, int var5, int var6, NumericOption var7)
    {
        super(var2, var3, var4, EnumOptions.ANAGLYPH, (String)null, (float)var7.getNormalizedDisplayValue());
        this.this$1 = var1;
        this._numeric = var7;
        this.width = var5;
        this.height = var6;
        this.onValueChanged();
    }

    public ConfigOption getOption()
    {
        return this._numeric;
    }

    public GuiButton getControl()
    {
        return this;
    }

    protected void onValueChanged()
    {
        long var1 = 6L;
        long var3 = (long)Math.pow(10.0D, (double)var1);

        for (long var5 = Math.round(this._numeric.getDisplayIncr() * (double)var3); var1 > 0L && var5 % 10L == 0L; --var1)
        {
            var5 /= 10L;
            var3 /= 10L;
        }

        this.displayString = String.format("%." + var1 + "f", new Object[] {Double.valueOf(this._numeric.getDisplayValue())});
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (super.mousePressed(var1, var2, var3))
        {
            this._numeric.setNormalizedDisplayValue((double)this.sliderValue);
            this.onValueChanged();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft var1, int var2, int var3)
    {
        super.mouseDragged(var1, var2, var3);
        this._numeric.setNormalizedDisplayValue((double)this.sliderValue);
        this.onValueChanged();
    }
}
