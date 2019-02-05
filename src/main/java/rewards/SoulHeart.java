package rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import patches.ui.SoulHeartPatch;

public class SoulHeart extends AbstractItem{

    public static Texture soulHeart = new Texture("images/ui/panel/SoulHeartHeart.png");

    public SoulHeart() {
        super();
        this.cards = null;
        this.outlineImg = this.img = soulHeart;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            this.text = "魂心。";
        }else {
            this.text = "Soul Heart";
        }
    }

    @Override
    public boolean claimReward() {
        SoulHeartPatch.soulHeart += 10;
        this.isDone = true;
        return true;
    }

}
