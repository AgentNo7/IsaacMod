package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ClickableRelic;
import screen.BoxForChargeRelicSelectScreen;

public class KeepersGift extends ClickableRelic {
    public static final String ID = "KeepersGift";
    public static final String IMG = "images/relics/KeepersGift.png";
    public static final String DESCRIPTION = "右击使用，从三个遗物中选择一个遗物。";

    public AbstractRelic addRelic = null;

    public KeepersGift() {
        super("KeepersGift", new Texture(Gdx.files.internal("images/relics/KeepersGift.png")), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new KeepersGift();
    }

    private boolean selected = false;

    //右键开大
    protected void onRightClick() {
        if (addRelic == null && !selected) {
            this.flash();
            selected = true;
            new BoxForChargeRelicSelectScreen(false, "选择一个遗物", "遗物选择", "", this).open();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void update() {
        super.update();
    }

}
