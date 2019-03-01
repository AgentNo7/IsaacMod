package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Distribution extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "Distribution";
    public static final String NAME;// = "分配";
    public static final String DESCRIPTION;// = "降低1力量，1敏捷，获得 !M! 集中";
//    public static final String imgUrl = "images/cards/Distribution.png";

    public Distribution() {
        super(ID, NAME, null, 1, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(AbstractDungeon.player, -1), -1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(AbstractDungeon.player, -1), -1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(AbstractDungeon.player, this.baseMagicNumber), this.baseMagicNumber));
    }

    public AbstractCard makeCopy() {
        return new Distribution();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            baseMagicNumber = 2;
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
