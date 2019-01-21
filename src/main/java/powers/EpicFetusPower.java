package powers;

import cards.tempCards.EpicFetusAttack;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EpicFetusPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "EpicFetusPower";
    public static final String NAME;// = "史诗婴儿";
    public static final String IMG = "images/powers/EpicFetusPower.png";
    public static final String[] DESCRIPTIONS;// = new String[]{"每回合开始获得", "张[史诗攻击]。"};

    public boolean isUpgraded;

    public EpicFetusPower(AbstractCreature owner, int bladeAmt, boolean isUpgraded) {
        this.name = NAME;
        this.ID = "EpicFetusPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/EpicFetusPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.isUpgraded = isUpgraded;
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            EpicFetusAttack attack = new EpicFetusAttack();
            if (isUpgraded) {
                attack.upgrade();
            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(attack, this.amount, false));
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
