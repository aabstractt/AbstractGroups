package dev.thatsmybaby.plugin.sender;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class NukkitSender extends AbstractSender {

    private final CommandSender sender;

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public String getXuid() {
        return this.sender instanceof Player ? ((Player) this.sender).getLoginChainData().getXUID() : "";
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public void sendMessage(String k, String... args) {
        this.sender.sendMessage(TextFormat.colorize(k));

        // TODO: Translate the message and send it
    }
}