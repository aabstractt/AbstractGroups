package dev.thatsmybaby.plugin;

import dev.thatsmybaby.shared.VersionInfo;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;

public final class AbstractGroups extends Plugin {

    @Getter private static AbstractGroups instance;

    public void onEnable() {
        instance = this;

        this.saveResource("config.yml");
        this.saveResource("messages.yml");

        VersionInfo.loadVersion();
    }
}