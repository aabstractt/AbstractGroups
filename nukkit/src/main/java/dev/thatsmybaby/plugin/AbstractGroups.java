package dev.thatsmybaby.plugin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import dev.thatsmybaby.plugin.command.NukkitCommand;
import dev.thatsmybaby.plugin.sender.NukkitSender;
import dev.thatsmybaby.shared.AbstractPlugin;
import dev.thatsmybaby.shared.VersionInfo;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.provider.MysqlProvider;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;

import java.io.File;
import java.util.stream.Stream;

public final class AbstractGroups extends PluginBase {

    @Getter private static AbstractGroups instance;

    private AbstractSender consoleSender;

    @Override
    public void onEnable() {
        instance = this;

        this.saveResource("hikari.properties");

        this.consoleSender = new NukkitSender(Server.getInstance().getConsoleSender());

        // Load AbstractPlugin instance
        AbstractPlugin.getInstance().setStoreLog(this.getConfig().getBoolean("log.store", true));
        AbstractPlugin.getInstance().setLogRemote(this.getConfig().getBoolean("log.notify-remote"));
        AbstractPlugin.getInstance().setFuturePrimaryThread(() -> Server.getInstance().isPrimaryThread());
        AbstractPlugin.getInstance().setFutureOnlineSenders(() -> Stream.concat(
                Stream.of(this.consoleSender),
                this.getServer().getOnlinePlayers().values().stream().map(this::wrapSender)
        ));

        VersionInfo.loadVersion();

        this.getServer().getCommandMap().register("coreadmin", new NukkitCommand("coreadmin"));

        MysqlProvider.getInstance().init(new File(this.getDataFolder(), "hikari.properties"));

        GroupCachedFactory.getInstance().init();
    }

    public AbstractSender wrapSender(CommandSender sender) {
        return sender instanceof Player ? new NukkitSender(sender) : this.consoleSender;
    }
}