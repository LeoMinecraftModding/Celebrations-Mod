package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import team.leomc.celebrations.config.CCommonConfig;
import team.leomc.celebrations.registry.CBlockEntities;
import team.leomc.celebrations.util.Lantern;

import java.util.List;

public class LanternBlockEntity extends BlockEntity {
	public LanternBlockEntity(BlockPos pos, BlockState state) {
		super(CBlockEntities.LANTERN.get(), pos, state);
	}

	private Lantern lantern = Lantern.DEFAULT;

	public Lantern getLantern() {
		return lantern;
	}

	public void setLantern(Lantern lantern) {
		this.lantern = lantern;
	}

	public void addEffect(MobEffectInstance newInstance) {
		if (newInstance.getEffect().value().isInstantenous()) {
			return;
		}
		boolean hasSame = false;
		for (int i = 0; i < lantern.effects().size(); i++) {
			MobEffectInstance instance = lantern.effects().get(i);
			if (instance.getEffect().is(newInstance.getEffect())) {
				hasSame = true;
				MobEffectInstance replaced = new MobEffectInstance(instance.getEffect(), Math.max(instance.getDuration(), newInstance.getDuration()), Math.max(instance.getAmplifier(), newInstance.getAmplifier()));
				lantern.effects().set(i, replaced);
			}
		}
		if (!hasSame) {
			lantern.effects().add(newInstance);
		}
	}

	public void clearEffects() {
		lantern.effects().clear();
	}

	public List<MobEffectInstance> getEffects() {
		return lantern.effects();
	}

	public void setGift(ItemStack gift) {
		this.lantern = new Lantern(lantern.effects(), gift, lantern.giftSender());
	}

	public ItemStack getGift() {
		return lantern.gift();
	}

	public void setGiftSender(Component giftSender) {
		this.lantern = new Lantern(lantern.effects(), lantern.gift(), giftSender);
	}

	public Component getGiftSender() {
		return lantern.giftSender();
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.lantern = Lantern.fromTag(tag.getCompound("lantern"), registries);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("lantern", lantern.toTag(registries));
	}

	private static void tryGiveEffect(MobEffectInstance instance, LivingEntity entity) {
		Holder<MobEffect> effect = instance.getEffect();
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
		double radius = CCommonConfig.LANTERN_EFFECT_RADIUS.get();
		boolean giveNonEnemyBeneficialEffect = CCommonConfig.GIVE_NON_ENEMY_BENEFICIAL_EFFECT.get();
		boolean giveNonEnemyHarmfulEffect = CCommonConfig.GIVE_NON_ENEMY_HARMFUL_EFFECT.get();
		boolean giveEnemyBeneficialEffect = CCommonConfig.GIVE_ENEMY_BENEFICIAL_EFFECT.get();
		boolean giveEnemyHarmfulEffect = CCommonConfig.GIVE_ENEMY_HARMFUL_EFFECT.get();

		if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
			for (int i = 0; i < entity.getEffects().size(); i++) {
				MobEffectInstance instance = entity.getEffects().get(i);
				MobEffectInstance replaced = new MobEffectInstance(instance.getEffect(), instance.getDuration() - 1, instance.getAmplifier());
				entity.getEffects().set(i, replaced);
			}
			entity.getEffects().removeIf(instance -> instance.getDuration() <= 0);
			for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(radius))) {
				for (MobEffectInstance instance : entity.getEffects()) {
					MobEffectInstance toGive = new MobEffectInstance(instance.getEffect(), 400, instance.getAmplifier());
					if (livingEntity instanceof Enemy && ((instance.getEffect().value().isBeneficial() && giveEnemyBeneficialEffect) || (!instance.getEffect().value().isBeneficial() && giveEnemyHarmfulEffect))) {
						tryGiveEffect(toGive, livingEntity);
					}
					if (!(livingEntity instanceof Enemy) && ((instance.getEffect().value().isBeneficial() && giveNonEnemyBeneficialEffect) || (!instance.getEffect().value().isBeneficial() && giveNonEnemyHarmfulEffect))) {
						tryGiveEffect(toGive, livingEntity);
					}
				}
			}
		}
	}
}
