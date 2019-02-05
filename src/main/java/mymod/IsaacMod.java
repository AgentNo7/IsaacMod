package mymod;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import cards.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.shop.ShopScreen;
import patches.event.AddEventPatch;
import patches.ui.SoulHeartPatch;
import powers.ConceitedPower;
import relics.*;
import rewards.Pill;
import screen.BloodShopScreen;
import screen.RelicSelectScreen;
import utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static utils.Utils.removeFromPool;
import static utils.Utils.removeRelicString;


@SpireInitializer
public class IsaacMod implements EditCardsSubscriber, EditRelicsSubscriber, PostDungeonInitializeSubscriber, EditStringsSubscriber, PreUpdateSubscriber, RenderSubscriber, PostUpdateSubscriber {//PostInitializeSubscriber EditCharactersSubscriber

    public static String[] relicsString = {
            "AnarchistCookbook",
            BFFS.ID,
            "BelialBook",
//            "BloodDonationBag",
            "BloodDonationMachine",
            "BookOfRevelations",
            "BookofShadows",
            Chaos.ID,
            "D4",
            "D6",
            "DeadEye",
            "DeathBook",
            "DoctorsRemote",
            "GnawedLeaf",
            GuppysHead.ID,
            GuppysPaw.ID,
            GuppysTail.ID,
            "HushsDoor",
            "HowToJump",
//            "KonoKeeper",
            "MaYiHuaBei",
            "MoneyPower",
            "PillsDrop",
            PokeGo.ID,
            PunchingBag.ID,
            "SalivaCoin",
            "SatanicBible",
            "KeepersGift",
            "SharpPlug",
            "SteamSale",
            //"TestRelic",
            "TheBean",
            "TheBible",
            "TuHaoJin",
            "UnicornStump",
            "YumHeart"};
    public static String[] cardsString = {"BettingIsNotGood", "BloodyBrimstone", "DoctorFetus", "EpicFetus",
            "ChaosCard",
            "SuicideKing",
            "Bomb"};

    public static String[] inPoolRelic = {ForgetMeNow.ID, BlankCard.ID, Diplopia.ID, EdensBlessing.ID,
            GuppysCollar.ID,
            GuppysHairball.ID,
            NineLifeCat.ID
    };

    public static String[] bossRelic = {MagicMushroom.ID, ChampionBelt.ID};
    public static String[] devilRelic = {GuppysHead.ID, GuppysTail.ID, GuppysCollar.ID, GuppysHairball.ID, GuppysPaw.ID, NineLifeCat.ID,
            SatanicBible.ID,
            DeathBook.ID,
            RottenBaby.ID,
            BelialBook.ID
    };
    public static String[] deviOnlyRelic = {Incubus.ID, MegaBlast.ID,
            Succubus.ID,
            MomsKnife.ID,
            RottenBaby.ID
    };

    public static List<String> relics = new ArrayList<>(Arrays.asList(relicsString));
    public static List<String> inPoolRelics = new ArrayList<>(Arrays.asList(inPoolRelic));
    public static List<String> cards = new ArrayList<>(Arrays.asList(cardsString));
    public static List<String> bossRelics = new ArrayList<>(Arrays.asList(bossRelic));
    public static List<String> devilRelics = new ArrayList<>(Arrays.asList(devilRelic));
    public static List<String> devilOnlyRelics = new ArrayList<>(Arrays.asList(deviOnlyRelic));

    public IsaacMod() {
    }

//    public static final Color GOLD = CardHelper.getColor(255.0F, 205.0F, 56.0F);

    public static void initialize() {
        BaseMod.subscribe(new IsaacMod());
//        BaseMod.addColor(CardEnum.POKEMON, GOLD, GOLD, GOLD, GOLD, GOLD, GOLD, GOLD, "images/cardui/512/bg_attack_red.png", "images/cardui/512/bg_skill_red.png", "images/cardui/512/bg_power_red.png", "images/cardui/512/card_red_orb.png", "images/cardui/1024/bg_attack_red.png", "images/cardui/1024/bg_skill_red.png", "images/cardui/1024/bg_power_red.png", "images/cardui/1024/card_red_orb.png");
    }

