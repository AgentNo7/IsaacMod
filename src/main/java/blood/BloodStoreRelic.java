package blood;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import relics.abstracrt.DevilRelic;

public class BloodStoreRelic extends StoreRelic {
    public AbstractRelic relic;
    private ShopScreen shopScreen;
    public int price;
    private int slot;
    public boolean isPurchased = false;
    private static final float RELIC_GOLD_OFFSET_X;
    private static final float RELIC_GOLD_OFFSET_Y;
    private static final float RELIC_PRICE_OFFSET_X;
    private static final float RELIC_PRICE_OFFSET_Y;
    private static final float GOLD_IMG_WIDTH;

    public BloodStoreRelic(AbstractRelic relic, int slot, ShopScreen screenRef, int price) {
        super(relic, slot, screenRef);
        this.relic = relic;
        this.price = price > 0 ? price : 10;
        this.slot = slot;
        this.shopScreen = screenRef;
    }

    public BloodStoreRelic(AbstractRelic relic, int slot, ShopScreen screenRef) {
        super(relic, slot, screenRef);
        this.relic = relic;
        this.price = relic.getPrice() / 30;
        //恶魔房定价规则
        if (this.relic instanceof DevilRelic) {
            this.price = 10;
        }
        this.slot = slot;
        this.shopScreen = screenRef;
    }

    public void update(float rugY) {
        if (this.relic != null) {
            this.relic.currentX = 1000.0F * Settings.scale + 150.0F * (float) this.slot * Settings.scale;
            this.relic.currentY = rugY + 400.0F * Settings.scale;
            this.relic.hb.move(this.relic.currentX, this.relic.currentY);
            this.relic.hb.update();
            if (this.relic.hb.hovered) {
                this.shopScreen.moveHand(this.relic.currentX - 190.0F * Settings.scale, this.relic.currentY - 70.0F * Settings.scale);
                if (InputHelper.justClickedLeft) {
                    this.relic.hb.clickStarted = true;
                }

                this.relic.scale = Settings.scale * 1.25F;
            } else {
                this.relic.scale = MathHelper.scaleLerpSnap(this.relic.scale, Settings.scale);
            }

            if (this.relic.hb.hovered && InputHelper.justClickedRight) {
                CardCrawlGame.relicPopup.open(this.relic);
            }

            if (this.relic.hb.clicked || this.relic.hb.hovered && CInputActionSet.select.isJustPressed()) {
                this.relic.hb.clicked = false;
                if (AbstractDungeon.player.maxHealth >= this.price) {
                    AbstractDungeon.player.decreaseMaxHealth(this.price);
                    CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
                    CardCrawlGame.metricData.addShopPurchaseData(this.relic.relicId);
                    AbstractDungeon.getCurrRoom().relics.add(this.relic);
                    this.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
                    this.relic.flash();
                    if (this.relic.relicId.equals("Membership Card")) {
                        this.shopScreen.applyDiscount(0.5F, true);
                    }

                    if (this.relic.relicId.equals("Smiling Mask")) {
                        ShopScreen.actualPurgeCost = 50;
                    }

                    if (this.relic.relicId.equals("Toxic Egg 2")) {
                        this.shopScreen.applyUpgrades(CardType.SKILL);
                    }

                    if (this.relic.relicId.equals("Molten Egg 2")) {
                        this.shopScreen.applyUpgrades(CardType.ATTACK);
                    }

                    if (this.relic.relicId.equals("Frozen Egg 2")) {
                        this.shopScreen.applyUpgrades(CardType.POWER);
                    }

                    this.shopScreen.playBuySfx();
                    this.shopScreen.createSpeech(ShopScreen.getBuyMsg());
//                    if (!this.relic.relicId.equals("The Courier") && !AbstractDungeon.player.hasRelic("The Courier")) {
                    this.isPurchased = true;
//                    }
//                    else {
//                        AbstractRelic tempRelic;
//                        for(tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier()); tempRelic instanceof OldCoin || tempRelic instanceof SmilingMask || tempRelic instanceof MawBank || tempRelic instanceof Courier; tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier())) {
//                            ;
//                        }
//
//                        this.relic = tempRelic;
//                        this.price = this.relic.getPrice() / 30;
//                        this.shopScreen.getNewPrice(this);
//                    }
                } else {
                    this.shopScreen.playCantBuySfx();
                    this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                }
            }
        }

    }

    public void hide() {
        if (this.relic != null) {
            this.relic.currentY = (float) Settings.HEIGHT + 200.0F * Settings.scale;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.relic != null) {
            this.relic.renderWithoutAmount(sb, new Color(0.0F, 0.0F, 0.0F, 0.25F));
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, this.relic.currentX + RELIC_GOLD_OFFSET_X, this.relic.currentY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color color = Color.WHITE;
            if (this.price > AbstractDungeon.player.maxHealth) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.relic.currentX + RELIC_PRICE_OFFSET_X, this.relic.currentY + RELIC_PRICE_OFFSET_Y, color);
        }

    }

    static {
        RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
        RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
        RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
        RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
        GOLD_IMG_WIDTH = (float) ImageMaster.TP_HP.getWidth() * Settings.scale;
    }
}
