package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ChargeableRelic;
import screen.ChargeRelicSelectScreen;

public class Battery extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "Battery";
    public static final String NAME;// = "电池";
    public static final String DESCRIPTION;// = "选择一个充能遗物并充满能。虚无。消耗。";
    public static final String UPGRADE_DESCRIPTION;// = "选择一个充能遗物并充满能。虚无。消耗。";
    public static final String imgUrl = "images/cards/Battery.png";

    public Battery() {
        super(ID, NAME, imgUrl, 3, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard cardToRemove = null;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.uuid.equals(this.uuid)) {
                cardToRemove = card;
                break;
            }
        }
        if (cardToRemove != null) {
            int size = AbstractDungeon.player.masterDeck.size();
            AbstractDungeon.player.masterDeck.removeCard(cardToRemove);
            if (AbstractDungeon.player.masterDeck.size() == size) {
                AbstractDungeon.player.masterDeck.removeCard(ID);
            }
        } else {
            AbstractDungeon.player.masterDeck.removeCard(ID);
        }
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof ChargeableRelic) {
                new ChargeRelicSelectScreen(false, "选择一件遗物充电", "充电页面", "极速快充2.0，瞬间满电，还在等什么？", this).open();
                return;
            }
        }
    }

    public AbstractCard makeCopy() {
        return new Battery();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.isEthereal = false;
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void update() {
        super.update();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
