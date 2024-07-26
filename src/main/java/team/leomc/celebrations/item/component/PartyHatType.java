package team.leomc.celebrations.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum PartyHatType implements StringRepresentable {
	STRIPES("stripes"),
	TILT_STRIPES("tilt_stripes"),
	DOTS("dots");

	public static final Codec<PartyHatType> CODEC = StringRepresentable.fromEnum(PartyHatType::values);
	private final String name;

	PartyHatType(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
