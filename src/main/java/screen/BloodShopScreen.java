package screen;


import blood.BloodStorePotion;
import blood.BloodStoreRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.OnSaleTag;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FloatyEffect;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import mymod.IsaacMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.DevilRelic;

import java.util.*;

import static utils.Utils.getRandomRelicRng;


public class BloodShopScreen extends ShopScreen {
    private static final Logger logger = LogManager.getLogger(ShopScreen.class.getName());
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Shop Tip");
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("Shop Screen");

    public static final String[] NAMES = characterStrings.NAMES;
    public static final String[] TEXT = characterStrings.TEXT;


    public boolean isActive = true;

    private int scale = 35;


    private static Texture rugImg = null;
    private static Texture removeServiceImg = null;
    private static Texture soldOutImg = null;
    private static Texture handImg = null;
    private float rugY = Settings.HEIGHT;

    private static final float RUG_SPEED = 5.0F;

    private ArrayList<AbstractCard> coloredCards = new ArrayList();
    private ArrayList<AbstractCard> colorlessCards = new ArrayList();
    private static final float DRAW_START_X = Settings.WIDTH * 0.16F;
    private static final int NUM_CARDS_PER_LINE = 5;
    private static final float CARD_PRICE_JITTER = 0.1F;
    private static final float TOP_ROW_Y = 730.0F * Settings.scale;
    private static final float BOTTOM_ROW_Y = 307.0F * Settings.scale;


    private float speechTimer = 0.0F;
    private static final float MIN_IDLE_MSG_TIME = 40.0F;
    private static final float MAX_IDLE_MSG_TIME = 60.0F;
    private static final float SPEECH_DURATION = 4.0F;
    private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
    private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
    private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
    private ShopSpeechBubble speechBubble = null;
    private SpeechTextEffect dialogTextEffect = null;
    private static final String WELCOME_MSG = NAMES[0];
    private ArrayList<String> idleMessages = new ArrayList();
    private boolean saidWelcome = false;
    private boolean somethingHovered = false;


    private ArrayList<BloodStoreRelic> relics = new ArrayList();

    private static final float RELIC_PRICE_JITTER = 0.05F;

    private ArrayList<BloodStorePotion> potions = new ArrayList();

    private static final float POTION_PRICE_JITTER = 0.05F;

    public boolean purgeAvailable = false;
    public static int purgeCost = 5;
    public static int actualPurgeCost = 5;
    private static final int PURGE_COST_RAMP = 25;
    private boolean purgeHovered = false;
    private float purgeCardX;
    private float purgeCardY;
    private float purgeCardScale = 1.0F;


    private FloatyEffect f_effect = new FloatyEffect(20.0F, 0.1F);
    private float handTimer = 1.0F;
    private float handX = Settings.WIDTH / 2.0F;
    private float handY = Settings.HEIGHT;
    private float handTargetX = 0.0F;
    private float handTargetY = Settings.HEIGHT;
    private static final float HAND_SPEED = 6.0F;
    private static float HAND_W;
    private static float HAND_H;
    private float notHoveredTimer = 0.0F;


    private static final float GOLD_IMG_WIDTH = ImageMaster.TP_HP.getWidth() * Settings.scale;
    private static final float COLORLESS_PRICE_BUMP = 1.2F;
    private OnSaleTag saleTag;
    private static final float GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
    private static final float GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
    private static final float PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
    private static final float PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;


    public void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        if (AbstractDungeon.id.equals("TheEnding")) {
            Collections.addAll(this.idleMessages, Merchant.ENDING_TEXT);
        } else {
            Collections.addAll(this.idleMessages, TEXT);
        }

        if (rugImg == null) {
            switch (Settings.language) {
                case DEU:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/deu.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/deu.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/deu.png");
                    break;
                case FRA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/fra.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/fra.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/fra.png");
                    break;
                case ITA:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ita.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ita.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ita.png");
                    break;
                case KOR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/kor.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/kor.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/kor.png");
                    break;
                case RUS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/rus.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/rus.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/rus.png");
                    break;
                case UKR:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/ukr.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/ukr.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/ukr.png");
                    break;
                case ZHS:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/zhs.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/zhs.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/zhs.png");
                    break;
                default:
                    rugImg = ImageMaster.loadImage("images/npcs/rug/eng.png");
                    removeServiceImg = ImageMaster.loadImage("images/npcs/purge/eng.png");
                    soldOutImg = ImageMaster.loadImage("images/npcs/sold_out/eng.png");
            }

            handImg = ImageMaster.loadImage("images/npcs/merchantHand.png");
        }

        HAND_W = handImg.getWidth() * Settings.scale;
        HAND_H = handImg.getHeight() * Settings.scale;


