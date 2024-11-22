package oldAndroidApp.DinoRun;

import java.util.Comparator;

public class SpriteComparator implements Comparator<Sprite> {
	@Override
    public int compare(Sprite s1, Sprite s2) {
        return s1.zindex - s2.zindex;
    }
}