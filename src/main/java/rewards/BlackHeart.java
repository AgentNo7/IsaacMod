package rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import patches.ui.SoulHeartPatch;

public class BlackHeart extends AbstractItem{

    public static Texture blackHeart = new Texture("images/ui/panel/BlackHeart.png");

    public BlackHeart() {
        super();
        this.cards = null;
        this.outlineImg = this.img = blackHeart;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            this.text = "黑心。";
        }else {
            this.text = "Black Heart";
        }
    }

    @Override
    public boolean claimReward() {
        SoulHeartPatch.blackHeart += 10;
        this.isDone = true;
        return true;
    }

}
