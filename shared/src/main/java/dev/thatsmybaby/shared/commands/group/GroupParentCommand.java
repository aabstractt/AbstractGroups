package dev.thatsmybaby.shared.commands.group;

import com.google.common.collect.ImmutableSet;
import dev.thatsmybaby.shared.command.AbstractArgument;
import dev.thatsmybaby.shared.command.AbstractParentCommand;
import dev.thatsmybaby.shared.factory.GroupCachedFactory;
import dev.thatsmybaby.shared.object.GroupCache;
import dev.thatsmybaby.shared.sender.AbstractSender;

public final class GroupParentCommand extends AbstractParentCommand<GroupCache> {

    public GroupParentCommand() {
        super("group", "abstractgroups.group.command", Type.TAKES_ARGUMENT_FOR_TARGET, ImmutableSet.<AbstractArgument<GroupCache>>builder()
                .add(new SetPrefixArgument())
                .build());
    }

    @Override
    protected GroupCache getTarget(AbstractSender sender, String targetParsed) {
        GroupCache groupCache = GroupCachedFactory.getInstance().getCachedGroup(targetParsed);

        if (groupCache == null) {
            sender.sendMessage("GROUP_NOT_FOUND", targetParsed);
        }

        return groupCache;
    }
}