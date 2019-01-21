package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import powers.DoctorFetusPlusPower;
import powers.DoctorFetusPower;


public class DoctorFetus extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "DoctorFetus";
    public static final String NAME;// = "婴儿博士";
    public static final String DESCRIPTION ;//= "每回合开始获得一张 !M! 费快爆炸弹";
    public static final String imgUrl = "images/cards/DoctorFetus.png";

    private boolean isUpgraded = false;

    public DoctorFetus() {
        super("DoctorFetus", NAME, imgUrl, 2, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!isUpgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoctorFetusPower(p, 1), 1));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoctorFetusPlusPower(p, 1), 1));
        }
    }

    public AbstractCard makeCopy() {
        return new DoctorFetus();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.magicNumber = 1;
            this.baseMagicNumber = 1;
            this.initializeDescription();
            isUpgraded = true;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