    private static AbstractRelic getRelic(String name) {
        Class c;
        try {
            c = Class.forName("relics." + name);
            return (CustomRelic) c.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void receiveEditRelics() {
        BaseMod.addRelic(new BloodDonationBag(), RelicType.SHARED);
        for (String relic : relics) {
            BaseMod.addRelic(getRelic(relic), RelicType.SHARED);
        }
        for (String relic : inPoolRelics) {
            BaseMod.addRelic(getRelic(relic), RelicType.SHARED);
        }
        for (String relic : bossRelics) {
            BaseMod.addRelic(getRelic(relic), RelicType.SHARED);
        }
        for (String relic : devilOnlyRelics) {
            BaseMod.addRelic(getRelic(relic), RelicType.SHARED);
        }
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Battery());
        BaseMod.addCard(new BettingIsNotGood());
        BaseMod.addCard(new DoctorFetus());
        BaseMod.addCard(new BloodyBrimstone());
        BaseMod.addCard(new EpicFetus());
        BaseMod.addCard(new Distribution());
        BaseMod.addCard(new Cancel());
        BaseMod.addCard(new ElectricPowerLearning());
        BaseMod.addCard(new Pulse());
        BaseMod.addCard(new Bomb());
        BaseMod.addCard(new ChaosCard());
        BaseMod.addCard(new SuicideKing());

//        //PokeMod
//        BaseMod.addCard(new TakeTurn());
//
//        BaseMod.addCard(new Disarm_Poke());//un
//        BaseMod.addCard(new Heal());//common
//
//        BaseMod.addCard(new Defend_Poke());//common
//        BaseMod.addCard(new Entrench_Poke());//un
//        BaseMod.addCard(new LimitBreak_Poke());//rare
//
//        BaseMod.addCard(new Combust_Poke());//un
//        BaseMod.addCard(new Evolve_Poke());//un
//        BaseMod.addCard(new Barricade_Poke());//rare
//        BaseMod.addCard(new KaKaForm());//rare
//        BaseMod.addCard(new HealForm());//rare
//
//        BaseMod.addCard(new Strike_Poke());//common
//        BaseMod.addCard(new LittleStrike_Poke());//un
//        BaseMod.addCard(new Suck());//rare
    }

    public void receiveEditStrings() {
        String LOCALIZATION_FOLDER;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            LOCALIZATION_FOLDER = "localization/zhs";
        } else {
            LOCALIZATION_FOLDER = "localization/eng";
        }

        String relicStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "relic_strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String cardStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "card-strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "power-strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String eventStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "event-strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
//        String uiStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "future_UI.json")).readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
//        String characterStrings = Gdx.files.internal(makePath(LOCALIZATION_FOLDER, "future_characters.json")).readString(String.valueOf(StandardCharsets.UTF_8));
//        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
    }

    public static String makePath(String folder, String resource) {
        return folder + "/" + resource;
    }

