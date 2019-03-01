package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import powers.ElectroPower;

public class Pulse extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String UPGRADE_DESCRIPTION;
    public static final String ID = "Pulse";
    public static final String NAME;// = "脉冲";
    public static final String DESCRIPTION ;//= "使用所有充能球被动一次。消耗。";
//    public static final String imgUrl = "images/cards/Pulse.png";

    public Pulse() {
        super(ID, NAME, null, 1, DESCRIPTION, CardType.SKILL, CardColor.BLUE, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            orb.onStartOfTurn();
            orb.onEndOfTurn();
        }
        if (ElectroPower.isSingle() && AbstractDungeon.player.hasPower(ElectroPower.POWER_ID)) {
            int cnt = AbstractDungeon.player.getPower(ElectroPower.POWER_ID).amount;
            for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                if (orb instanceof Lightning) {
                    for (int i = 1; i < cnt; i++) {
                        orb.onStartOfTurn();
                        orb.onEndOfTurn();
                    }
                }
            }
        }
    }

    public AbstractCard makeCopy() {
        return new Pulse();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
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
