package cn.leolezury.celebrations.mixin;

import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.HorizontalScrollBlockEntity;
import cn.leolezury.celebrations.client.gui.screens.CoupletEditScreen;
import cn.leolezury.celebrations.client.gui.screens.HorizontalScrollEditScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@OnlyIn(Dist.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Shadow @Final protected Minecraft minecraft;

    @Inject(method = ("openTextEdit"), at = @At("HEAD"), cancellable = true)
    protected void openTextEdit(SignBlockEntity entity, boolean frontText, CallbackInfo info) {
        if (entity instanceof CoupletBlockEntity blockEntity) {
            this.minecraft.setScreen(new CoupletEditScreen(blockEntity, minecraft.isTextFilteringEnabled()));
            info.cancel();
        } else if (entity instanceof HorizontalScrollBlockEntity blockEntity) {
            this.minecraft.setScreen(new HorizontalScrollEditScreen(blockEntity, true, minecraft.isTextFilteringEnabled()));
            info.cancel();
        }
    }
}
