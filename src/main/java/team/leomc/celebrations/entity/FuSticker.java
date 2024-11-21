package team.leomc.celebrations.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.registry.CEntities;

public class FuSticker extends HangingEntity {
	private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(FuSticker.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<BlockPos> ATTACHED_POS = SynchedEntityData.defineId(FuSticker.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<BlockPos> ORIGINAL_POS = SynchedEntityData.defineId(FuSticker.class, EntityDataSerializers.BLOCK_POS);
	private ItemStack fuSticker;
	private BlockPos attachedPos;
	private BlockPos originalPos;

	public FuSticker(EntityType<? extends FuSticker> entityType, Level level) {
		super(entityType, level);
	}

	private FuSticker(Level level, BlockPos pos, Direction direction) {
		super(CEntities.FU_STICKER.get(), level, pos);
		setDirection(direction);
	}

	public static FuSticker create(Level level, BlockPos pos, BlockPos attachedPos,Direction direction, ItemStack itemStack) {
		FuSticker fuStickerEntity = new FuSticker(level, pos, direction);
		fuStickerEntity.setItem(itemStack);
		fuStickerEntity.setAttachedPos(attachedPos);
		fuStickerEntity.setOriginalPos(pos);
		return fuStickerEntity;
	}

	@Override
	protected AABB calculateBoundingBox(BlockPos blockPos, Direction direction) {
		Direction.Axis direction$axis = direction.getAxis();
		BlockState attachedBlockState = getAttachedBlockState();

		if (attachedBlockState.getBlock() instanceof DoorBlock) {
			if (attachedBlockState.getValue(DoorBlock.OPEN)) {
				this.pos = getAttachedPos();
				Direction.Axis counterClockWiseAxis = direction.getCounterClockWise().getAxis();
				double d0 = counterClockWiseAxis == Direction.Axis.X ? 0.0625 : 0.5;
				double d1 = counterClockWiseAxis == Direction.Axis.Y ? 0.0625 : 0.5;
				double d2 = counterClockWiseAxis == Direction.Axis.Z ? 0.0625 : 0.5;
				if (attachedBlockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT) {
					Vec3 leftVec3 = Vec3.atCenterOf(getAttachedPos()).relative(direction.getClockWise(), 0.28125);
					return AABB.ofSize(leftVec3, d0, d1, d2);
				} else {
					Vec3 rightVec3 = Vec3.atCenterOf(getAttachedPos()).relative(direction.getCounterClockWise(), 0.28125);
					return AABB.ofSize(rightVec3, d0, d1, d2);
				}
			}
		}

		double d0 = direction$axis == Direction.Axis.X ? 0.0625 : 0.5;
		double d1 = direction$axis == Direction.Axis.Y ? 0.0625 : 0.5;
		double d2 = direction$axis == Direction.Axis.Z ? 0.0625 : 0.5;

		if (isAttachedPane()) {
			Vec3 paneVec3 = Vec3.atCenterOf(getAttachedPos()).relative(direction, 0.09375);
			return AABB.ofSize(paneVec3, d0, d1, d2);
		}

		this.pos = getOriginalPos();
		Vec3 blockVec3 = Vec3.atCenterOf(getOriginalPos()).relative(direction, -0.46875);
		return AABB.ofSize(blockVec3, d0, d1, d2);
	}

	public BlockState getAttachedBlockState() {
		return this.level().getBlockState(getAttachedPos());
	}

	public boolean isAttachedPane() {
		return this.level().getBlockState(getAttachedPos()).getBlock() instanceof IronBarsBlock;
	}

	@Override
	public boolean survives() {
		if (!(this.level().getBlockState(getAttachedPos()).getBlock() instanceof AirBlock)) {
			return true;
		}
		return super.survives();
	}

	@Override
	public void playPlacementSound() {
		this.playSound(SoundEvents.AZALEA_LEAVES_PLACE, 1.0F, 1.0F);
	}

	@Override
	public void dropItem(@Nullable Entity entity) {
		if (fuSticker == null) {
			return;
		}
		if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			this.playSound(SoundEvents.AZALEA_LEAVES_BREAK, 1.0F, 1.0F);
			if (entity instanceof Player player) {
				if (player.hasInfiniteMaterials()) {
					return;
				}
			}

			this.spawnAtLocation(this.fuSticker);
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(ITEM_STACK, ItemStack.EMPTY);
		builder.define(ATTACHED_POS, BlockPos.ZERO);
		builder.define(ORIGINAL_POS, BlockPos.ZERO);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putByte("facing", (byte)this.direction.get2DDataValue());
		compound.put("Item", this.fuSticker.save(this.registryAccess()));
		compound.putInt("attachedPosX", this.attachedPos.getX());
		compound.putInt("attachedPosY", this.attachedPos.getY());
		compound.putInt("attachedPosZ", this.attachedPos.getZ());
		compound.putInt("originalPosX", this.originalPos.getX());
		compound.putInt("originalPosY", this.originalPos.getY());
		compound.putInt("originalPosZ", this.originalPos.getZ());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.direction = Direction.from2DDataValue(compound.getByte("facing"));
		CompoundTag compoundtag = compound.getCompound("Item");
		setItem(ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY));
		BlockPos blockpos = new BlockPos(compound.getInt("attachedPosX"), compound.getInt("attachedPosY"), compound.getInt("attachedPosZ"));
		setAttachedPos(blockpos);
		BlockPos blockpos1 = new BlockPos(compound.getInt("originalPosX"), compound.getInt("originalPosY"), compound.getInt("originalPosZ"));
		setOriginalPos(blockpos1);
		super.readAdditionalSaveData(compound);
		this.setDirection(this.direction);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.setDirection(Direction.from3DDataValue(packet.getData()));
	}

	public void setItem(ItemStack stack) {
		this.fuSticker = stack.copyWithCount(1);
		this.getEntityData().set(ITEM_STACK, stack.copyWithCount(1));
	}

	public ItemStack getItem() {
		return this.getEntityData().get(ITEM_STACK);
	}

	public void setAttachedPos(BlockPos attachedPos) {
		this.attachedPos = attachedPos;
		this.getEntityData().set(ATTACHED_POS, attachedPos);
	}

	public BlockPos getAttachedPos() {
		return this.getEntityData().get(ATTACHED_POS);
	}

	public void setOriginalPos(BlockPos originalPos) {
		this.originalPos = originalPos;
		this.getEntityData().set(ORIGINAL_POS, originalPos);
	}

	public BlockPos getOriginalPos() {
		return this.getEntityData().get(ORIGINAL_POS);
	}

	@Override
	public void tick() {
		super.tick();
		recalculateBoundingBox();
	}
}
