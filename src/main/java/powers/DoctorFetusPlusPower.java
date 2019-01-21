package powers;

import cards.tempCards.MyBomb;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DoctorFetusPlusPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "DoctorFetusPlusPower";
    public static final String NAME;//= "婴儿博士+";
    public static final String IMG = "images/powers/DoctorFetusPlusPower.png";
    public static final String[] DESCRIPTIONS;//= new String[]{"每回合开始获得", "张炸弹。消耗。虚无。"};
    ;

    public DoctorFetusPlusPower(AbstractCreature owner, int bladeAmt) {
        this.name = NAME;
        this.ID = "DoctorFetusPlusPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/DoctorFetusPlusPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            MyBomb zeroCost = new MyBomb(true);
            zeroCost.cost = 1;
            zeroCost.setCostForTurn(1);
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(zeroCost, this.amount, false));
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
