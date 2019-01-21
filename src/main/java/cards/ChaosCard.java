package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import monsters.Hush;

public class ChaosCard extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "ChaosCard";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String imgUrl = "images/cards/ChaosCard.png";

    public ChaosCard() {
        super("ChaosCard", NAME, imgUrl, 3, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m instanceof Hush) {
            AbstractDungeon.player.masterDeck.removeCard(this.cardID);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, (int) (m.maxHealth * 0.99), DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE));
            if ((m.currentHealth > (m.maxHealth / 100))) {
                m.currentHealth = m.maxHealth / 100;
            }
        }
        AbstractDungeon.player.masterDeck.removeCard(this.cardID);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, (int) (m.maxHealth * 0.99), DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        AbstractDungeon.player.masterDeck.removeCard(this.cardID);
    }

    public AbstractCard makeCopy() {
        return new ChaosCard();
    }

    public void upgrade() {
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
