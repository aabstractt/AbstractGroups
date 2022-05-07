package dev.thatsmybaby.groups;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import dev.thatsmybaby.groups.sender.NukkitSender;
import dev.thatsmybaby.shared.VersionInfo;
import dev.thatsmybaby.shared.command.CoreAdminCommand;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;

import java.io.File;

public final class AbstractGroups extends PluginBase {

    @Getter private AbstractGroups instance;

    @Override @SuppressWarnings("unchecked")
    public void onEnable() {
        instance = this;

        VersionInfo.loadVersion();

        PluginCommand<AbstractGroups> command = (PluginCommand<AbstractGroups>) this.getPluginCommand("coreadmin");

        if (command == null) {
            this.getLogger().warning("Command 'coreadmin' not registered...");

            return;
        }

        command.setExecutor((commandSender, command1, label, args) -> {
            CoreAdminCommand.getInstance().execute(wrapSender(commandSender), label, null, args);

            return false;
        });

        MysqlProvider.getInstance().init(new File(this.getDataFolder(), "hikari.properties"));
        GroupCachedFactory.getInstance().init();
    }

    public AbstractSender wrapSender(CommandSender sender) {
        return new NukkitSender(sender);
    }
}