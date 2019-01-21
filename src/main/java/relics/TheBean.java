package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;

import java.util.Iterator;

public class TheBean extends ChargeableRelic {
    public static final String ID = "TheBean";
    public static final String IMG = "images/relics/TheBean.png";
    public static final String DESCRIPTION = "一充能，满充能时右击给所有敌人施加五层毒。";

    public TheBean() {
        super("TheBean", new Texture(Gdx.files.internal("images/relics/TheBean.png")), RelicTier.COMMON, LandingSound.CLINK, 1);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TheBean();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            if (AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.flash();
                Iterator var3 = AbstractDungeon.getMonsters().monsters.iterator();

                while (var3.hasNext()) {
                    AbstractMonster monster = (AbstractMonster) var3.next();
                    if (!monster.isDead && !monster.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, AbstractDungeon.player, new PoisonPower(monster, AbstractDungeon.player, 5), 5));
                    }
                }
            }
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
