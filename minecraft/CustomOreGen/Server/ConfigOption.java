package CustomOreGen.Server;

import CustomOreGen.Server.ConfigOption$DisplayGroup;
import CustomOreGen.Server.ConfigOption$DisplayState;

public abstract class ConfigOption
{
    private final String _name;
    private String _displayName = null;
    private String _description = null;
    private ConfigOption$DisplayState _displayState;
    private ConfigOption$DisplayGroup _displayGroup;

    public ConfigOption(String var1)
    {
        this._displayState = ConfigOption$DisplayState.hidden;
        this._displayGroup = null;
        this._name = var1;
    }

    public String getName()
    {
        return this._name;
    }

    public String getDisplayName()
    {
        return this._displayName == null ? this._name : this._displayName;
    }

    public void setDisplayName(String var1)
    {
        this._displayName = var1;
    }

    public String getDescription()
    {
        return this._description;
    }

    public void setDescription(String var1)
    {
        this._description = var1;
    }

    public void setDisplayState(ConfigOption$DisplayState var1)
    {
        this._displayState = var1;
    }

    public ConfigOption$DisplayState getDisplayState()
    {
        return this._displayState;
    }

    public void setDisplayGroup(ConfigOption$DisplayGroup var1)
    {
        this._displayGroup = var1;
    }

    public ConfigOption$DisplayGroup getDisplayGroup()
    {
        return this._displayGroup;
    }

    public abstract Object getValue();

    public abstract boolean setValue(Object var1);
}
