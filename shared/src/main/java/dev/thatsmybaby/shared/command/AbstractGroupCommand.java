package dev.thatsmybaby.shared.command;

import dev.thatsmybaby.shared.sender.AbstractSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGroupCommand {

    private final Set<AbstractArgument> abstractArguments = new HashSet<>();

    protected void addArgument(AbstractArgument... abstractArguments) {
        this.abstractArguments.addAll(Arrays.asList(abstractArguments));
    }

    protected AbstractArgument getArgument(String argumentLabel) {
        return this.abstractArguments.stream()
                .filter(abstractArgument -> abstractArgument.getName().equalsIgnoreCase(argumentLabel))
                .findFirst()
                .orElse(null);
    }

    protected void execute(AbstractSender sender, String commandLabel, String argumentLabel, String[] args) {
        if (!sender.hasPermission("abstractgroups.command")) {
            sender.sendMessage("&cYou don't have permissions to use this command.");

            return;
        }

        AbstractArgument argument = this.getArgument(argumentLabel);

        if (argument == null) {
            // TODO: Send message help

            return;
        }

        if (argument.getPermission() != null && !sender.hasPermission(argument.getPermission())) {
            sender.sendMessage("&cYou don't have permissions to use this command.");

            return;
        }

        argument.execute(sender, commandLabel, argumentLabel, args);
    }
}