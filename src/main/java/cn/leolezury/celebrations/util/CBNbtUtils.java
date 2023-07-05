package cn.leolezury.celebrations.util;

import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class CBNbtUtils {
    public static void writeLanternInfo(LanternBlockEntity lanternBlockEntity, CompoundTag tag) {
        if (!lanternBlockEntity.getEffects().isEmpty()) {
            ListTag listTag = new ListTag();

            for (MobEffectInstance instance : lanternBlockEntity.getEffects()) {
                listTag.add(instance.save(new CompoundTag()));
            }

            tag.put("Effects", listTag);
        }
        if (!lanternBlockEntity.getGift().isEmpty()) {
            CompoundTag compoundTag = new CompoundTag();
            lanternBlockEntity.getGift().save(compoundTag);
            tag.put("GiftItem", compoundTag);
            tag.putString("GiftOwnerType", lanternBlockEntity.getGiftOwnerType());
            tag.putUUID("GiftOwnerUUID", lanternBlockEntity.getGiftOwnerUUID());
            tag.putString("GiftOwnerName", lanternBlockEntity.getGiftOwnerName());
        }
    }

    public static void readLanternInfo(LanternBlockEntity lanternBlockEntity, CompoundTag tag) {
        if (tag.contains("Effects")) {
            ListTag listTag = tag.getList("Effects", 10);
            lanternBlockEntity.clearEffects();

            for (int i = 0; i < listTag.size(); ++i) {
                MobEffectInstance instance = MobEffectInstance.load(listTag.getCompound(i));
                if (instance != null) {
                    lanternBlockEntity.addEffect(instance);
                }
            }
        }
        ItemStack gift = ItemStack.of(tag.getCompound("GiftItem"));
        if (!gift.isEmpty()) {
            lanternBlockEntity.setGift(gift);
            lanternBlockEntity.setGiftOwnerType(tag.getString("GiftOwnerType"));
            if (tag.hasUUID("GiftOwnerUUID")) {
                lanternBlockEntity.setGiftOwnerUUID(tag.getUUID("GiftOwnerUUID"));
            }
            lanternBlockEntity.setGiftOwnerName(tag.getString("GiftOwnerName"));
        }
    }
}
