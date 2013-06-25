package CustomOreGen.Server;

import CustomOreGen.Util.Transform;

public class WireframeBuilder
{
    public static WireframeBuilder instance = new WireframeBuilder();
    public long quads = 0L;
    public long lines = 0L;
    protected final Transform defaultTrans = new Transform();

    public int startDisplayList()
    {
        this.quads = this.lines = 0L;
        return 1;
    }

    public void endDisplayList() {}

    public void deleteDisplayList(int var1) {}

    public void setColor(long var1) {}

    public void setTransform(Transform var1) {}

    public Transform getDefaultTransform()
    {
        return this.defaultTrans;
    }

    public void addLine(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        ++this.lines;
    }

    public void addQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12)
    {
        ++this.quads;
    }

    public void addQuadStrip(float[] var1, float[] var2, int var3, boolean var4)
    {
        this.quads += (long)(var3 - 1);
    }

    public void addCube()
    {
        this.quads += 6L;
    }

    public void addCircle(int var1)
    {
        this.lines += (long)var1;
    }

    public void addSphere(int var1, int var2)
    {
        this.quads += (long)(var1 * var2);
    }

    public void getCirclePoints(float[] var1, int var2, float var3, float var4) {}
}
