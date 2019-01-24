package cards.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import relics.PokeGo;

import static com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.BOSS;

public class PokeBall extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "PokeBall";
    public static final String NAME;
    public static final String DESCRIPTION;

    private PokeGo pokeGo;

    public PokeBall(PokeGo pokeGo) {
        super("PokeBall", NAME, "images/cards/PokeBall.png", 1, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
        this.pokeGo = pokeGo;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.type == BOSS) {
            return;
        }
        if (m.currentHealth == 1) {
            pokeGo.counter = m.maxHealth;
            m.currentHealth = 0;
            m.die();
            pokeGo.monsterClass = m.getClass();
        }
    }

    public AbstractCard makeCopy() {
        return new PokeBall(pokeGo);
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
