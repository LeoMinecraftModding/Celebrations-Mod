package team.leomc.celebrations.entity;

public interface PartyHatWearer {
	void beforeClientGetItem();

	void afterClientGetItem();

	boolean isWearingPartyHat();
}
