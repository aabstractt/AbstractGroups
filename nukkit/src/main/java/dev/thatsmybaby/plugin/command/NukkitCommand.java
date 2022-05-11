package dev.thatsmybaby.plugin.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import dev.thatsmybaby.plugin.AbstractGroups;
import dev.thatsmybaby.shared.command.CoreAdminCommand;

public final class NukkitCommand extends Command {

    public NukkitCommand(String name) {
        super(name);

        this.setAliases(new String[]{"ca"});
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        CoreAdminCommand.getInstance().execute(AbstractGroups.getInstance().wrapSender(commandSender), s, null, args);

        return false;
    }
}