package powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DecreaseDamagePower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "DecreaseDamagePower";
    public static final String NAME ;//= "减伤buff";
    public static final String IMG = "images/powers/DecreaseDamagePower.png";
    public static final String[] DESCRIPTIONS ;//= new String[]{"每次攻击增加减伤(1%)，本次只受到", "%的伤害。（最低50%）"};

    private int damageReceived = 100;

    public DecreaseDamagePower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "DecreaseDamagePower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/DecreaseDamagePower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS) {
            int finalDamage = damageAmount * damageReceived / 100;
            if (damageAmount > owner.currentBlock) {
                if (damageReceived > 50) {
                    damageReceived -= 1;
                }
                updateDescription();
                return super.onAttacked(info, finalDamage);
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.damageReceived + DESCRIPTIONS[1];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
