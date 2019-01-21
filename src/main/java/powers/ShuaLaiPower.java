package powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ShuaLaiPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "ShuaLaiPower";
    public static final String NAME;// = "耍赖";
    public static final String IMG = "images/powers/ShuaLaiPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"受到的攻击有50%的概率会失效"};

    private boolean justApplied;

    public ShuaLaiPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "ShuaLaiPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/ShuaLaiPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.priority = 99;
        this.justApplied = true;
    }

//    public void playApplyPowerSfx() {
//        CardCrawlGame.sound.play("POWER_ShuaLaiPower", 0.05F);
//    }

    public float atDamageReceive(float damage, DamageType type) {
        int rnd = AbstractDungeon.aiRng.random(0, 99);
        if (rnd < 50 && damage > 0.0F) {
            this.flash();
            damage = 0.0F;
        }
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "ShuaLaiPower"));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "ShuaLaiPower", 1));
            }
        }
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
