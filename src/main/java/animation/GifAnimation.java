package animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

/**
 * Created by Keeper on 2019/3/16.
 */
public class GifAnimation{
    Animation<TextureRegion> animation;
    float elapsed;

    public GifDecoder gdec;

    public static HashMap<String, Animation<TextureRegion>> cache = new HashMap<>();

    public GifAnimation(String filePath) {
        create(filePath);
    }

    public void create(String filePath) {
        if (cache.containsKey(filePath)) {
            animation = cache.get(filePath);
        } else {
            animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(filePath).read(), this);
            cache.put(filePath, animation);
        }
    }

    public void render(SpriteBatch sb, final float x, final float y, final float width, final float height) {
        elapsed += Gdx.graphics.getDeltaTime();
//        Gdx.gl.glClearColor(1, 0, 0, 0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.draw(animation.getKeyFrame(elapsed), x, y, width, height);
    }

    public void render(SpriteBatch sb,  float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        elapsed += Gdx.graphics.getDeltaTime();
        sb.draw(animation.getKeyFrame(elapsed), x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }

    public void dispose(SpriteBatch sb) {
        sb.dispose();
    }

    public int getWidth() {
        return gdec.getWidth();
    }

    public int getHeight() {
        return gdec.getHeight();
    }
}
