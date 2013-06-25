package CustomOreGen.Util;

import CustomOreGen.Util.GeometryStream$1;
import CustomOreGen.Util.GeometryStream$GeometryStreamException;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class GeometryStream implements IGeometryBuilder
{
    private static final float[] _normalVectorTable = new float[6240];
    private byte[] _stream = null;
    private int _readPos = 0;
    private int _endPos = 0;
    private int _flags = 0;

    public GeometryStream() {}

    public GeometryStream(InputStream var1) throws IOException
    {
        if (var1 != null)
        {
            int var2 = var1.read();
            var2 |= var1.read() << 8;
            var2 |= var1.read() << 16;
            var2 |= var1.read() << 24;

            if (var2 > 0)
            {
                this._stream = new byte[Integer.highestOneBit(var2 * 2 - 1)];

                for (int var3 = 0; var3 < var2; var3 += var1.read(this._stream, var3, var2 - var3))
                {
                    ;
                }

                this._endPos = var2 * 8;
                this._readPos = 0;
            }
        }
    }

    public void setPositionTransform(Transform var1)
    {
        if (var1 != null)
        {
            this.packBits(15, 6);

            for (int var2 = 0; var2 < 12; ++var2)
            {
                this.packBits(Float.floatToRawIntBits(var1.element(var2 / 4, var2 % 4)), 32);
            }
        }
        else
        {
            this.packBits(14, 6);
        }
    }

    public void setNormal(float[] var1)
    {
        if (var1 != null)
        {
            this.packBits(13, 6);
            this.packBits(compressNormalVector(var1[0], var1[1], var1[2]), 18);
        }
        else
        {
            this.packBits(12, 6);
        }
    }

    public void setColor(float[] var1)
    {
        if (var1 != null)
        {
            this.packBits(11, 6);
            this.packBits(to32BitColor(var1[0], var1[1], var1[2], var1.length > 3 ? var1[3] : 1.0F), 32);
        }
        else
        {
            this.packBits(10, 6);
        }
    }

    public void setTexture(String var1)
    {
        if (var1 != null)
        {
            if (var1.length() > 65535)
            {
                throw new IllegalArgumentException("Texture URIs longer than 65,535 characters are not supported");
            }

            this.packBits(39, 6);
            this.packBits(var1.length(), 16);

            for (int var2 = 0; var2 < var1.length(); ++var2)
            {
                this.packBits(var1.charAt(var2), 8);
            }
        }
        else
        {
            this.packBits(36, 6);
        }
    }

    public void setTextureTransform(Transform var1)
    {
        if (var1 != null)
        {
            this.packBits(9, 6);

            for (int var2 = 0; var2 < 12; ++var2)
            {
                this.packBits(Float.floatToRawIntBits(var1.element(var2 / 4, var2 % 4)), 32);
            }
        }
        else
        {
            this.packBits(8, 6);
        }
    }

    public void setTextureCoordinates(float[] var1)
    {
        if (var1 != null)
        {
            int var2 = 34;
            boolean var3 = this.useFullTexPrecision(var1);

            if (var3)
            {
                var2 |= 1;
            }

            this.packBits(var2, 6);

            if (var3)
            {
                this.packBits(Float.floatToRawIntBits(var1[0]), 32);
                this.packBits(Float.floatToRawIntBits(var1[1]), 32);
            }
            else
            {
                this.packBits(toHalfFloat(var1[0]), 16);
                this.packBits(toHalfFloat(var1[1]), 16);
            }
        }
        else
        {
            this.packBits(32, 6);
        }
    }

    public void setVertexMode(IGeometryBuilder$PrimitiveType var1, int ... var2)
    {
        int var3 = 16;
        byte var4 = 0;

        if (var1 != null)
        {
            switch (GeometryStream$1.$SwitchMap$net$minecraft$src$CustomOreGen$Util$IGeometryBuilder$PrimitiveType[var1.ordinal()])
            {
                case 1:
                    var3 |= 1;
                    break;

                case 2:
                    var3 |= 2;
                    var4 = 1;
                    break;

                case 3:
                    var3 |= 4;
                    var4 = 2;
                    break;

                case 4:
                    var3 |= 8;
                    var4 = 2;
                    break;

                case 5:
                    var3 |= 12;
                    var4 = 3;
            }
        }

        int var5 = Math.min(var4, var2.length);
        this.packBits(var3 | var5, 6);

        for (int var6 = 0; var6 < var5; ++var6)
        {
            if (var2[var6] > 65535)
            {
                throw new IllegalArgumentException("Vertex indices larger than 65,535 are not supported");
            }

            this.packBits(var2[var6], 16);
        }
    }

    public void addVertex(float[] var1)
    {
        this.addVertex(var1, (float[])null, (float[])null, (float[])null);
    }

    public void addVertex(float[] var1, float[] var2, float[] var3, float[] var4)
    {
        boolean var5 = this.useFullPosPrecision(var1);
        boolean var6 = false;
        byte var7 = 32;
        int var8;

        if (var4 != null)
        {
            var8 = var7 | 16;
            var6 = this.useFullTexPrecision(var4);

            if (var6)
            {
                var8 |= 8;
            }
        }
        else
        {
            var8 = var7 | 8;
        }

        if (var3 != null)
        {
            var8 |= 4;
        }

        if (var2 != null)
        {
            var8 |= 2;
        }

        if (var5)
        {
            var8 |= 1;
        }

        this.packBits(var8, 6);

        if (var5)
        {
            this.packBits(Float.floatToRawIntBits(var1[0]), 32);
            this.packBits(Float.floatToRawIntBits(var1[1]), 32);
            this.packBits(Float.floatToRawIntBits(var1[2]), 32);
        }
        else
        {
            this.packBits(toHalfFloat(var1[0]), 16);
            this.packBits(toHalfFloat(var1[1]), 16);
            this.packBits(toHalfFloat(var1[2]), 16);
        }

        if (var2 != null)
        {
            this.packBits(compressNormalVector(var2[0], var2[1], var2[2]), 18);
        }

        if (var3 != null)
        {
            this.packBits(to32BitColor(var3[0], var3[1], var3[2], var3.length > 3 ? var3[3] : 1.0F), 32);
        }

        if (var4 != null)
        {
            if (var6)
            {
                this.packBits(Float.floatToRawIntBits(var4[0]), 32);
                this.packBits(Float.floatToRawIntBits(var4[1]), 32);
            }
            else
            {
                this.packBits(toHalfFloat(var4[0]), 16);
                this.packBits(toHalfFloat(var4[1]), 16);
            }
        }
    }

    public void addVertexRef(int var1)
    {
        if (var1 < 256)
        {
            this.packBits(6, 6);
            this.packBits(var1, 8);
        }
        else
        {
            this.packBits(7, 6);
            this.packBits(var1, 32);
        }
    }

    private void execSetPositionTransform(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            Transform var3 = new Transform();

            for (int var4 = 0; var4 < 12; ++var4)
            {
                var3.setElement(var4 / 4, var4 % 4, Float.intBitsToFloat(this.unpackBits(32)));
            }

            var2.setPositionTransform(var3);
        }
        else
        {
            var2.setPositionTransform((Transform)null);
        }
    }

    private void execSetNormal(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            var2.setNormal(decompressNormalVector(this.unpackBits(18), (float[])null));
        }
        else
        {
            var2.setNormal((float[])null);
        }
    }

    private void execSetColor(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            var2.setColor(toFloatColor(this.unpackBits(32), (float[])null));
        }
        else
        {
            var2.setColor((float[])null);
        }
    }

    private void execSetTexture(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 3) != 0)
        {
            if ((var1 & 3) != 3)
            {
                throw new GeometryStream$GeometryStreamException("Unexpected SetTexture mode (" + var1 + ").");
            }

            int var3 = this.unpackBits(16);
            StringBuilder var4 = new StringBuilder(var3);

            for (int var5 = 0; var5 < var3; ++var5)
            {
                var4.append((char)this.unpackBits(8));
            }

            var2.setTexture(var4.toString());
        }
        else
        {
            var2.setTexture((String)null);
        }
    }

    private void execSetTextureTransform(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            Transform var3 = new Transform();

            for (int var4 = 0; var4 < 12; ++var4)
            {
                var3.setElement(var4 / 4, var4 % 4, Float.intBitsToFloat(this.unpackBits(32)));
            }

            var2.setTextureTransform(var3);
        }
        else
        {
            var2.setTextureTransform((Transform)null);
        }
    }

    private void execSetTextureCoordinates(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 2) != 0)
        {
            float[] var3 = new float[2];

            if ((var1 & 1) != 0)
            {
                var3[0] = Float.intBitsToFloat(this.unpackBits(32));
                var3[1] = Float.intBitsToFloat(this.unpackBits(32));
            }
            else
            {
                var3[0] = fromHalfFloat(this.unpackBits(16));
                var3[1] = fromHalfFloat(this.unpackBits(16));
            }

            var2.setTextureCoordinates(var3);
        }
        else
        {
            if ((var1 & 1) != 0)
            {
                throw new GeometryStream$GeometryStreamException("Unexpected SetTextureCoordinates mode (" + var1 + ").");
            }

            var2.setTextureCoordinates((float[])null);
        }
    }

    private void execSetVertexMode(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        IGeometryBuilder$PrimitiveType var3 = null;
        int var4 = 0;

        if ((var1 & 8) != 0)
        {
            if ((var1 & 4) != 0)
            {
                var3 = IGeometryBuilder$PrimitiveType.QUAD;
                var4 = var1 & 3;
            }
            else
            {
                var3 = IGeometryBuilder$PrimitiveType.TRIANGLE;
                var4 = var1 & 3;

                if (var4 == 3)
                {
                    throw new IllegalArgumentException("Triangle mode may not specify more than 3 implict references!");
                }
            }
        }
        else if ((var1 & 4) != 0)
        {
            var3 = IGeometryBuilder$PrimitiveType.TRIANGLE_ALT;
            var4 = var1 & 3;

            if (var4 == 3)
            {
                throw new IllegalArgumentException("Triangle mode may not specify more than 3 implict references!");
            }
        }
        else if ((var1 & 2) != 0)
        {
            var3 = IGeometryBuilder$PrimitiveType.LINE;
            var4 = var1 & 1;
        }
        else if ((var1 & 1) != 0)
        {
            var3 = IGeometryBuilder$PrimitiveType.POINT;
        }

        int[] var5 = new int[var4];

        for (int var6 = 0; var6 < var4; ++var6)
        {
            var5[var6] = this.unpackBits(16);
        }

        var2.setVertexMode(var3, var5);
    }

    private void execAddVertex(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        float[] var3 = new float[3];

        if ((var1 & 1) != 0)
        {
            var3[0] = Float.intBitsToFloat(this.unpackBits(32));
            var3[1] = Float.intBitsToFloat(this.unpackBits(32));
            var3[2] = Float.intBitsToFloat(this.unpackBits(32));
        }
        else
        {
            var3[0] = fromHalfFloat(this.unpackBits(16));
            var3[1] = fromHalfFloat(this.unpackBits(16));
            var3[2] = fromHalfFloat(this.unpackBits(16));
        }

        float[] var4 = null;

        if ((var1 & 2) != 0)
        {
            var4 = decompressNormalVector(this.unpackBits(18), (float[])null);
        }

        float[] var5 = null;

        if ((var1 & 4) != 0)
        {
            var5 = toFloatColor(this.unpackBits(32), (float[])null);
        }

        float[] var6 = null;

        if ((var1 & 16) != 0)
        {
            var6 = new float[2];

            if ((var1 & 8) != 0)
            {
                var6[0] = Float.intBitsToFloat(this.unpackBits(32));
                var6[1] = Float.intBitsToFloat(this.unpackBits(32));
            }
            else
            {
                var6[0] = fromHalfFloat(this.unpackBits(16));
                var6[1] = fromHalfFloat(this.unpackBits(16));
            }
        }

        var2.addVertex(var3, var4, var5, var6);
    }

    private void execAddVertexRef(int var1, IGeometryBuilder var2) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            var2.addVertexRef(this.unpackBits(32));
        }
        else
        {
            var2.addVertexRef(this.unpackBits(8));
        }
    }

    private void execNOP(int var1) throws GeometryStream$GeometryStreamException
    {
        if ((var1 & 1) != 0)
        {
            this._readPos = this._readPos + 7 & -8;
        }
    }

    public int executeStream(IGeometryBuilder var1) throws GeometryStream$GeometryStreamException
    {
        if (var1 == null)
        {
            return 0;
        }
        else
        {
            int var2 = this._readPos;
            int var3 = 0;

            while (this._readPos < this._endPos)
            {
                int var4 = this.unpackBits(6);
                ++var3;

                if ((var4 & 32) != 0)
                {
                    if ((var4 & 24) != 0)
                    {
                        this.execAddVertex(var4, var1);
                    }
                    else if ((var4 & 4) != 0)
                    {
                        this.execSetTexture(var4, var1);
                    }
                    else
                    {
                        this.execSetTextureCoordinates(var4, var1);
                    }
                }
                else if ((var4 & 16) != 0)
                {
                    this.execSetVertexMode(var4, var1);
                }
                else if ((var4 & 8) != 0)
                {
                    if ((var4 & 4) != 0)
                    {
                        if ((var4 & 2) != 0)
                        {
                            this.execSetPositionTransform(var4, var1);
                        }
                        else
                        {
                            this.execSetNormal(var4, var1);
                        }
                    }
                    else if ((var4 & 2) != 0)
                    {
                        this.execSetColor(var4, var1);
                    }
                    else
                    {
                        this.execSetTextureTransform(var4, var1);
                    }
                }
                else if ((var4 & 4) != 0)
                {
                    if ((var4 & 2) == 0)
                    {
                        throw new GeometryStream$GeometryStreamException("Invalid opcode (" + var4 + ") found in stream.");
                    }

                    this.execAddVertexRef(var4, var1);
                }
                else
                {
                    if ((var4 & 2) == 0)
                    {
                        throw new GeometryStream$GeometryStreamException("Invalid opcode (" + var4 + ") found in stream.");
                    }

                    this.execNOP(var4);
                    --var3;
                }
            }

            this._readPos = var2;
            return var3;
        }
    }

    public int getStreamDataSize()
    {
        return (this._endPos + 32 + 13) / 8;
    }

    private void getRawStreamData(OutputStream var1) throws IOException
    {
        if (var1 != null)
        {
            int var2 = this._endPos;
            this.packBits(3, 6);
            var1.write(this._stream, 0, (this._endPos + 7) / 8);
            this._endPos = var2;
        }
    }

    public int getStreamData(OutputStream var1) throws IOException
    {
        if (var1 == null)
        {
            return 0;
        }
        else
        {
            int var2 = (this._endPos + 13) / 8;
            var1.write((byte)var2);
            var1.write((byte)(var2 >> 8));
            var1.write((byte)(var2 >> 16));
            var1.write((byte)(var2 >> 24));
            this.getRawStreamData(var1);
            return var2 + 4;
        }
    }

    public static int getStreamData(Collection var0, OutputStream var1) throws IOException
    {
        if (var1 == null)
        {
            return 0;
        }
        else
        {
            int var2 = 0;
            Iterator var3 = var0.iterator();
            GeometryStream var4;

            while (var3.hasNext())
            {
                var4 = (GeometryStream)var3.next();

                if (var4 != null)
                {
                    var2 += (var4._endPos + 13) / 8;
                }
            }

            var1.write((byte)var2);
            var1.write((byte)(var2 >> 8));
            var1.write((byte)(var2 >> 16));
            var1.write((byte)(var2 >> 24));
            var3 = var0.iterator();

            while (var3.hasNext())
            {
                var4 = (GeometryStream)var3.next();

                if (var4 != null)
                {
                    var4.getRawStreamData(var1);
                }
            }

            return var2 + 4;
        }
    }

    public void forceFullPrecisionPosCoords(boolean var1)
    {
        if (var1)
        {
            this._flags |= 1;
        }
        else
        {
            this._flags &= -2;
        }
    }

    public void forceFullPrecisionTexCoords(boolean var1)
    {
        if (var1)
        {
            this._flags |= 2;
        }
        else
        {
            this._flags &= -3;
        }
    }

    private boolean useFullPosPrecision(float[] var1)
    {
        return (this._flags & 1) != 0;
    }

    private boolean useFullTexPrecision(float[] var1)
    {
        return (this._flags & 2) != 0;
    }

    private void packBits(int var1, int var2)
    {
        if (var2 > 0)
        {
            if (var2 > 32)
            {
                var2 = 32;
            }

            if (this._stream == null)
            {
                this._stream = new byte[192];
                this._endPos = 0;
            }
            else if ((this._endPos + var2 + 7) / 8 > this._stream.length)
            {
                this._stream = Arrays.copyOf(this._stream, this._stream.length * 2);
            }

            int var3 = var2 == 32 ? var1 : var1 & ~(-1 << var2);
            int var4 = this._endPos / 8;
            int var5 = this._endPos % 8;

            for (this._endPos += var2; var2 > 0; ++var4)
            {
                int var6 = Math.min(var2, 8 - var5);
                this._stream[var4] = (byte)(this._stream[var4] | var3 << var5);
                var2 -= var6;
                var3 >>>= var6;
                var5 = 0;
            }
        }
    }

    private int unpackBits(int var1) throws GeometryStream$GeometryStreamException
    {
        if (var1 <= 0)
        {
            return 0;
        }
        else
        {
            if (var1 > 32)
            {
                var1 = 32;
            }

            if (this._readPos <= 0)
            {
                this._readPos = 0;
            }

            if (this._readPos + var1 > this._endPos)
            {
                throw new GeometryStream$GeometryStreamException("Unexpected end of stream.");
            }
            else
            {
                int var2 = 0;
                int var3 = this._readPos / 8;
                int var4 = this._readPos % 8;
                this._readPos += var1;

                for (int var5 = 0; var1 > 0; ++var3)
                {
                    int var6 = Math.min(var1, 8 - var4);
                    var1 -= var6;
                    var2 |= (this._stream[var3] >>> var4 & ~(-1 << var6)) << var5;
                    var5 += var6;
                    var4 = 0;
                }

                return var2;
            }
        }
    }

    private static int to32BitColor(float var0, float var1, float var2, float var3)
    {
        if (var0 < 0.0F)
        {
            var0 = 0.0F;
        }
        else if (var0 > 1.0F)
        {
            var0 = 1.0F;
        }

        if (var1 < 0.0F)
        {
            var1 = 0.0F;
        }
        else if (var1 > 1.0F)
        {
            var1 = 1.0F;
        }

        if (var2 < 0.0F)
        {
            var2 = 0.0F;
        }
        else if (var2 > 1.0F)
        {
            var2 = 1.0F;
        }

        if (var3 < 0.0F)
        {
            var3 = 0.0F;
        }
        else if (var3 > 1.0F)
        {
            var3 = 1.0F;
        }

        int var4 = (int)(var0 * 255.0F) << 24;
        var4 |= (int)(var1 * 255.0F) << 16;
        var4 |= (int)(var2 * 255.0F) << 8;
        var4 |= (int)(var3 * 255.0F);
        return var4;
    }

    private static float[] toFloatColor(int var0, float[] var1)
    {
        if (var1 == null)
        {
            var1 = new float[4];
        }

        var1[0] = (float)(var0 >>> 24 & 255) / 255.0F;
        var1[1] = (float)(var0 >>> 16 & 255) / 255.0F;
        var1[2] = (float)(var0 >>> 8 & 255) / 255.0F;
        var1[3] = (float)(var0 & 255) / 255.0F;
        return var1;
    }

    private static int toHalfFloat(float var0)
    {
        int var1 = Float.floatToIntBits(var0);
        int var2 = var1 >>> 16 & 32768;
        int var3 = (var1 & Integer.MAX_VALUE) + 4096;

        if (var3 >= 1199570944)
        {
            return (var1 & Integer.MAX_VALUE) >= 1199570944 ? (var3 < 2139095040 ? var2 | 31744 : var2 | 31744 | (var1 & 8388607) >>> 13) : var2 | 31743;
        }
        else if (var3 >= 947912704)
        {
            return var2 | var3 - 939524096 >>> 13;
        }
        else if (var3 < 855638016)
        {
            return var2;
        }
        else
        {
            var3 = (var1 & Integer.MAX_VALUE) >>> 23;
            return var2 | (var1 & 8388607 | 8388608) + (8388608 >>> var3 - 102) >>> 126 - var3;
        }
    }

    private static float fromHalfFloat(int var0)
    {
        int var1 = var0 & 1023;
        int var2 = var0 & 31744;

        if (var2 == 31744)
        {
            var2 = 261120;
        }
        else if (var2 != 0)
        {
            var2 += 114688;

            if (var1 == 0 && var2 > 115712)
            {
                return Float.intBitsToFloat((var0 & 32768) << 16 | var2 << 13 | 1023);
            }
        }
        else if (var1 != 0)
        {
            var2 = 115712;

            do
            {
                var1 <<= 1;
                var2 -= 1024;
            }
            while ((var1 & 1024) == 0);

            var1 &= 1023;
        }

        return Float.intBitsToFloat((var0 & 32768) << 16 | (var2 | var1) << 13);
    }

    private static int compressNormalVector(float var0, float var1, float var2)
    {
        if (var0 == 0.0F && var1 == 0.0F && var2 == 0.0F)
        {
            throw new IllegalArgumentException("Zero-length normal vector");
        }
        else
        {
            int var3 = 0;

            if (var0 < 0.0F)
            {
                var0 = -var0;
                var3 |= 1;
            }

            if (var1 < 0.0F)
            {
                var1 = -var1;
                var3 |= 2;
            }

            if (var2 < 0.0F)
            {
                var2 = -var2;
                var3 |= 4;
            }

            int var4 = 0;
            float var5;

            if (var0 < var2)
            {
                var5 = var0;
                var0 = var2;
                var2 = var5;
                var4 |= 4;
            }

            if (var1 < var2)
            {
                var5 = var1;
                var1 = var2;
                var2 = var5;
                var4 |= 2;
            }

            if (var0 < var1)
            {
                var5 = var0;
                var0 = var1;
                var1 = var5;
                var4 |= 1;
            }

            double var15 = Math.atan2((double)var1, (double)var0);
            double var7 = Math.asin((double)var2 / Math.sqrt((double)(var0 * var0 + var1 * var1)));
            double var9 = 63.0D * var15 * 4.0D / Math.PI;
            double var11 = 63.0D * var7 * 4.0D / Math.PI;
            int var13 = (int)(var9 + 0.5D);
            int var14 = (int)(var11 + 0.5D);
            return var3 << 15 | var4 << 12 | var13 << 6 | var14;
        }
    }

    private static float[] decompressNormalVector(int var0, float[] var1) throws GeometryStream$GeometryStreamException
    {
        int var2 = var0 & 63;
        int var3 = var0 >>> 6 & 63;
        int var4 = var0 >>> 12 & 7;
        int var5 = var0 >>> 15 & 7;

        if (var2 > var3)
        {
            throw new GeometryStream$GeometryStreamException("Invalid or corrupt compressed vector (" + var0 + ")");
        }
        else
        {
            int var6 = (var3 * (var3 + 1) / 2 + var2) * 3;
            float var7 = _normalVectorTable[var6 + 0];
            float var8 = _normalVectorTable[var6 + 1];
            float var9 = _normalVectorTable[var6 + 2];

            if ((var4 & 3) == 3)
            {
                throw new GeometryStream$GeometryStreamException("Invalid or corrupt compressed vector (" + var0 + ")");
            }
            else
            {
                float var10;

                if ((var4 & 1) != 0)
                {
                    var10 = var7;
                    var7 = var8;
                    var8 = var10;
                }

                if ((var4 & 2) != 0)
                {
                    var10 = var8;
                    var8 = var9;
                    var9 = var10;
                }

                if ((var4 & 4) != 0)
                {
                    var10 = var7;
                    var7 = var9;
                    var9 = var10;
                }

                if ((var5 & 4) != 0)
                {
                    var9 = -var9;
                }

                if ((var5 & 2) != 0)
                {
                    var8 = -var8;
                }

                if ((var5 & 1) != 0)
                {
                    var7 = -var7;
                }

                if (var1 == null)
                {
                    var1 = new float[] {var7, var8, var9};
                }
                else
                {
                    var1[0] = var7;
                    var1[1] = var8;
                    var1[2] = var9;
                }

                return var1;
            }
        }
    }

    static
    {
        for (int var0 = 0; var0 < 64; ++var0)
        {
            for (int var1 = 0; var1 <= var0; ++var1)
            {
                int var2 = (var0 * (var0 + 1) / 2 + var1) * 3;
                double var3 = (Math.PI / 4D) * ((double)var0 / 63.0D);
                double var5 = Math.atan(Math.sin((Math.PI / 4D) * ((double)var1 / 63.0D)));
                double var7 = Math.cos(var5) * Math.cos(var3);
                double var9 = Math.cos(var5) * Math.sin(var3);
                double var11 = Math.sin(var5);
                _normalVectorTable[var2 + 0] = (float)var7;
                _normalVectorTable[var2 + 1] = (float)var9;
                _normalVectorTable[var2 + 2] = (float)var11;
            }
        }
    }
}
