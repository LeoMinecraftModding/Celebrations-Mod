package team.leomc.celebrations.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.item.component.PartyHatType;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;

import java.util.Arrays;

public class PartyHatUtils {
	private static final Int2ObjectArrayMap<ItemStack> HAT_CACHE = new Int2ObjectArrayMap<>();

	public static ItemStack getMobPartyHatItem(Mob mob) {
		if (HAT_CACHE.size() > 65536) {
			HAT_CACHE.clear();
		}
		if (HAT_CACHE.containsKey(mob.getId())) {
			return HAT_CACHE.get(mob.getId()).copy();
		}
		ItemStack stack = CItems.PARTY_HAT.get().getDefaultInstance();
		PartyHat hat = new PartyHat(Arrays.stream(PartyHatType.values()).toList().get(mob.getRandom().nextInt(PartyHatType.values().length)), Arrays.stream(DyeColor.values()).toList().get(mob.getRandom().nextInt(DyeColor.values().length)));
		stack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.PART_HAT.get(), hat).build());
		HAT_CACHE.put(mob.getId(), stack);
		return stack;
	}
}
