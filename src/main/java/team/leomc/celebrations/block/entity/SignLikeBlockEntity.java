package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.util.SignLikeText;

import java.util.ArrayList;
import java.util.function.Function;

public class SignLikeBlockEntity extends BlockEntity {
	protected SignLikeText text = new SignLikeText(new ArrayList<>(), DyeColor.BLACK, false);

	public SignLikeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public SignLikeText getText() {
		return text;
	}

	public void setText(SignLikeText text) {
		this.text = text;
	}

	public boolean updateCoupletText(Function<SignLikeText, SignLikeText> function) {
		SignLikeText oldText = text.copy();
		this.text = function.apply(text);
		return !oldText.equals(this.text);
	}

	public int getTextLineHeight() {
		return 10;
	}

	public int getMaxTextLineWidth() {
		return 90;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("Text", text.toTag(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.text = SignLikeText.fromTag(tag.get("Text"), registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		tag.put("Text", text.toTag(registries));
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		this.text = SignLikeText.fromTag(tag.get("Text"), lookupProvider);
	}
}