        this.coloredCards.clear();
        this.colorlessCards.clear();
        ArrayList<AbstractCard> cards1 = new ArrayList<>();
        AbstractCard c;
        for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.50F), CardType.ATTACK, true).makeCopy(); c.color == CardColor.COLORLESS || c instanceof Feed; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.ATTACK, true).makeCopy()) {
            ;
        }

        cards1.add(c);

        for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.50F), CardType.ATTACK, true).makeCopy(); Objects.equals(c.cardID, ((AbstractCard) cards1.get(cards1.size() - 1)).cardID) || c.color == CardColor.COLORLESS || c instanceof Feed; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.ATTACK, true).makeCopy()) {
            ;
        }

        cards1.add(c);

        for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.50F), CardType.SKILL, true).makeCopy(); c.color == CardColor.COLORLESS || c instanceof Feed; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.SKILL, true).makeCopy()) {
            ;
        }

        cards1.add(c);

        for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.50F), CardType.SKILL, true).makeCopy(); Objects.equals(c.cardID, ((AbstractCard) cards1.get(cards1.size() - 1)).cardID) || c.color == CardColor.COLORLESS || c instanceof Feed; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.SKILL, true).makeCopy()) {
            ;
        }

        cards1.add(c);

        for (c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRareOrUncommon(0.50F), CardType.POWER, true).makeCopy(); c.color == CardColor.COLORLESS || c instanceof Feed; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), CardType.POWER, true).makeCopy()) {
            ;
        }

        cards1.add(c);
        this.coloredCards = cards1;
        this.colorlessCards = colorlessCards;
        initCards();


        initRelics();


        initPotions();


        this.purgeAvailable = true;
        this.purgeCardY = -1000.0F;
        this.purgeCardX = (1400.0F * Settings.scale);
        this.purgeCardScale = 0.7F;
        actualPurgeCost = purgeCost;

        CardCrawlGame.nextDungeon = getNextDungeonName();
    }

    private String getNextDungeonName() {
        switch (AbstractDungeon.id) {
            case "Exordium":
                return "TheCity";
            case "TheCity":
                return "TheBeyond";
            case "TheBeyond":
                if (Settings.isEndless) {
                    return "Exordium";
                }
                return null;
        }
        return null;
    }

    public static void resetPurgeCost() {
        purgeCost = 5;
        actualPurgeCost = 5;
    }

    private void initCards() {
        int tmp = (int) (Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;

        float padX = (int) (tmp + AbstractCard.IMG_WIDTH_S);


        for (int i = 0; i < this.coloredCards.size(); i++) {
            float tmpPrice = AbstractCard.getPrice(((AbstractCard) this.coloredCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);


            AbstractCard c = (AbstractCard) this.coloredCards.get(i);
            c.price = ((int) tmpPrice) / scale;
            if (c.rarity == AbstractCard.CardRarity.UNCOMMON) {
                c.price++;
            }
            c.current_x = (Settings.WIDTH / 2);
            c.target_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);

            if ((c.type == CardType.ATTACK) && (AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
                c.upgrade();
            } else if ((c.type == CardType.SKILL) && (AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
                c.upgrade();
            } else if ((c.type == CardType.POWER) && (AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
                c.upgrade();
            }
        }


        for (int i = 0; i < this.colorlessCards.size(); i++) {
            float tmpPrice = AbstractCard.getPrice(((AbstractCard) this.colorlessCards.get(i)).rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);


            tmpPrice *= 1.2F;

            AbstractCard c = (AbstractCard) this.colorlessCards.get(i);

            c.price = ((int) tmpPrice) / scale;
            c.current_x = (Settings.WIDTH / 2);
            c.target_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);

            if ((c.type == CardType.ATTACK) && (AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
                c.upgrade();
            } else if ((c.type == CardType.SKILL) && (AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
                c.upgrade();
            } else if ((c.type == CardType.POWER) && (AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
                c.upgrade();
            }
        }

        setStartingCardPositions();
    }

    public static void purgeCard() {
        AbstractDungeon.player.decreaseMaxHealth(actualPurgeCost);
        CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
        purgeCost += 2;
        actualPurgeCost = purgeCost;
    }

    public void updatePurge() {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            purgeCard();
            this.purgeAvailable = false;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
                AbstractDungeon.topLevelEffects.add(new com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.shopScreen.purgeAvailable = false;
        }
    }

    public static String getCantBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[1]);
        list.add(NAMES[2]);
        list.add(NAMES[3]);
        list.add(NAMES[4]);
        list.add(NAMES[5]);
        list.add(NAMES[6]);

        return (String) list.get(MathUtils.random(list.size() - 1));
    }

    public static String getBuyMsg() {
        ArrayList<String> list = new ArrayList();
        list.add(NAMES[7]);
        list.add(NAMES[8]);
        list.add(NAMES[9]);
        list.add(NAMES[10]);
        list.add(NAMES[11]);

        return (String) list.get(MathUtils.random(list.size() - 1));
    }

    public void applyUpgrades(CardType type) {
        for (AbstractCard c : this.coloredCards) {
            if (c.type == type) {
                c.upgrade();
            }
        }
        for (AbstractCard c : this.colorlessCards) {
            if (c.type == type) {
                c.upgrade();
            }
        }
    }

    public void applyDiscount(float multiplier, boolean affectPurge) {
    }

    private void initRelics() {
        this.relics.clear();
        this.relics = new ArrayList();

        List<String> tempDevilRelics = new ArrayList<>(IsaacMod.devilRelics);
        List<String> tempDevilOnlyRelics = new ArrayList<>(IsaacMod.devilOnlyRelics);
        for (int i = 0; i < 3; i++) {
            AbstractRelic tempRelic = null;
            // todo 恶魔房专属遗物
            if (IsaacMod.devilOnlyRelics.size() > 0 && AbstractDungeon.relicRng.randomBoolean(0.24F)) {
                tempRelic = getRandomRelicRng(IsaacMod.devilOnlyRelics);
            } else if (IsaacMod.devilRelics.size() > 0) {
                tempRelic = getRandomRelicRng(IsaacMod.devilRelics);
            }
            if (tempRelic == null) {
                tempRelic = AbstractDungeon.returnRandomRelicEnd(AbstractRelic.RelicTier.RARE);
            }
            BloodStoreRelic relic;
            if (tempRelic instanceof DevilRelic) {
                relic = new BloodStoreRelic(tempRelic, i, this, ((DevilRelic) tempRelic).price);
            } else if (tempRelic instanceof ChargeableRelic) {
                relic = new BloodStoreRelic(tempRelic, i, this, ((ChargeableRelic) tempRelic).price);
            } else {
                relic = new BloodStoreRelic(tempRelic, i, this);
            }
            this.relics.add(relic);
        }
        IsaacMod.devilRelics = tempDevilRelics;
        IsaacMod.devilOnlyRelics = tempDevilOnlyRelics;
    }

    private void initPotions() {
        this.potions.clear();
        this.potions = new ArrayList();
        int rare = AbstractDungeon.potionRng.random(1, 2);
        for (int i = 0; i < 3; i++) {
            AbstractPotion abstractPotion = (i <= rare) ? AbstractDungeon.returnRandomPotion(PotionRarity.RARE, true) : AbstractDungeon.returnRandomPotion(PotionRarity.UNCOMMON, true);
            BloodStorePotion potion = new BloodStorePotion(abstractPotion, i, this);
            if (!Settings.isDailyRun) {
                potion.price = MathUtils.round(potion.price * AbstractDungeon.merchantRng
                        .random(0.95F, 1.05F));
            }

            this.potions.add(potion);
        }
    }

    public void getNewPrice(BloodStoreRelic r) {
        int retVal = r.price;
        r.price = retVal;
    }

    public void getNewPrice(BloodStorePotion r) {
        int retVal = r.price;

        r.price = retVal;
    }

    private int applyDiscountToRelic(int price, float multiplier) {
        return MathUtils.round(price * multiplier);
    }


    public static AbstractRelic.RelicTier rollRelicTier() {
        int roll = AbstractDungeon.merchantRng.random(99);
        logger.info("ROLL " + roll);


        if (roll < 48) {
            return AbstractRelic.RelicTier.COMMON;
        }
        if (roll < 82) {
            return AbstractRelic.RelicTier.UNCOMMON;
        }

        return AbstractRelic.RelicTier.RARE;
    }

    private void setStartingCardPositions() {
        int tmp = (int) (Settings.WIDTH - DRAW_START_X * 2.0F - AbstractCard.IMG_WIDTH_S * 5.0F) / 4;

        float padX = (int) (tmp + AbstractCard.IMG_WIDTH_S) + 10.0F * Settings.scale;

        for (int i = 0; i < this.coloredCards.size(); i++) {
            ((AbstractCard) this.coloredCards.get(i)).updateHoverLogic();
            ((AbstractCard) this.coloredCards.get(i)).targetDrawScale = 0.75F;
            ((AbstractCard) this.coloredCards.get(i)).current_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);
            ((AbstractCard) this.coloredCards.get(i)).target_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);
            ((AbstractCard) this.coloredCards.get(i)).target_y = (9999.0F * Settings.scale);
            ((AbstractCard) this.coloredCards.get(i)).current_y = (9999.0F * Settings.scale);
        }

        for (int i = 0; i < this.colorlessCards.size(); i++) {
            ((AbstractCard) this.colorlessCards.get(i)).updateHoverLogic();
            ((AbstractCard) this.colorlessCards.get(i)).targetDrawScale = 0.75F;
            ((AbstractCard) this.colorlessCards.get(i)).current_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);
            ((AbstractCard) this.colorlessCards.get(i)).target_x = (DRAW_START_X + AbstractCard.IMG_WIDTH_S / 2.0F + padX * i);
            ((AbstractCard) this.colorlessCards.get(i)).target_y = (9999.0F * Settings.scale);
            ((AbstractCard) this.colorlessCards.get(i)).current_y = (9999.0F * Settings.scale);
        }
    }

    public void open() {
        CardCrawlGame.sound.play("SHOP_OPEN");
        setStartingCardPositions();
        this.purgeCardY = -1000.0F;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.SHOP;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show(NAMES[12]);
        for (BloodStoreRelic r : this.relics) {
            r.hide();
        }
        for (BloodStorePotion p : this.potions) {
            p.hide();
        }
        this.rugY = Settings.HEIGHT;
        this.handX = (Settings.WIDTH / 2.0F);
        this.handY = Settings.HEIGHT;
        this.handTargetX = this.handX;
        this.handTargetY = this.handY;
        this.handTimer = 1.0F;
        this.speechTimer = 1.5F;
        this.speechBubble = null;
        this.dialogTextEffect = null;
        AbstractDungeon.overlayMenu.showBlackScreen();


        for (AbstractCard c : this.coloredCards) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }
        for (AbstractCard c : this.colorlessCards) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }
        for (BloodStoreRelic r : this.relics) {
            if (r.relic != null) {
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
            }
        }
        if (com.megacrit.cardcrawl.helpers.ModHelper.isModEnabled("Hoarder")) {
            this.purgeAvailable = false;
        }
    }

    public void update() {
        if (this.handTimer != 0.0F) {
            this.handTimer -= Gdx.graphics.getDeltaTime();
            if (this.handTimer < 0.0F) {
                this.handTimer = 0.0F;
            }
        }
        this.f_effect.update();

        this.somethingHovered = false;
        updateControllerInput();
        updatePurgeCard();
        updatePurge();
        updateRelics();
        updatePotions();
        updateRug();
        updateSpeech();
        updateCards();


        updateHand();
        AbstractCard hoveredCard = null;
        for (AbstractCard c : this.coloredCards) {
            if (c.hb.hovered) {
                hoveredCard = c;
                this.somethingHovered = true;
                moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
                break;
            }
        }

        for (AbstractCard c : this.colorlessCards) {
            if (c.hb.hovered) {
                hoveredCard = c;
                this.somethingHovered = true;
                moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
                break;
            }
        }

        if (!this.somethingHovered) {
            this.notHoveredTimer += Gdx.graphics.getDeltaTime();
            if (this.notHoveredTimer > 1.0F) {
                this.handTargetY = Settings.HEIGHT;
            }
        } else {
            this.notHoveredTimer = 0.0F;
        }


        if ((hoveredCard != null) && (InputHelper.justClickedLeft)) {
            hoveredCard.hb.clickStarted = true;
        }

        if ((hoveredCard != null) && ((InputHelper.justClickedRight) || (CInputActionSet.proceed.isJustPressed()))) {
            CardCrawlGame.cardPopup.open(hoveredCard);
        }

        if ((hoveredCard != null) && ((hoveredCard.hb.clicked) || (CInputActionSet.select.isJustPressed()))) {
            hoveredCard.hb.clicked = false;

            if (AbstractDungeon.player.maxHealth >= hoveredCard.price) {
                CardCrawlGame.metricData.addShopPurchaseData(hoveredCard.getMetricID());
                this.coloredCards.remove(hoveredCard);
                this.colorlessCards.remove(hoveredCard);
                AbstractDungeon.topLevelEffects.add(new com.megacrit.cardcrawl.vfx.FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));

                AbstractDungeon.player.decreaseMaxHealth(hoveredCard.price);
                CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);

//                if (AbstractDungeon.player.hasRelic("The Courier")) {
//                    if (hoveredCard.color == CardColor.COLORLESS) {
//                        AbstractCard.CardRarity tempRarity = AbstractCard.CardRarity.UNCOMMON;
//                        if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
//                            tempRarity = AbstractCard.CardRarity.RARE;
//                        }
//                        AbstractCard c = AbstractDungeon.getColorlessCardFromPool(tempRarity).makeCopy();
//                        if ((c.type == CardType.ATTACK) && (AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
//                            c.upgrade();
//                        } else if ((c.type == CardType.SKILL) && (AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
//                            c.upgrade();
//                        } else if ((c.type == CardType.POWER) && (AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
//                            c.upgrade();
//                        }
//                        c.current_x = hoveredCard.current_x;
//                        c.current_y = hoveredCard.current_y;
//                        c.target_x = c.current_x;
//                        c.target_y = c.current_y;
//                        setPrice(c);
//
//                        this.colorlessCards.add(c);
//                    } else {
//                        AbstractCard c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy();
//                        while (c.color == CardColor.COLORLESS) {
//
//                            c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy();
//                        }
//                        if ((c.type == CardType.ATTACK) && (AbstractDungeon.player.hasRelic("Molten Egg 2"))) {
//                            c.upgrade();
//                        } else if ((c.type == CardType.SKILL) && (AbstractDungeon.player.hasRelic("Toxic Egg 2"))) {
//                            c.upgrade();
//                        } else if ((c.type == CardType.POWER) && (AbstractDungeon.player.hasRelic("Frozen Egg 2"))) {
//                            c.upgrade();
//                        }
//                        c.current_x = hoveredCard.current_x;
//                        c.current_y = hoveredCard.current_y;
//                        c.target_x = c.current_x;
//                        c.target_y = c.current_y;
//                        setPrice(c);
//
//                        this.coloredCards.add(c);
//                    }
//                }

                hoveredCard = null;
                InputHelper.justClickedLeft = false;
                this.notHoveredTimer = 1.0F;
                this.speechTimer = MathUtils.random(40.0F, 60.0F);
                playBuySfx();
                createSpeech(getBuyMsg());
            } else {
                this.speechTimer = MathUtils.random(40.0F, 60.0F);
                playCantBuySfx();
                createSpeech(getCantBuyMsg());
            }
        }
    }

    private void updateCards() {
        for (int i = 0; i < this.coloredCards.size(); i++) {
            ((AbstractCard) this.coloredCards.get(i)).update();
            ((AbstractCard) this.coloredCards.get(i)).updateHoverLogic();
            ((AbstractCard) this.coloredCards.get(i)).current_y = (this.rugY + TOP_ROW_Y);
            ((AbstractCard) this.coloredCards.get(i)).target_y = ((AbstractCard) this.coloredCards.get(i)).current_y;
        }


        for (int i = 0; i < this.colorlessCards.size(); i++) {
            ((AbstractCard) this.colorlessCards.get(i)).update();
            ((AbstractCard) this.colorlessCards.get(i)).updateHoverLogic();
            ((AbstractCard) this.colorlessCards.get(i)).current_y = (this.rugY + BOTTOM_ROW_Y);
            ((AbstractCard) this.colorlessCards.get(i)).target_y = ((AbstractCard) this.colorlessCards.get(i)).current_y;
        }
    }


    private void setPrice(AbstractCard card) {
        float tmpPrice = AbstractCard.getPrice(card.rarity) * AbstractDungeon.merchantRng.random(0.9F, 1.1F);


        if (card.color == CardColor.COLORLESS) {
            tmpPrice *= 1.2F;
        }

//
//        if (AbstractDungeon.player.hasRelic("The Courier")) {
//            tmpPrice *= 0.8F;
//        }
//
//
//        if (AbstractDungeon.player.hasRelic("Membership Card")) {
//            tmpPrice *= 0.5F;
//        }


        card.price = ((int) tmpPrice);
    }

    public void moveHand(float x, float y) {
        this.handTargetX = (x - 50.0F * Settings.scale);
        this.handTargetY = (y + 90.0F * Settings.scale);
    }

    private static enum StoreSelectionType {
        RELIC, COLOR_CARD, COLORLESS_CARD, POTION, PURGE;

        private StoreSelectionType() {
        }
    }

    private void updateControllerInput() {
        if ((!Settings.isControllerMode) || (AbstractDungeon.topPanel.selectPotionMode) || (!AbstractDungeon.topPanel.potionUi.isHidden)) {
            return;
        }

        StoreSelectionType type = null;
        int index = 0;


        for (AbstractCard c : this.coloredCards) {
            if (c.hb.hovered) {
                type = StoreSelectionType.COLOR_CARD;
                break;
            }
            index++;
        }


        if (type == null) {
            index = 0;
            for (BloodStoreRelic r : this.relics) {
                if (r.relic.hb.hovered) {
                    type = StoreSelectionType.RELIC;
                    break;
                }
                index++;
            }
        }


        if (type == null) {
            index = 0;
            for (AbstractCard c : this.colorlessCards) {
                if (c.hb.hovered) {
                    type = StoreSelectionType.COLORLESS_CARD;
                    break;
                }
                index++;
            }
        }


        if (type == null) {
            index = 0;
            for (BloodStorePotion p : this.potions) {
                if (p.potion.hb.hovered) {
                    type = StoreSelectionType.POTION;
                    break;
                }
                index++;
            }
        }


        if ((type == null) &&
                (this.purgeHovered)) {
            type = StoreSelectionType.PURGE;
        }


        if (type == null) {
            if (!this.coloredCards.isEmpty()) {
                Gdx.input.setCursorPosition(
                        (int) ((AbstractCard) this.coloredCards.get(0)).hb.cX, Settings.HEIGHT -
                                (int) ((AbstractCard) this.coloredCards.get(0)).hb.cY);
            } else if (!this.colorlessCards.isEmpty()) {
                Gdx.input.setCursorPosition(
                        (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT -
                                (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cY);
            } else if (!this.relics.isEmpty()) {
                Gdx.input.setCursorPosition(
                        (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cX, Settings.HEIGHT -
                                (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cY);
            } else if (!this.potions.isEmpty()) {
                Gdx.input.setCursorPosition(
                        (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cX, Settings.HEIGHT -
                                (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cY);
            } else if (this.purgeAvailable) {
                Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
            }
        } else {
            switch (type) {
                case COLOR_CARD:
                    if ((CInputActionSet.left.isJustPressed()) || (CInputActionSet.altLeft.isJustPressed())) {
                        index--;
                        if (index < 0) {
                            index = 0;
                        }
                        Gdx.input.setCursorPosition(
                                (int) ((AbstractCard) this.coloredCards.get(index)).hb.cX, Settings.HEIGHT -
                                        (int) ((AbstractCard) this.coloredCards.get(index)).hb.cY);
                    } else if ((CInputActionSet.right.isJustPressed()) || (CInputActionSet.altRight.isJustPressed())) {
                        index++;
                        if (index > this.coloredCards.size() - 1) {
                            index--;
                        }
                        Gdx.input.setCursorPosition(
                                (int) ((AbstractCard) this.coloredCards.get(index)).hb.cX, Settings.HEIGHT -
                                        (int) ((AbstractCard) this.coloredCards.get(index)).hb.cY);

                    } else if ((CInputActionSet.down.isJustPressed()) || (CInputActionSet.altDown.isJustPressed())) {
                        if ((((AbstractCard) this.coloredCards.get(index)).hb.cX < 550.0F * Settings.scale) &&
                                (!this.colorlessCards.isEmpty())) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cY);
                            return;
                        }


                        if (((AbstractCard) this.coloredCards.get(index)).hb.cX < 850.0F * Settings.scale) {
                            if (!this.colorlessCards.isEmpty()) {
                                if (this.colorlessCards.size() > 1) {
                                    Gdx.input.setCursorPosition(
                                            (int) ((AbstractCard) this.colorlessCards.get(1)).hb.cX, Settings.HEIGHT -
                                                    (int) ((AbstractCard) this.colorlessCards.get(1)).hb.cY);
                                } else {
                                    Gdx.input.setCursorPosition(
                                            (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cX, Settings.HEIGHT -
                                                    (int) ((AbstractCard) this.colorlessCards.get(0)).hb.cY);
                                }
                                return;
                            }
                            if (!this.relics.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cY);
                                return;
                            }
                            if (!this.potions.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cY);
                            } else if (this.purgeAvailable) {
                                Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
                                return;
                            }
                        }

                        if (((AbstractCard) this.coloredCards.get(index)).hb.cX < 1400.0F * Settings.scale) {
                            if (!this.relics.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cY);
                                return;
                            }
                            if (!this.potions.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cY);
                            }
                        }

                        Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
                    }
                    break;
                case COLORLESS_CARD:
                    if ((CInputActionSet.left.isJustPressed()) || (CInputActionSet.altLeft.isJustPressed())) {
                        index--;
                        if (index < 0) {
                            index = 0;
                        }
                        Gdx.input.setCursorPosition(
                                (int) ((AbstractCard) this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT -
                                        (int) ((AbstractCard) this.colorlessCards.get(index)).hb.cY);
                    } else if ((CInputActionSet.right.isJustPressed()) || (CInputActionSet.altRight.isJustPressed())) {
                        index++;
                        if (index > this.colorlessCards.size() - 1) {
                            if (!this.relics.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cY);
                            } else if (!this.potions.isEmpty()) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cY);
                            } else if (this.purgeAvailable) {
                                Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
                            } else {
                                index = 0;
                            }
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.colorlessCards.get(index)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.colorlessCards.get(index)).hb.cY);
                        }

                    } else if (((CInputActionSet.up.isJustPressed()) || (CInputActionSet.altUp.isJustPressed())) &&
                            (!this.coloredCards.isEmpty())) {
                        if (((AbstractCard) this.colorlessCards.get(index)).hb.cX < 550.0F * Settings.scale) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.coloredCards.get(0)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.coloredCards.get(0)).hb.cY);
                        } else if (this.coloredCards.size() > 1) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.coloredCards.get(1)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.coloredCards.get(1)).hb.cY);
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.coloredCards.get(0)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.coloredCards.get(0)).hb.cY);
                        }
                    }

                    break;
                case RELIC:
                    if ((CInputActionSet.left.isJustPressed()) || (CInputActionSet.altLeft.isJustPressed())) {
                        index--;
                        if ((index < 0) && (!this.colorlessCards.isEmpty())) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cY);
                        }
                    } else if ((CInputActionSet.right.isJustPressed()) || (CInputActionSet.altRight.isJustPressed())) {
                        index++;
                        if ((index > this.relics.size() - 1) && (this.purgeAvailable)) {
                            Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
                        } else if (index <= this.relics.size() - 1) {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cY);
                        }
                    } else if (((CInputActionSet.down.isJustPressed()) || (CInputActionSet.altDown.isJustPressed())) &&
                            (!this.potions.isEmpty())) {
                        if (this.potions.size() - 1 >= index) {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cY);
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStorePotion) this.potions.get(0)).potion.hb.cY);
                        }
                    } else if (((CInputActionSet.up.isJustPressed()) || (CInputActionSet.altUp.isJustPressed())) &&
                            (!this.coloredCards.isEmpty())) {
                        if (this.coloredCards.size() > 3) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.coloredCards.get(2)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.coloredCards.get(2)).hb.cY);
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.coloredCards.get(0)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.coloredCards.get(0)).hb.cY);
                        }
                    }
                    break;
                case POTION:
                    if ((CInputActionSet.left.isJustPressed()) || (CInputActionSet.altLeft.isJustPressed())) {
                        index--;
                        if ((index < 0) && (!this.colorlessCards.isEmpty())) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                        } else {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cY);
                        }
                    } else if ((CInputActionSet.right.isJustPressed()) || (CInputActionSet.altRight.isJustPressed())) {
                        index++;
                        if ((index > this.potions.size() - 1) && (this.purgeAvailable)) {
                            Gdx.input.setCursorPosition((int) this.purgeCardX, Settings.HEIGHT - (int) this.purgeCardY);
                        } else if (index <= this.potions.size() - 1) {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStorePotion) this.potions.get(index)).potion.hb.cY);
                        }
                    } else if ((CInputActionSet.up.isJustPressed()) || (CInputActionSet.altUp.isJustPressed())) {
                        if (!this.relics.isEmpty()) {
                            if (this.relics.size() - 1 >= index) {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStoreRelic) this.relics.get(index)).relic.hb.cY);
                            } else {
                                Gdx.input.setCursorPosition(
                                        (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cX, Settings.HEIGHT -
                                                (int) ((BloodStoreRelic) this.relics.get(0)).relic.hb.cY);
                            }
                        } else if (!this.coloredCards.isEmpty()) {
                            if (this.coloredCards.size() > 3) {
                                Gdx.input.setCursorPosition(
                                        (int) ((AbstractCard) this.coloredCards.get(2)).hb.cX, Settings.HEIGHT -
                                                (int) ((AbstractCard) this.coloredCards.get(2)).hb.cY);
                            } else {
                                Gdx.input.setCursorPosition(
                                        (int) ((AbstractCard) this.coloredCards.get(0)).hb.cX, Settings.HEIGHT -
                                                (int) ((AbstractCard) this.coloredCards.get(0)).hb.cY);
                            }
                        }
                    }
                    break;
                case PURGE:
                    if ((CInputActionSet.left.isJustPressed()) || (CInputActionSet.altLeft.isJustPressed())) {
                        if (!this.relics.isEmpty()) {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStoreRelic) this.relics.get(this.relics.size() - 1)).relic.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStoreRelic) this.relics.get(this.relics.size() - 1)).relic.hb.cY);
                        } else if (!this.potions.isEmpty()) {
                            Gdx.input.setCursorPosition(
                                    (int) ((BloodStorePotion) this.potions.get(this.potions.size() - 1)).potion.hb.cX, Settings.HEIGHT -
                                            (int) ((BloodStorePotion) this.potions.get(this.potions.size() - 1)).potion.hb.cY);
                        } else if (this.colorlessCards.isEmpty()) {
                            Gdx.input.setCursorPosition(
                                    (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cX, Settings.HEIGHT -
                                            (int) ((AbstractCard) this.colorlessCards.get(this.colorlessCards.size() - 1)).hb.cY);
                        }
                    } else if (((CInputActionSet.up.isJustPressed()) || (CInputActionSet.altUp.isJustPressed())) &&
                            (!this.coloredCards.isEmpty())) {
                        Gdx.input.setCursorPosition(
                                (int) ((AbstractCard) this.coloredCards.get(this.coloredCards.size() - 1)).hb.cX, Settings.HEIGHT -
                                        (int) ((AbstractCard) this.coloredCards.get(this.coloredCards.size() - 1)).hb.cY);
                    }

                    break;
            }

        }
    }


    private void updatePurgeCard() {
        this.purgeCardX = (1554.0F * Settings.scale);
        this.purgeCardY = (this.rugY + BOTTOM_ROW_Y);

        if (this.purgeAvailable) {
            float CARD_W = 110.0F * Settings.scale;
            float CARD_H = 150.0F * Settings.scale;

            if ((InputHelper.mX > this.purgeCardX - CARD_W) && (InputHelper.mX < this.purgeCardX + CARD_W) && (InputHelper.mY > this.purgeCardY - CARD_H) && (InputHelper.mY < this.purgeCardY + CARD_H)) {
                this.purgeHovered = true;
                moveHand(this.purgeCardX - AbstractCard.IMG_WIDTH / 2.0F, this.purgeCardY);
                this.somethingHovered = true;
                this.purgeCardScale = Settings.scale;
            } else {
                this.purgeHovered = false;
            }

            if (!this.purgeHovered) {
                this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);

            } else {
                if ((InputHelper.justClickedLeft) || (CInputActionSet.select.isJustPressed())) {
                    CInputActionSet.select.unpress();
                    this.purgeHovered = false;
                    if (AbstractDungeon.player.maxHealth >= actualPurgeCost) {
                        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;

                        AbstractDungeon.gridSelectScreen.open(
                                CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                        .getPurgeableCards()), 1, NAMES[13], false, false, true, true);
                    } else {


                        playCantBuySfx();
                        createSpeech(getCantBuyMsg());
                    }
                }
                com.megacrit.cardcrawl.helpers.TipHelper.renderGenericTip(InputHelper.mX - 360.0F * Settings.scale, InputHelper.mY - 70.0F * Settings.scale, LABEL[0], MSG[0] + 25 + MSG[1]);
            }


        } else {
            this.purgeCardScale = MathHelper.cardScaleLerpSnap(this.purgeCardScale, 0.75F * Settings.scale);
        }
    }

    private void updateRelics() {
        for (Iterator<BloodStoreRelic> i = this.relics.iterator(); i.hasNext(); ) {
            BloodStoreRelic r = (BloodStoreRelic) i.next();
            r.update(this.rugY);
            if (r.isPurchased) {
                i.remove();
                break;
            }
        }
    }

    private void updatePotions() {
        for (Iterator<BloodStorePotion> i = this.potions.iterator(); i.hasNext(); ) {
            BloodStorePotion p = (BloodStorePotion) i.next();
            p.update(this.rugY);
            if (p.isPurchased) {
                i.remove();
                break;
            }
        }
    }

    public void createSpeech(String msg) {
        boolean isRight = MathUtils.randomBoolean();
        float x = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
        float y = Settings.HEIGHT - 380.0F * Settings.scale;
        this.speechBubble = new ShopSpeechBubble(x, y, 4.0F, msg, isRight);
        float offset_x = 0.0F;
        if (isRight) {
            offset_x = SPEECH_TEXT_R_X;
        } else {
            offset_x = SPEECH_TEXT_L_X;
        }
        this.dialogTextEffect = new SpeechTextEffect(x + offset_x, y + SPEECH_TEXT_Y, 4.0F, msg, com.megacrit.cardcrawl.ui.DialogWord.AppearEffect.BUMP_IN);
    }


    private void updateSpeech() {
        if (this.speechBubble != null) {
            this.speechBubble.update();
            if ((this.speechBubble.hb.hovered) && (this.speechBubble.duration > 0.3F)) {
                this.speechBubble.duration = 0.3F;
                this.dialogTextEffect.duration = 0.3F;
            }
            if (this.speechBubble.isDone) {
                this.speechBubble = null;
            }
        }
        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.update();
            if (this.dialogTextEffect.isDone) {
                this.dialogTextEffect = null;
            }
        }

        this.speechTimer -= Gdx.graphics.getDeltaTime();
        if ((this.speechBubble == null) && (this.dialogTextEffect == null) && (this.speechTimer <= 0.0F)) {
            this.speechTimer = MathUtils.random(40.0F, 60.0F);
            if (!this.saidWelcome) {
                createSpeech(WELCOME_MSG);
                this.saidWelcome = true;
                welcomeSfx();
            } else {
                playMiscSfx();
                createSpeech(getIdleMsg());
            }
        }
    }

    private void welcomeSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }
    }

    private void playMiscSfx() {
        int roll = MathUtils.random(5);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_MA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_MB");
        } else if (roll == 2) {
            CardCrawlGame.sound.play("VO_MERCHANT_MC");
        } else if (roll == 3) {
            CardCrawlGame.sound.play("VO_MERCHANT_3A");
        } else if (roll == 4) {
            CardCrawlGame.sound.play("VO_MERCHANT_3B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_3C");
        }
    }

    public void playBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_KA");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_KB");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_KC");
        }
    }

    public void playCantBuySfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_MERCHANT_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_MERCHANT_2B");
        } else {
            CardCrawlGame.sound.play("VO_MERCHANT_2C");
        }
    }

    private String getIdleMsg() {
        return (String) this.idleMessages.get(MathUtils.random(this.idleMessages.size() - 1));
    }

    private void updateRug() {
        if (this.rugY != 0.0F) {
            this.rugY = MathUtils.lerp(this.rugY, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
            if (Math.abs(this.rugY - 0.0F) < 0.5F) {
                this.rugY = 0.0F;
            }
        }
    }

    private void updateHand() {
        if (this.handTimer == 0.0F) {
            if (this.handX != this.handTargetX) {
                this.handX = MathUtils.lerp(this.handX, this.handTargetX, Gdx.graphics.getDeltaTime() * 6.0F);
            }
            if (this.handY != this.handTargetY) {
                if (this.handY > this.handTargetY) {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F);
                } else {
                    this.handY = MathUtils.lerp(this.handY, this.handTargetY, Gdx.graphics.getDeltaTime() * 6.0F / 4.0F);
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(rugImg, 0.0F, this.rugY, Settings.WIDTH, Settings.HEIGHT);

        renderCardsAndPrices(sb);
        renderRelics(sb);
        renderPotions(sb);
        renderPurge(sb);


        sb.draw(handImg, this.handX + this.f_effect.x, this.handY + this.f_effect.y, HAND_W, HAND_H);

        if (this.speechBubble != null) {
            this.speechBubble.render(sb);
        }
        if (this.dialogTextEffect != null) {
            this.dialogTextEffect.render(sb);
        }
    }

    private void renderRelics(SpriteBatch sb) {
        for (BloodStoreRelic r : this.relics) {
            r.render(sb);
        }
    }

    private void renderPotions(SpriteBatch sb) {
        for (BloodStorePotion p : this.potions) {
            p.render(sb);
        }
    }


    private void renderCardsAndPrices(SpriteBatch sb) {
        for (AbstractCard c : this.coloredCards) {
            c.render(sb);


            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);


            Color color = Color.WHITE.cpy();
            if (c.price > AbstractDungeon.player.maxHealth) {
                color = Color.SALMON.cpy();
            }
//            else if (c.equals(this.saleTag.card)) {
//                color = Color.SKY.cpy();
//            }


            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,


                    Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color);
        }


        for (AbstractCard c : this.colorlessCards) {
            c.render(sb);


            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, c.current_x + GOLD_IMG_OFFSET_X, c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);


            Color color = Color.WHITE.cpy();
            if (c.price > AbstractDungeon.player.maxHealth) {
                color = Color.SALMON.cpy();
            }
//            else if (c.equals(this.saleTag.card)) {
//                color = Color.SKY.cpy();
//            }


            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,


                    Integer.toString(c.price), c.current_x + PRICE_TEXT_OFFSET_X, c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale, color);
        }


