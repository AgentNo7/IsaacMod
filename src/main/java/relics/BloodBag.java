package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BloodBag extends CustomRelic {
    public static final String ID = "BloodBag";
    public static final String IMG = "images/relics/BloodBag.png";
    public static final String DESCRIPTION = "你的主动在使用后将会保留一充能。";

    public BloodBag() {
        super(ID, new Texture(Gdx.files.internal(IMG)), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BloodBag();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.increaseMaxHp(7, true);
    }
}
