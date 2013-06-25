package CustomOreGen.Server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCustomOreGenSettings$GuiOpenMenuButton extends GuiButton
{
    protected final GuiScreen _parentGui;
    protected final GuiScreen _targetGui;

    public GuiCustomOreGenSettings$GuiOpenMenuButton(GuiScreen var1, int var2, int var3, int var4, int var5, int var6, String var7, GuiScreen var8)
    {
        super(var2, var3, var4, var5, var6, var7);
        this._parentGui = var1;
        this._targetGui = var8;
    }

    public int getWidth()
    {
        return this.width;
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft var1, int var2, int var3)
    {
        if (super.mousePressed(var1, var2, var3))
        {
            var1.displayGuiScreen(this._targetGui);
            return true;
        }
        else
        {
            return false;
        }
    }
}
