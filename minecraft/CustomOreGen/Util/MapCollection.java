package CustomOreGen.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class MapCollection implements Collection
{
    protected final Map backingMap;

    public MapCollection(Map var1)
    {
        this.backingMap = var1;
        Iterator var2 = var1.entrySet().iterator();
        Object var4;
        Object var5;
        Object var6;

        do
        {
            do
            {
                if (!var2.hasNext())
                {
                    return;
                }

                Entry var3 = (Entry)var2.next();
                var4 = var3.getKey();
                var5 = var3.getValue();
                var6 = this.getKey(var5);
            }
            while (var4 == var6);
        }
        while (var4 != null && var4.equals(var6));

        throw new IllegalArgumentException("Backing set contains inconsistent key/value pair \'" + var4 + "\' -> \'" + var5 + "\', expected \'" + var6 + "\' -> \'" + var5 + "\'");
    }

    protected abstract Object getKey(Object var1);

    public int size()
    {
        return this.backingMap.size();
    }

    public boolean isEmpty()
    {
        return this.backingMap.isEmpty();
    }

    public boolean contains(Object var1)
    {
        Object var2 = this.getKey(var1);
        return this.backingMap.containsKey(var2);
    }

    public Iterator iterator()
    {
        return this.backingMap.values().iterator();
    }

    public Object[] toArray()
    {
        return this.backingMap.values().toArray();
    }

    public Object[] toArray(Object[] var1)
    {
        return this.backingMap.values().toArray(var1);
    }

    public boolean add(Object var1)
    {
        Object var2 = this.getKey(var1);

        if (var1 != null)
        {
            return this.backingMap.put(var2, var1) != var1;
        }
        else
        {
            boolean var3 = this.backingMap.containsKey(var2);
            Object var4 = this.backingMap.put(var2, var1);
            return !var3 || var1 != var4;
        }
    }

    public boolean remove(Object var1)
    {
        Object var2 = this.getKey(var1);
        return this.backingMap.keySet().remove(var2);
    }

    public boolean containsAll(Collection var1)
    {
        Iterator var2 = var1.iterator();
        Object var3;

        do
        {
            if (!var2.hasNext())
            {
                return true;
            }

            var3 = var2.next();
        }
        while (this.contains(var3));

        return false;
    }

    public boolean addAll(Collection var1)
    {
        boolean var2 = false;
        Object var4;

        for (Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.add(var4))
        {
            var4 = var3.next();
        }

        return var2;
    }

    public boolean removeAll(Collection var1)
    {
        boolean var2 = false;
        Object var4;

        for (Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.remove(var4))
        {
            var4 = var3.next();
        }

        return var2;
    }

    public boolean retainAll(Collection var1)
    {
        ArrayList var2 = new ArrayList(this.backingMap.size());
        Iterator var3 = var1.iterator();

        while (var3.hasNext())
        {
            Object var4 = var3.next();
            var2.add(this.getKey(var4));
        }

        return this.backingMap.keySet().retainAll(var2);
    }

    public void clear()
    {
        this.backingMap.clear();
    }

    public int hashCode()
    {
        return this.backingMap.hashCode();
    }

    public boolean equals(Object var1)
    {
        return var1 instanceof MapCollection ? this.backingMap.equals(((MapCollection)var1).backingMap) : false;
    }

    public String toString()
    {
        return this.backingMap.values().toString();
    }
}
