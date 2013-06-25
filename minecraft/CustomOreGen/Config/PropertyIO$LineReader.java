package CustomOreGen.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

class PropertyIO$LineReader
{
    byte[] inByteBuf;
    char[] inCharBuf;
    char[] lineBuf = new char[1024];
    int inLimit = 0;
    int inOff = 0;
    InputStream inStream;
    Reader reader;

    public PropertyIO$LineReader(InputStream var1)
    {
        this.inStream = var1;
        this.inByteBuf = new byte[8192];
    }

    int readLine() throws IOException
    {
        int var1 = 0;
        boolean var2 = false;
        boolean var3 = true;
        boolean var4 = false;
        boolean var5 = true;
        boolean var6 = false;
        boolean var7 = false;
        boolean var8 = false;

        while (true)
        {
            if (this.inOff >= this.inLimit)
            {
                this.inLimit = this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf);
                this.inOff = 0;

                if (this.inLimit <= 0)
                {
                    if (var1 != 0 && !var4)
                    {
                        return var1;
                    }

                    return -1;
                }
            }

            char var11;

            if (this.inStream != null)
            {
                var11 = (char)(255 & this.inByteBuf[this.inOff++]);
            }
            else
            {
                var11 = this.inCharBuf[this.inOff++];
            }

            if (var8)
            {
                var8 = false;

                if (var11 == 10)
                {
                    continue;
                }
            }

            if (var3)
            {
                if (var11 == 32 || var11 == 9 || var11 == 12 || !var6 && (var11 == 13 || var11 == 10))
                {
                    continue;
                }

                var3 = false;
                var6 = false;
            }

            if (var5)
            {
                var5 = false;

                if (var11 == 35 || var11 == 33)
                {
                    var4 = true;
                    continue;
                }
            }

            if (var11 != 10 && var11 != 13)
            {
                this.lineBuf[var1++] = var11;

                if (var1 == this.lineBuf.length)
                {
                    int var9 = this.lineBuf.length * 2;

                    if (var9 < 0)
                    {
                        var9 = Integer.MAX_VALUE;
                    }

                    char[] var10 = new char[var9];
                    System.arraycopy(this.lineBuf, 0, var10, 0, this.lineBuf.length);
                    this.lineBuf = var10;
                }

                if (var11 == 92)
                {
                    var7 = !var7;
                }
                else
                {
                    var7 = false;
                }
            }
            else if (!var4 && var1 != 0)
            {
                if (this.inOff >= this.inLimit)
                {
                    this.inLimit = this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf);
                    this.inOff = 0;

                    if (this.inLimit <= 0)
                    {
                        return var1;
                    }
                }

                if (!var7)
                {
                    return var1;
                }

                --var1;
                var3 = true;
                var6 = true;
                var7 = false;

                if (var11 == 13)
                {
                    var8 = true;
                }
            }
            else
            {
                var4 = false;
                var5 = true;
                var3 = true;
                var1 = 0;
            }
        }
    }
}
