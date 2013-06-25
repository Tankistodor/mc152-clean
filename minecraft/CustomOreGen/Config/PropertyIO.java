package CustomOreGen.Config;

import CustomOreGen.Config.PropertyIO$LineReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PropertyIO
{
    private static final char[] hexDigit = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static void save(Map var0, OutputStream var1, String var2) throws IOException
    {
        BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(var1, "8859_1"));
        boolean var4 = true;

        if (var2 != null)
        {
            writeComments(var3, var2);
        }

        var3.write("#" + (new Date()).toString());
        var3.newLine();
        Iterator var5 = var0.entrySet().iterator();

        while (var5.hasNext())
        {
            Entry var6 = (Entry)var5.next();
            String var7 = saveConvert((String)var6.getKey(), true, var4);
            String var8 = saveConvert((String)var6.getValue(), false, var4);
            var3.write(var7 + "=" + var8);
            var3.newLine();
        }

        var3.flush();
    }

    private static void writeComments(BufferedWriter var0, String var1) throws IOException
    {
        var0.write("#");
        int var2 = var1.length();
        int var3 = 0;
        int var4 = 0;

        for (char[] var5 = new char[] {'\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000'}; var3 < var2; ++var3)
        {
            char var6 = var1.charAt(var3);

            if (var6 > 255 || var6 == 10 || var6 == 13)
            {
                if (var4 != var3)
                {
                    var0.write(var1.substring(var4, var3));
                }

                if (var6 > 255)
                {
                    var5[2] = hexDigit[var6 >> 12 & 15];
                    var5[3] = hexDigit[var6 >> 8 & 15];
                    var5[4] = hexDigit[var6 >> 4 & 15];
                    var5[5] = hexDigit[var6 & 15];
                    var0.write(new String(var5));
                }
                else
                {
                    var0.newLine();

                    if (var6 == 13 && var3 != var2 - 1 && var1.charAt(var3 + 1) == 10)
                    {
                        ++var3;
                    }

                    if (var3 == var2 - 1 || var1.charAt(var3 + 1) != 35 && var1.charAt(var3 + 1) != 33)
                    {
                        var0.write("#");
                    }
                }

                var4 = var3 + 1;
            }
        }

        if (var4 != var3)
        {
            var0.write(var1.substring(var4, var3));
        }

        var0.newLine();
    }

    private static String saveConvert(String var0, boolean var1, boolean var2)
    {
        int var3 = var0.length();
        int var4 = var3 * 2;

        if (var4 < 0)
        {
            var4 = Integer.MAX_VALUE;
        }

        StringBuffer var5 = new StringBuffer(var4);

        for (int var6 = 0; var6 < var3; ++var6)
        {
            char var7 = var0.charAt(var6);

            if (var7 > 61 && var7 < 127)
            {
                if (var7 == 92)
                {
                    var5.append('\\');
                    var5.append('\\');
                }
                else
                {
                    var5.append(var7);
                }
            }
            else
            {
                switch (var7)
                {
                    case 9:
                        var5.append('\\');
                        var5.append('t');
                        break;

                    case 10:
                        var5.append('\\');
                        var5.append('n');
                        break;

                    case 12:
                        var5.append('\\');
                        var5.append('f');
                        break;

                    case 13:
                        var5.append('\\');
                        var5.append('r');
                        break;

                    case 32:
                        if (var6 == 0 || var1)
                        {
                            var5.append('\\');
                        }

                        var5.append(' ');
                        break;

                    case 33:
                    case 35:
                    case 58:
                    case 61:
                        var5.append('\\');
                        var5.append(var7);
                        break;

                    default:
                        if ((var7 < 32 || var7 > 126) & var2)
                        {
                            var5.append('\\');
                            var5.append('u');
                            var5.append(hexDigit[var7 >> 12 & 15]);
                            var5.append(hexDigit[var7 >> 8 & 15]);
                            var5.append(hexDigit[var7 >> 4 & 15]);
                            var5.append(hexDigit[var7 & 15]);
                        }
                        else
                        {
                            var5.append(var7);
                        }
                }
            }
        }

        return var5.toString();
    }

    public static void load(Map var0, InputStream var1) throws IOException
    {
        PropertyIO$LineReader var2 = new PropertyIO$LineReader(var1);
        char[] var3 = new char[1024];
        int var4;

        while ((var4 = var2.readLine()) >= 0)
        {
            boolean var7 = false;
            int var5 = 0;
            int var6 = var4;
            boolean var8 = false;
            boolean var9 = false;

            while (true)
            {
                char var12;

                if (var5 < var4)
                {
                    var12 = var2.lineBuf[var5];

                    if ((var12 == 61 || var12 == 58) && !var9)
                    {
                        var6 = var5 + 1;
                        var8 = true;
                    }
                    else
                    {
                        if (var12 != 32 && var12 != 9 && var12 != 12 || var9)
                        {
                            if (var12 == 92)
                            {
                                var9 = !var9;
                            }
                            else
                            {
                                var9 = false;
                            }

                            ++var5;
                            continue;
                        }

                        var6 = var5 + 1;
                    }
                }

                for (; var6 < var4; ++var6)
                {
                    var12 = var2.lineBuf[var6];

                    if (var12 != 32 && var12 != 9 && var12 != 12)
                    {
                        if (var8 || var12 != 61 && var12 != 58)
                        {
                            break;
                        }

                        var8 = true;
                    }
                }

                String var10 = loadConvert(var2.lineBuf, 0, var5, var3);
                String var11 = loadConvert(var2.lineBuf, var6, var4 - var6, var3);
                var0.put(var10, var11);
                break;
            }
        }
    }

    private static String loadConvert(char[] var0, int var1, int var2, char[] var3)
    {
        if (var3.length < var2)
        {
            int var4 = var2 * 2;

            if (var4 < 0)
            {
                var4 = Integer.MAX_VALUE;
            }

            var3 = new char[var4];
        }

        char[] var5 = var3;
        int var6 = 0;
        int var7 = var1 + var2;

        while (var1 < var7)
        {
            char var10 = var0[var1++];

            if (var10 == 92)
            {
                var10 = var0[var1++];

                if (var10 == 117)
                {
                    int var8 = 0;

                    for (int var9 = 0; var9 < 4; ++var9)
                    {
                        var10 = var0[var1++];

                        switch (var10)
                        {
                            case 48:
                            case 49:
                            case 50:
                            case 51:
                            case 52:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            case 57:
                                var8 = (var8 << 4) + var10 - 48;
                                break;

                            case 58:
                            case 59:
                            case 60:
                            case 61:
                            case 62:
                            case 63:
                            case 64:
                            case 71:
                            case 72:
                            case 73:
                            case 74:
                            case 75:
                            case 76:
                            case 77:
                            case 78:
                            case 79:
                            case 80:
                            case 81:
                            case 82:
                            case 83:
                            case 84:
                            case 85:
                            case 86:
                            case 87:
                            case 88:
                            case 89:
                            case 90:
                            case 91:
                            case 92:
                            case 93:
                            case 94:
                            case 95:
                            case 96:
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");

                            case 65:
                            case 66:
                            case 67:
                            case 68:
                            case 69:
                            case 70:
                                var8 = (var8 << 4) + 10 + var10 - 65;
                                break;

                            case 97:
                            case 98:
                            case 99:
                            case 100:
                            case 101:
                            case 102:
                                var8 = (var8 << 4) + 10 + var10 - 97;
                        }
                    }

                    var5[var6++] = (char)var8;
                }
                else
                {
                    if (var10 == 116)
                    {
                        var10 = 9;
                    }
                    else if (var10 == 114)
                    {
                        var10 = 13;
                    }
                    else if (var10 == 110)
                    {
                        var10 = 10;
                    }
                    else if (var10 == 102)
                    {
                        var10 = 12;
                    }

                    var5[var6++] = var10;
                }
            }
            else
            {
                var5[var6++] = var10;
            }
        }

        return new String(var5, 0, var6);
    }
}
