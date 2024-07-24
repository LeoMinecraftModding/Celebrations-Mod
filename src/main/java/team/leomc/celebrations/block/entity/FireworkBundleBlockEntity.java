package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.leomc.celebrations.block.FireworkBundleBlock;
import team.leomc.celebrations.registry.CBlockEntities;

public class FireworkBundleBlockEntity extends BlockEntity {

	public FireworkBundleBlockEntity(BlockPos pos, BlockState state) {
		super(CBlockEntities.FIREWORK_BUNDLE.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FireworkBundleBlockEntity entity) {
		if (state.getValue(FireworkBundleBlock.LIT) && level.getGameTime() % 10 == 0) {
			int amount = state.getValue(FireworkBundleBlock.FIREWORK_AMOUNT);
			if (amount <= 0) {
				return;
			}
			RandomSource rand = RandomSource.create();
			if (!level.isClientSide) {
				int numFireworks = Math.min(4, amount);

				for (int i = 0; i <= numFireworks; i++) {
					ItemStack stack = Items.FIREWORK_ROCKET.getDefaultInstance();
					CompoundTag fireworkTag = new CompoundTag();

					ListTag listTag = new ListTag();

                    /*for (int j = 0; j < 3; j++) {
                        List<Integer> colorList = Lists.newArrayList();
                        for (int k = 0; k < 5; k++) {
                            colorList.add(DyeColor.values()[rand.nextInt(DyeColor.values().length)].getFireworkColor());
                        }
                        List<Integer> fadeToColorList = Lists.newArrayList();
                        for (int k = 0; k < 5; k++) {
                            fadeToColorList.add(DyeColor.values()[rand.nextInt(DyeColor.values().length)].getFireworkColor());
                        }

                        FireworkRocketItem.Shape shape = FireworkRocketItem.Shape.values()[rand.nextInt(FireworkRocketItem.Shape.values().length)];
                        CompoundTag tag = new CompoundTag();
                        tag.putBoolean("Trail", true);
                        tag.putBoolean("Flicker", true);
                        tag.putIntArray("Colors", colorList);
                        tag.putIntArray("FadeColors", fadeToColorList);
                        tag.putByte("Type", (byte) shape.getId());
                        listTag.add(tag);
                    }

                    fireworkTag.put("Explosions", listTag);
                    fireworkTag.putByte("Flight", (byte) 3);
                    stack.getOrCreateTag().put("Fireworks", fireworkTag);
                    Vec3 vec3 = pos.getCenter().offsetRandom(rand, 1);
                    FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(level, vec3.x, pos.getCenter().y + 0.6, vec3.z, stack);
                    level.addFreshEntity(fireworkRocket);*/
				}

				level.setBlockAndUpdate(pos, state.setValue(FireworkBundleBlock.FIREWORK_AMOUNT, amount - numFireworks).setValue(FireworkBundleBlock.LIT, amount - numFireworks > 0));
			} else {
				Vec3 vec3 = pos.getCenter().offsetRandom(rand, 1);
				level.addParticle(ParticleTypes.FLASH, vec3.x, pos.getCenter().y + 0.6, vec3.z, 0, 0, 0);
			}
		}
	}
}
