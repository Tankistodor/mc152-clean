package CustomOreGen.Server;

import CustomOreGen.Server.MapGenVeins$BranchType;

class MapGenVeins$1
{
    static final int[] $SwitchMap$net$minecraft$src$CustomOreGen$Server$MapGenVeins$BranchType = new int[MapGenVeins$BranchType.values().length];

    static
    {
        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Server$MapGenVeins$BranchType[MapGenVeins$BranchType.Ellipsoid.ordinal()] = 1;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$net$minecraft$src$CustomOreGen$Server$MapGenVeins$BranchType[MapGenVeins$BranchType.Bezier.ordinal()] = 2;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
