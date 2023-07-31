package cn.leolezury.celebrations.command;

import cn.leolezury.celebrations.util.CelebrationUtils;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Calendar;
import java.util.Map;

public class CelebrationsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("celebrations").requires((stack) -> stack.hasPermission(2)).then(Commands.literal("info").executes((context) -> {
            ServerLevel serverLevel = context.getSource().getLevel();
            context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.info_is_celebrating").append(Component.literal(String.valueOf(CelebrationUtils.isCelebrating(serverLevel)))), false);
            context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.info_ticks_since_last_celebration").append(Component.literal(String.valueOf(CelebrationUtils.getTicksSinceLastCelebration(serverLevel)))), false);
            context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.info_ticks_since_celebration_started").append(Component.literal(String.valueOf(CelebrationUtils.getCelebrationTicks(serverLevel)))), false);
            return 0;
        })).then(Commands.literal("start").executes((context) -> {
            ServerLevel serverLevel = context.getSource().getLevel();
            CelebrationUtils.setCelebrating(serverLevel, true);
            context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.info_celebration_started"), true);
            return 0;
        })).then(Commands.literal("terminate").executes((context) -> {
            ServerLevel serverLevel = context.getSource().getLevel();
            CelebrationUtils.setCelebrating(serverLevel, false);
            context.getSource().sendSuccess(() -> Component.translatable("message.celebrations.info_celebration_terminated"), true);
            return 0;
        })));
    }
}
