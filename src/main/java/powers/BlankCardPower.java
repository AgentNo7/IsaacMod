package powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BlankCardPower extends AbstractPower {
    private static final PowerStrings powerString;
    public static final String POWER_ID = "BlankCardPower";
    public static final String NAME;// = "白卡";
    public static final String IMG = "images/powers/BlankCardPower.png";
    public static final String[] DESCRIPTIONS ;//= new String[]{"每回合开始打出一张[", "]。", "未指定卡牌"};

    public AbstractCard card;

    public BlankCardPower(AbstractCreature owner, int bladeAmt, AbstractCard card) {
        this.name = NAME;
        this.ID = "BlankCardPower";
        this.owner = owner;
        this.amount = bladeAmt;
        this.img = ImageMaster.loadImage("images/powers/BlankCardPower.png");
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.card = card.makeStatEquivalentCopy();
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            for (int i = 0; i < this.amount; i++) {
                if (card.target == AbstractCard.CardTarget.ENEMY) {
                    playAgain(card, AbstractDungeon.getRandomMonster());
                } else {
                    card.applyPowers();
                    playAgain(card, null);
                }
            }
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        if (card != null) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + card.name + DESCRIPTIONS[2];
        } else {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + DESCRIPTIONS[2];
        }
    }

    public static AbstractCard playAgain(AbstractCard card, AbstractMonster m) {
        AbstractCard tmp = card.makeSameInstanceOf();
        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
        tmp.target_y = (Settings.HEIGHT / 2.0F);
        tmp.freeToPlayOnce = true;
        if (m != null) {
            tmp.calculateCardDamage(m);
        }
        tmp.purgeOnUse = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));
        return tmp;
    }

    static {
        powerString = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerString.NAME;
        DESCRIPTIONS = powerString.DESCRIPTIONS;
    }

}
