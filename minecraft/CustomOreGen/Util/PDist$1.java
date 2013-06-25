package CustomOreGen.Util;

import CustomOreGen.Util.PDist$Type;

class PDist$1
{
    static final int[] $SwitchMap$net$minecraft$src$CustomOreGen$Util$PDist$Type = new int[PDist$Type.values().length];

    static
    {
        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Util$PDist$Type[PDist$Type.uniform.ordinal()] = 1;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Util$PDist$Type[PDist$Type.normal.ordinal()] = 2;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
