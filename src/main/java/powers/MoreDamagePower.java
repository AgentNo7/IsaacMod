package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MoreDamagePower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "MoreDamagePower";
    public static final String NAME;
    public static final String IMG = "images/powers/MoreDamagePower.png";
    public static final String[] DESCRIPTIONS;


    public MoreDamagePower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "MoreDamagePower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/MoreDamagePower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        damageAmount *= 3 * this.amount;
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, ID, 1));
        }
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }

}
