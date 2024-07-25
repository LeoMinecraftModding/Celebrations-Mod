package team.leomc.celebrations.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class LanternItem extends BlockItem {
	public LanternItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		ItemStack gift = ItemStack.EMPTY;
		if (stack.has(CDataComponents.LANTERN.get())) {
			Lantern lantern = stack.get(CDataComponents.LANTERN.get());
			if (lantern != null) {
				gift = lantern.gift().copy();
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
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}

		InteractionHand oppositeHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		ItemStack newGift = player.getItemInHand(oppositeHand).copy();
		if (gift.isEmpty() && !newGift.isEmpty() && !(newGift.getItem() instanceof LanternItem)) {
			Lantern lantern = Lantern.DEFAULT;
			if (stack.has(CDataComponents.LANTERN.get())) {
				lantern = stack.get(CDataComponents.LANTERN.get());
			}
			if (lantern != null) {
				lantern = new Lantern(lantern.effects(), newGift, player.getName());
				stack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.LANTERN.get(), lantern).build());
				if (!player.getAbilities().instabuild) {
					player.setItemInHand(oppositeHand, ItemStack.EMPTY);
				}
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

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		Lantern lantern = stack.get(CDataComponents.LANTERN.get());
		if (lantern != null) {
			if (!lantern.effects().isEmpty()) {
				tooltipComponents.add(Component.translatable("tooltip." + Celebrations.ID + ".lantern_with_potion").withStyle(ChatFormatting.DARK_GREEN));
				addPotionTooltip(lantern.effects(), tooltipComponents::add, 1.0F, context.tickRate());
			}
			if (!lantern.gift().isEmpty()) {
				tooltipComponents.add(Component.translatable("tooltip." + Celebrations.ID + ".gift").withStyle(ChatFormatting.YELLOW));
			}
			if (!lantern.giftSender().getString().isEmpty()) {
				tooltipComponents.add(Component.translatable("tooltip." + Celebrations.ID + ".gift_sender").append(lantern.giftSender()).withStyle(ChatFormatting.YELLOW));
			}
		}
	}

	public static void addPotionTooltip(Iterable<MobEffectInstance> effects, Consumer<Component> tooltipAdder, float durationFactor, float ticksPerSecond) {
		Iterator<MobEffectInstance> effectsIterator;
		MutableComponent component;
		Holder<MobEffect> effectHolder;
		for (effectsIterator = effects.iterator(); effectsIterator.hasNext(); tooltipAdder.accept(component.withStyle(effectHolder.value().getCategory().getTooltipFormatting()))) {
			MobEffectInstance mobeffectinstance = effectsIterator.next();
			component = Component.translatable(mobeffectinstance.getDescriptionId());
			effectHolder = mobeffectinstance.getEffect();
			if (mobeffectinstance.getAmplifier() > 0) {
				component = Component.translatable("potion.withAmplifier", component, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
			}
			if (!mobeffectinstance.endsWithin(20)) {
				component = Component.translatable("potion.withDuration", component, MobEffectUtil.formatDuration(mobeffectinstance, durationFactor, ticksPerSecond));
			}
		}
	}
}
