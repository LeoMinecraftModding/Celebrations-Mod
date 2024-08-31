package team.leomc.celebrations.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.leomc.celebrations.entity.PartyHatWearer;
import team.leomc.celebrations.event.CEvents;
import team.leomc.celebrations.util.PartyHatUtils;

@Mixin(Mob.class)
public abstract class MobMixin implements PartyHatWearer {
	@Unique
	private boolean celebrations$wearingPartyHat = false;

	@Inject(method = "getItemBySlot", at = @At("RETURN"), cancellable = true)
	protected void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
		Mob mob = ((Mob) (Object) this);
		ItemStack original = cir.getReturnValue();
		if ((original.isEmpty() || original.getItem() instanceof ArmorItem) && celebrations$wearingPartyHat && isWearingPartyHat() && mob.level().isClientSide) {
			cir.setReturnValue(PartyHatUtils.getMobPartyHatItem(mob));
		}
	}

	@Override
	public void beforeClientGetItem() {
		celebrations$wearingPartyHat = true;
	}

	@Override
	public void afterClientGetItem() {
		celebrations$wearingPartyHat = false;
	}

	@Override
	public boolean isWearingPartyHat() {
		return ((Mob) (Object) this).getPersistentData().getBoolean(CEvents.TAG_PARTY_HAT);
	}
}
