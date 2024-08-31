package team.leomc.celebrations.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import team.leomc.celebrations.entity.Balloon;
import team.leomc.celebrations.entity.BalloonOwner;

@Mixin(Player.class)
public abstract class PlayerMixin implements BalloonOwner {
	@Unique
	private Balloon celebrations$mainHandBalloon;
	@Unique
	private Balloon celebrations$offhandBalloon;

	@Override
	public Balloon getMainHandBalloon() {
		return celebrations$mainHandBalloon;
	}

	@Override
	public Balloon getOffhandBalloon() {
		return celebrations$offhandBalloon;
	}

	@Override
	public void setMainHandBalloon(Balloon balloon) {
		celebrations$mainHandBalloon = balloon;
	}

	@Override
	public void setOffhandBalloon(Balloon balloon) {
		celebrations$offhandBalloon = balloon;
	}
}
