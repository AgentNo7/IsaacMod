package cards.tempCards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import monsters.pet.ScapeGoatPet;
import patches.action.ChangeTargetPatch;

public class ScapeGoat extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "ScapeGoat";
    public static final String NAME;
    public static final String DESCRIPTION;

    private ScapeGoatPet scapeGoatPet;

    public ScapeGoat(ScapeGoatPet scapeGoatPet) {
        super("ScapeGoat", NAME, "images/cards/ScapeGoat.png", 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
        this.scapeGoatPet = scapeGoatPet;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
        ChangeTargetPatch.source.add(m);
        ChangeTargetPatch.target = scapeGoatPet;
    }

    public AbstractCard makeCopy() {
        return new ScapeGoat(scapeGoatPet);
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
