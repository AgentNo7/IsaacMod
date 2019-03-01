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

public class SatanicBible extends BookSuit {
    public static final String ID = "SatanicBible";
    public static final String IMG = "images/relics/SatanicBible.png";
    public static final String DESCRIPTION = "六充能，满充能时右击在本场战斗获得壁垒和10点黑心。";

    public SatanicBible() {
        super("SatanicBible", new Texture(Gdx.files.internal("images/relics/SatanicBible.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 6);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SatanicBible();
    }

    //右键开大
    public void onRightClick() {
        if (counter >= maxCharge) {
            this.flash();
            if (AbstractDungeon.getMonsters() != null) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BarricadePower(AbstractDungeon.player)));
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
            SoulHeartPatch.blackHeart += 10;
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

    @Override
    public void onEquip() {
        super.onEquip();
    }
}
