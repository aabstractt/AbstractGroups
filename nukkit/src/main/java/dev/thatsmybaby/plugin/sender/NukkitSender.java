package dev.thatsmybaby.plugin.sender;

import cn.nukkit.command.CommandSender;
import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class NukkitSender extends AbstractSender {

    private final CommandSender sender;

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void sendMessage(String k, String... args) {

    }
}