package cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;


public class SuicideKing extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "SuicideKing";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String imgUrl = "images/cards/SuicideKing.png";

    public SuicideKing() {
        super(ID, NAME, imgUrl, 3, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.masterDeck.removeCard(this.cardID);
        p.currentHealth = -999999;
        AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(p, 99999999, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        int relic = AbstractDungeon.treasureRng.random(1, 4);
        for (int i = 0; i < relic; i++) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
        }
        int card = AbstractDungeon.treasureRng.random(0, 3);
        for (int i = 0; i < card; i++) {
            AbstractDungeon.getCurrRoom().addCardToRewards();
        }
        int potion = AbstractDungeon.treasureRng.random(0, 3);
        for (int i = 0; i < potion; i++) {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomPotion()));
        }
        for (int i = 0; i < 10 - relic - card - potion; i++) {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.treasureRng.random(35, 65)));//addGoldToRewards(AbstractDungeon.treasureRng.random(35, 65));
        }
    }

    public AbstractCard makeCopy() {
        return new SuicideKing();
    }

    public void upgrade() {
    }

    @Override
    public void triggerWhenDrawn(){
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
