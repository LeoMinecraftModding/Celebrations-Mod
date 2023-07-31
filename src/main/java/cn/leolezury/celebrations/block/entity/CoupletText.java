package cn.leolezury.celebrations.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignText;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CoupletText extends SignText {
    private static final Codec<Component[]> LINES_CODEC = ExtraCodecs.FLAT_COMPONENT.listOf().comapFlatMap((componentList) -> {
        return Util.fixedSize(componentList, 7).map((components) -> {
            return new Component[]{components.get(0), components.get(1), components.get(2), components.get(3), components.get(4), components.get(5), components.get(6)};
        });
    }, (components) -> {
        return List.of(components[0], components[1], components[2], components[3], components[4], components[5], components[6]);
    });
    public static final Codec<CoupletText> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(LINES_CODEC.fieldOf("messages").forGetter((coupletText) -> {
            return coupletText.messages;
        }), LINES_CODEC.optionalFieldOf("filtered_messages").forGetter((coupletText -> {
            return coupletText.getOnlyFilteredMessages();
        })), DyeColor.CODEC.fieldOf("color").orElse(DyeColor.BLACK).forGetter((coupletText) -> {
            return coupletText.color;
        }), Codec.BOOL.fieldOf("has_glowing_text").orElse(false).forGetter((coupletText) -> {
            return coupletText.hasGlowingText;
        })).apply(instance, CoupletText::load);
    });

    public CoupletText() {
        this(emptyMessages(), emptyMessages(), DyeColor.BLACK, false);
    }

    public CoupletText(Component[] components, Component[] components1, DyeColor dyeColor, boolean b) {
        this.messages = components;
        this.filteredMessages = components1;
        this.color = dyeColor;
        this.hasGlowingText = b;
    }

    private static CoupletText load(Component[] components, Optional<Component[]> optionalComponents, DyeColor dyeColor, boolean b) {
        Component[] acomponent = optionalComponents.orElseGet(CoupletText::emptyMessages);
        populateFilteredMessagesWithRawMessages(components, acomponent);
        return new CoupletText(components, acomponent, dyeColor, b);
    }

    public CoupletText setHasGlowingText(boolean b) {
        return b == this.hasGlowingText ? this : new CoupletText(this.messages, this.filteredMessages, this.color, b);
    }

    public CoupletText setColor(DyeColor dyeColor) {
        return dyeColor == this.getColor() ? this : new CoupletText(this.messages, this.filteredMessages, dyeColor, this.hasGlowingText);
    }

    public CoupletText setMessage(int i, Component component) {
        return this.setMessage(i, component, component);
    }

    public CoupletText setMessage(int i, Component component, Component component1) {
        Component[] acomponent = Arrays.copyOf(this.messages, this.messages.length);
        Component[] acomponent1 = Arrays.copyOf(this.filteredMessages, this.filteredMessages.length);
        acomponent[i] = component;
        acomponent1[i] = component1;
        return new CoupletText(acomponent, acomponent1, this.color, this.hasGlowingText);
    }

    private static Component[] emptyMessages() {
        return new Component[]{CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY, CommonComponents.EMPTY};
    }

    private static void populateFilteredMessagesWithRawMessages(Component[] components, Component[] components1) {
        for (int i = 0; i < 7; ++i) {
            if (components1[i].equals(CommonComponents.EMPTY)) {
                components1[i] = components[i];
            }
        }
    }

    public FormattedCharSequence[] getRenderMessages(boolean b, Function<Component, FormattedCharSequence> sequenceFunction) {
        if (this.renderMessages == null || this.renderMessagedFiltered != b) {
            this.renderMessagedFiltered = b;
            this.renderMessages = new FormattedCharSequence[7];

            for(int i = 0; i < 7; ++i) {
                this.renderMessages[i] = sequenceFunction.apply(this.getMessage(i, b));
            }
        }

        return this.renderMessages;
    }

    private Optional<Component[]> getOnlyFilteredMessages() {
        Component[] acomponent = new Component[7];
        boolean flag = false;

        for(int i = 0; i < 7; ++i) {
            Component component = this.filteredMessages[i];
            if (!component.equals(this.messages[i])) {
                acomponent[i] = component;
                flag = true;
            } else {
                acomponent[i] = CommonComponents.EMPTY;
            }
        }

        return flag ? Optional.of(acomponent) : Optional.empty();
    }
}