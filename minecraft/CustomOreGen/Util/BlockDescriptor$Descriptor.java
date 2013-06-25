package CustomOreGen.Util;

import java.util.regex.Pattern;

public class BlockDescriptor$Descriptor
{
    public final String description;
    public final float weight;
    public int matches = 0;
    private Pattern pattern = null;

    public BlockDescriptor$Descriptor(String var1, float var2)
    {
        this.description = var1;
        this.weight = var2;
    }

    public Pattern getPattern()
    {
        if (this.pattern == null)
        {
            this.pattern = Pattern.compile(this.description, 2);
        }

        return this.pattern;
    }

    public String toString()
    {
        return this.description + " - " + Float.toString(this.weight);
    }
}
