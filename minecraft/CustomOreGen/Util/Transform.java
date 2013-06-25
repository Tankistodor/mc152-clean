package CustomOreGen.Util;

public class Transform implements Cloneable
{
    private float[] mat;

    public Transform()
    {
        this.mat = new float[] {1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F};
    }

    protected Transform(float[] var1)
    {
        this.mat = var1;
    }

    public Transform clone()
    {
        return new Transform((float[])this.mat.clone());
    }

    public float element(int var1, int var2)
    {
        return this.mat[(var1 & 3) << 2 | var2 & 3];
    }

    public void setElement(int var1, int var2, float var3)
    {
        this.mat[(var1 & 3) << 2 | var2 & 3] = var3;
    }

    public float[] elements()
    {
        return this.mat;
    }

    public Transform identity()
    {
        this.mat[0] = 1.0F;
        this.mat[1] = 0.0F;
        this.mat[2] = 0.0F;
        this.mat[3] = 0.0F;
        this.mat[4] = 0.0F;
        this.mat[5] = 1.0F;
        this.mat[6] = 0.0F;
        this.mat[7] = 0.0F;
        this.mat[8] = 0.0F;
        this.mat[9] = 0.0F;
        this.mat[10] = 1.0F;
        this.mat[11] = 0.0F;
        this.mat[12] = 0.0F;
        this.mat[13] = 0.0F;
        this.mat[14] = 0.0F;
        this.mat[15] = 1.0F;
        return this;
    }

    public Transform transform(Transform var1)
    {
        mult(this.mat, var1.mat);
        return this;
    }

    public void transformVector(float[] var1)
    {
        float var2 = var1.length > 3 ? var1[3] : 1.0F;
        float var3 = this.mat[0] * var1[0] + this.mat[1] * var1[1] + this.mat[2] * var1[2] + this.mat[3] * var2;
        float var4 = this.mat[4] * var1[0] + this.mat[5] * var1[1] + this.mat[6] * var1[2] + this.mat[7] * var2;
        float var5 = this.mat[8] * var1[0] + this.mat[9] * var1[1] + this.mat[10] * var1[2] + this.mat[11] * var2;
        float var6 = this.mat[12] * var1[0] + this.mat[13] * var1[1] + this.mat[14] * var1[2] + this.mat[15] * var2;
        var1[0] = var3;
        var1[1] = var4;
        var1[2] = var5;

        if (var1.length > 3)
        {
            var1[3] = var6;
        }
    }

    public void transformVectors(float[] var1, int var2, int var3, int var4)
    {
        if (var2 >= 1 && var2 <= 4)
        {
            if (var1.length < var3 + var4 * var2)
            {
                throw new RuntimeException("Attempting to transform vector array that is too short.");
            }
            else
            {
                for (int var5 = var3; var5 < var3 + var4 * var2; var5 += var2)
                {
                    float var6 = var1[var5 + 0];
                    float var7 = var2 > 1 ? var1[var5 + 1] : 0.0F;
                    float var8 = var2 > 2 ? var1[var5 + 2] : 0.0F;
                    float var9 = var2 > 3 ? var1[var5 + 3] : 1.0F;
                    var1[var5 + 0] = this.mat[0] * var6 + this.mat[1] * var7 + this.mat[2] * var8 + this.mat[3] * var9;

                    if (var2 > 1)
                    {
                        var1[var5 + 1] = this.mat[4] * var6 + this.mat[5] * var7 + this.mat[6] * var8 + this.mat[7] * var9;
                    }

                    if (var2 > 2)
                    {
                        var1[var5 + 2] = this.mat[8] * var6 + this.mat[9] * var7 + this.mat[10] * var8 + this.mat[11] * var9;
                    }

                    if (var2 > 3)
                    {
                        var1[var5 + 3] = this.mat[12] * var6 + this.mat[13] * var7 + this.mat[14] * var8 + this.mat[15] * var9;
                    }
                }
            }
        }
        else
        {
            throw new RuntimeException("Attempting to transform vectors of invalid size.");
        }
    }

