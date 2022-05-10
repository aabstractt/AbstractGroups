package dev.thatsmybaby.plugin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import dev.thatsmybaby.plugin.sender.NukkitSender;
import dev.thatsmybaby.shared.AbstractPlugin;
import dev.thatsmybaby.shared.VersionInfo;
import dev.thatsmybaby.shared.command.CoreAdminCommand;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;

import java.io.File;
import java.util.stream.Stream;

public final class AbstractGroups extends PluginBase implements AbstractPlugin {

    @Getter private static AbstractGroups instance;

    private AbstractSender consoleSender;

    @Override @SuppressWarnings("unchecked")
    public void onEnable() {
        instance = this;

        this.saveResource("hikari.properties");

        this.consoleSender = new NukkitSender(Server.getInstance().getConsoleSender());

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
        return sender instanceof Player ? new NukkitSender(sender) : this.consoleSender;
    }

    @Override
    public boolean isPrimaryThread() {
        return Server.getInstance().isPrimaryThread();
    }

    public Stream<AbstractSender> getOnlineSenders() {
        return Stream.concat(
                Stream.of(this.consoleSender),
                this.getServer().getOnlinePlayers().values().stream().map(this::wrapSender)
        );
    }

    @Override
    public boolean storeLog() {
        return this.getConfig().getBoolean("log.store", true);
    }

    @Override
    public boolean logRemote() {
        return this.getConfig().getBoolean("log.notify-remote");
    }
}