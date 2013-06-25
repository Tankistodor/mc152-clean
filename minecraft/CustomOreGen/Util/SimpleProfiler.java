package CustomOreGen.Util;

import CustomOreGen.Util.SimpleProfiler$1;
import CustomOreGen.Util.SimpleProfiler$Section;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class SimpleProfiler
{
    public static final SimpleProfiler globalProfiler = new SimpleProfiler();
    private int _sectionCount = 0;
    private SimpleProfiler$Section[] _sections = new SimpleProfiler$Section[32];
    private int _openCount = 0;
    private SimpleProfiler$Section[] _openSections = new SimpleProfiler$Section[32];

    private int getSlot(Object var1)
    {
        int var2;

        for (var2 = Math.abs(var1.hashCode()) % this._sections.length; this._sections[var2] != null && this._sections[var2].key != var1; var2 = (var2 + 1) % this._sections.length)
        {
            ;
        }

        return var2;
    }

    private SimpleProfiler$Section getSection(Object var1)
    {
        if (this._sectionCount * 4 > this._sections.length)
        {
            SimpleProfiler$Section[] var2 = this._sections;
            this._sections = new SimpleProfiler$Section[var2.length * 2];
            SimpleProfiler$Section[] var3 = var2;
            int var4 = var2.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                SimpleProfiler$Section var6 = var3[var5];

                if (var6 != null)
                {
                    var6.slot = this.getSlot(var6.key);
                    this._sections[var6.slot] = var6;
                }
            }
        }

        int var7 = this.getSlot(var1);

        if (this._sections[var7] == null)
        {
            this._sections[var7] = new SimpleProfiler$Section(var1, var7, ++this._sectionCount, (SimpleProfiler$1)null);
        }

        return this._sections[var7];
    }

    public void startSection(Object var1)
    {
        long var2 = System.nanoTime();
        SimpleProfiler$Section var4;

        if (this._openCount > 0)
        {
            var4 = this._openSections[this._openCount - 1];

            if (var4.running > 0 && --var4.running == 0)
            {
                var4.runTime += var2;
            }
        }

        var4 = this.getSection(var1);
        ++var4.hits;

        if (this._openCount >= this._openSections.length)
        {
            this._openSections = (SimpleProfiler$Section[])Arrays.copyOf(this._openSections, this._openCount * 2);
        }

        this._openSections[this._openCount++] = var4;

        if (var4.running++ == 0)
        {
            var4.runTime -= var2;
        }

        if (var4.open++ == 0)
        {
            var4.openTime -= var2;
        }
    }

    public void pauseSection()
    {
        if (this._openCount > 0)
        {
            SimpleProfiler$Section var1 = this._openSections[this._openCount - 1];

            if (var1.running > 0 && --var1.running == 0)
            {
                var1.runTime += System.nanoTime();
            }
        }
    }

    public void unpauseSection()
    {
        if (this._openCount > 0)
        {
            SimpleProfiler$Section var1 = this._openSections[this._openCount - 1];

            if (var1.running++ == 0)
            {
                var1.runTime -= System.nanoTime();
            }
        }
    }

    public void endSection()
    {
        long var1 = System.nanoTime();

        if (this._openCount <= 0)
        {
            throw new RuntimeException("Open/Close section mismatch.");
        }
        else
        {
            SimpleProfiler$Section var3 = this._openSections[--this._openCount];

            if (--var3.running == 0)
            {
                var3.runTime += var1;
            }

            if (--var3.open == 0)
            {
                var3.openTime += var1;
            }

            if (this._openCount > 0)
            {
                SimpleProfiler$Section var4 = this._openSections[this._openCount - 1];

                if (var4.running++ == 0)
                {
                    var4.runTime -= var1;
                }
            }
        }
    }

    public void dumpSections(int var1)
    {
        double var2 = 0.0D;
        Vector var4 = new Vector(this._sectionCount);
        int var5;

        for (var5 = 0; var5 < this._sections.length; ++var5)
        {
            if (this._sections[var5] != null)
            {
                if ((double)this._sections[var5].openTime > var2)
                {
                    var2 = (double)this._sections[var5].openTime;
                }

                var4.add(this._sections[var5]);
            }
        }

        SimpleProfiler$Section.sortField = var1;
        Collections.sort(var4);
        System.out.println("Open Sections (" + this._openCount + ") :");

        for (var5 = 0; var5 < this._openCount; ++var5)
        {
            System.out.println("  " + this._openSections[var5].key);
        }

        System.out.format("%40s    %8s          %21s               %21s       \n", new Object[] {"Key", "Hits", "Run Time (us)", "Open Time (us)"});
        Iterator var7 = var4.iterator();

        while (var7.hasNext())
        {
            SimpleProfiler$Section var6 = (SimpleProfiler$Section)var7.next();
            System.out.format("%40s =  %8d  %12d (%8.0f) (%6.2f%%)  %12d (%8.0f) (%6.2f%%)\n", new Object[] {var6.key, Integer.valueOf(var6.hits), Long.valueOf(var6.runTime / 1000L), Float.valueOf((float)var6.runTime / 1000.0F / (float)var6.hits), Double.valueOf(100.0D * (double)var6.runTime / var2), Long.valueOf(var6.openTime / 1000L), Float.valueOf((float)var6.openTime / 1000.0F / (float)var6.hits), Double.valueOf(100.0D * (double)var6.openTime / var2)});
        }
    }

    public void clear()
    {
        this._sectionCount = 0;
        Arrays.fill(this._sections, (Object)null);
        this._openCount = 0;
        Arrays.fill(this._openSections, (Object)null);
    }
}
