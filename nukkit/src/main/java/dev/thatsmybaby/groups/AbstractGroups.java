package dev.thatsmybaby.groups;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;

public final class AbstractGroups extends PluginBase {

    @Getter private AbstractGroups instance;

    @Override
    public void onEnable() {
        instance = this;
    }
}