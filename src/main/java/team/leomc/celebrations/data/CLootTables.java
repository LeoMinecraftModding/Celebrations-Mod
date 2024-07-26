package team.leomc.celebrations.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import team.leomc.celebrations.Celebrations;

public class CLootTables {
	public static final ResourceKey<LootTable> VILLAGER_GIFT = create("gameplay/villager_gift");

	public static ResourceKey<LootTable> create(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, Celebrations.id(name));
	}
}
