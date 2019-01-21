package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

public class DeathBook extends ChargeableRelic {
    public static final String ID = "DeathBook";
    public static final String IMG = "images/relics/DeathBook.png";
    public static final String DESCRIPTION = "四充能，每打一个怪物房间加一充能，满充能时右击对所有怪物造成 #b40 伤害。";

    public DeathBook() {
        super("DeathBook", new Texture(Gdx.files.internal("images/relics/DeathBook.png")), RelicTier.UNCOMMON, LandingSound.CLINK, 4);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new DeathBook();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            if (AbstractDungeon.getMonsters() != null) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(40, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                counter = 0;
                this.stopPulse();
                show();
            }
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
