package cn.leolezury.celebrations.init;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.HorizontalScrollBlockEntity;
import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Celebrations.MOD_ID);

    public static final RegistryObject<BlockEntityType<LanternBlockEntity>> LANTERN = BLOCK_ENTITIES.register("lantern", () -> BlockEntityType.Builder.of(LanternBlockEntity::new, BlockInit.CHINESE_STYLED_BAMBOO_LANTERN.get(), BlockInit.CHINESE_STYLED_PAPER_LANTERN.get(), BlockInit.CHINESE_STYLED_RED_LANTERN.get(), BlockInit.JAPANESE_STYLED_PAPER_LANTERN.get(), BlockInit.JAPANESE_STYLED_RED_LANTERN.get()).build(null));
    public static final RegistryObject<BlockEntityType<CoupletBlockEntity>> COUPLET = BLOCK_ENTITIES.register("couplet", () -> BlockEntityType.Builder.of(CoupletBlockEntity::new, BlockInit.COUPLET.get()).build(null));
    public static final RegistryObject<BlockEntityType<HorizontalScrollBlockEntity>> HORIZONTAL_SCROLL = BLOCK_ENTITIES.register("horizontal_scroll", () -> BlockEntityType.Builder.of(HorizontalScrollBlockEntity::new, BlockInit.HORIZONTAL_SCROLL.get()).build(null));
}
