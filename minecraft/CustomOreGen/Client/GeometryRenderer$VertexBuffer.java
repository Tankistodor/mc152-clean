package CustomOreGen.Client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class GeometryRenderer$VertexBuffer
{
    public final int renderMode;
    public final boolean hasNormal;
    public final boolean hasColor;
    public final int texture;
    private ByteBuffer vBuffer;
    private IntBuffer xBuffer;

    public GeometryRenderer$VertexBuffer(int var1, int var2, boolean var3, boolean var4, int var5)
    {
        this.renderMode = var2;
        this.hasNormal = var3;
        this.hasColor = var4;
        this.texture = var5;

        if (var1 <= 0)
        {
            var1 = 1;
        }

        this.vBuffer = ByteBuffer.allocateDirect(var1 * this.getVertexSize()).order(ByteOrder.nativeOrder());
        this.xBuffer = ByteBuffer.allocateDirect(var1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        this.clear();
    }

    public void clear()
    {
        this.vBuffer.limit(0);
        this.xBuffer.limit(0);
    }

    private int getVertexSize()
    {
        int var1 = 3;

        if (this.hasNormal)
        {
            var1 += 3;
        }

        if (this.hasColor)
        {
            ++var1;
        }

        if (this.texture >= 0)
        {
            var1 += 2;
        }

        return var1 * 4;
    }

    public int getVertexCount()
    {
        return this.vBuffer.limit() / this.getVertexSize();
    }

    public int addVertex(float[] ... var1)
    {
        int var2 = this.vBuffer.limit();
        int var3 = this.getVertexSize();

        if (var2 + var3 > this.vBuffer.capacity())
        {
            ByteBuffer var4 = this.vBuffer;
            this.vBuffer = ByteBuffer.allocateDirect(var4.capacity() * 2).order(ByteOrder.nativeOrder());
            var4.rewind();
            this.vBuffer.put(var4);
        }

        this.vBuffer.limit(var2 + var3);
        this.vBuffer.position(var2);
        byte var9 = 0;
        this.vBuffer.putFloat(var1[var9][0]);
        this.vBuffer.putFloat(var1[var9][1]);
        this.vBuffer.putFloat(var1[var9][2]);
        int var10 = var9 + 1;

        if (this.hasNormal)
        {
            this.vBuffer.putFloat(var1[var10][0]);
            this.vBuffer.putFloat(var1[var10][1]);
            this.vBuffer.putFloat(var1[var10][2]);
            ++var10;
        }

        if (this.hasColor)
        {
            int var5 = (int)((double)(var1[var10][0] * 255.0F) + 0.5D);

            if (var5 > 255)
            {
                var5 = 255;
            }
            else if (var5 < 0)
            {
                var5 = 0;
            }

            int var6 = (int)((double)(var1[var10][1] * 255.0F) + 0.5D);

            if (var6 > 255)
            {
                var6 = 255;
            }
            else if (var6 < 0)
            {
                var6 = 0;
            }

            int var7 = (int)((double)(var1[var10][2] * 255.0F) + 0.5D);

            if (var7 > 255)
            {
                var7 = 255;
            }
            else if (var7 < 0)
            {
                var7 = 0;
            }

            int var8 = 255;

            if (var1[var10].length > 3)
            {
                var8 = (int)((double)(var1[var10][3] * 255.0F) + 0.5D);

                if (var8 > 255)
                {
                    var8 = 255;
                }
                else if (var8 < 0)
                {
                    var8 = 0;
                }
            }

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            {
                this.vBuffer.putInt(var8 << 24 | var7 << 16 | var6 << 8 | var5);
            }
            else
            {
                this.vBuffer.putInt(var5 << 24 | var6 << 16 | var7 << 8 | var8);
            }

            ++var10;
        }

        if (this.texture >= 0)
        {
            this.vBuffer.putFloat(var1[var10][0]);
            this.vBuffer.putFloat(var1[var10][1]);
            ++var10;
        }

        return var2 / var3;
    }

    public boolean canCopyFrom(GeometryRenderer$VertexBuffer var1)
    {
        return var1 == null ? false : (this.hasNormal && !var1.hasNormal ? false : (this.hasColor && !var1.hasColor ? false : this.texture < 0 || this.texture == var1.texture));
    }

    public int copyVertex(GeometryRenderer$VertexBuffer var1, int var2)
    {
        if (!this.canCopyFrom(var1))
        {
            return -1;
        }
        else if (var2 >= 0 && var2 <= var1.getVertexCount())
        {
            byte[] var3 = new byte[16];
            var1.vBuffer.position(var2 * var1.getVertexSize());
            int var4 = this.vBuffer.limit();
            int var5 = this.getVertexSize();

            if (var4 + var5 > this.vBuffer.capacity())
            {
                ByteBuffer var6 = this.vBuffer;
                this.vBuffer = ByteBuffer.allocateDirect(var6.capacity() * 2).order(ByteOrder.nativeOrder());
                var6.rewind();
                this.vBuffer.put(var6);
            }

            this.vBuffer.limit(var4 + var5);
            this.vBuffer.position(var4);
            var1.vBuffer.get(var3, 0, 12);
            this.vBuffer.put(var3, 0, 12);

            if (var1.hasNormal)
            {
                var1.vBuffer.get(var3, 0, 12);
            }

            if (this.hasNormal)
            {
                this.vBuffer.put(var3, 0, 12);
            }

            if (var1.hasColor)
            {
                var1.vBuffer.get(var3, 0, 4);
            }

            if (this.hasColor)
            {
                this.vBuffer.put(var3, 0, 4);
            }

            if (var1.texture >= 0)
            {
                var1.vBuffer.get(var3, 0, 8);
            }

            if (this.texture >= 0)
            {
                this.vBuffer.put(var3, 0, 8);
            }

            return var4 / var5;
        }
        else
        {
            return -1;
        }
    }

    public int getIndexCount()
    {
        return this.xBuffer.limit();
    }

    public void addIndex(int var1)
    {
        int var2 = this.xBuffer.limit();

        if (var2 == this.xBuffer.capacity())
        {
            IntBuffer var3 = this.xBuffer;
            this.xBuffer = ByteBuffer.allocateDirect(var3.capacity() * 8).order(ByteOrder.nativeOrder()).asIntBuffer();
            var3.rewind();
            this.xBuffer.put(var3);
        }

        this.xBuffer.limit(var2 + 1);
        this.xBuffer.position(var2);
        this.xBuffer.put(var1);
    }

    public void drawBuffer()
    {
        int var1 = this.getVertexCount();
        int var2 = this.getVertexSize();
        int var3 = this.getIndexCount();

        if (this.renderMode >= 0 && var1 != 0 && var3 != 0)
        {
            byte var4 = 0;
            this.vBuffer.position(var4);
            GL11.glVertexPointer(3, var2, this.vBuffer.asFloatBuffer());
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            int var5 = var4 + 12;

            if (this.hasNormal)
            {
                this.vBuffer.position(var5);
                GL11.glNormalPointer(var2, this.vBuffer.asFloatBuffer());
                GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
                var5 += 12;
            }

            if (this.hasColor)
            {
                this.vBuffer.position(var5);
                GL11.glColorPointer(4, true, var2, this.vBuffer);
                GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
                var5 += 4;
            }

            if (this.texture >= 0)
            {
                this.vBuffer.position(var5);
                GL11.glTexCoordPointer(2, var2, this.vBuffer.asFloatBuffer());
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                var5 += 8;
            }

            if (this.texture >= 0 && this.texture != GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D))
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
            }

            this.xBuffer.rewind();
            GL11.glDrawElements(this.renderMode, this.xBuffer);
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

            if (this.hasNormal)
            {
                GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            }

            if (this.hasColor)
            {
                GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
            }

            if (this.texture >= 0)
            {
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            }
        }
    }
}
