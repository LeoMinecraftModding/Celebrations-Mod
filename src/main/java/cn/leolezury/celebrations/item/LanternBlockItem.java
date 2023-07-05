package cn.leolezury.celebrations.item;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.CBLanternBlock;
import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import cn.leolezury.celebrations.util.CBNbtUtils;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LanternBlockItem extends BlockItem {
    public LanternBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        ItemStack gift = ItemStack.of(tag.getCompound("GiftItem"));
        if (!gift.isEmpty() && (player.addItem(gift) || level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), gift)))) {
            tag.put("GiftItem", new CompoundTag());
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("message." + Celebrations.MOD_ID + ".take_gift").withStyle(ChatFormatting.ITALIC));
            }
            return InteractionResultHolder.success(stack);
        }

        InteractionHand oppositeHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack newGift = player.getItemInHand(oppositeHand);
        if (gift.isEmpty() && !newGift.isEmpty() && !(newGift.getItem() instanceof LanternBlockItem)) {
            CompoundTag compoundTag = new CompoundTag();
            newGift.save(compoundTag);
            tag.put("GiftItem", compoundTag);
            tag.putString("GiftOwnerType", "Player");
            tag.putString("GiftOwnerName", player.getName().getString());
            tag.putUUID("GiftOwnerUUID", player.getUUID());
            if (!player.getAbilities().instabuild) {
                player.setItemInHand(oppositeHand, ItemStack.EMPTY);
            }
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable("message." + Celebrations.MOD_ID + ".put_gift").withStyle(ChatFormatting.ITALIC));
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        if (face.equals(Direction.DOWN) && level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN, SupportType.CENTER)) {
            return super.place(context);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (getBlock() instanceof CBLanternBlock lanternBlock) {
            BlockEntity entity = lanternBlock.newBlockEntity(pos, state);
            CompoundTag tag = context.getItemInHand().getOrCreateTag();

            if (entity instanceof LanternBlockEntity lanternBlockEntity) {
                CBNbtUtils.readLanternInfo(lanternBlockEntity, tag);
                boolean result = level.setBlock(pos, state, 11);
                if (result) {
                    level.getChunkAt(pos).addAndRegisterBlockEntity(entity);
                    return true;
                }
            }
        }
        return false;
    }

    public static void addPotionTooltip(List<MobEffectInstance> effectList, List<Component> components, float f) {
        List<Pair<Attribute, AttributeModifier>> list = Lists.newArrayList();
        if (effectList.isEmpty()) {
            components.add(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY));
        } else {
            for (MobEffectInstance mobeffectinstance : effectList) {
                MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
                MobEffect mobeffect = mobeffectinstance.getEffect();
                Map<Attribute, AttributeModifier> map = mobeffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), mobeffect.getAttributeModifierValue(mobeffectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list.add(new Pair<>(entry.getKey(), attributemodifier1));
                    }
                }

                if (mobeffectinstance.getAmplifier() > 0) {
                    mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
                }

                if (!mobeffectinstance.endsWithin(20)) {
                    mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, f));
                }

                components.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
            }
        }

        if (!list.isEmpty()) {
            components.add(CommonComponents.EMPTY);
            components.add(Component.translatable("tooltip." + Celebrations.MOD_ID + ".when_applied").withStyle(ChatFormatting.DARK_PURPLE));

            for (Pair<Attribute, AttributeModifier> pair : list) {
                AttributeModifier attributemodifier2 = pair.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;
                if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    d1 = attributemodifier2.getAmount();
                } else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    components.add(Component.translatable("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    components.add(Component.translatable("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(pair.getFirst().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        List<MobEffectInstance> effectInstances = new ArrayList<>();
        Component ownerName = null;
        boolean hasGift = false;
        if (!ItemStack.of(tag.getCompound("GiftItem")).isEmpty()) {
            hasGift = true;

            String giftOwnerType = tag.getString("GiftOwnerType");
            String giftOwnerName = tag.getString("GiftOwnerName");

            if (giftOwnerType.equals("Player") && tag.hasUUID("GiftOwnerUUID")) {
                UUID giftOwnerUUID = tag.getUUID("GiftOwnerUUID");
                if (level != null) {
                    Player player = level.getPlayerByUUID(giftOwnerUUID);
                    if (player != null) {
                        ownerName = player.getName();
                    }
                }
                if (ownerName == null && !giftOwnerName.isEmpty()) {
                    ownerName = Component.literal(giftOwnerName);
                }
            } else if (tag.getString("GiftOwnerType").equals("Mob")) {
                if (!giftOwnerName.isEmpty()) {
                    ownerName = Component.translatable(giftOwnerName);
                }
            }
        }
        if (tag.contains("Effects")) {
            ListTag listTag = tag.getList("Effects", 10);
            for (int i = 0; i < listTag.size(); ++i) {
                MobEffectInstance instance = MobEffectInstance.load(listTag.getCompound(i));
                if (instance != null) {
                    effectInstances.add(instance);
                }
            }
        }

        if (hasGift) {
            components.add(Component.translatable("tooltip." + Celebrations.MOD_ID + ".gift").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD));
            if (ownerName != null) {
                components.add(Component.translatable("tooltip." + Celebrations.MOD_ID + ".gift_owner").append(ownerName).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD));
            }
        }

        if (!effectInstances.isEmpty()) {
            components.add(Component.translatable("tooltip." + Celebrations.MOD_ID + ".lantern_with_potion").withStyle(ChatFormatting.ITALIC));
        }
        addPotionTooltip(effectInstances, components, 1.0F);
    }
}
