package oldAndroidApp.DinoRun;

import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyCanvas extends View {
	List<Sprite> sprites;
	Paint paint;

	public MyCanvas(Context context) {
		super(context);
		init();
	}

	public MyCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		paint = new Paint();
		// trump = BitmapFactory.decodeResource(getResources(),
		// R.drawable.trump);
	}

	void setSprites(List<Sprite> sprites) {
		this.sprites = sprites;
	}

	void draw() {
		invalidate();
	}

	Bitmap scaleBitmap(Bitmap bitmap, int w, int h) {
		// return Bitmap.createScaledBitmap(img, getWidth(), getHeight(), true);
		return Bitmap.createScaledBitmap(bitmap, w, h, true);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (sprites != null) {
			Collections.sort(sprites, new SpriteComparator());
			for (int i = 0; i < sprites.size(); i++) {
				Sprite sprite = sprites.get(i);
				if(sprite.bitmaps !=null){
					canvas.drawBitmap(
							scaleBitmap(sprite.bitmaps.get(sprite.bitmapIndex), (int) sprite.w,
									(int) sprite.h), sprite.x, sprite.y, paint);
				}
			}
		}
	}
}
