package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import patches.ui.SoulHeartPatch;
import relics.abstracrt.BookSuit;

public class BookOfRevelations extends BookSuit {
    public static final String ID = "BookOfRevelations";
    public static final String IMG = "images/relics/BookOfRevelations.png";
    public static final String DESCRIPTION = "六充能，满充能时右击，在本场战斗获得壁垒和10点魂心。";

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
            SoulHeartPatch.soulHeart += 10;
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            counter = 0;
            this.stopPulse();
            show();
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
