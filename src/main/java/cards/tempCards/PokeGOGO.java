package cards.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import relics.PokeGo;

public class PokeGOGO extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "PokeGOGO";
    public static final String NAME;
    public static final String DESCRIPTION;

    private PokeGo pokeGo;

    public PokeGOGO(PokeGo pokeGo) {
        super("PokeGOGO", NAME + (pokeGo != null ? pokeGo.pet.name : "") + "!", "images/cards/PokeBall.png", 1, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
        this.pokeGo = pokeGo;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        pokeGo.target = m;
    }

    public AbstractCard makeCopy() {
        return new PokeGOGO(pokeGo);
    }

    public void upgrade() {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
