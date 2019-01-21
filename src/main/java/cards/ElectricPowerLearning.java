package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.ElectroPower;

public class ElectricPowerLearning extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String ID = "ElectricPowerLearning";
    public static final String NAME;//= "电力洞穴";
    public static final String DESCRIPTION;// = "闪电球将攻击所有敌人。";
//    public static final String imgUrl = "images/cards/ElectricPowerLearning.png";

    public ElectricPowerLearning() {
        super("ElectricPowerLearning", NAME, null, 2, DESCRIPTION, CardType.POWER, CardColor.BLUE, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ElectroPower(p, 2), 2));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ElectroPower(p, 1), 1));
        }
    }

    public AbstractCard makeCopy() {
        return new ElectricPowerLearning();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
