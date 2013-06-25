package CustomOreGen.Client;

import CustomOreGen.Client.GeometryRenderer$1;
import CustomOreGen.Client.GeometryRenderer$VertexBuffer;
import CustomOreGen.Util.IGeometryBuilder;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import CustomOreGen.Util.Transform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GeometryRenderer implements IGeometryBuilder
{
    private int _curBufferIdx = -1;
    private Transform _posTrans = null;
    private Transform _nmlTrans = null;
    private float[] _normal = null;
    private float[] _color = null;
    private float[] _texcoords = null;
    private int _texture = -1;
    private Transform _texTrans = null;
    private IGeometryBuilder$PrimitiveType _primitive = null;
    private int[] _implicitRefs = null;
    private Map _textureMap = null;
    private ArrayList _vertexBuffers = new ArrayList();
    private GeometryRenderer$VertexBuffer _dbgNormalLines = null;
    private long[] _vertexIndexMap = new long[256];
    private int _vertexCount = 0;
    private int _flushedVertexCount = 0;
    private int _processedVertexCount = 0;
    private boolean _polygonParity = false;

    public void mapTexture(String var1, int var2)
    {
        if (this._textureMap == null)
        {
            this._textureMap = new HashMap();
        }

        if (var2 < 0)
        {
            this._textureMap.remove(var1);
        }
        else
        {
            this._textureMap.put(var1, Integer.valueOf(var2));
        }
    }

    public void setPositionTransform(Transform var1)
    {
        this._posTrans = var1;

        if (var1 != null && this._normal != null)
        {
            this._nmlTrans = var1.clone().inverse().transpose();
        }
        else
        {
            this._nmlTrans = null;
        }
    }

    public void setNormal(float[] var1)
    {
        if (this._normal == null != (var1 == null))
        {
            this._curBufferIdx = -1;

            if (var1 == null)
            {
                this._nmlTrans = null;
            }
            else if (this._posTrans != null)
            {
                this._nmlTrans = this._posTrans.clone().inverse().transpose();
            }
        }

        this._normal = var1;
    }

    public void setColor(float[] var1)
    {
        if (this._color == null != (var1 == null))
        {
            this._curBufferIdx = -1;
        }

        this._color = var1;
    }

    public void setTexture(String var1)
    {
        int var2 = -1;

        if (var1 != null && this._textureMap != null)
        {
            Integer var3 = (Integer)this._textureMap.get(var1);

            if (var3 != null)
            {
                var2 = var3.intValue();
            }
        }

        if (this._texcoords != null && this._texture != var2)
        {
            this._curBufferIdx = -1;
        }

        this._texture = var2;
    }

    public void setTextureTransform(Transform var1)
    {
        this._texTrans = var1;
    }

    public void setTextureCoordinates(float[] var1)
    {
        if (this._texture >= 0 && this._texcoords == null != (var1 == null))
        {
            this._curBufferIdx = -1;
        }

        this._texcoords = var1;
    }

    public void setVertexMode(IGeometryBuilder$PrimitiveType var1, int ... var2)
    {
        if (this._primitive != var1)
        {
            this._curBufferIdx = -1;
        }

        this._primitive = var1;
        int var3 = this._implicitRefs == null ? 0 : this._implicitRefs.length;
        int var4 = var2 == null ? 0 : var2.length;

        if (var2 != this._implicitRefs && (var3 > 0 || var4 > 0) && !Arrays.equals(var2, this._implicitRefs))
        {
            ;
        }

        this._implicitRefs = var2;
        this._polygonParity = false;
        this._processedVertexCount = this._vertexCount;
        this._flushedVertexCount = this._vertexCount;
    }

    public void addVertex(float[] var1)
    {
        this.addVertex(var1, (float[])null, (float[])null, (float[])null);
    }

    public void addVertex(float[] var1, float[] var2, float[] var3, float[] var4)
    {
        if (this._curBufferIdx < 0)
        {
            this.setVertexBuffer();
        }

        GeometryRenderer$VertexBuffer var5 = (GeometryRenderer$VertexBuffer)this._vertexBuffers.get(this._curBufferIdx);
        float[][] var6 = new float[4][];
        byte var7 = 0;

        if (this._posTrans != null)
        {
            var1 = Arrays.copyOf(var1, 3);
            this._posTrans.transformVector(var1);
        }

        int var10 = var7 + 1;
        var6[var7] = var1;

        if (this._normal != null)
        {
            if (var2 == null)
            {
                var2 = this._normal;
            }

            if (this._nmlTrans != null)
            {
                var2 = Arrays.copyOf(var2, 3);
                this._nmlTrans.transformVector(var2);
            }

            var6[var10++] = var2;

            if (this._dbgNormalLines != null)
            {
                var2 = Arrays.copyOf(var2, 3);
                var2[0] *= 0.1F;
                var2[1] *= 0.1F;
                var2[2] *= 0.1F;
                var2[0] += var1[0];
                var2[1] += var1[1];
                var2[2] += var1[2];
                this._dbgNormalLines.addIndex(this._dbgNormalLines.addVertex(new float[][] {var1}));
                this._dbgNormalLines.addIndex(this._dbgNormalLines.addVertex(new float[][] {var2}));
            }
        }

        if (this._color != null)
        {
            if (var3 == null)
            {
                var3 = this._color;
            }

            var6[var10++] = var3;
        }

        if (this._texture >= 0 && this._texcoords != null)
        {
            if (var4 == null)
            {
                var4 = this._texcoords;
            }

            if (this._texTrans != null)
            {
                var4 = Arrays.copyOf(var4, 2);
                this._texTrans.transformVector(var4);
            }

            var6[var10++] = var4;
        }

        int var8 = var5.addVertex(var6);

        if (this._vertexCount == this._vertexIndexMap.length)
        {
            this._vertexIndexMap = Arrays.copyOf(this._vertexIndexMap, this._vertexCount * 2);
        }

        this._vertexIndexMap[this._vertexCount] = (long)this._curBufferIdx << 32 | (long)var8;
        ++this._vertexCount;
        this.processVertices();
    }

    public void addVertexRef(int var1)
    {
        if (this._curBufferIdx < 0)
        {
            this.setVertexBuffer();
        }

        if (this._vertexCount == this._vertexIndexMap.length)
        {
            this._vertexIndexMap = Arrays.copyOf(this._vertexIndexMap, this._vertexCount * 2);
        }

        if (var1 >= 1 && var1 <= this._vertexCount)
        {
            this._vertexIndexMap[this._vertexCount] = this._vertexIndexMap[this._vertexCount - var1];
        }
        else
        {
            this._vertexIndexMap[this._vertexCount] = -1L;
        }

        ++this._vertexCount;
        this.processVertices();
    }

    public void draw()
    {
        Iterator var1 = this._vertexBuffers.iterator();

        while (var1.hasNext())
        {
            GeometryRenderer$VertexBuffer var2 = (GeometryRenderer$VertexBuffer)var1.next();
            var2.drawBuffer();
        }

        if (this._dbgNormalLines != null)
        {
            this._dbgNormalLines.drawBuffer();
        }
    }

    public void enableDebuggingNormalLines(boolean var1)
    {
        if (!var1)
        {
            this._dbgNormalLines = null;
        }
        else if (this._dbgNormalLines == null)
        {
            this._dbgNormalLines = new GeometryRenderer$VertexBuffer(128, 1, false, false, -1);
        }
    }

    private void processVertices()
    {
        GeometryRenderer$VertexBuffer var1 = this._curBufferIdx >= 0 ? (GeometryRenderer$VertexBuffer)this._vertexBuffers.get(this._curBufferIdx) : null;

        if (var1 != null && this._primitive != null)
        {
            byte var2 = 0;

            switch (GeometryRenderer$1.$SwitchMap$net$minecraft$src$CustomOreGen$Util$IGeometryBuilder$PrimitiveType[this._primitive.ordinal()])
            {
                case 1:
                    var2 = 1;
                    break;

                case 2:
                    var2 = 2;
                    break;

                case 3:
                case 4:
                    var2 = 3;
                    break;

                case 5:
                    var2 = 4;
            }

            int var3 = this._implicitRefs == null ? 0 : this._implicitRefs.length;

            if (var3 >= var2)
            {
                var3 = var2 - 1;
            }

            int var4 = 0;
            int var5;

            for (var5 = 0; var5 < var3; ++var5)
            {
                if (this._implicitRefs[var5] > var4)
                {
                    var4 = this._implicitRefs[var5];
                }
            }

            if (this._processedVertexCount - this._flushedVertexCount < var4)
            {
                this._processedVertexCount = Math.min(this._flushedVertexCount + var4, this._vertexCount);
            }

            var5 = var2 - var3;

            while (this._vertexCount - this._processedVertexCount >= var5)
            {
                GeometryRenderer$VertexBuffer[] var6 = new GeometryRenderer$VertexBuffer[var2];
                int[] var7 = new int[var2];
                boolean var8 = true;
                int var9 = 0;

                while (true)
                {
                    if (var9 < var2)
                    {
                        long var10 = -1L;

                        if (var9 < var3)
                        {
                            if (this._implicitRefs[var9] >= 1)
                            {
                                var10 = this._vertexIndexMap[this._processedVertexCount - this._implicitRefs[var9]];
                            }
                        }
                        else
                        {
                            var10 = this._vertexIndexMap[this._processedVertexCount + (var9 - var3)];
                        }

                        if (var10 == -1L)
                        {
                            var8 = false;
                        }
                        else
                        {
                            var6[var9] = (GeometryRenderer$VertexBuffer)this._vertexBuffers.get((int)(var10 >>> 32));

                            if (!var1.canCopyFrom(var6[var9]))
                            {
                                var8 = false;
                            }
                            else
                            {
                                var7[var9] = (int)(var10 & -1L);

                                if (var7[var9] >= 0 && var7[var9] < var6[var9].getVertexCount())
                                {
                                    ++var9;
                                    continue;
                                }

                                var8 = false;
                            }
                        }
                    }

                    if (var8)
                    {
                        for (var9 = 0; var9 < var2; ++var9)
                        {
                            int var12 = this._primitive == IGeometryBuilder$PrimitiveType.TRIANGLE_ALT && this._polygonParity ? var2 - 1 - var9 : var9;

                            if (var6[var12] != var1)
                            {
                                var7[var12] = var1.copyVertex(var6[var12], var7[var12]);
                            }

                            var1.addIndex(var7[var12]);
                        }

                        this._polygonParity = !this._polygonParity;
                    }

                    this._processedVertexCount += var5;
                    break;
                }
            }
        }
    }

    private void setVertexBuffer()
    {
        byte var1 = -1;

        if (this._primitive != null)
        {
            switch (GeometryRenderer$1.$SwitchMap$net$minecraft$src$CustomOreGen$Util$IGeometryBuilder$PrimitiveType[this._primitive.ordinal()])
            {
                case 1:
                    var1 = 0;
                    break;

                case 2:
                    var1 = 1;
                    break;

                case 3:
                case 4:
                    var1 = 4;
                    break;

                case 5:
                    var1 = 7;
            }
        }

        boolean var2 = this._normal != null;
        boolean var3 = this._color != null;
        int var4 = this._texcoords == null ? -1 : this._texture;
        GeometryRenderer$VertexBuffer var5 = this._curBufferIdx >= 0 ? (GeometryRenderer$VertexBuffer)this._vertexBuffers.get(this._curBufferIdx) : null;

        if (var5 == null || var1 != var5.renderMode || var2 != var5.hasNormal || var3 != var5.hasColor || var4 != var5.texture)
        {
            GeometryRenderer$VertexBuffer var6 = null;

            for (int var7 = 0; var7 < this._vertexBuffers.size(); ++var7)
            {
                GeometryRenderer$VertexBuffer var8 = (GeometryRenderer$VertexBuffer)this._vertexBuffers.get(var7);

                if (var8 != var5 && var1 == var8.renderMode && var2 == var8.hasNormal && var3 == var8.hasColor && var4 == var8.texture)
                {
                    this._curBufferIdx = var7;
                    var6 = var8;
                    break;
                }
            }

            if (var6 == null)
            {
                var6 = new GeometryRenderer$VertexBuffer(var1 > 0 ? 128 : 0, var1, var2, var3, var4);
                this._curBufferIdx = this._vertexBuffers.size();
                this._vertexBuffers.add(var6);
            }
        }
    }
}
