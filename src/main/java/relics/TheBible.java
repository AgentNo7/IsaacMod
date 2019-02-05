package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.FlightPower;
import relics.abstracrt.BookSuit;

public class TheBible extends BookSuit {
    public static final String ID = "TheBible";
    public static final String IMG = "images/relics/TheBible.png";
    public static final String DESCRIPTION = "六充能，每打一个怪物房间加一充能，满充能时右击对使自己获得6层飞行。";

    public TheBible() {
        super("TheBible", new Texture(Gdx.files.internal("images/relics/TheBible.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 6);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TheBible();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            if (AbstractDungeon.getMonsters() != null) {
                this.flash();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlightPower(AbstractDungeon.player, 6), 6));
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                counter = 0;
                this.stopPulse();
                show();
            }
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
    }
}
