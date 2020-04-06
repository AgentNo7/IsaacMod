package powers;

import actions.CreateIntentAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ImpatiencePower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "ImpatiencePower";
    public static final String NAME;
    public static final String IMG = "images/powers/ImpatiencePower.png";
    public static final String[] DESCRIPTIONS;

    private static final int maxCardPlay = 15;

    public ImpatiencePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.img = ImageMaster.loadImage(IMG);
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        ++amount;
        if (amount >= maxCardPlay) {
            amount = 0;
            takeTurn();
        }
    }

    private void takeTurn() {
        if (!(owner instanceof AbstractMonster)) {
            return;
        }
        AbstractMonster monster = (AbstractMonster) owner;
        monster.takeTurn();
        AbstractDungeon.actionManager.addToBottom(new CreateIntentAction(monster));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (maxCardPlay - this.amount) + DESCRIPTIONS[1];
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }
}
