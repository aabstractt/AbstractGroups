package dev.thatsmybaby.shared.command;

import dev.thatsmybaby.shared.sender.AbstractSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public abstract class AbstractArgument {

    private final String name;
    private final String description;
    private final String permission;
    private final String usage;

    public abstract void execute(AbstractSender sender, String commandLabel, String argumentLabel, String[] args);
}