    public void transformBB(float[] var1)
    {
        float[] var2 = new float[3];
        float var3 = Float.POSITIVE_INFINITY;
        float var4 = Float.POSITIVE_INFINITY;
        float var5 = Float.POSITIVE_INFINITY;
        float var6 = Float.NEGATIVE_INFINITY;
        float var7 = Float.NEGATIVE_INFINITY;
        float var8 = Float.NEGATIVE_INFINITY;

        for (int var9 = 0; var9 < 8; ++var9)
        {
            var2[0] = var1[(var9 & 1) == 0 ? 0 : 3];
            var2[1] = var1[(var9 & 2) == 0 ? 1 : 4];
            var2[2] = var1[(var9 & 4) == 0 ? 2 : 5];
            this.transformVector(var2);

            if (var2[0] < var3)
            {
                var3 = var2[0];
            }

            if (var2[1] < var4)
            {
                var4 = var2[1];
            }

            if (var2[2] < var5)
            {
                var5 = var2[2];
            }

            if (var2[0] > var6)
            {
                var6 = var2[0];
            }

            if (var2[1] > var7)
            {
                var7 = var2[1];
            }

            if (var2[2] > var8)
            {
                var8 = var2[2];
            }
        }

        var1[0] = var3;
        var1[1] = var4;
        var1[2] = var5;
        var1[3] = var6;
        var1[4] = var7;
        var1[5] = var8;
    }

    public Transform rotate(float var1, float var2, float var3, float var4)
    {
        float var5 = var2 * var2 + var3 * var3 + var4 * var4;

        if (var5 == 0.0F)
        {
            throw new RuntimeException("Attempting to rotate about a null vector");
        }
        else
        {
            if (var5 != 1.0F)
            {
                var5 = (float)Math.sqrt((double)var5);
                var2 /= var5;
                var3 /= var5;
                var4 /= var5;
            }

            float var6 = (float)Math.sin((double)var1);
            float var7 = 1.0F - (float)Math.cos((double)var1);
            float[] var8 = new float[16];
            var8[0] = 1.0F + (var2 * var2 - 1.0F) * var7;
            var8[1] = var2 * var3 * var7 - var4 * var6;
            var8[2] = var2 * var4 * var7 + var3 * var6;
            var8[4] = var2 * var3 * var7 + var4 * var6;
            var8[5] = 1.0F + (var3 * var3 - 1.0F) * var7;
            var8[6] = var3 * var4 * var7 - var2 * var6;
            var8[8] = var2 * var4 * var7 - var3 * var6;
            var8[9] = var3 * var4 * var7 + var2 * var6;
            var8[10] = 1.0F + (var4 * var4 - 1.0F) * var7;
            var8[15] = 1.0F;
            mult(this.mat, var8);
            return this;
        }
    }

    public Transform rotateX(float var1)
    {
        float var2 = (float)Math.sin((double)var1);
        float var3 = (float)Math.cos((double)var1);
        float var4 = 0.0F;
        var4 = this.mat[1];
        this.mat[1] = var4 * var3 + this.mat[2] * var2;
        this.mat[2] = this.mat[2] * var3 - var4 * var2;
        var4 = this.mat[5];
        this.mat[5] = var4 * var3 + this.mat[6] * var2;
        this.mat[6] = this.mat[6] * var3 - var4 * var2;
        var4 = this.mat[9];
        this.mat[9] = var4 * var3 + this.mat[10] * var2;
        this.mat[10] = this.mat[10] * var3 - var4 * var2;
        var4 = this.mat[13];
        this.mat[13] = var4 * var3 + this.mat[14] * var2;
        this.mat[14] = this.mat[14] * var3 - var4 * var2;
        return this;
    }

    public Transform rotateY(float var1)
    {
        float var2 = (float)Math.sin((double)var1);
        float var3 = (float)Math.cos((double)var1);
        float var4 = 0.0F;
        var4 = this.mat[0];
        this.mat[0] = var4 * var3 - this.mat[2] * var2;
        this.mat[2] = this.mat[2] * var3 + var4 * var2;
        var4 = this.mat[4];
        this.mat[4] = var4 * var3 - this.mat[6] * var2;
        this.mat[6] = this.mat[6] * var3 + var4 * var2;
        var4 = this.mat[8];
        this.mat[8] = var4 * var3 - this.mat[10] * var2;
        this.mat[10] = this.mat[10] * var3 + var4 * var2;
        var4 = this.mat[12];
        this.mat[12] = var4 * var3 - this.mat[14] * var2;
        this.mat[14] = this.mat[14] * var3 + var4 * var2;
        return this;
    }

