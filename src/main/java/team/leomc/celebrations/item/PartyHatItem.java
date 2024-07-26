package team.leomc.celebrations.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.registry.CDataComponents;

import java.util.List;

public class PartyHatItem extends Item implements Equipable {
	public PartyHatItem(Properties properties) {
		super(properties);
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		PartyHat hat = stack.get(CDataComponents.PART_HAT.get());
		if (hat != null) {
			tooltipComponents.add(Component.translatable("color.minecraft." + hat.color().getName())
				.append(Component.translatable("party_hat_type." + Celebrations.ID + "." + hat.type().getSerializedName()))
				.withStyle(style -> style.withColor(hat.color().getTextColor())));
		}
	}
}
