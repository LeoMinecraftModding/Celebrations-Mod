package cn.leolezury.celebrations.block.entity;

import cn.leolezury.celebrations.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.UnaryOperator;

public class CoupletBlockEntity extends SignBlockEntity {
    private CoupletText frontText = this.createDefaultCoupletText();

    public CoupletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COUPLET.get(), pos, state);
    }

    protected CoupletText createDefaultCoupletText() {
        return new CoupletText();
    }

    public CoupletText getTextFacingPlayer(Player player) {
        return this.getText(this.isFacingFrontText(player));
    }

    public CoupletText getText(boolean b) {
        return b ? this.frontText : new CoupletText();
    }

    public CoupletText getFrontText() {
        return this.frontText;
    }

    public void updateSignText(Player player, boolean b, List<FilteredText> filteredTexts) {
        if (!this.isWaxed() && player.getUUID().equals(this.getPlayerWhoMayEdit()) && this.level != null) {
            this.updateCoupletText((text) -> {
                return this.setMessages(player, filteredTexts, text);
            }, b);
            this.setAllowedPlayerEditor(null);
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        } else {
            LOGGER.warn("Player {} just tried to change non-editable couplet", player.getName().getString());
        }
    }

    public boolean updateCoupletText(UnaryOperator<CoupletText> unaryOperator, boolean b) {
        CoupletText coupletText = this.getText(b);
        return this.setText(unaryOperator.apply(coupletText), b);
    }

    private CoupletText setMessages(Player player, List<FilteredText> filteredTexts, CoupletText coupletText) {
        for(int i = 0; i < filteredTexts.size(); ++i) {
            FilteredText filteredtext = filteredTexts.get(i);
            Style style = coupletText.getMessage(i, player.isTextFilteringEnabled()).getStyle();
            if (player.isTextFilteringEnabled()) {
                coupletText = coupletText.setMessage(i, Component.literal(filteredtext.filteredOrEmpty()).setStyle(style));
            } else {
                coupletText = coupletText.setMessage(i, Component.literal(filteredtext.raw()).setStyle(style), Component.literal(filteredtext.filteredOrEmpty()).setStyle(style));
            }
        }

        return coupletText;
    }

    public boolean setText(CoupletText coupletText, boolean b) {
        return b ? this.setFrontText(coupletText) : true;
    }

    private boolean setFrontText(CoupletText coupletText) {
        if (coupletText != this.frontText) {
            this.frontText = coupletText;
            this.markUpdated();
            return true;
        } else {
            return false;
        }
    }

    private void markUpdated() {
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        // Forge things
        if (getPersistentData() != null) compoundTag.put("ForgeData", getPersistentData().copy());
        if (getCapabilities() != null) compoundTag.put("ForgeCaps", serializeCaps());

        CoupletText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, frontText).resultOrPartial(LOGGER::error).ifPresent((tag) -> {
            compoundTag.put("front_text", tag);
        });
    }

    @Override
    public void load(CompoundTag compoundTag) {
        // Forge things
        if (compoundTag.contains("ForgeData")) getPersistentData().merge(compoundTag.getCompound("ForgeData"));
        if (getCapabilities() != null && compoundTag.contains("ForgeCaps")) deserializeCaps(compoundTag.getCompound("ForgeCaps"));

        if (compoundTag.contains("front_text")) {
            CoupletText.DIRECT_CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound("front_text")).resultOrPartial(LOGGER::error).ifPresent((text) -> {
                this.frontText = this.loadLines(text);
            });
        }
    }

    private CoupletText loadLines(CoupletText text) {
        for(int i = 0; i < 7; ++i) {
            Component component = this.loadLine(text.getMessage(i, false));
            Component component1 = this.loadLine(text.getMessage(i, true));
            text = text.setMessage(i, component, component1);
        }
        return text;
    }
}
