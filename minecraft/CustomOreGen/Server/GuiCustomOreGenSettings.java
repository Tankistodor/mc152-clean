package CustomOreGen.Server;

import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.ConfigOption$DisplayState;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiGroupPanel;
import CustomOreGen.Server.GuiCustomOreGenSettings$GuiOptionSlot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiCustomOreGenSettings extends GuiScreen
{
    protected final GuiScreen _parentGui;
    protected int refreshGui = 3;
    protected GuiButton _doneButton = null;
    protected GuiButton _resetButton = null;
    protected GuiCustomOreGenSettings$GuiOptionSlot _optionPanel = null;
    protected GuiCustomOreGenSettings$GuiGroupPanel _groupPanel = null;
    protected String _toolTip = null;

    public GuiCustomOreGenSettings(GuiScreen var1)
    {
        this._parentGui = var1;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        ConfigOption$DisplayGroup var1 = this._groupPanel == null ? null : this._groupPanel.getSelectedGroup();

        if (this.refreshGui >= 2)
        {
            if (this.refreshGui >= 3)
            {
                WorldConfig.loadedOptionOverrides[0] = null;
            }

            WorldConfig var2 = null;

            while (var2 == null)
            {
                try
                {
                    var2 = new WorldConfig();
                }
                catch (Exception var7)
                {
                    if (ServerState.onConfigError(var7))
                    {
                        var2 = null;
                        continue;
                    }

                    var2 = WorldConfig.createEmptyConfig();
                }

                WorldConfig.loadedOptionOverrides[0] = var2.getConfigOptions();

                if (var1 != null)
                {
                    ConfigOption var3 = var2.getConfigOption(var1.getName());

                    if (var3 instanceof ConfigOption$DisplayGroup)
                    {
                        var1 = (ConfigOption$DisplayGroup)var3;
                    }
                }
            }
        }

        this.refreshGui = 0;
        Vector var9 = new Vector();
        Vector var8 = new Vector();
        Iterator var4 = WorldConfig.loadedOptionOverrides[0].iterator();
        label80:

        while (var4.hasNext())
        {
            ConfigOption var5 = (ConfigOption)var4.next();

            if (var5.getDisplayState() != null && var5.getDisplayState() != ConfigOption$DisplayState.hidden)
            {
                ConfigOption$DisplayGroup var6;

                if (var5 instanceof ConfigOption$DisplayGroup)
                {
                    for (var6 = var1; var6 != var5.getDisplayGroup(); var6 = var6.getDisplayGroup())
                    {
                        if (var6 == null)
                        {
                            continue label80;
                        }
                    }

                    var9.add((ConfigOption$DisplayGroup)var5);
                }
                else
                {
                    for (var6 = var5.getDisplayGroup(); var6 != var1; var6 = var6.getDisplayGroup())
                    {
                        if (var6 == null)
                        {
                            continue label80;
                        }
                    }

                    var8.add(var5);
                }
            }
        }

        if (!var9.isEmpty())
        {
            this._groupPanel = new GuiCustomOreGenSettings$GuiGroupPanel(this, 0, 20, this.width, 20, var1, var9);
        }
        else
        {
            this._groupPanel = null;
        }

        this._optionPanel = new GuiCustomOreGenSettings$GuiOptionSlot(this, var9.isEmpty() ? 16 : 40, this.height - 30, 25, var8);
        this._optionPanel.registerScrollButtons(this.buttonList, 1, 2);
        this._doneButton = new GuiButton(0, this.width / 2 - 155, this.height - 24, 150, 20, "Done");
        this._resetButton = new GuiButton(0, this.width / 2 + 5, this.height - 24, 150, 20, "Defaults");
        this.buttonList.add(this._doneButton);
        this.buttonList.add(this._resetButton);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        super.actionPerformed(var1);

        if (var1 == this._doneButton)
        {
            this.mc.displayGuiScreen(this._parentGui);
        }
        else if (var1 == this._resetButton)
        {
            this.refreshGui = 3;
        }
        else
        {
            this._optionPanel.actionPerformed(var1);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);
        this._groupPanel.mouseClicked(var1, var2, var3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        this._optionPanel.drawScreen(var1, var2, var3);
        this._groupPanel.drawScreen(var1, var2, var3);
        this.drawCenteredString(this.fontRenderer, "CustomOreGen Options", this.width / 2, 4, 16777215);
        super.drawScreen(var1, var2, var3);

        if (this._toolTip != null)
        {
            List var4 = this.fontRenderer.listFormattedStringToWidth(this._toolTip, 2 * this.width / 5 - 8);
            int[] var5 = new int[var4.size()];
            int var6 = 0;
            int var7 = 8 + this.fontRenderer.FONT_HEIGHT * var4.size();
            int var8 = 0;

            for (Iterator var9 = var4.iterator(); var9.hasNext(); ++var8)
            {
                String var10 = (String)var9.next();
                var5[var8] = this.fontRenderer.getStringWidth(var10) + 8;

                if (var6 < var5[var8])
                {
                    var6 = var5[var8];
                }
            }

            int var13 = var1;
            int var14 = var2;

            if (var1 > 2 * this.width / 5)
            {
                var13 = var1 - var6;
            }

            if (var2 > this.height / 2)
            {
                var14 = var2 - var7;
            }

            this.drawGradientRect(var13, var14, var13 + var6, var14 + var7, -15724528, -14671840);
            var8 = 0;

            for (Iterator var11 = var4.iterator(); var11.hasNext(); ++var8)
            {
                String var12 = (String)var11.next();
                this.fontRenderer.drawString(var12, var13 + (var6 - var5[var8]) / 2 + 4, var14 + 4 + var8 * this.fontRenderer.FONT_HEIGHT, 16777215);
            }

            this._toolTip = null;
        }

        if (this.refreshGui > 0)
        {
            this.initGui();
        }
    }

    static Minecraft access$000(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$100(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$200(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static FontRenderer access$300(GuiCustomOreGenSettings var0)
    {
        return var0.fontRenderer;
    }

    static FontRenderer access$400(GuiCustomOreGenSettings var0)
    {
        return var0.fontRenderer;
    }

    static FontRenderer access$500(GuiCustomOreGenSettings var0)
    {
        return var0.fontRenderer;
    }

    static Minecraft access$600(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static FontRenderer access$700(GuiCustomOreGenSettings var0)
    {
        return var0.fontRenderer;
    }

    static FontRenderer access$800(GuiCustomOreGenSettings var0)
    {
        return var0.fontRenderer;
    }

    static Minecraft access$900(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1000(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1100(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1200(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1300(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1400(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1500(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }

    static Minecraft access$1600(GuiCustomOreGenSettings var0)
    {
        return var0.mc;
    }
}