//        if (this.coloredCards.contains(this.saleTag.card)) {
//            this.saleTag.render(sb);
//        }
//        if (this.colorlessCards.contains(this.saleTag.card)) {
//            this.saleTag.render(sb);
//        }


        for (AbstractCard c : this.coloredCards) {
            c.renderCardTip(sb);
        }


        for (AbstractCard c : this.colorlessCards) {
            c.renderCardTip(sb);
        }
    }


    private void renderPurge(SpriteBatch sb) {
        sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.25F));
        sb.draw(ImageMaster.CARD_SKILL_BG_SILHOUETTE, this.purgeCardX - 256.0F + 18.0F * Settings.scale, this.purgeCardY - 256.0F - 14.0F * Settings.scale, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);


        sb.setColor(Color.WHITE);
        if (this.purgeAvailable) {
            sb.draw(removeServiceImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);


            sb.draw(ImageMaster.TP_HP, this.purgeCardX + GOLD_IMG_OFFSET_X, this.purgeCardY + GOLD_IMG_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);


            Color color = Color.WHITE;
            if (actualPurgeCost > AbstractDungeon.player.maxHealth) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,


                    Integer.toString(actualPurgeCost), this.purgeCardX + PRICE_TEXT_OFFSET_X, this.purgeCardY + PRICE_TEXT_OFFSET_Y - (this.purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale, color);


        } else {

            sb.draw(soldOutImg, this.purgeCardX - 256.0F, this.purgeCardY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.purgeCardScale, this.purgeCardScale, 0.0F, 0, 0, 512, 512, false, false);
        }
    }
}