package blood;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;

public class BloodStorePotion extends StorePotion{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public AbstractPotion potion;
    private ShopScreen shopScreen;
    public int price;
    private int slot;
    public boolean isPurchased = false;
    private static final float RELIC_GOLD_OFFSET_X;
    private static final float RELIC_GOLD_OFFSET_Y;
    private static final float RELIC_PRICE_OFFSET_X;
    private static final float RELIC_PRICE_OFFSET_Y;
    private static final float GOLD_IMG_WIDTH;

    public BloodStorePotion(AbstractPotion potion, int slot, ShopScreen screenRef) {
        super(potion, slot, screenRef);
        this.potion = potion;
        this.price = potion.getPrice() / 25;
        this.slot = slot;
        this.shopScreen = screenRef;
    }

    public void update(float rugY) {
        if (this.potion != null) {
            this.potion.posX = 1000.0F * Settings.scale + 150.0F * (float)this.slot * Settings.scale;
            this.potion.posY = rugY + 200.0F * Settings.scale;
            this.potion.hb.move(this.potion.posX, this.potion.posY);
            this.potion.hb.update();
            if (this.potion.hb.hovered) {
                this.shopScreen.moveHand(this.potion.posX - 190.0F * Settings.scale, this.potion.posY - 70.0F * Settings.scale);
                if (InputHelper.justClickedLeft) {
                    this.potion.hb.clickStarted = true;
                }
            }

            if (this.potion.hb.clicked || this.potion.hb.hovered && CInputActionSet.select.isJustPressed()) {
                this.potion.hb.clicked = false;
                if (AbstractDungeon.player.hasRelic("Sozu")) {
                    AbstractDungeon.player.getRelic("Sozu").flash();
                    return;
                }

                if (AbstractDungeon.player.maxHealth >= this.price) {
                    if (AbstractDungeon.player.obtainPotion(this.potion)) {
                        AbstractDungeon.player.decreaseMaxHealth(this.price);
                        CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
                        CardCrawlGame.metricData.addShopPurchaseData(this.potion.ID);
                        this.shopScreen.playBuySfx();
                        this.shopScreen.createSpeech(ShopScreen.getBuyMsg());
//                        if (AbstractDungeon.player.hasRelic("The Courier")) {
//                            this.potion = AbstractDungeon.returnRandomPotion();
//                            this.price = this.potion.getPrice() / 25;
//                            this.shopScreen.getNewPrice(this);
//                        } else {
                            this.isPurchased = true;
//                        }

                        return;
                    }

                    this.shopScreen.createSpeech(TEXT[0]);
                    AbstractDungeon.topPanel.flashRed();
                } else {
                    this.shopScreen.playCantBuySfx();
                    this.shopScreen.createSpeech(ShopScreen.getCantBuyMsg());
                }
            }
        }

    }

    public void hide() {
        if (this.potion != null) {
            this.potion.posY = (float)Settings.HEIGHT + 200.0F * Settings.scale;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.potion != null) {
            this.potion.shopRender(sb);
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, this.potion.posX + RELIC_GOLD_OFFSET_X, this.potion.posY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color color = Color.WHITE;
            if (this.price > AbstractDungeon.player.maxHealth) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.potion.posX + RELIC_PRICE_OFFSET_X, this.potion.posY + RELIC_PRICE_OFFSET_Y, color);
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("StorePotion");
        TEXT = uiStrings.TEXT;
        RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
        RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
        RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
        RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
        GOLD_IMG_WIDTH = (float)ImageMaster.TP_HP.getWidth() * Settings.scale;
    }
}
