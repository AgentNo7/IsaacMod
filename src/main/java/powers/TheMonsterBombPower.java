package powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TheMonsterBombPower extends AbstractPower {
    public static final String POWER_ID = "TheBomb";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private int damage;
    private static int bombIdOffset;

    public TheMonsterBombPower(AbstractCreature owner, int turns, int damage) {
        this.name = NAME;
        this.ID = "TheBomb" + bombIdOffset;
        ++bombIdOffset;
        this.owner = owner;
        this.amount = turns;
        this.damage = damage;
        this.updateDescription();
        this.loadRegion("combust");
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
            if (this.amount == 1) {
                DamageInfo damageInfo = new DamageInfo(this.owner, this.damage, DamageType.THORNS);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damageInfo, AttackEffect.FIRE, true));
            }
        }

    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[1], this.damage);
        } else {
            this.description = String.format(DESCRIPTIONS[0], this.amount, this.damage);
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("TheBomb");
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
