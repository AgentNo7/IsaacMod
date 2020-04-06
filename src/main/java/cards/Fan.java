package cards;

import animation.AbstractGIFCard;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import orbs.Wind;

/**
 * Created by Keeper on 2019/3/27.
 */
public class Fan extends AbstractGIFCard
{
    public static final String ID = "Fan";
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String imgUrl = "images/cards/Fan.gif";
    public static final String NAME;
    public static final String DESCRIPTION;

    public Fan() {
        super("Fan", NAME, imgUrl, 2, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i) {
            final AbstractOrb orb = new Wind();
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(orb));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fan();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}