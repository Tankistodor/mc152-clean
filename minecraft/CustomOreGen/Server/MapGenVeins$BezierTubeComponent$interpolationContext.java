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
    { //TODO JOPA
        float pX = this.pos[0];
        float pY = this.pos[1];
        float pZ = this.pos[2];
        float dX = this.der[0];
        float dY = this.der[1];
        float dZ = this.der[2];
        float r = this.radius;

        do
        {
            float nt = this.t + this.dt;
            this.this$1.interpolatePosition(this.pos, nt);
            float deltaX = pX - this.pos[0];
            float deltaY = pY - this.pos[1];
            float deltaZ = pZ - this.pos[2];
            float d2 = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
            this.err = d2;
            this.radius = this.this$1.interpolateRadius(nt); //MAY BE HERE
            float avg2R = r + this.radius;
            float sin2;

            if (this.calcDer)
            {
                this.this$1.interpolateDerivative(this.der, nt);
                this.derLen = MathHelper.sqrt_float(this.der[0] * this.der[0] + this.der[1] * this.der[1] + this.der[2] * this.der[2]);
                this.der[0] /= this.derLen;
                this.der[1] /= this.derLen;
                this.der[2] /= this.derLen;
                deltaX = -dZ * this.der[1] + dY * this.der[2];
                deltaY = dZ * this.der[0] - dX * this.der[2];
                deltaZ = -dY * this.der[0] + dX * this.der[1];
                sin2 = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                this.err += avg2R * avg2R * sin2;
            }

            sin2 = var1 * var1;

            if (this.err > sin2)
            {
                this.dt = (float)((double)this.dt * 0.6D);
            }
            else
            {
                if (this.err >= sin2 / 5.0F)
                {
                    this.t += this.dt;
                    return this.t < 0.5F;
                }

                if (this.radius == 0) {
                	return false;
                }
                
                if (Float.isNaN(this.radius)) {
                	return false;
                }
                
                this.dt = (float)((double)this.dt * 1.8D);
            }
        }
        while (this.dt >= Math.ulp(this.t) * 1.0F);

        throw new RuntimeException("CustomOreGen: Detected a possible infinite loop during bezier interpolation.  Please report this error.");
    }
}
