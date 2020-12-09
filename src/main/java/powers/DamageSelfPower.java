package powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;

public class DamageSelfPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "DamageSelf";
    public static final String NAME;
    public static final String IMG = "images/powers/DamageSelf.png";
    public static final String[] DESCRIPTIONS;

    private static final int triggerAmount = 4;

    private static final int damageAmount = 1000;

    public DamageSelfPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage(IMG);
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.priority = 99;
    }


    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount == triggerAmount) {
            if (owner.currentHealth > 1000) {
                owner.currentHealth -= 1000;
                AbstractDungeon.effectList.add(new StrikeEffect(owner, owner.hb.cX, owner.hb.cY, 1000));
            } else if (owner.currentHealth > 500) {
                owner.currentHealth -= 500;
                AbstractDungeon.effectList.add(new StrikeEffect(owner, owner.hb.cX, owner.hb.cY, 500));
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        amount = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + triggerAmount + DESCRIPTIONS[1] + damageAmount + DESCRIPTIONS[2];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
