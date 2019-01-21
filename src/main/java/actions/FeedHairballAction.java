package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import monsters.pet.Hairball;
import relics.GuppysHairball;

public class FeedHairballAction extends AbstractGameAction {
    private int increaseStrengthAmount;
    private DamageInfo info;
    private static final float DURATION = 0.1F;

    public FeedHairballAction(AbstractCreature target, DamageInfo info, int increaseStrengthAmount) {
        this.info = info;
        this.setValues(target, info);
        this.increaseStrengthAmount = increaseStrengthAmount;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {
                GuppysHairball guppysHairball = (GuppysHairball) AbstractDungeon.player.getRelic(GuppysHairball.ID);
                if (!guppysHairball.addedPower) {
                    guppysHairball.counter += increaseStrengthAmount;
                    guppysHairball.addedPower = true;
                }
                Hairball hairball = (Hairball) AbstractDungeon.getMonsters().getMonster("Hairball");
                if (hairball != null) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(hairball,hairball, new StrengthPower(hairball,  increaseStrengthAmount), increaseStrengthAmount));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
