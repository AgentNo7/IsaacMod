package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

public class BookOfRevelations extends ChargeableRelic {
    public static final String ID = "BookOfRevelations";
    public static final String IMG = "images/relics/BookOfRevelations.png";
    public static final String DESCRIPTION = "六充能，满充能时右击，在本场战斗获得壁垒和30点格挡。";

    public BookOfRevelations() {
        super("BookOfRevelations", new Texture(Gdx.files.internal("images/relics/BookOfRevelations.png")), RelicTier.COMMON, LandingSound.CLINK, 6);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BookOfRevelations();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge && AbstractDungeon.getMonsters() != null) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BarricadePower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 30));
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            counter = 0;
            this.stopPulse();
            show();
        }
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
