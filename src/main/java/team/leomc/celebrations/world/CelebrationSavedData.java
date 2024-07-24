package team.leomc.celebrations.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import team.leomc.celebrations.config.CCommonConfig;

public class CelebrationSavedData extends SavedData {
	private boolean celebrating = false;
	private int ticksSinceLastCelebration;
	private int celebrationTicks;
	private int celebrationCoolDown = 12000 * 2 * 30;
	private int celebrationTime = 12000 * 2 * 2;

	public static CelebrationSavedData load(CompoundTag tag) {
		CelebrationSavedData data = new CelebrationSavedData();
		data.celebrating = tag.getBoolean("Celebrating");
		data.ticksSinceLastCelebration = tag.getInt("TicksSinceLastCelebration");
		data.celebrationTicks = tag.getInt("CelebrationTicks");
		data.celebrationCoolDown = CCommonConfig.CELEBRATION_INTERVAL.get();
		data.celebrationTime = CCommonConfig.CELEBRATION_DURATION.get();
		return data;
	}

	public static SavedData.Factory<CelebrationSavedData> factory() {
		return new SavedData.Factory<>(CelebrationSavedData::new, (compoundTag, lookup) -> load(compoundTag), null);
	}

	public void tick() {
		if (celebrating) {
			celebrationTicks++;
			ticksSinceLastCelebration = 0;
		} else {
			ticksSinceLastCelebration++;
			celebrationTicks = 0;
		}

		if (ticksSinceLastCelebration > celebrationCoolDown) {
			celebrating = true;
			ticksSinceLastCelebration = 0;
		}
		if (celebrationTicks > celebrationTime) {
			celebrating = false;
			celebrationTicks = 0;
		}
		if ((!celebrating && ticksSinceLastCelebration % 200 == 0) || (celebrating && celebrationTicks % 200 == 0)) {
			setDirty();
		}
	}

	public void setCelebrating(boolean celebrating) {
		this.celebrating = celebrating;
		setDirty();
	}

	public boolean isCelebrating() {
		return celebrating;
	}

	public void setTicksSinceLastCelebration(int ticksSinceLastCelebration) {
		this.ticksSinceLastCelebration = ticksSinceLastCelebration;
		setDirty();
	}

	public int getTicksSinceLastCelebration() {
		return ticksSinceLastCelebration;
	}

	public void setCelebrationTicks(int celebrationTicks) {
		this.celebrationTicks = celebrationTicks;
		setDirty();
	}

	public int getCelebrationTicks() {
		return celebrationTicks;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag.putBoolean("Celebrating", celebrating);
		tag.putInt("TicksSinceLastCelebration", ticksSinceLastCelebration);
		tag.putInt("CelebrationTicks", celebrationTicks);
		return tag;
	}
}
