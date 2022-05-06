package dev.thatsmybaby.groups;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import dev.thatsmybaby.shared.command.CoreAdminCommand;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;

public final class AbstractGroups extends PluginBase {

    @Getter private AbstractGroups instance;

    @Override @SuppressWarnings("unchecked")
    public void onEnable() {
        instance = this;

        PluginCommand<AbstractGroups> command = (PluginCommand<AbstractGroups>) this.getPluginCommand("coreadmin");

        if (command == null) {
            this.getLogger().warning("Command 'coreadmin' not registered...");

            return;
        }

        command.setExecutor((commandSender, command1, label, args) -> {
            CoreAdminCommand.getInstance().execute(wipeSender(commandSender), label, null, args);

            return false;
        });
    }

    public AbstractSender wipeSender(CommandSender sender) {
        return null;
    }
}