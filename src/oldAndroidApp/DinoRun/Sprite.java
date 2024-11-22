package oldAndroidApp.DinoRun;

import java.util.List;

import android.graphics.Bitmap;

public class Sprite {
	SpriteEvent spriteEvent;
	List<Bitmap> bitmaps;
	int bitmapIndex;
	int tox;
	int toy;
	int speed;
	float x;
	float y;
	float w;
	float h;
	int time = 0;
	int zindex = 0;
	boolean live;

	public Sprite(SpriteEvent spriteEvent) {
		this.spriteEvent = spriteEvent;
		this.live = true;
		spriteEvent.onInit(this);
	}

	void update() {
		if (live) {
			time += 1;
			spriteEvent.onUpdate(this);
		}
	}

	void die() {
		live = false;
		spriteEvent.onDie();
	}
}
