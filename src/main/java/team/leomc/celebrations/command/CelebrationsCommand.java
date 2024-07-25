package team.leomc.celebrations.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import team.leomc.celebrations.util.CelebrationUtils;

public class CelebrationsCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("celebrations").requires((stack) -> stack.hasPermission(2)).then(Commands.literal("info").executes((context) -> {
			ServerLevel serverLevel = context.getSource().getLevel();
			context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.is_celebrating").append(Component.literal(String.valueOf(CelebrationUtils.isCelebrating(serverLevel)))), false);
			context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.ticks_since_last_celebration").append(Component.literal(String.valueOf(CelebrationUtils.getTicksSinceLastCelebration(serverLevel)))), false);
			context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.ticks_since_celebration_started").append(Component.literal(String.valueOf(CelebrationUtils.getCelebrationTicks(serverLevel)))), false);
			return 0;
		})).then(Commands.literal("start").executes((context) -> {
			ServerLevel serverLevel = context.getSource().getLevel();
			CelebrationUtils.setCelebrating(serverLevel, true);
			context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.celebration_started"), true);
			return 0;
		})).then(Commands.literal("terminate").executes((context) -> {
			ServerLevel serverLevel = context.getSource().getLevel();
			CelebrationUtils.setCelebrating(serverLevel, false);
			context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.celebration_terminated"), true);
			return 0;
		})));
	}
}
