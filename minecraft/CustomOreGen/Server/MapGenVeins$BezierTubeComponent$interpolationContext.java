package CustomOreGen.Server;

import CustomOreGen.Server.MapGenVeins$BezierTubeComponent;
import net.minecraft.util.MathHelper;

class MapGenVeins$BezierTubeComponent$interpolationContext
{
    public float[] pos;
    public float[] der;
    public float derLen;
    public float radius;
    public float err;
    public float t;
    public float dt;
    public boolean calcDer;

    final MapGenVeins$BezierTubeComponent this$1;

    public MapGenVeins$BezierTubeComponent$interpolationContext(MapGenVeins$BezierTubeComponent var1)
    {
        this.this$1 = var1;
        this.pos = new float[3];
        this.der = new float[3];
        this.t = 10.0F;
        this.dt = 0.05F;
    }

    public void init(float var1, boolean var2)
    {
        this.t = this.this$1.prev != null && this.this$1.prev.next != this.this$1 ? -1.0F : -0.5F;

        if (var1 > 0.0F)
        {
            this.dt = var1;
        }

        this.this$1.interpolatePosition(this.pos, this.t);
        this.radius = this.this$1.interpolateRadius(this.t);
        this.calcDer = var2;

        if (this.calcDer)
        {
            this.this$1.interpolateDerivative(this.der, this.t);
            this.derLen = MathHelper.sqrt_float(this.der[0] * this.der[0] + this.der[1] * this.der[1] + this.der[2] * this.der[2]);
            this.der[0] /= this.derLen;
            this.der[1] /= this.derLen;
            this.der[2] /= this.derLen;
        }
        else
        {
            this.derLen = 0.0F;
            this.der[0] = this.der[1] = this.der[2] = 0.0F;
        }

        this.err = 0.0F;
    }

    public boolean advance(float var1)
    {
        float var2 = this.pos[0];
        float var3 = this.pos[1];
        float var4 = this.pos[2];
        float var5 = this.der[0];
        float var6 = this.der[1];
        float var7 = this.der[2];
        float var8 = this.radius;

        do
        {
            float var9 = this.t + this.dt;
            this.this$1.interpolatePosition(this.pos, var9);
            float var10 = var2 - this.pos[0];
            float var11 = var3 - this.pos[1];
            float var12 = var4 - this.pos[2];
            float var13 = var10 * var10 + var11 * var11 + var12 * var12;
            this.err = var13;
            this.radius = this.this$1.interpolateRadius(var9);
            float var14 = var8 + this.radius;
            float var15;

            if (this.calcDer)
            {
                this.this$1.interpolateDerivative(this.der, var9);
                this.derLen = MathHelper.sqrt_float(this.der[0] * this.der[0] + this.der[1] * this.der[1] + this.der[2] * this.der[2]);
                this.der[0] /= this.derLen;
                this.der[1] /= this.derLen;
                this.der[2] /= this.derLen;
                var10 = -var7 * this.der[1] + var6 * this.der[2];
                var11 = var7 * this.der[0] - var5 * this.der[2];
                var12 = -var6 * this.der[0] + var5 * this.der[1];
                var15 = var10 * var10 + var11 * var11 + var12 * var12;
                this.err += var14 * var14 * var15;
            }

            var15 = var1 * var1;

            if (this.err > var15)
            {
                this.dt = (float)((double)this.dt * 0.6D);
            }
            else
            {
                if (this.err >= var15 / 5.0F)
                {
                    this.t += this.dt;
                    return this.t < 0.5F;
                }

                this.dt = (float)((double)this.dt * 1.8D);
            }
        }
        while (this.dt >= Math.ulp(this.t) * 2.0F);

        throw new RuntimeException("CustomOreGen: Detected a possible infinite loop during bezier interpolation.  Please report this error.");
    }
}
