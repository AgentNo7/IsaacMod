package rewards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import patches.ui.SoulHeartPatch;

public class BlackHeart extends Heart{

    public static Texture blackHeart = new Texture("images/ui/panel/BlackHeart.png");

    private static String name = Settings.language == Settings.GameLanguage.ZHS ? "黑心" : "Black Heart";

    public static String ID = "black_heart";

    public BlackHeart() {
        this(AbstractDungeon.aiRng.random(3, 11));
    }

    public BlackHeart(int amount) {
        super(blackHeart, name, HeartRewardPatch.HEART, ID);
        this.amount = amount;
        this.text += "(" + amount + ")";
    }

    @Override
    public boolean claimReward() {
        SoulHeartPatch.blackHeart += amount;
        this.isDone = true;
        return true;
    }

}