    public Transform rotateZ(float var1)
    {
        float var2 = (float)Math.sin((double)var1);
        float var3 = (float)Math.cos((double)var1);
        float var4 = 0.0F;
        var4 = this.mat[0];
        this.mat[0] = var4 * var3 + this.mat[1] * var2;
        this.mat[1] = this.mat[1] * var3 - var4 * var2;
        var4 = this.mat[4];
        this.mat[4] = var4 * var3 + this.mat[5] * var2;
        this.mat[5] = this.mat[5] * var3 - var4 * var2;
        var4 = this.mat[8];
        this.mat[8] = var4 * var3 + this.mat[9] * var2;
        this.mat[9] = this.mat[9] * var3 - var4 * var2;
        var4 = this.mat[12];
        this.mat[12] = var4 * var3 + this.mat[13] * var2;
        this.mat[13] = this.mat[13] * var3 - var4 * var2;
        return this;
    }

    public Transform rotateXInto(float var1, float var2, float var3)
    {
        float var4 = var1 * var1 + var2 * var2 + var3 * var3;

        if (var4 == 0.0F)
        {
            throw new RuntimeException("Attempting to rotate into a null vector");
        }
        else
        {
            if (var4 != 1.0F)
            {
                var4 = (float)Math.sqrt((double)var4);
                var1 /= var4;
                var2 /= var4;
                var3 /= var4;
            }

            float var5 = var2 * var2 + var3 * var3;

            if (var5 == 0.0F)
            {
                return this;
            }
            else
            {
                float[] var6 = new float[16];
                var6[0] = var1;
                var6[1] = -var2;
                var6[2] = -var3;
                var6[4] = var2;
                var6[5] = (var2 * var2 * var1 + var3 * var3) / var5;
                var6[6] = var2 * var3 * (var1 - 1.0F) / var5;
                var6[8] = var3;
                var6[9] = var2 * var3 * (var1 - 1.0F) / var5;
                var6[10] = (var2 * var2 + var3 * var3 * var1) / var5;
                var6[15] = 1.0F;
                mult(this.mat, var6);
                return this;
            }
        }
    }

    public Transform rotateYInto(float var1, float var2, float var3)
    {
        float var4 = var1 * var1 + var2 * var2 + var3 * var3;

        if (var4 == 0.0F)
        {
            throw new RuntimeException("Attempting to rotate into a null vector");
        }
        else
        {
            if (var4 != 1.0F)
            {
                var4 = (float)Math.sqrt((double)var4);
                var1 /= var4;
                var2 /= var4;
                var3 /= var4;
            }

            float var5 = var1 * var1 + var3 * var3;

            if (var5 == 0.0F)
            {
                return this;
            }
            else
            {
                float[] var6 = new float[16];
                var6[0] = (var1 * var1 * var2 + var3 * var3) / var5;
                var6[1] = var1;
                var6[2] = var1 * var3 * (var2 - 1.0F) / var5;
                var6[4] = -var1;
                var6[5] = var2;
                var6[6] = -var3;
                var6[8] = var1 * var3 * (var2 - 1.0F) / var5;
                var6[9] = var3;
                var6[10] = (var1 * var1 + var3 * var3 * var2) / var5;
                var6[15] = 1.0F;
                mult(this.mat, var6);
                return this;
            }
        }
    }

    public Transform rotateZInto(float var1, float var2, float var3)
    {
        float var4 = var1 * var1 + var2 * var2 + var3 * var3;

        if (var4 == 0.0F)
        {
            throw new RuntimeException("Attempting to rotate into a null vector");
        }
        else
        {
            if (var4 != 1.0F)
            {
                var4 = (float)Math.sqrt((double)var4);
                var1 /= var4;
                var2 /= var4;
                var3 /= var4;
            }

            float var5 = var1 * var1 + var2 * var2;

            if (var5 == 0.0F)
            {
                return this;
            }
            else
            {
                float[] var6 = new float[16];
                var6[0] = (var1 * var1 * var3 + var2 * var2) / var5;
                var6[1] = var1 * var2 * (var3 - 1.0F) / var5;
                var6[2] = var1;
                var6[4] = var1 * var2 * (var3 - 1.0F) / var5;
                var6[5] = (var1 * var1 + var2 * var2 * var3) / var5;
                var6[6] = var2;
                var6[8] = -var1;
                var6[9] = -var2;
                var6[10] = var3;
                var6[15] = 1.0F;
                mult(this.mat, var6);
                return this;
            }
        }
    }

