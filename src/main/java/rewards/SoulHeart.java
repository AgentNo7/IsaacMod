package rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.ui.SoulHeartPatch;

public class SoulHeart extends Heart {

    public static Texture soulHeart = new Texture("images/ui/panel/SoulHeart.png");

    private static String name = Settings.language == Settings.GameLanguage.ZHS ? "魂心" : "Soul Heart";

    public static String ID = "soul_heart";

    public SoulHeart() {
        this(AbstractDungeon.aiRng.random(5, 10));
    }

    public SoulHeart(int amount) {
        super(soulHeart, name, HeartRewardPatch.HEART, ID);
        this.amount = amount;
        this.text += "(" + amount + ")";
    }

    @Override
    public boolean claimReward() {
        SoulHeartPatch.soulHeart += amount;
        this.isDone = true;
        return true;
    }

}
