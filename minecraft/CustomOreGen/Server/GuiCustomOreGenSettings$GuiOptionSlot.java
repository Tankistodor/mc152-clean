package CustomOreGen.Server;

import CustomOreGen.Server.ConfigOption$DisplayState;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOptionSlot$GuiChoiceButton;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOptionSlot$GuiNumericSlider;
import CustomOreGen.Server.GuiCustomOreGenSettings$IOptionControl;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;

public class GuiCustomOreGenSettings$GuiOptionSlot extends GuiSlot
{
    protected final Vector _optionControls;
    protected GuiCustomOreGenSettings$IOptionControl _clickTarget;

    final GuiCustomOreGenSettings this$0;

    public GuiCustomOreGenSettings$GuiOptionSlot(GuiCustomOreGenSettings var1, int var2, int var3, int var4, Vector var5)
    {
        super(GuiCustomOreGenSettings.access$000(var1), var1.width, var1.height, var2, var3, var4);
        this.this$0 = var1;
        this._optionControls = new Vector();
        this._clickTarget = null;

        for (int var6 = 0; var6 < var5.size(); ++var6)
        {
            ConfigOption var7 = (ConfigOption)var5.get(var6);
            Object var8 = null;

            if (var7 instanceof ChoiceOption)
            {
                this._optionControls.add(new GuiCustomOreGenSettings$GuiOptionSlot$GuiChoiceButton(this, var6, 2 * var1.width / 5 + 15, 0, var1.width / 10 + 100, var4 - 6, (ChoiceOption)var7));
            }
            else if (var7 instanceof NumericOption)
            {
                this._optionControls.add(new GuiCustomOreGenSettings$GuiOptionSlot$GuiNumericSlider(this, var6, 2 * var1.width / 5 + 15, 0, var1.width / 10 + 100, var4 - 6, (NumericOption)var7));
            }
        }
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return this._optionControls.size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int var1, boolean var2)
    {
        if (this._clickTarget != null)
        {
            this._clickTarget.getControl().mouseReleased(this.mouseX, this.mouseY);

            if (this._clickTarget.getOption().getDisplayState() == ConfigOption$DisplayState.shown_dynamic)
            {
                this.this$0.refreshGui = 2;
            }

            this._clickTarget = null;
        }

        GuiCustomOreGenSettings$IOptionControl var3 = (GuiCustomOreGenSettings$IOptionControl)this._optionControls.get(var1);

        if (var3.getControl().mousePressed(GuiCustomOreGenSettings.access$100(this.this$0), this.mouseX, this.mouseY))
        {
            GuiCustomOreGenSettings.access$200(this.this$0).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            this._clickTarget = var3;
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int var1)
    {
        return false;
    }

    public boolean isInBounds(int var1, int var2)
    {
        return this.mouseY >= this.top && this.mouseY <= this.bottom && this.mouseX >= 0 && this.mouseX <= this.this$0.width;
    }

    protected void drawBackground() {}

    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5)
    {
        GuiCustomOreGenSettings$IOptionControl var6 = (GuiCustomOreGenSettings$IOptionControl)this._optionControls.get(var1);
        ConfigOption var7 = var6.getOption();
        GuiButton var8 = var6.getControl();
        String var9 = var7.getDisplayName();
        int var10 = GuiCustomOreGenSettings.access$300(this.this$0).getStringWidth(var9);
        int var11 = 2 * this.this$0.width / 5 - 15 - var10;
        int var12 = GuiCustomOreGenSettings.access$400(this.this$0).FONT_HEIGHT;
        int var13 = var3 + 8;
        this.this$0.drawString(GuiCustomOreGenSettings.access$500(this.this$0), var9, var11, var13, 16777215);

        if (this.mouseX >= var11 && this.mouseX <= var11 + var10 && this.mouseY >= var13 && this.mouseY <= var13 + var12 && this.isInBounds(this.mouseX, this.mouseY))
        {
            this.this$0._toolTip = var7.getDescription();
        }

        var8.yPosition = var3 + 3;
        var8.drawButton(GuiCustomOreGenSettings.access$600(this.this$0), this.mouseX, this.mouseY);
    }

    /**
     * draws the slot to the screen, pass in mouse's current x and y and partial ticks
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        super.drawScreen(var1, var2, var3);

        if (this._clickTarget != null && !Mouse.isButtonDown(0))
        {
            this._clickTarget.getControl().mouseReleased(var1, var2);

            if (this._clickTarget.getOption().getDisplayState() == ConfigOption$DisplayState.shown_dynamic)
            {
                this.this$0.refreshGui = 2;
            }

            this._clickTarget = null;
        }
    }
}
