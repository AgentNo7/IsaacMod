package cards.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.TheBombPower;

public class MyBomb extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "MyBomb";
    public static final String NAME;//= "快爆炸弹";
    public static final String DESCRIPTION;//= "两回合后爆炸，对所有敌人造成40点伤害。虚无。消耗。";

    private boolean upgrade;

    public MyBomb(boolean upgrade) {
        super("MyBomb", NAME, "colorless/skill/the_bomb", 2, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = 40;
        this.isEthereal = true;
        this.upgrade = upgrade;
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TheBombPower(p, 2, this.magicNumber), 2));
    }

    public AbstractCard makeCopy() {
        return new MyBomb(upgrade);
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.cost = 1;
            this.upgradeName();
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
