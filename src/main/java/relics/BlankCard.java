package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import powers.BlankCardPower;
import relics.abstracrt.ChargeableRelic;

public class BlankCard extends ChargeableRelic {
    public static final String ID = "BlankCard";
    public static final String IMG = "images/relics/BlankCard.png";
    public static final String DESCRIPTION = "四充能，满充能时右击从手牌中选择一张卡，在每回合开始时打出一次";

    private boolean isValid = false;

    public BlankCard() {
        super("BlankCard", new Texture(Gdx.files.internal("images/relics/BlankCard.png")), RelicTier.RARE, LandingSound.CLINK, 4);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BlankCard();
    }

    //右键开大
    protected void onRightClick() {
        if (counter >= maxCharge) {
            this.flash();
            CardGroup all = new CardGroup(CardGroup.CardGroupType.HAND);
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                all.addToRandomSpot(c);
            }
            AbstractDungeon.gridSelectScreen.open(all, 1, "选择一张牌", false);
            counter = 0;
            isValid = true;
            this.stopPulse();
            show();
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
        if (isValid && AbstractDungeon.gridSelectScreen != null && AbstractDungeon.gridSelectScreen.selectedCards != null) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0) {
                AbstractCard selectedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                if (selectedCard != null) {
                    System.out.println("拿到辽：" + selectedCard.name);
                    BlankCardPower power = new BlankCardPower(AbstractDungeon.player, 1, selectedCard);
                    boolean hasPower = false;
                    for (AbstractPower power1 : AbstractDungeon.player.powers) {
                        if (power1 instanceof BlankCardPower && ((BlankCardPower) power1).card.cardID.equals(selectedCard.cardID)) {
                            power1.amount++;
                            power1.updateDescription();
                            hasPower = true;
                            break;
                        }
                    }
                    if (!hasPower) {
                        AbstractDungeon.player.powers.add(power);
                    }
                    power.updateDescription();
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    isValid = false;
                }
            }
        }
    }
}
