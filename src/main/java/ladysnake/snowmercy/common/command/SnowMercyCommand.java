package ladysnake.snowmercy.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import ladysnake.snowmercy.cca.SnowMercyComponents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SnowMercyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal
        		("snowmercy").requires((serverCommandSource) -> serverCommandSource.hasPermission(2)))
                .then((Commands.literal("start").executes((commandContext) -> executeStart(commandContext.getSource()))))
                .then(Commands.literal("stop").executes((commandContext) -> executeStop(commandContext.getSource()))));
    }

    private static int executeStart(CommandSourceStack source) {
    	//TODO
        //SnowMercyComponents.SNOWMERCY.get(source.getLevel()).startEvent(source.getLevel());
    	SnowMercyComponents.SNOWMERCY.startEvent(source.getLevel());
        return 0;
    }

    private static int executeStop(CommandSourceStack source) {
    	//TODO
        //SnowMercyComponents.SNOWMERCY.get(source.getLevel()).stopEvent(source.getLevel());
    	SnowMercyComponents.SNOWMERCY.stopEvent(source.getLevel());
        return 0;
    }
}