    public Transform scale(float var1, float var2, float var3, float var4)
    {
        float var5 = var2 * var2 + var3 * var3 + var4 * var4;

        if (var5 == 0.0F)
        {
            throw new RuntimeException("Attempting to scale along a null vector");
        }
        else
        {
            if (var5 != 1.0F)
            {
                var5 = (float)Math.sqrt((double)var5);
                var2 /= var5;
                var3 /= var5;
                var4 /= var5;
            }

            --var1;
            float[] var6 = new float[16];
            var6[0] = var1 * var2 * var2 + 1.0F;
            var6[1] = var1 * var2 * var3;
            var6[2] = var1 * var2 * var4;
            var6[4] = var1 * var2 * var3;
            var6[5] = var1 * var3 * var3 + 1.0F;
            var6[6] = var1 * var3 * var4;
            var6[8] = var1 * var2 * var4;
            var6[9] = var1 * var3 * var4;
            var6[10] = var1 * var4 * var4 + 1.0F;
            var6[15] = 1.0F;
            mult(this.mat, var6);
            return this;
        }
    }

    public Transform scale(float var1, float var2, float var3)
    {
        this.mat[0] *= var1;
        this.mat[1] *= var2;
        this.mat[2] *= var3;
        this.mat[4] *= var1;
        this.mat[5] *= var2;
        this.mat[6] *= var3;
        this.mat[8] *= var1;
        this.mat[9] *= var2;
        this.mat[10] *= var3;
        this.mat[12] *= var1;
        this.mat[13] *= var2;
        this.mat[14] *= var3;
        return this;
    }

    public Transform shear(float var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        float var8 = var5 * var5 + var6 * var6 + var7 * var7;

        if (var8 == 0.0F)
        {
            throw new RuntimeException("Attempting to shear with a null invariant vector");
        }
        else
        {
            if (var8 != 1.0F)
            {
                var8 = (float)Math.sqrt((double)var8);
                var5 /= var8;
                var6 /= var8;
                var7 /= var8;
            }

            float var9 = var2 * var5 + var3 * var6 + var4 * var7;

            if (var9 != 0.0F)
            {
                var2 -= var9 * var5;
                var3 -= var9 * var6;
                var4 -= var9 * var7;
            }

            float var10 = var2 * var2 + var3 * var3 + var4 * var4;

            if (var10 == 0.0F)
            {
                throw new RuntimeException("Attempting to shear with a null or parallel shear vector");
            }
            else
            {
                if (var10 != 1.0F)
                {
                    var10 = (float)Math.sqrt((double)var10);
                    var2 /= var10;
                    var3 /= var10;
                    var4 /= var10;
                }

                float var11 = (float)Math.tan((double)var1);
                float[] var12 = new float[16];
                var12[0] = var2 * var5 * var11 + 1.0F;
                var12[1] = var2 * var6 * var11;
                var12[2] = var2 * var7 * var11;
                var12[4] = var3 * var5 * var11;
                var12[5] = var3 * var6 * var11 + 1.0F;
                var12[6] = var3 * var7 * var11;
                var12[8] = var4 * var5 * var11;
                var12[9] = var4 * var6 * var11;
                var12[10] = var4 * var7 * var11 + 1.0F;
                var12[15] = 1.0F;
                mult(this.mat, var12);
                return this;
            }
        }
    }

    public Transform reflect(float var1, float var2, float var3)
    {
        float var4 = var1 * var1 + var2 * var2 + var3 * var3;

        if (var4 == 0.0F)
        {
            throw new RuntimeException("Attempting to reflect across a null plane");
        }
        else
        {
            if (var4 != 1.0F)
            {
                var4 = (float)Math.sqrt((double)var4);
                var1 /= var4;
                var2 /= var4;
                var3 /= var4;
            }

            float[] var5 = new float[16];
            var5[0] = 1.0F - 2.0F * var1 * var1;
            var5[1] = -2.0F * var1 * var2;
            var5[2] = -2.0F * var1 * var3;
            var5[4] = -2.0F * var1 * var2;
            var5[5] = 1.0F - 2.0F * var2 * var2;
            var5[6] = -2.0F * var2 * var3;
            var5[8] = -2.0F * var1 * var3;
            var5[9] = -2.0F * var2 * var3;
            var5[10] = 1.0F - 2.0F * var3 * var3;
            var5[15] = 1.0F;
            mult(this.mat, var5);
            return this;
        }
    }

