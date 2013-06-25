package CustomOreGen.Util;

import CustomOreGen.Util.SimpleProfiler$1;

class SimpleProfiler$Section implements Comparable
{
    public final Object key;
    public int slot;
    public int order;
    public int hits;
    public int running;
    public long runTime;
    public int open;
    public long openTime;
    public static int sortField = 2;

    private SimpleProfiler$Section(Object var1, int var2, int var3)
    {
        this.key = var1;
        this.slot = var2;
        this.order = var3;
    }

    public int compareTo(SimpleProfiler$Section var1)
    {
        int var2 = (int)Math.signum((float)sortField);

        switch (Math.abs(sortField))
        {
            case 1:
                return var2 * Double.compare((double)this.runTime, (double)var1.runTime);

            case 2:
                return var2 * Double.compare((double)this.openTime, (double)var1.openTime);

            case 3:
                return var2 * Double.compare((double)this.hits, (double)var1.hits);

            case 4:
                return var2 * Double.compare((double)this.order, (double)var1.order);

            default:
                return 0;
        }
    }

    public int compareTo(Object var1)
    {
        return this.compareTo((SimpleProfiler$Section)var1);
    }

    SimpleProfiler$Section(Object var1, int var2, int var3, SimpleProfiler$1 var4)
    {
        this(var1, var2, var3);
    }
}
