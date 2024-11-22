package oldAndroidApp.DinoRun;

public class Unit {
		static float calculateDistance(Sprite rect1, Sprite rect2) {
		float centerX1 = rect1.x + rect1.w / 2;
		float centerY1 = rect1.y + rect1.h / 2;
		float centerX2 = rect2.x + rect2.w / 2;
		float centerY2 = rect2.y + rect2.h / 2;
		float distanceX = centerX2 - centerX1;
		float distanceY = centerY2 - centerY1;
		return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
	}
}
