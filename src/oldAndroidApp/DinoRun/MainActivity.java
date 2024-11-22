package oldAndroidApp.DinoRun;

import oldAndroidApp.DinoRun.util.SystemUiHider;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	Context context;
	MediaPlayer mediaPlayer;
	MediaPlayer bgMediaPlayer;

	MyCanvas myCanvas;
	Handler handler;
	Runnable runnable;

	ImageView start_button;
	Button jump_button;
	ImageView restart_button;

	TextView scoreLabel;
	int score = 0;

	Boolean loop = false;

	List<Sprite> sprites = new ArrayList<>();

	int time = 0;

	int screenWidth = 0;
	int screenHeight = 0;

	Sprite player;

	void setScore(int s) {
		score = s;
		scoreLabel.setText("SCORE:" + score);
	}

	void makeEnemy() {
		long seed = System.currentTimeMillis();
		final Random randomWithSeed = new Random(seed);
		final int randomIntInRange = randomWithSeed.nextInt(10);

		Sprite enemy = new Sprite(new SpriteEvent() {

			@Override
			public void onInit(Sprite sprite) {
				// TODO Auto-generated method stub
				sprite.zindex = 1;
				if (randomIntInRange > 5) {
					sprite.w = 20f;
					sprite.h = 30f;
				} else {
					sprite.w = 30f;
					sprite.h = 40f;
				}
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.tree));
				sprite.x = screenWidth + 0f + randomWithSeed.nextInt(10) * 10;
				sprite.y = screenHeight / 4 * 3 - sprite.h;
				sprite.bitmaps = bitmaps;
				sprites.add(sprite);
			}

			@Override
			public void onUpdate(Sprite sprite) {
				// TODO Auto-generated method stub

				sprite.x -= 2f;
				if (sprite.x < -1f * sprite.w) {
					sprite.die();
				}
				if (Unit.calculateDistance(sprite, player) < player.w / 2
						|| Unit.calculateDistance(sprite, player) < player.h / 2) {
					player.die();
				}
			}

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				setScore(score + 1);
			}

		});
		sprites.add(enemy);
	}

	void initPlayer() {
		setScore(0);
		player = new Sprite(new SpriteEvent() {

			@Override
			public void onInit(Sprite sprite) {
				sprite.zindex = 100;
				sprite.w = 60f;
				sprite.h = 60f;
				sprite.x = 10f;
				sprite.y = screenHeight / 4 * 3 - sprite.h;
				sprite.live = true;
				sprite.speed = 20;
				sprite.toy = 0;
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.dino1));
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.dino2));
				sprite.bitmaps = bitmaps;

			}

			@Override
			public void onUpdate(Sprite sprite) {
				if (sprite.toy == 0) {
					if (sprite.time % 4 == 0) {
						sprite.bitmapIndex += 1;
						if (sprite.bitmapIndex >= sprite.bitmaps.size()) {
							sprite.bitmapIndex = 0;
						}
					}
				}

				sprite.y += sprite.toy * sprite.speed;

				if (sprite.toy < 0) {
					sprite.speed -= 2;
				}
				if (sprite.toy > 0) {
					sprite.speed += 2;
				}

				if (sprite.speed <= 1) {
					sprite.toy = 1;
				}

				if (sprite.toy == 1
						&& sprite.y >= screenHeight / 4 * 3 - sprite.h) {
					sprite.y = screenHeight / 4 * 3 - sprite.h;
					sprite.toy = 0;
					sprite.speed = 20;
				}
			}

			@Override
			public void onDie() {
				bgMediaPlayer.stop();
				mediaPlayer = MediaPlayer.create(context, R.drawable.dead);
				mediaPlayer.setVolume(0.5f, 0.5f);
				mediaPlayer.start(); //
				loop = false;
				// hideAll();
				findViewById(R.id.gameover_screen).setVisibility(View.VISIBLE);
			}

		});

		sprites = new ArrayList<Sprite>();
		myCanvas.setSprites(sprites);

		sprites.add(player);
		initCloud();
		initBG();
		bgMediaPlayer = MediaPlayer.create(context, R.drawable.underwater);
		bgMediaPlayer.setLooping(true);
		bgMediaPlayer.setVolume(0.5f, 0.5f);
		bgMediaPlayer.start();
		loop = true;
	}

	void initCloud() {
		sprites.add(new Sprite(new SpriteEvent() {
			@Override
			public void onInit(Sprite sprite) {
				sprite.w = 96f;
				sprite.h = 30f;
				sprite.x = screenWidth / 2;
				sprite.y = screenHeight / 4;
				sprite.live = true;
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.cloud));
				sprite.bitmaps = bitmaps;

			}

			@Override
			public void onUpdate(Sprite sprite) {
				sprite.x -= 0.2f;
				if (sprite.x <= -1 * screenWidth) {
					sprite.x = screenWidth;
				}
			}

			@Override
			public void onDie() {
			}

		}));
		sprites.add(new Sprite(new SpriteEvent() {
			@Override
			public void onInit(Sprite sprite) {
				sprite.w = 96f;
				sprite.h = 30f;
				sprite.x = screenWidth;
				sprite.y = screenHeight / 3;
				sprite.live = true;
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.cloud));
				sprite.bitmaps = bitmaps;

			}

			@Override
			public void onUpdate(Sprite sprite) {
				sprite.x -= 0.5f;
				if (sprite.x <= -1 * screenWidth) {
					sprite.x = screenWidth;
				}
			}

			@Override
			public void onDie() {
			}

		}));
	}

	void initBG() {
		sprites.add(new Sprite(new SpriteEvent() {
			@Override
			public void onInit(Sprite sprite) {
				sprite.w = screenWidth;
				sprite.h = 50;
				sprite.x = 0;
				sprite.y = screenHeight / 4 * 3 - sprite.h / 2 - 0;
				sprite.live = true;
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.bg));
				sprite.bitmaps = bitmaps;

			}

			@Override
			public void onUpdate(Sprite sprite) {
				sprite.x -= 4f;
				if (sprite.x <= -1 * screenWidth) {
					sprite.x = screenWidth;
				}
			}

			@Override
			public void onDie() {
			}

		}));
		sprites.add(new Sprite(new SpriteEvent() {
			@Override
			public void onInit(Sprite sprite) {
				sprite.w = screenWidth;
				sprite.h = 50;
				sprite.x = screenWidth;
				sprite.y = screenHeight / 4 * 3 - sprite.h / 2 - 0;
				sprite.live = true;
				List<Bitmap> bitmaps = new ArrayList<>();
				bitmaps.add(BitmapFactory.decodeResource(getResources(),
						R.drawable.bg));
				sprite.bitmaps = bitmaps;

			}

			@Override
			public void onUpdate(Sprite sprite) {
				sprite.x -= 4f;
				if (sprite.x <= -1 * screenWidth) {
					sprite.x = screenWidth;
				}
			}

			@Override
			public void onDie() {
			}

		}));
	}

	void hideAll() {
		findViewById(R.id.gamestart_screen).setVisibility(View.GONE);
		findViewById(R.id.gamelooping_screen).setVisibility(View.GONE);
		findViewById(R.id.gamelooping_ctrl_screen).setVisibility(View.GONE);
		findViewById(R.id.gameover_screen).setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		// get screen size begin
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// get screen size end

		hideAll();
		findViewById(R.id.gamestart_screen).setVisibility(View.VISIBLE);

		myCanvas = (MyCanvas) findViewById(R.id.myCanvas);
		myCanvas.setSprites(sprites);

		jump_button = (Button) findViewById(R.id.ctrl_jump_button);
		jump_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (player.toy == 0) {
					player.toy = -1;
					mediaPlayer = MediaPlayer.create(context, R.drawable.jump);
					mediaPlayer.setVolume(0.1f, 0.1f);
					mediaPlayer.start();

				}
			}
		});
		start_button = (ImageView) findViewById(R.id.start_button);
		start_button.setClickable(true);
		start_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideAll();
				findViewById(R.id.gamelooping_screen).setVisibility(
						View.VISIBLE);
				findViewById(R.id.gamelooping_ctrl_screen).setVisibility(
						View.VISIBLE);
				initPlayer();
			}
		});

		restart_button = (ImageView) findViewById(R.id.restart_button);
		restart_button.setClickable(true);
		restart_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideAll();
				findViewById(R.id.gamelooping_screen).setVisibility(
						View.VISIBLE);
				findViewById(R.id.gamelooping_ctrl_screen).setVisibility(
						View.VISIBLE);
				initPlayer();
			}
		});

		scoreLabel = (TextView) findViewById(R.id.score);

		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {

				time += 1;

				if (loop) {
					if (time % 60 == 0) {
						makeEnemy();
					}
					for (int i = 0; i < sprites.size(); i++) {
						sprites.get(i).update();
					}
				}

				myCanvas.draw();

				handler.postDelayed(this, 1000 / 60);
			}

		};

		handler.postDelayed(runnable, 100);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mediaPlayer != null) {
			mediaPlayer.release(); // ͷ Դ
		}
		if (bgMediaPlayer != null) {
			bgMediaPlayer.release(); // ͷ Դ
		}
	}
}
