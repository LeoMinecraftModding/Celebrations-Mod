package team.leomc.celebrations.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.CLanternBlock;
import team.leomc.celebrations.block.entity.LanternBlockEntity;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.util.Lantern;

public class LanternBlockItem extends BlockItem {
	public LanternBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		ItemStack gift = ItemStack.EMPTY;
		if (stack.has(CDataComponents.LANTERN.get())) {
			Lantern lantern = stack.get(CDataComponents.LANTERN.get());
			if (lantern != null) {
				gift = lantern.gift();
			}
		}
		if (!gift.isEmpty() && (player.addItem(gift) || level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), gift)))) {
			if (stack.has(CDataComponents.LANTERN.get())) {
				Lantern lantern = stack.get(CDataComponents.LANTERN.get());
				if (lantern != null) {
					lantern = new Lantern(lantern.effects(), ItemStack.EMPTY, Component.empty());
					stack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.LANTERN.get(), lantern).build());
				}
			}
			if (!level.isClientSide) {
				player.sendSystemMessage(Component.translatable("message." + Celebrations.ID + ".take_gift").withStyle(ChatFormatting.ITALIC));
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}

		InteractionHand oppositeHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		ItemStack newGift = player.getItemInHand(oppositeHand);
		if (gift.isEmpty() && !newGift.isEmpty() && !(newGift.getItem() instanceof LanternBlockItem)) {
			Lantern lantern = Lantern.DEFAULT;
			if (stack.has(CDataComponents.LANTERN.get())) {
				lantern = stack.get(CDataComponents.LANTERN.get());
			}
			if (lantern != null) {
				lantern = new Lantern(lantern.effects(), newGift.copy(), player.getName());
				stack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.LANTERN.get(), lantern).build());
				if (!player.getAbilities().instabuild) {
					player.setItemInHand(oppositeHand, ItemStack.EMPTY);
				}
			}
			if (!level.isClientSide) {
				player.sendSystemMessage(Component.translatable("message." + Celebrations.ID + ".put_gift").withStyle(ChatFormatting.ITALIC));
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();

		if (getBlock() instanceof CLanternBlock lanternBlock) {
			BlockEntity entity = lanternBlock.newBlockEntity(pos, state);
			if (stack.has(CDataComponents.LANTERN)) {
				Lantern lantern = stack.get(CDataComponents.LANTERN.get());
				if (lantern != null && entity instanceof LanternBlockEntity lanternBlockEntity) {
					lanternBlockEntity.setLantern(lantern);
				}
			}

			if (entity != null) {
				boolean result = level.setBlock(pos, state, 11);
				if (result) {
					level.getChunkAt(pos).addAndRegisterBlockEntity(entity);
					return true;
				}
			}
		}

		return false;
	}
}
