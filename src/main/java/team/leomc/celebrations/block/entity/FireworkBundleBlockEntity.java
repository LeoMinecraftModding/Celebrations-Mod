package team.leomc.celebrations.block.entity;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import team.leomc.celebrations.block.FireworkBundleBlock;
import team.leomc.celebrations.registry.CBlockEntities;

import java.util.Arrays;
import java.util.List;

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
			RandomSource random = level.getRandom();
			if (!level.isClientSide) {
				int numFireworks = Math.min(4, amount);
				List<FireworkExplosion.Shape> shapes = Arrays.stream(FireworkExplosion.Shape.values()).toList();
				List<DyeColor> dyeColors = Arrays.stream(DyeColor.values()).toList();
				for (int i = 0; i <= numFireworks; i++) {
					ItemStack stack = Items.FIREWORK_ROCKET.getDefaultInstance();
					FireworkExplosion explosion = new FireworkExplosion(shapes.get(random.nextInt(shapes.size())), IntList.of(dyeColors.get(random.nextInt(dyeColors.size())).getFireworkColor()), IntList.of(dyeColors.get(random.nextInt(dyeColors.size())).getFireworkColor()), random.nextBoolean(), random.nextBoolean());
					stack.set(DataComponents.FIREWORKS, new Fireworks(random.nextInt(3), List.of(explosion)));
					stack.set(DataComponents.FIREWORK_EXPLOSION, explosion);
					Vec3 vec3 = pos.getCenter().offsetRandom(random, 1);
					FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(level, vec3.x, pos.getCenter().y + 0.6, vec3.z, stack);
					level.addFreshEntity(fireworkRocket);
				}

				level.setBlockAndUpdate(pos, state.setValue(FireworkBundleBlock.FIREWORK_AMOUNT, amount - numFireworks).setValue(FireworkBundleBlock.LIT, amount - numFireworks > 0));
			} else {
				Vec3 vec3 = pos.getCenter().offsetRandom(random, 1);
				level.addParticle(ParticleTypes.FLASH, vec3.x, pos.getCenter().y + 0.6, vec3.z, 0, 0, 0);
			}
		}
	}
}
