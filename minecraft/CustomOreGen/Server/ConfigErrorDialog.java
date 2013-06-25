package CustomOreGen.Server;

import CustomOreGen.CustomOreGenBase;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import org.lwjgl.opengl.Display;

public class ConfigErrorDialog implements WindowListener, ActionListener
{
    private boolean _waiting = false;
    private Dialog _dialog = null;
    private Button _abort = null;
    private Button _retry = null;
    private Button _ignore = null;
    private int _returnVal = 0;

    public int showDialog(Frame var1, Throwable var2)
    {
        if (this._dialog != null)
        {
            throw new IllegalStateException("CustomOreGen Config Error Dialog is already open!");
        }
        else
        {
            this._dialog = new Dialog(var1, "CustomOreGen Config Error", false);
            this._dialog.addWindowListener(this);
            TextArea var3 = new TextArea(this.getMessage(var2), 30, 120, 1);
            var3.setEditable(false);
            var3.setBackground(Color.WHITE);
            var3.setFont(new Font("Monospaced", 0, 12));
            this._dialog.add(var3);
            Panel var4 = new Panel();
            this._abort = new Button("Abort");
            this._abort.addActionListener(this);
            var4.add(this._abort);
            this._retry = new Button("Retry");
            this._retry.addActionListener(this);
            var4.add(this._retry);
            this._ignore = new Button("Ignore");
            this._ignore.addActionListener(this);
            var4.add(this._ignore);
            var4.setLayout(new BoxLayout(var4, 0));
            this._dialog.add(var4);
            this._dialog.setLayout(new BoxLayout(this._dialog, 1));
            this._dialog.pack();
            Point var5 = var1.getLocation();
            Dimension var6 = var1.getSize();
            Dimension var7 = this._dialog.getSize();
            var5.x += (var6.width - var7.width) / 2;
            var5.y += (var6.height - var7.height) / 2;
            this._dialog.setLocation(var5);
            this._waiting = true;
            this._returnVal = 0;
            this._dialog.setVisible(true);
            boolean var8 = CustomOreGenBase.isClassLoaded("org.lwjgl.opengl.Display");

            while (this._waiting)
            {
                if (var8 && Display.isCreated())
                {
                    Display.processMessages();
                }
            }

            this._abort = null;
            this._retry = null;
            this._ignore = null;
            this._dialog.setVisible(false);
            this._dialog.dispose();
            this._dialog = null;
            return this._returnVal;
        }
    }

    protected String getMessage(Throwable var1)
    {
        StringBuilder var2 = new StringBuilder();
        var2.append("CustomOreGen has detected an error while trying to load its config files.\n");
        var2.append("At this time you may: \n");
        var2.append("  (1) Abort loading and close Minecraft (click \'Abort\').\n");
        var2.append("  (2) Try to fix the error and then reload the config files (click \'Retry\').\n");
        var2.append("  (3) Ignore the error and continue without loading the config files (click \'Ignore\').\n");
        var2.append("It is strongly recommended that you do not ignore the error.\n");
        var2.append('\n');
        var2.append("------ Error Message ------\n\n");
        var2.append(var1.toString());
        var2.append("\n\n");

        for (Throwable var3 = var1.getCause(); var3 != null; var3 = var3.getCause())
        {
            var2.append("-------- Caused By --------\n\n");
            var2.append(var3.toString());
            var2.append("\n\n");
        }

        return var2.toString();
    }

    public void actionPerformed(ActionEvent var1)
    {
        if (var1.getSource() == this._abort)
        {
            this._returnVal = 0;
            this._waiting = false;
        }
        else if (var1.getSource() == this._retry)
        {
            this._returnVal = 1;
            this._waiting = false;
        }
        else if (var1.getSource() == this._ignore)
        {
            this._returnVal = 2;
            this._waiting = false;
        }
    }

    public void windowClosing(WindowEvent var1)
    {
        this._waiting = false;
    }

    public void windowActivated(WindowEvent var1) {}

    public void windowClosed(WindowEvent var1) {}

    public void windowDeactivated(WindowEvent var1) {}

    public void windowDeiconified(WindowEvent var1) {}

    public void windowIconified(WindowEvent var1) {}

    public void windowOpened(WindowEvent var1) {}
}
