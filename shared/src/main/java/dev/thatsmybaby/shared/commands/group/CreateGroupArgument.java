package dev.thatsmybaby.shared.commands.group;

import dev.thatsmybaby.shared.TaskUtils;
import dev.thatsmybaby.shared.actionlog.LoggedAction;
import dev.thatsmybaby.shared.command.AbstractArgument;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.sender.AbstractSender;

public final class CreateGroupArgument extends AbstractArgument<Void> {

    public CreateGroupArgument(String name, String description, String permission, String usage) {
        super(name, description, permission, usage);
    }

    @Override
    public void execute(AbstractSender sender, String commandLabel, String argumentLabel, Void ignored, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("GROUP_CREATE_USAGE", commandLabel);

            return;
        }

        GroupCachedFactory factory = GroupCachedFactory.getInstance();

        if (factory.getCachedGroup(args[0]) != null) {
            sender.sendMessage("GROUP_ALREADY_EXISTS", args[0]);

            return;
        }

        TaskUtils.runAsync(() -> {
            GroupCache groupCache = factory.storeGroup(args[0]);

            if (groupCache == null) {
                sender.sendMessage("&cAn error occurred when tried insert a group into database");

                return;
            }

            factory.setCachedGroup(groupCache);

            LoggedAction.build().timestamp().source(sender).target(null, args[0])
                    .type(LoggedAction.Type.GROUP)
                    .action("create")
                    .build().submit(sender);
        });
    }
}