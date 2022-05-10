package dev.thatsmybaby.shared.commands.group;

import dev.thatsmybaby.shared.command.AbstractArgument;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.object.MetaCache;
import dev.thatsmybaby.shared.sender.AbstractSender;

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

        MetaCache metaCache = groupCache.getMetaCache();

        metaCache.invalidate();

        metaCache.addPrefix(args[0]);
        metaCache.recalculate();

        // TODO: Execute storeMeta, no storeGroup because i need store the MetaCache

        GroupCachedFactory.getInstance().storeGroup(groupCache);

        // TODO: Send log action
    }
}