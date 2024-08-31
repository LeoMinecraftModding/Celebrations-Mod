package team.leomc.celebrations.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.leomc.celebrations.item.component.BalloonData;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.util.CMathUtil;

import java.util.UUID;

public class Balloon extends Entity {
	private LivingEntity owner;
	private UUID ownerId;
	protected static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(Balloon.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(Balloon.class, EntityDataSerializers.ITEM_STACK);
	protected static final EntityDataAccessor<Boolean> MAIN_HAND = SynchedEntityData.defineId(Balloon.class, EntityDataSerializers.BOOLEAN);

	public Balloon(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.noCulling = true;
	}

	public LivingEntity getOwner() {
		return owner;
	}

	public void setOwner(LivingEntity owner) {
		this.ownerId = owner.getUUID();
		this.owner = owner;
		this.setOwnerId(owner.getId());
	}

	public int getOwnerId() {
		return this.getEntityData().get(OWNER_ID);
	}

	public void setOwnerId(int ownerId) {
		this.getEntityData().set(OWNER_ID, ownerId);
	}

	public void setItem(ItemStack stack) {
		this.getEntityData().set(ITEM_STACK, stack.copyWithCount(1));
	}

	public ItemStack getItem() {
		return this.getEntityData().get(ITEM_STACK);
	}

	public void setMainHand(boolean mainHand) {
		this.getEntityData().set(MAIN_HAND, mainHand);
	}

	public boolean isMainHand() {
		return this.getEntityData().get(MAIN_HAND);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(OWNER_ID, -1)
			.define(ITEM_STACK, CItems.BALLOON.get().getDefaultInstance())
			.define(MAIN_HAND, true);
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			if (owner == null && ownerId != null) {
				if (((ServerLevel) this.level()).getEntity(ownerId) instanceof LivingEntity livingEntity) {
					owner = livingEntity;
					setOwnerId(livingEntity.getId());
				}
				if (owner == null) {
					ownerId = null;
				}
			}
			if (owner != null) {
				ItemStack stack = isMainHand() ? owner.getMainHandItem() : owner.getOffhandItem();
				BalloonData data = stack.get(CDataComponents.BALLOON_DATA.get());
				if (data == null) {
					data = BalloonData.DEFAULT;
				}
				setCustomName(data.name());
				setCustomNameVisible(!data.name().getString().isEmpty());
			}
		} else {
			owner = level().getEntity(getOwnerId()) instanceof LivingEntity livingEntity ? livingEntity : null;
			if (owner instanceof BalloonOwner balloonOwner) {
				if (isMainHand()) {
					balloonOwner.setMainHandBalloon(this);
				} else {
					balloonOwner.setOffhandBalloon(this);
				}
			}
		}
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if (owner != null) {
			if (!level().isClientSide && owner instanceof BalloonOwner balloonOwner && (isMainHand() ? balloonOwner.getMainHandBalloon() : balloonOwner.getOffhandBalloon()) != this) {
				discard();
			}
			if (!level().isClientSide && isMainHand() && !ItemStack.isSameItemSameComponents(getItem(), owner.getMainHandItem())) {
				discard();
			}
			if (!level().isClientSide && !isMainHand() && !ItemStack.isSameItemSameComponents(getItem(), owner.getOffhandItem())) {
				discard();
			}
			Vec3 targetPos = getTargetPos(1);
			Vec3 diff = targetPos.subtract(position());
			double length = diff.length();
			this.setDeltaMovement(CMathUtil.lerpVec(targetPos.distanceTo(position()) > 5 ? 0.25f : 0.05f, getDeltaMovement(), diff.normalize().scale(Math.min(length, 0.3))));
		} else {
			Vec3 movement = this.getDeltaMovement();
			this.setDeltaMovement(movement.x, Math.min(movement.y + 0.02, 0.08), movement.z);
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
	}

	public Vec3 getTargetPos(float partialTicks) {
		return CMathUtil.rotationToPosition(owner.getPosition(partialTicks).add(0, owner.getBbHeight() * 1.3f, 0), owner.getBbWidth() * 1.2f, 0, (Mth.lerp(partialTicks, owner.yRotO, owner.getYRot())) + 90 + ((isMainHand() && owner.getMainArm() == HumanoidArm.RIGHT) || (!isMainHand() && owner.getMainArm() == HumanoidArm.LEFT) ? 55 : -55));
	}

	public int getRenderColor() {
		BalloonData data = getItem().get(CDataComponents.BALLOON_DATA.get());
		if (data != null) {
			return data.color().getTextureDiffuseColor();
		}
		return -1;
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Component component = stack.get(DataComponents.CUSTOM_NAME);
		if (stack.is(Items.NAME_TAG) && component != null) {
			if (!level().isClientSide) {
				ItemStack balloon = isMainHand() ? owner.getMainHandItem() : owner.getOffhandItem();
				BalloonData data = balloon.get(CDataComponents.BALLOON_DATA.get());
				if (data == null) {
					data = BalloonData.DEFAULT;
				}
				balloon.set(CDataComponents.BALLOON_DATA.get(), data.withName(component));
				setItem(balloon);
			}
			stack.consume(1, player);
			return InteractionResult.sidedSuccess(player.level().isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onClientRemoval() {
		super.onClientRemoval();
		if (owner instanceof BalloonOwner balloonOwner) {
			if (isMainHand()) {
				balloonOwner.setMainHandBalloon(null);
			} else {
				balloonOwner.setOffhandBalloon(null);
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		if (compoundTag.hasUUID("owner")) {
			ownerId = compoundTag.getUUID("owner");
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		if (owner != null) {
			compoundTag.putUUID("owner", owner.getUUID());
		}
	}
}
