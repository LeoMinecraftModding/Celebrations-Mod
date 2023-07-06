package cn.leolezury.celebrations.init;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.CBLanternBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Celebrations.MOD_ID);

    public static final RegistryObject<Block> CHINESE_STYLED_RED_LANTERN = BLOCKS.register("chinese_styled_red_lantern", () -> new CBLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).sound(SoundType.WOOL).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)){
        @Override
        public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
            VoxelShape shape = Shapes.empty();
            shape = Shapes.or(shape, Shapes.create(0.3125, 0.125, 0.3125, 0.6875, 0.25, 0.6875));
            shape = Shapes.or(shape, Shapes.create(0.125, 0.25, 0.125, 0.875, 0.8125, 0.875));
            shape = Shapes.or(shape, Shapes.create(0.25, 0.8125, 0.25, 0.75, 0.9375, 0.75));
            shape = Shapes.or(shape, Shapes.create(0.40625, 0.9375, 0.5, 0.59375, 1.0625, 0.5));
            shape = Shapes.or(shape, Shapes.create(0.40625, 0.9375, 0.5, 0.59375, 1.0625, 0.5));
            shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));
            shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));
            
            return shape;
        }
    });
}
