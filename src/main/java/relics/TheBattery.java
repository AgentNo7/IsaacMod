package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TheBattery extends CustomRelic {
    public static final String ID = "TheBattery";
    public static final String IMG = "images/relics/TheBattery.png";
    public static final String DESCRIPTION = "主动道具获得额外一倍的充能上限。";

    public TheBattery() {
        super(ID, new Texture(Gdx.files.internal(IMG)), RelicTier.SHOP, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TheBattery();
    }

    @Override
    public void update() {
        super.update();
    }
}
