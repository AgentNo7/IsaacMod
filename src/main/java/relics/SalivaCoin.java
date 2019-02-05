package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SalivaCoin extends CustomRelic {
    public static final String ID = "SalivaCoin";
    public static final String IMG = "images/relics/SalivaCoin.png";
    public static final String DESCRIPTION = "每次受伤获得 #b7 块钱。";

    public void show() {
        this.flash();
    }

    public SalivaCoin() {
        super("SalivaCoin", new Texture(Gdx.files.internal("images/relics/SalivaCoin.png")), RelicTier.COMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SalivaCoin();
    }

    @Override
    public void onLoseHp(int damageAmount) {
        AbstractDungeon.player.gainGold(7);
        show();
    }
}
