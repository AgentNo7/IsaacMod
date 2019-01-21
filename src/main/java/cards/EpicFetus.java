package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.EpicFetusPower;

public class EpicFetus extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String ID = "EpicFetus";
    public static final String NAME ;//= "史诗婴儿";
    public static final String DESCRIPTION;// = "每回合开始获得一张 [史诗攻击]。";
    public static final String imgUrl = "images/cards/EpicFetus.png";

    public EpicFetus() {
        super("EpicFetus", NAME, imgUrl, 3, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EpicFetusPower(p, 1, this.upgraded), 1));
    }

    public AbstractCard makeCopy() {
        return new EpicFetus();
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
