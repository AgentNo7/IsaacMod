package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NineVolt extends CustomRelic {
    public static final String ID = "NineVolt";
    public static final String IMG = "images/relics/NineVolt.png";
    public static final String DESCRIPTION = "你的主动在使用后将会增加一充能。";

    public NineVolt() {
        super(ID, new Texture(Gdx.files.internal(IMG)), RelicTier.SHOP, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new NineVolt();
    }

    @Override
    public void update() {
        super.update();
    }
}
