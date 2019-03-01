package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ClickableRelic;
import screen.ChargeRelicSelectScreen;

public class Diplopia extends ClickableRelic {
    public static final String ID = "Diplopia";
    public static final String IMG = "images/relics/Diplopia.png";
    public static final String DESCRIPTION = "右击使用，选择并复制一个已有的遗物。（复视复视会得到一个堪比头环的复视）";

    public static boolean used = false;
    public AbstractRelic addRelic = null;

    public Diplopia() {
        super("Diplopia", new Texture(Gdx.files.internal("images/relics/Diplopia.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Diplopia();
    }

    //右键开大
    public void onRightClick() {
        if (!used) {
            this.flash();
            new ChargeRelicSelectScreen(false, "选择一件遗物复制", "遗物选择", "不要选择复视哦，没有用的", this).open();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void onEquip() {
        super.onEquip();
        used = false;
    }

    @Override
    public void update() {
        super.update();
    }

}
