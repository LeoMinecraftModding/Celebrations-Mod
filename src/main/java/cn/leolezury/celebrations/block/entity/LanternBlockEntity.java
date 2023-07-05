package cn.leolezury.celebrations.block.entity;

import cn.leolezury.celebrations.init.BlockEntityInit;
import cn.leolezury.celebrations.util.CBNbtUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class LanternBlockEntity extends BlockEntity {
    public LanternBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.LANTERN.get(), pos, state);
    }

    private final List<MobEffectInstance> effects = Lists.newArrayList();

    private ItemStack gift = ItemStack.EMPTY;
    private String giftOwnerType = null;
    private UUID giftOwnerUUID = null;
    private String giftOwnerName = null;

    public void addEffect(MobEffectInstance newInstance) {
        if (newInstance.getEffect().isInstantenous()) {
            return;
        }
        boolean hasSame = false;
        for (int i = 0; i < effects.size(); i++) {
            MobEffectInstance instance = effects.get(i);
            if (instance.getEffect().equals(newInstance.getEffect())) {
                hasSame = true;
                MobEffectInstance replaced = new MobEffectInstance(instance.getEffect(), Math.max(instance.getDuration(), newInstance.getDuration()), Math.max(instance.getAmplifier(), newInstance.getAmplifier()));
                effects.set(i, replaced);
            }
        }
        if (!hasSame) {
            effects.add(newInstance);
        }
    }

    public void clearEffects() {
        effects.clear();
    }

    public List<MobEffectInstance> getEffects() {
        return effects;
    }

    public void setGift(ItemStack gift) {
        this.gift = gift;
    }

    public ItemStack getGift() {
        return gift;
    }

    public void setGiftOwnerType(String giftOwnerType) {
        this.giftOwnerType = giftOwnerType;
    }

    public String getGiftOwnerType() {
        return giftOwnerType;
    }

    public void setGiftOwnerUUID(UUID giftOwnerUUID) {
        this.giftOwnerUUID = giftOwnerUUID;
    }

    public UUID getGiftOwnerUUID() {
        return giftOwnerUUID;
    }

    public void setGiftOwnerName(String giftOwnerName) {
        this.giftOwnerName = giftOwnerName;
    }

    public String getGiftOwnerName() {
        return giftOwnerName;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CBNbtUtils.readLanternInfo(this, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CBNbtUtils.writeLanternInfo(this, tag);
    }

    private static void tryGiveEffect(MobEffectInstance instance, LivingEntity entity) {
        MobEffect effect = instance.getEffect();
        if (entity.hasEffect(effect)) {
            MobEffectInstance oldInstance = entity.getEffect(effect);
            if (oldInstance != null && oldInstance.getDuration() > 200) {
                if (oldInstance.getAmplifier() < instance.getAmplifier()) {
                    entity.addEffect(new MobEffectInstance(effect, Math.max(oldInstance.getDuration(), instance.getDuration()), Math.max(oldInstance.getAmplifier(), instance.getAmplifier())));
                }
            } else {
                entity.addEffect(instance);
            }
        } else {
            entity.addEffect(instance);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LanternBlockEntity entity) {
        //TODO: config
        int radius = 20;
        boolean giveNonEnemyBeneficialEffect = true;
        boolean giveNonEnemyHarmfulEffect = true;
        boolean giveEnemyBeneficialEffect = true;
        boolean giveEnemyHarmfulEffect = true;

        if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
            for (int i = 0; i < entity.effects.size(); i++) {
                MobEffectInstance instance = entity.effects.get(i);
                MobEffectInstance replaced = new MobEffectInstance(instance.getEffect(), instance.getDuration() - 1, instance.getAmplifier());
                entity.effects.set(i, replaced);
            }
            entity.effects.removeIf(instance -> instance.getDuration() <= 0);
            for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(radius))) {
                for (MobEffectInstance instance : entity.effects) {
                    MobEffectInstance toGive = new MobEffectInstance(instance.getEffect(), 400, instance.getAmplifier());
                    if (livingEntity instanceof Enemy && ((instance.getEffect().isBeneficial() && giveEnemyBeneficialEffect) || giveEnemyHarmfulEffect)) {
                        tryGiveEffect(toGive, livingEntity);
                    }
                    if (!(livingEntity instanceof Enemy) && ((instance.getEffect().isBeneficial() && giveNonEnemyBeneficialEffect) || giveNonEnemyHarmfulEffect)) {
                        tryGiveEffect(toGive, livingEntity);
                    }
                }
            }
        }
    }
}
