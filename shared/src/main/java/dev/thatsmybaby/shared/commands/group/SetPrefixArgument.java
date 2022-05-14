package dev.thatsmybaby.shared.commands.group;

import dev.thatsmybaby.shared.actionlog.LoggedAction;
import dev.thatsmybaby.shared.command.AbstractArgument;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.MetaCache;
import dev.thatsmybaby.shared.sender.AbstractSender;

import java.util.Arrays;

public final class SetPrefixArgument extends AbstractArgument<GroupCache> {

    public SetPrefixArgument() {
        super("setprefix", "Set prefix", "abstractgroups.group.setprefix", "/coreadmin group <group> setprefix <prefix>");
    }

    @Override
    public void execute(AbstractSender sender, String commandLabel, String argumentLabel, GroupCache groupCache, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("COMMAND_INVALID_USAGE", commandLabel, argumentLabel);

            return;
        }

        Object priorityArgument = this.parseArgumentContext("priority", Arrays.copyOfRange(args, 1, args.length));

        if (priorityArgument != null && !(priorityArgument instanceof Integer)) {
            sender.sendMessage("INVALID_ARGUMENT_CONTEXT");

            return;
        }

        int priority = priorityArgument == null ? 0 : (int) priorityArgument;

        if (priority < 0) priority = 0;

        MetaCache metaCache = groupCache.getMetaCache();

        if (metaCache.findPrefix(args[0])) {
            // TODO: Stuff

            return;
        }

        metaCache.invalidatePrefix(args[0]);
        metaCache.recalculate();

        GroupCachedFactory.getInstance().storeMeta(groupCache.getRowId(), LoggedAction.Type.GROUP, "prefix", args[0]);

        // TODO: Send log action
    }
}