    private List<AbstractCard> getRandomCards(int num) {
        List<AbstractCard> getCards = new ArrayList<>();
        try {
            String[] rnd = Utils.getRandomCards(cards.size(), num);
            for (String card : rnd) {
                Class c = Class.forName("cards." + card);
                getCards.add((AbstractCard) c.newInstance());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("你没找到类");
            e.printStackTrace();
        }
        return getCards;
    }

    private void preSettings() {
        //各种参数初始化
        receivedCard = false;
        removeRelicString(HushsDoor.ID);
        removeRelicString(KeepersGift.ID);
        relics = new ArrayList<>(Arrays.asList(relicsString));
        inPoolRelics = new ArrayList<>(Arrays.asList(inPoolRelic));
        cards = new ArrayList<>(Arrays.asList(cardsString));
        bossRelics = new ArrayList<>(Arrays.asList(bossRelic));
        devilRelics = new ArrayList<>(Arrays.asList(devilRelic));
        devilOnlyRelics = new ArrayList<>(Arrays.asList(deviOnlyRelic));
        BloodDonationMachine.canUse = true;
        BloodDonationBag.canUse = true;
        BloodDonationMachine.broken = false;
        BloodDonationMachine.isBag = false;
        AddEventPatch.hidenRoomTimes = 0;
        Pill.forgotColor = Pill.getRandomColor();
        Utils.removeFromPool(new BloodDonationBag());
        if (AbstractDungeon.shopScreen instanceof BloodShopScreen) {
            AbstractDungeon.shopScreen = new ShopScreen();
        }
        BloodShopScreen.resetPurgeCost();
        SoulHeartPatch.soulHeart = 0;
        SoulHeartPatch.blackHeart = 0;
        ConceitedPower.canSee = true;
        HushsDoor.guppyCount = 0;
        HushsDoor.bookCount = 0;
    }

    /**
     * todo 初始化在这里
     */
    public void receivePostDungeonInitialize() {
        if (!AbstractDungeon.player.hasRelic(HushsDoor.ID)) {
            //各种参数初始化
            preSettings();
            //初始遗物赠送
            obtain(AbstractDungeon.player, HushsDoor.ID, false);
            obtain(AbstractDungeon.player, KeepersGift.ID, false);
//            obtain(AbstractDungeon.player, Chaos.ID, false);
            AbstractDungeon.uncommonCardPool.addToBottom(new Bomb());
            AbstractDungeon.commonCardPool.addToBottom(new Bomb());
//            obtain(AbstractDungeon.player, PokeGo.ID, true);
//            obtain(AbstractDungeon.player, new FusionHammer(), true);
//            obtain(AbstractDungeon.player, new RottenBaby(), true);
//            obtain(AbstractDungeon.player, new FossilizedHelix(), true);
//            obtain(AbstractDungeon.player, TestRelic.ID, false);
//            obtain(AbstractDungeon.player, ChampionBelt.ID, false);
//            obtain(AbstractDungeon.player, ForgetMeNow.ID, false);
//            obtain(AbstractDungeon.player, TestRelic.ID, false);
//            obtain(AbstractDungeon.player, D6.ID, false);
//            obtain(AbstractDungeon.player, MagicMushroom.ID, true);
//            obtain(AbstractDungeon.player, GuppysPaw.ID, false);
//            obtain(AbstractDungeon.player, GuppysHairball.ID, false);
            //初始卡牌赠送
//        ElectricPowerLearning electricPowerLearning = new ElectricPowerLearning();
//        AbstractDungeon.player.masterDeck.addToBottom(electricPowerLearning);
//            for (int i = 0; i < 20; i++) {
//                AbstractDungeon.player.masterDeck.addToBottom(new Pummel());
//            }
//            AbstractDungeon.player.masterDeck.addToBottom(new Battery());
//            AbstractDungeon.player.masterDeck.addToBottom(new BloodyBrimstone());
            //牌库卡牌添加
            //事件添加
//        for (int i = 0; i < 100; i++) {
//            BaseMod.addEvent(HidenRoomEvent.ID, HidenRoomEvent.class, "" + i);
//        }
//            if (AbstractDungeon.player instanceof Defect) {
//                AbstractDungeon.removeCardFromPool(Electrodynamics.ID, Electrodynamics.NAME, Electrodynamics.CardRarity.RARE, AbstractCard.CardColor.BLUE);
//                AbstractCard[] cards = {new ElectricPowerLearning(), new Distribution(), new Cancel(), new Pulse()};
//                AbstractDungeon.player.masterDeck.addToBottom(cards[new Random().nextInt(cards.length)]);
//            }
            //初始玫瑰赠送
            if (EdensBlessing.obtained) {
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                while (relic instanceof OldCoin || !relic.canSpawn()) {
                    relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                }
                obtain(AbstractDungeon.player, relic, false);
                Utils.removeFromPool(relic);
                EdensBlessing.obtained = false;
            }
        } else {
            int cnt = 0;
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (AscendersBane.ID.equals(card.cardID)) {
                    cnt++;
                }
            }
            while (cnt > 1) {
                AbstractDungeon.player.masterDeck.removeCard(AscendersBane.ID);
                cnt--;
            }
        }
    }

    private boolean receivedCard = false;

