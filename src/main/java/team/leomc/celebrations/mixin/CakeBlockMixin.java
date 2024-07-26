package team.leomc.celebrations.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.leomc.celebrations.event.CEvents;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin {
	@Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	protected void useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
		List<Allay> allays = level.getEntitiesOfClass(Allay.class, player.getBoundingBox().inflate(40));
		for (Allay allay : allays) {
			Optional<UUID> playerId = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
			if (playerId.isPresent() && playerId.get().equals(player.getUUID())) {
				allay.getPersistentData().putInt(CEvents.TAG_PLACE_CANDLE_CELEBRATION_TICKS_LEFT, 48000);
			}
		}
	}
}
