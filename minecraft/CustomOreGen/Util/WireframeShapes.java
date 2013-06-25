package CustomOreGen.Util;

import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import java.util.Hashtable;
import java.util.Map;

public class WireframeShapes
{
    private static float[][] _cubePoints = new float[][] {{1.0F, -1.0F, 1.0F}, { -1.0F, -1.0F, 1.0F}, {1.0F, 1.0F, 1.0F}, { -1.0F, 1.0F, 1.0F}, {1.0F, 1.0F, -1.0F}, { -1.0F, 1.0F, -1.0F}, {1.0F, -1.0F, -1.0F}, { -1.0F, -1.0F, -1.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F}};
    private static Map _circlePointCache;

    public static void addUnitWireCube(IGeometryBuilder var0)
    {
        var0.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {1, 2});
        var0.addVertex(_cubePoints[0], _cubePoints[8], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[1], _cubePoints[9], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[2], _cubePoints[10], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[3], _cubePoints[11], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[4], _cubePoints[12], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[5], _cubePoints[13], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[6], _cubePoints[14], (float[])null, (float[])null);
        var0.addVertex(_cubePoints[7], _cubePoints[15], (float[])null, (float[])null);
        var0.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {1, 2});
        var0.addVertexRef(6);
        var0.addVertexRef(5);
        var0.addVertexRef(10);
        var0.addVertexRef(5);
        var0.addVertexRef(11);
        var0.addVertexRef(6);
        var0.addVertexRef(11);
        var0.addVertexRef(10);
    }

    public static void addUnitWireSphere(IGeometryBuilder var0, int var1, int var2)
    {
        if (var1 >= 2 && var2 >= 2)
        {
            var0.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {var1 + 1, var1 + 2, 1});
            float[][] var3 = getCirclePoints(var1, (float[][])null);
            float[][] var4 = getCirclePoints(2 * var2, (float[][])null);
            float[] var5 = new float[3];
            int var6;
            int var7;

            for (var6 = 1; var6 < var2; ++var6)
            {
                for (var7 = 0; var7 < var1; ++var7)
                {
                    var5[0] = var4[var6][1] * var3[var7][0];
                    var5[1] = var4[var6][1] * var3[var7][1];
                    var5[2] = var4[var6][0];
                    var0.addVertex(var5, var5, (float[])null, (float[])null);
                }

                var0.addVertexRef(var1);
            }

            var0.setVertexMode(IGeometryBuilder$PrimitiveType.TRIANGLE, new int[] {1});
            var5[0] = var5[1] = 0.0F;
            var5[2] = 1.0F;
            var6 = (var1 + 1) * (var2 - 2) + 1;
            var0.addVertexRef(var6);

            for (var7 = 1; var7 <= var1; ++var7)
            {
                if (var7 == 1)
                {
                    var0.addVertex(var5, var5, (float[])null, (float[])null);
                }
                else
                {
                    var0.addVertexRef(2);
                }

                var0.addVertexRef(var6 + 3 * var7);
            }

            var0.setVertexMode(IGeometryBuilder$PrimitiveType.TRIANGLE, new int[] {1});
            var5[0] = var5[1] = 0.0F;
            var5[2] = -1.0F;
            var6 = 3 * var1 + 2;
            var0.addVertexRef(var6);

            for (var7 = 1; var7 <= var1; ++var7)
            {
                if (var7 == 1)
                {
                    var0.addVertex(var5, var5, (float[])null, (float[])null);
                }
                else
                {
                    var0.addVertexRef(2);
                }

                var0.addVertexRef(var6 + var7);
            }
        }
    }

    public static void addUnitMercatorSphere(IGeometryBuilder var0, int var1, int var2)
    {
        if (var1 >= 2 && var2 >= 2)
        {
            var0.setVertexMode(IGeometryBuilder$PrimitiveType.QUAD, new int[] {var1 + 1, var1 + 2, 1});
            float[][] var3 = getCirclePoints(var1, (float[][])null);
            float[][] var4 = getCirclePoints(2 * var2, (float[][])null);
            float[] var5 = new float[3];
            float[] var6 = new float[2];

            for (int var7 = 0; var7 <= var2; ++var7)
            {
                double var8 = Math.log((double)((1.0F + var4[var7][0]) / var4[var7][1]));
                var6[1] = (float)Math.max(0.0D, Math.min(1.0D, var8 / 4.8725D + 0.5D));

                for (int var10 = 0; var10 < var1; ++var10)
                {
                    var5[0] = var4[var7][1] * var3[var10][0];
                    var5[1] = var4[var7][1] * var3[var10][1];
                    var5[2] = var4[var7][0];
                    var6[0] = (float)var10 / (float)var1;
                    var0.addVertex(var5, var5, (float[])null, var6);
                }

                var5[0] = var4[var7][1] * var3[0][0];
                var5[1] = var4[var7][1] * var3[0][1];
                var5[2] = var4[var7][0];
                var6[0] = 1.0F;
                var0.addVertex(var5, var5, (float[])null, var6);
            }
        }
    }

    public static void addUnitCircle(IGeometryBuilder var0, int var1)
    {
        if (var1 >= 2)
        {
            float[][] var2 = getCirclePoints(var1, (float[][])null);
            var0.setVertexMode(IGeometryBuilder$PrimitiveType.LINE, new int[] {1});

            for (int var3 = 0; var3 < var1; ++var3)
            {
                var0.addVertex(var2[var3], var2[var3], (float[])null, (float[])null);
            }

            var0.addVertexRef(var1);
        }
    }

    public static float[][] getCirclePoints(int var0, float[][] var1)
    {
        if (var1 == null)
        {
            var1 = (float[][])_circlePointCache.get(Integer.valueOf(var0));

            if (var1 != null)
            {
                return var1;
            }

            var1 = new float[var0][3];
            _circlePointCache.put(Integer.valueOf(var0), var1);
        }

        float var2 = ((float)Math.PI * 2F) / (float)var0;
        float var3 = 0.0F;

        for (int var4 = 0; var4 < var0; ++var4)
        {
            var1[var4][0] = (float)Math.cos((double)var3);
            var1[var4][1] = (float)Math.sin((double)var3);
            var1[var4][2] = 0.0F;
            var3 += var2;
        }

        return var1;
    }

    static
    {
        for (int var0 = 0; var0 < 24; ++var0)
        {
            _cubePoints[8 + var0 % 8][var0 / 8] = _cubePoints[var0 % 8][var0 / 8] * (float)Math.sqrt(3.0D);
        }

        _circlePointCache = new Hashtable();
    }
}
