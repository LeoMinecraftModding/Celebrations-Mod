package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.util.SignLikeText;

import java.util.function.Function;

public class SignLikeBlockEntity extends BlockEntity {
	protected SignLikeText text = SignLikeText.DEFAULT;

	public SignLikeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public SignLikeText getText() {
		return text;
	}

	public void setText(SignLikeText text) {
		this.text = text;
		if (this.level != null) {
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public boolean updateSignLikeText(Function<SignLikeText, SignLikeText> function) {
		SignLikeText oldText = text.copy();
		setText(function.apply(text));
		return !oldText.equals(this.text);
	}

	public int getTextLineHeight() {
		return 10;
	}

	public int getMaxTextLineWidth() {
		return 90;
	}

	public int getMaxTextLineNumber() {
		return 7;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("text", text.toTag(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		setText(SignLikeText.fromTag(tag.get("text"), registries));
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		tag.put("text", text.toTag(registries));
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		this.text = SignLikeText.fromTag(tag.get("text"), lookupProvider);
	}
}