    public Transform translate(float var1, float var2, float var3)
    {
        this.mat[3] += this.mat[0] * var1 + this.mat[1] * var2 + this.mat[2] * var3;
        this.mat[7] += this.mat[4] * var1 + this.mat[5] * var2 + this.mat[6] * var3;
        this.mat[11] += this.mat[8] * var1 + this.mat[9] * var2 + this.mat[10] * var3;
        this.mat[15] += this.mat[12] * var1 + this.mat[13] * var2 + this.mat[14] * var3;
        return this;
    }

    public float determinant()
    {
        return this.mat[0] * (this.mat[5] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) + this.mat[6] * (this.mat[11] * this.mat[13] - this.mat[15] * this.mat[9]) + this.mat[7] * (this.mat[14] * this.mat[9] - this.mat[10] * this.mat[13])) + this.mat[1] * (this.mat[4] * (this.mat[11] * this.mat[14] - this.mat[10] * this.mat[15]) + this.mat[6] * (this.mat[15] * this.mat[8] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[10] * this.mat[12] - this.mat[14] * this.mat[8])) + this.mat[2] * (this.mat[4] * (this.mat[15] * this.mat[9] - this.mat[11] * this.mat[13]) + this.mat[5] * (this.mat[11] * this.mat[12] - this.mat[15] * this.mat[8]) + this.mat[7] * (this.mat[13] * this.mat[8] - this.mat[12] * this.mat[9])) + this.mat[3] * (this.mat[4] * (this.mat[10] * this.mat[13] - this.mat[14] * this.mat[9]) + this.mat[5] * (this.mat[14] * this.mat[8] - this.mat[10] * this.mat[12]) + this.mat[6] * (this.mat[12] * this.mat[9] - this.mat[13] * this.mat[8]));
    }

    public Transform inverse()
    {
        float var1 = this.determinant();

        if (var1 == 0.0F)
        {
            throw new RuntimeException("Attempting to invert a singular matrix");
        }
        else
        {
            float var2 = this.mat[5] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) + this.mat[6] * (this.mat[11] * this.mat[13] - this.mat[15] * this.mat[9]) + this.mat[7] * (this.mat[14] * this.mat[9] - this.mat[10] * this.mat[13]);
            float var3 = this.mat[1] * (this.mat[11] * this.mat[14] - this.mat[10] * this.mat[15]) + this.mat[2] * (this.mat[15] * this.mat[9] - this.mat[11] * this.mat[13]) + this.mat[3] * (this.mat[10] * this.mat[13] - this.mat[14] * this.mat[9]);
            float var4 = this.mat[1] * (this.mat[15] * this.mat[6] - this.mat[14] * this.mat[7]) + this.mat[2] * (this.mat[13] * this.mat[7] - this.mat[15] * this.mat[5]) + this.mat[3] * (this.mat[14] * this.mat[5] - this.mat[13] * this.mat[6]);
            float var5 = this.mat[1] * (this.mat[10] * this.mat[7] - this.mat[11] * this.mat[6]) + this.mat[2] * (this.mat[11] * this.mat[5] - this.mat[7] * this.mat[9]) + this.mat[3] * (this.mat[6] * this.mat[9] - this.mat[10] * this.mat[5]);
            float var6 = this.mat[4] * (this.mat[11] * this.mat[14] - this.mat[10] * this.mat[15]) + this.mat[6] * (this.mat[15] * this.mat[8] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[10] * this.mat[12] - this.mat[14] * this.mat[8]);
            float var7 = this.mat[0] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) + this.mat[2] * (this.mat[11] * this.mat[12] - this.mat[15] * this.mat[8]) + this.mat[3] * (this.mat[14] * this.mat[8] - this.mat[10] * this.mat[12]);
            float var8 = this.mat[0] * (this.mat[14] * this.mat[7] - this.mat[15] * this.mat[6]) + this.mat[2] * (this.mat[15] * this.mat[4] - this.mat[12] * this.mat[7]) + this.mat[3] * (this.mat[12] * this.mat[6] - this.mat[14] * this.mat[4]);
            float var9 = this.mat[0] * (this.mat[11] * this.mat[6] - this.mat[10] * this.mat[7]) + this.mat[2] * (this.mat[7] * this.mat[8] - this.mat[11] * this.mat[4]) + this.mat[3] * (this.mat[10] * this.mat[4] - this.mat[6] * this.mat[8]);
            float var10 = this.mat[4] * (this.mat[15] * this.mat[9] - this.mat[11] * this.mat[13]) + this.mat[5] * (this.mat[11] * this.mat[12] - this.mat[15] * this.mat[8]) + this.mat[7] * (this.mat[13] * this.mat[8] - this.mat[12] * this.mat[9]);
            float var11 = this.mat[0] * (this.mat[11] * this.mat[13] - this.mat[15] * this.mat[9]) + this.mat[1] * (this.mat[15] * this.mat[8] - this.mat[11] * this.mat[12]) + this.mat[3] * (this.mat[12] * this.mat[9] - this.mat[13] * this.mat[8]);
            float var12 = this.mat[0] * (this.mat[15] * this.mat[5] - this.mat[13] * this.mat[7]) + this.mat[1] * (this.mat[12] * this.mat[7] - this.mat[15] * this.mat[4]) + this.mat[3] * (this.mat[13] * this.mat[4] - this.mat[12] * this.mat[5]);
            float var13 = this.mat[0] * (this.mat[7] * this.mat[9] - this.mat[11] * this.mat[5]) + this.mat[1] * (this.mat[11] * this.mat[4] - this.mat[7] * this.mat[8]) + this.mat[3] * (this.mat[5] * this.mat[8] - this.mat[4] * this.mat[9]);
            float var14 = this.mat[4] * (this.mat[10] * this.mat[13] - this.mat[14] * this.mat[9]) + this.mat[5] * (this.mat[14] * this.mat[8] - this.mat[10] * this.mat[12]) + this.mat[6] * (this.mat[12] * this.mat[9] - this.mat[13] * this.mat[8]);
            float var15 = this.mat[0] * (this.mat[14] * this.mat[9] - this.mat[10] * this.mat[13]) + this.mat[1] * (this.mat[10] * this.mat[12] - this.mat[14] * this.mat[8]) + this.mat[2] * (this.mat[13] * this.mat[8] - this.mat[12] * this.mat[9]);
            float var16 = this.mat[0] * (this.mat[13] * this.mat[6] - this.mat[14] * this.mat[5]) + this.mat[1] * (this.mat[14] * this.mat[4] - this.mat[12] * this.mat[6]) + this.mat[2] * (this.mat[12] * this.mat[5] - this.mat[13] * this.mat[4]);
            float var17 = this.mat[0] * (this.mat[10] * this.mat[5] - this.mat[6] * this.mat[9]) + this.mat[1] * (this.mat[6] * this.mat[8] - this.mat[10] * this.mat[4]) + this.mat[2] * (this.mat[4] * this.mat[9] - this.mat[5] * this.mat[8]);
            this.mat[0] = var2 / var1;
            this.mat[1] = var3 / var1;
            this.mat[2] = var4 / var1;
            this.mat[3] = var5 / var1;
            this.mat[4] = var6 / var1;
            this.mat[5] = var7 / var1;
            this.mat[6] = var8 / var1;
            this.mat[7] = var9 / var1;
            this.mat[8] = var10 / var1;
            this.mat[9] = var11 / var1;
            this.mat[10] = var12 / var1;
            this.mat[11] = var13 / var1;
            this.mat[12] = var14 / var1;
            this.mat[13] = var15 / var1;
            this.mat[14] = var16 / var1;
            this.mat[15] = var17 / var1;
            return this;
        }
    }

    public Transform transpose()
    {
        float var1 = 0.0F;
        var1 = this.mat[1];
        this.mat[1] = this.mat[4];
        this.mat[4] = var1;
        var1 = this.mat[2];
        this.mat[2] = this.mat[8];
        this.mat[8] = var1;
        var1 = this.mat[3];
        this.mat[3] = this.mat[12];
        this.mat[12] = var1;
        var1 = this.mat[6];
        this.mat[6] = this.mat[9];
        this.mat[9] = var1;
        var1 = this.mat[7];
        this.mat[7] = this.mat[13];
        this.mat[13] = var1;
        var1 = this.mat[11];
        this.mat[11] = this.mat[14];
        this.mat[14] = var1;
        return this;
    }

    public String toString()
    {
        return String.format("{%#7.4f,%#7.4f,%#7.4f,%#7.4f},\n{%#7.4f,%#7.4f,%#7.4f,%#7.4f},\n{%#7.4f,%#7.4f,%#7.4f,%#7.4f},\n{%#7.4f,%#7.4f,%#7.4f,%#7.4f}", new Object[] {Float.valueOf(this.mat[0]), Float.valueOf(this.mat[1]), Float.valueOf(this.mat[2]), Float.valueOf(this.mat[3]), Float.valueOf(this.mat[4]), Float.valueOf(this.mat[5]), Float.valueOf(this.mat[6]), Float.valueOf(this.mat[7]), Float.valueOf(this.mat[8]), Float.valueOf(this.mat[9]), Float.valueOf(this.mat[10]), Float.valueOf(this.mat[11]), Float.valueOf(this.mat[12]), Float.valueOf(this.mat[13]), Float.valueOf(this.mat[14]), Float.valueOf(this.mat[15])});
    }

    protected static void mult(float[] var0, float[] var1)
    {
        float var2 = var0[0] * var1[0] + var0[1] * var1[4] + var0[2] * var1[8] + var0[3] * var1[12];
        float var3 = var0[0] * var1[1] + var0[1] * var1[5] + var0[2] * var1[9] + var0[3] * var1[13];
        float var4 = var0[0] * var1[2] + var0[1] * var1[6] + var0[2] * var1[10] + var0[3] * var1[14];
        float var5 = var0[0] * var1[3] + var0[1] * var1[7] + var0[2] * var1[11] + var0[3] * var1[15];
        float var6 = var0[4] * var1[0] + var0[5] * var1[4] + var0[6] * var1[8] + var0[7] * var1[12];
        float var7 = var0[4] * var1[1] + var0[5] * var1[5] + var0[6] * var1[9] + var0[7] * var1[13];
        float var8 = var0[4] * var1[2] + var0[5] * var1[6] + var0[6] * var1[10] + var0[7] * var1[14];
        float var9 = var0[4] * var1[3] + var0[5] * var1[7] + var0[6] * var1[11] + var0[7] * var1[15];
        float var10 = var0[8] * var1[0] + var0[9] * var1[4] + var0[10] * var1[8] + var0[11] * var1[12];
        float var11 = var0[8] * var1[1] + var0[9] * var1[5] + var0[10] * var1[9] + var0[11] * var1[13];
        float var12 = var0[8] * var1[2] + var0[9] * var1[6] + var0[10] * var1[10] + var0[11] * var1[14];
        float var13 = var0[8] * var1[3] + var0[9] * var1[7] + var0[10] * var1[11] + var0[11] * var1[15];
        float var14 = var0[12] * var1[0] + var0[13] * var1[4] + var0[14] * var1[8] + var0[15] * var1[12];
        float var15 = var0[12] * var1[1] + var0[13] * var1[5] + var0[14] * var1[9] + var0[15] * var1[13];
        float var16 = var0[12] * var1[2] + var0[13] * var1[6] + var0[14] * var1[10] + var0[15] * var1[14];
        float var17 = var0[12] * var1[3] + var0[13] * var1[7] + var0[14] * var1[11] + var0[15] * var1[15];
        var0[0] = var2;
        var0[1] = var3;
        var0[2] = var4;
        var0[3] = var5;
        var0[4] = var6;
        var0[5] = var7;
        var0[6] = var8;
        var0[7] = var9;
        var0[8] = var10;
        var0[9] = var11;
        var0[10] = var12;
        var0[11] = var13;
        var0[12] = var14;
        var0[13] = var15;
        var0[14] = var16;
        var0[15] = var17;
    }

    /*public Object clone() throws CloneNotSupportedException
    {
        return this.clone();
    }*/
}
