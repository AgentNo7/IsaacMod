package powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class MonstroPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "MonstroPower";
    public static final String NAME;
    public static final String IMG = "images/powers/MonstroPower.png";
    public static final String[] DESCRIPTIONS;

    private boolean justApplied;

    public MonstroPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "MonstroPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/MonstroPower.png");
        this.updateDescription();
        this.type = PowerType.DEBUFF;
        this.priority = 99;
        this.justApplied = true;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    private int times = 0;

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((++times) % 3 == 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -1), -1));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new GainStrengthPower(this.owner, 1), 1));
        }
        return super.onAttacked(info, damageAmount);
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
