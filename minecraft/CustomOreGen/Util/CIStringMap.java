package CustomOreGen.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CIStringMap implements Map
{
    protected final Map backingMap;
    protected final Map keyMap;

    public CIStringMap(Map var1)
    {
        this.backingMap = var1;
        this.keyMap = new HashMap();
        Iterator var2 = var1.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();
            String var4 = (String)var3.getKey();
            String var5 = this.uniformKey(var4);

            if (this.keyMap.containsKey(var5))
            {
                throw new IllegalArgumentException("Backing set contains duplicate key \'" + var4 + "\'");
            }

            this.keyMap.put(var5, var4);
        }
    }

    public CIStringMap()
    {
        this.backingMap = new HashMap();
        this.keyMap = new HashMap();
    }

    public int size()
    {
        return this.backingMap.size();
    }

    public boolean isEmpty()
    {
        return this.backingMap.isEmpty();
    }

    public boolean containsKey(Object var1)
    {
        if (var1 != null)
        {
            String var2 = this.uniformKey((String)var1);
            var1 = this.keyMap.get(var2);

            if (var1 == null)
            {
                return false;
            }
        }

        return this.backingMap.containsKey(var1);
    }

    public String getCanonicalKey(String var1)
    {
        String var2 = this.uniformKey(var1);
        return (String)this.keyMap.get(var2);
    }

    public boolean containsValue(Object var1)
    {
        return this.backingMap.containsValue(var1);
    }

    public Object get(Object var1)
    {
        if (var1 != null)
        {
            String var2 = this.uniformKey((String)var1);
            var1 = this.keyMap.get(var2);

            if (var1 == null)
            {
                return null;
            }
        }

        return this.backingMap.get(var1);
    }

    public Object put(String var1, Object var2)
    {
        if (var1 != null)
        {
            String var3 = this.uniformKey(var1);
            String var4 = (String)this.keyMap.get(var3);
            this.keyMap.put(var3, var1);

            if (var4 != null)
            {
                Object var5 = this.backingMap.remove(var4);
                this.backingMap.put(var1, var2);
                return var5;
            }
        }

        return this.backingMap.put(var1, var2);
    }

    public Object remove(Object var1)
    {
        if (var1 != null)
        {
            String var2 = this.uniformKey((String)var1);
            var1 = this.keyMap.remove(var2);

            if (var1 == null)
            {
                return null;
            }
        }

        return this.backingMap.remove(var1);
    }

    public void putAll(Map var1)
    {
        Iterator var2 = var1.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();
            this.put((String)var3.getKey(), var3.getValue());
        }
    }

    public void clear()
    {
        this.keyMap.clear();
        this.backingMap.clear();
    }

    public Set keySet()
    {
        return this.backingMap.keySet();
    }

    public Collection values()
    {
        return this.backingMap.values();
    }

    public Set entrySet()
    {
        return this.backingMap.entrySet();
    }

    public int hashCode()
    {
        return this.backingMap.hashCode();
    }

    public boolean equals(Object var1)
    {
        return var1 instanceof CIStringMap ? this.backingMap.equals(((CIStringMap)var1).backingMap) : false;
    }

    public String toString()
    {
        return this.backingMap.toString();
    }

    protected String uniformKey(String var1)
    {
        return var1 == null ? null : var1.toLowerCase();
    }

    public Object put(Object var1, Object var2)
    {
        return this.put((String)var1, var2);
    }
}
