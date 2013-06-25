package CustomOreGen.Config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

class ValidatorImport$WildcardFileFilter implements FilenameFilter
{
    private Pattern _pattern;

    public ValidatorImport$WildcardFileFilter(String var1)
    {
        this._pattern = Pattern.compile("\\Q" + var1.replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q") + "\\E");
    }

    public boolean accept(File var1, String var2)
    {
        return this._pattern.matcher(var2).matches();
    }
}
