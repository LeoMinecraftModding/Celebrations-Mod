package team.leomc.celebrations.entity;

public interface BalloonOwner {
	Balloon getMainHandBalloon();

	Balloon getOffhandBalloon();

	void setMainHandBalloon(Balloon balloon);

	void setOffhandBalloon(Balloon balloon);
}
