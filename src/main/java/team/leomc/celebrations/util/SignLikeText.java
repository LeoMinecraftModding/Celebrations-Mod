package team.leomc.celebrations.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record SignLikeText(List<Component> messages, DyeColor color, boolean glow) {
	public static final Codec<SignLikeText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ComponentSerialization.CODEC.listOf().fieldOf("messages").forGetter(SignLikeText::messages),
		DyeColor.CODEC.fieldOf("color").forGetter(SignLikeText::color),
		Codec.BOOL.fieldOf("glow").forGetter(SignLikeText::glow)
	).apply(instance, SignLikeText::new));

	public static final SignLikeText DEFAULT = new SignLikeText(new ArrayList<>(), DyeColor.BLACK, false);

	public static SignLikeText fromTag(Tag tag, HolderLookup.Provider provider) {
		return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(DEFAULT);
	}

	public Tag toTag(HolderLookup.Provider provider) {
		return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this).resultOrPartial().orElseGet(CompoundTag::new);
	}

	public SignLikeText copy() {
		return new SignLikeText(new ArrayList<>(messages()), color(), glow());
	}

	public SignLikeText setMessage(int index, Component component) {
		List<Component> newMessages = new ArrayList<>(messages());
		if (newMessages.size() <= index) {
			for (int i = 0; i < index - messages().size() + 1; i++) {
				newMessages.add(Component.empty());
			}
		}
		newMessages.set(index, component);
		return new SignLikeText(newMessages, color(), glow());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SignLikeText that = (SignLikeText) o;
		return glow == that.glow && color == that.color && Objects.equals(messages, that.messages);
	}

	@Override
	public int hashCode() {
		return Objects.hash(messages, color, glow);
	}

	public FormattedCharSequence[] getRenderMessages(Function<Component, FormattedCharSequence> function) {
		return messages().stream().map(function).toArray(FormattedCharSequence[]::new);
	}
}