    @Override
    public void receivePostUpdate() {
        //初始卡牌赠送
        //AbstractDungeon.combatRewardScreen.rewards.add(/*EL:59*/new RewardItem(new TinyHouse()));
        if (!receivedCard && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.floorNum < 1) {
            receivedCard = true;
            RewardItem cardReward = new RewardItem();
            cardReward.cards.clear();
            cardReward.cards.add(getRandomCards(1).get(0));
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.getCurrRoom().rewards.add(cardReward);
            AbstractDungeon.combatRewardScreen.open("Select or Not?");
            AbstractDungeon.combatRewardScreen.rewards.remove(1);
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
        }
        if (AbstractDungeon.player != null) {
            BloodDonationMachine machine = (BloodDonationMachine) AbstractDungeon.player.getRelic(BloodDonationMachine.ID);
            if (machine != null && BloodDonationMachine.broken && BloodDonationMachine.isBag) {
                if (AbstractDungeon.player.getRelic("BloodDonationBag") == null) {
                    obtain(AbstractDungeon.player, new BloodDonationBag(), false);
                }
            }
            Diplopia diplopia = (Diplopia) AbstractDungeon.player.getRelic(Diplopia.ID);
            if (diplopia != null && !Diplopia.used && diplopia.addRelic != null) {
                obtain(AbstractDungeon.player, diplopia.addRelic.makeCopy(), true);
                AbstractDungeon.player.loseRelic(diplopia.relicId);
                diplopia.addRelic = null;
                Diplopia.used = true;
            }
            if (AbstractDungeon.player.hasRelic(KeepersGift.ID)) {
                KeepersGift keepersGift = (KeepersGift) AbstractDungeon.player.getRelic(KeepersGift.ID);
                if (keepersGift.addRelic != null) {
                    AbstractDungeon.player.loseRelic(keepersGift.relicId);
                    removeFromPool(keepersGift.addRelic.makeCopy());
                    obtain(AbstractDungeon.player, keepersGift.addRelic.makeCopy(), false);
                    removeRelicString(keepersGift.addRelic.relicId);
                    keepersGift.addRelic = null;
                }
            }
            if (AbstractDungeon.player.hasRelic(D4.ID)) {
                D4 d4 = (D4) AbstractDungeon.player.getRelic(D4.ID);
                if (d4.roll) {
                    d4.roll = false;
                    Iterator<AbstractRelic> iterator = AbstractDungeon.player.relics.iterator();
                    while (iterator.hasNext()) {
                        AbstractRelic relic = iterator.next();
                        if (!(relic instanceof HushsDoor) && !(relic instanceof D4) && !(relic instanceof NineLifeCat)) {
                            relic.onUnequip();
                            iterator.remove();
                        }
                    }
                    for (int i = 0; i < d4.starter; i++) {
                        int rnd = AbstractDungeon.relicRng.random(0, 2);
                        AbstractRelic relic = null;
                        switch (rnd) {
                            case 0:
                                relic = new BurningBlood();
                                break;
                            case 1:
                                relic = new SnakeRing();
                                break;
                            case 2:
                                relic = new CrackedCore();
                                break;
                        }
                        obtain(AbstractDungeon.player, relic, false);
                    }
                    for (int i = 0; i < d4.rare + d4.special; i++) {
                        obtain(AbstractDungeon.player, AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE), true);
                    }
                    for (int i = 0; i < d4.uncommon; i++) {
                        obtain(AbstractDungeon.player, AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON), true);
                    }
                    for (int i = 0; i < d4.common; i++) {
                        obtain(AbstractDungeon.player, AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON), true);
                    }
                    for (int i = 0; i < d4.boss; i++) {
                        AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
                        while (relic instanceof CallingBell) {
                            relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
                        }
                        obtain(AbstractDungeon.player, relic, false);
                    }
                    for (int i = 0; i < d4.shop; i++) {
                        obtain(AbstractDungeon.player, AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.SHOP), true);
                    }
                    for (int i = 0; i < d4.devilOnlyRelic; i++) {
                        obtain(AbstractDungeon.player, Utils.getRandomRelicRng(devilOnlyRelics), true);
                    }
                    d4.init();
                    AbstractDungeon.player.reorganizeRelics();
                }
            }
        }
    }

    public static boolean obtain(AbstractPlayer p, String relicId, boolean canDuplicate) {
        AbstractRelic r = getRelic(relicId);
        if (r == null) return false;
        removeRelicString(relicId);
        Utils.removeFromPool(r);
        return obtain(p, r, canDuplicate);
    }


    public static boolean obtain(AbstractPlayer p, AbstractRelic r, boolean canDuplicate) {
        if (r == null) {
            return false;
        } else if (p.hasRelic(r.relicId) && !canDuplicate) {
            return false;
        } else {
            int slot = p.relics.size();
            r.makeCopy().instantObtain(p, slot, true);
            return true;
        }
    }

    @Override
    public void receivePreUpdate() {
        RelicSelectScreen.updateScreen();
    }

    @Override
    public void receiveRender(SpriteBatch spriteBatch) {
        RelicSelectScreen.updateRender(spriteBatch);
    }

//    private void addEvent() {
////        Class[] events = new Class[]{HidenRoomEvent.class};
////        BaseMod.addEvent(HidenRoomEvent.ID, HidenRoomEvent.class);
////        BaseMod.addEvent(HidenRoomEvent.ID, HidenRoomEvent.class, "Exordium");
////        BaseMod.addEvent(HidenRoomEvent.ID, HidenRoomEvent.class, "TheCity");
////        BaseMod.addEvent(HidenRoomEvent.ID, HidenRoomEvent.class, "TheBeyond");
//    }

//    @Override
//    public void receivePostInitialize() {
//        addEvent();
//    }


//    public void receiveEditCharacters() {
//        BaseMod.addCharacter(new AshKetchum("Ash Ketchum"), "images/ui/charSelect/AshKetchumButton.png", "images/ui/charSelect/AshKetchumPortrait.jpg", ModClassEnum.AshKetchum);
//    }
}
