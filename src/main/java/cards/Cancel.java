package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Cancel extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "Cancel";
    public static final String NAME;// = "撤销";
    public static final String DESCRIPTION;// = "将你的生命变成上回合的数值。消耗。";
//    public static final String imgUrl = "images/cards/Cancel.png";

    public Cancel() {
        super("Cancel", NAME, null, 1, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    private int lastTurnHp = -1;
    private int thisTurnHp = -1;

    @Override
    public void atTurnStart() {
        if (thisTurnHp != -1) {
            lastTurnHp = thisTurnHp;
        }
        thisTurnHp = AbstractDungeon.player.currentHealth;
        super.atTurnStart();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (lastTurnHp != -1) {
            int hp = AbstractDungeon.player.currentHealth;
            if (hp <= lastTurnHp) {
                AbstractDungeon.player.heal(lastTurnHp - hp ,true);
            } else {
                AbstractDungeon.player.damage(new DamageInfo(null, hp - lastTurnHp, DamageInfo.DamageType.HP_LOSS));
            }
        }
    }

    public AbstractCard makeCopy() {
        return new Cancel();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.initializeDescription();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
