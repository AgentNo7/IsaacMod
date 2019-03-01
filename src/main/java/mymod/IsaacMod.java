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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.shop.ShopScreen;
import patches.event.AddEventPatch;
import patches.player.PlayerDamagePatch;
import patches.ui.SoulHeartPatch;
import powers.ConceitedPower;
import relics.*;
import relics.Void;
import rewards.Heart;
import rewards.Pill;
import rewards.SoulHeart;
import screen.BloodShopScreen;
import screen.RelicSelectScreen;
import utils.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static patches.ui.JudasPatch.haveJudas;
import static rewards.HeartRewardPatch.HEART;
import static utils.Utils.removeRelicString;


@SpireInitializer
public class IsaacMod implements EditCardsSubscriber, EditRelicsSubscriber, PostDungeonInitializeSubscriber, EditStringsSubscriber, PreUpdateSubscriber, RenderSubscriber, PostUpdateSubscriber , PostInitializeSubscriber{//PostInitializeSubscriber EditCharactersSubscriber

    public static String[] relicsString = {
            AnarchistCookbook.ID,
            BFFS.ID,
            BelialBook.ID,
            BloodDonationMachine.ID,
            BookOfRevelations.ID,
            BookofShadows.ID,
            Chaos.ID,
            D6.ID,
            DeadEye.ID,
            DeathBook.ID,
            DoctorsRemote.ID,
            GnawedLeaf.ID,
            GuppysHead.ID,
            GuppysPaw.ID,
            GuppysTail.ID,
            HowToJump.ID,
            Leech.ID,
            MaYiHuaBei.ID,
            MoneyPower.ID,
            PillsDrop.ID,
            PokeGo.ID,
            PunchingBag.ID,
            SalivaCoin.ID,
            SatanicBible.ID,
            SharpPlug.ID,
            SteamSale.ID,
            TheBean.ID,
            TheBible.ID,
            TuHaoJin.ID,
            UnicornStump.ID,
            Void.ID,
            YumHeart.ID
    };
    public static String[] cardsString = {BettingIsNotGood.ID, BloodyBrimstone.ID, DoctorFetus.ID, EpicFetus.ID,
            ChaosCard.ID,
            SuicideKing.ID,
            Bomb.ID
    };

    public static String[] inPoolRelic = {
            KeepersGift.ID,
            ForgetMeNow.ID, BlankCard.ID, Diplopia.ID, EdensBlessing.ID,
            GuppysCollar.ID,
            GuppysHairball.ID,
            D4.ID
    };

    public static String[] bossRelic = {MagicMushroom.ID, ChampionBelt.ID};
    public static String[] devilRelic = {GuppysHead.ID, GuppysTail.ID, GuppysCollar.ID, GuppysHairball.ID, GuppysPaw.ID,
            SatanicBible.ID,
            DeathBook.ID,
            BelialBook.ID
    };
    public static String[] deviOnlyRelic = {Incubus.ID, MegaBlast.ID,
            Succubus.ID,
            MomsKnife.ID,
            NineLifeCat.ID,
            JudasShadow.ID,
            RottenBaby.ID,
            MyShadow.ID
    };

    public static List<String> relics = new ArrayList<>(Arrays.asList(relicsString));
    public static List<String> inPoolRelics = new ArrayList<>(Arrays.asList(inPoolRelic));
    public static List<String> cards = new ArrayList<>(Arrays.asList(cardsString));
    public static List<String> bossRelics = new ArrayList<>(Arrays.asList(bossRelic));
    public static List<String> devilRelics = new ArrayList<>(Arrays.asList(devilRelic));
    public static List<String> devilOnlyRelics = new ArrayList<>(Arrays.asList(deviOnlyRelic));

    public IsaacMod() {
    }

    public static void initialize() {
        BaseMod.subscribe(new IsaacMod());
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
        BaseMod.addRelic(new HushsDoor(), RelicType.SHARED);
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
    }

    public void receiveEditStrings() {
        String LOCALIZATION_FOLDER;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            LOCALIZATION_FOLDER = "isaacLocalization/zhs";
        } else {
            LOCALIZATION_FOLDER = "isaacLocalization/eng";
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
        HushsDoor.isMonstro = false;
        HushsDoor.lastDungeon = null;
        haveJudas = false;
        PokeGo.slotNum = 0;
        PlayerDamagePatch.resurrectRelics.clear();
    }

    /**
     * todo 初始化在这里
     */
    public void receivePostDungeonInitialize() {
        if (!AbstractDungeon.player.hasRelic(HushsDoor.ID)) {
            //各种参数初始化
            preSettings();
            //初始遗物赠送
            if (AbstractDungeon.currMapNode != null) {
                obtain(AbstractDungeon.player, HushsDoor.ID, false);
                obtain(AbstractDungeon.player, KeepersGift.ID, false);
            } else {
                HushsDoor hushsDoor = new HushsDoor();
                obtain(AbstractDungeon.player, hushsDoor, false, false);
                hushsDoor.onEquip();
                obtain(AbstractDungeon.player, KeepersGift.ID, false, false);
            }
//            obtain(AbstractDungeon.player, Void.ID, true);
//            obtain(AbstractDungeon.player, DeathBook.ID, true);
//            obtain(AbstractDungeon.player, PokeGo.ID, false);
//            obtain(AbstractDungeon.player, D6.ID, false);
//            obtain(AbstractDungeon.player, D4.ID, false);
//            obtain(AbstractDungeon.player, YumHeart.ID, false);
//            obtain(AbstractDungeon.player, BookOfRevelations.ID, false);
//            obtain(AbstractDungeon.player, new LizardTail(), false);
//            obtain(AbstractDungeon.player, new TestRelic(), false);
            AbstractDungeon.uncommonCardPool.addToBottom(new Bomb());
            AbstractDungeon.commonCardPool.addToBottom(new Bomb());
            doDaily("20190221");
            doDaily("20190222");
            //初始卡牌赠送
//            AbstractDungeon.player.masterDeck.addToBottom(new SuicideKing());
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
        }
    }

    private static boolean receivedCard = false;

    public static List<AbstractRelic> obtainRelics = new ArrayList<>();
    public static List<AbstractRelic> removeRelics = new ArrayList<>();


    @Override
    public void receivePostUpdate() {
        //初始卡牌赠送
        if (!receivedCard && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.floorNum < 1) {
            receivedCard = true;
            RewardItem cardReward = new RewardItem();
            cardReward.cards.clear();
            cardReward.cards.add(getRandomCards(1).get(0));
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.getCurrRoom().rewards.add(cardReward);
            AbstractDungeon.combatRewardScreen.open("Select or Not?");
            if (AbstractDungeon.combatRewardScreen.rewards.size() > 1) {
                AbstractDungeon.combatRewardScreen.rewards.remove(1);
            }
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 1.0F;
        }
        if (obtainRelics.size() > 0) {
            for (AbstractRelic relic : obtainRelics) {
                obtain(AbstractDungeon.player, relic, true);
            }
            obtainRelics.clear();
        }
        if (removeRelics.size() > 0) {
            for (AbstractRelic relic : removeRelics) {
                AbstractDungeon.player.loseRelic(relic.relicId);
            }
            removeRelics.clear();
        }
        if (AbstractDungeon.player != null) {
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
                        obtain(AbstractDungeon.player, returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE), true);
                    }
                    for (int i = 0; i < d4.uncommon; i++) {
                        obtain(AbstractDungeon.player, returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON), true);
                    }
                    for (int i = 0; i < d4.common; i++) {
                        obtain(AbstractDungeon.player, returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON), true);
                    }
                    for (int i = 0; i < d4.boss; i++) {
                        AbstractRelic relic = returnRandomScreenlessRelic(AbstractRelic.RelicTier.BOSS);
                        while (relic instanceof CallingBell) {
                            relic = returnRandomScreenlessRelic(AbstractRelic.RelicTier.BOSS);
                        }
                        obtain(AbstractDungeon.player, relic, false);
                    }
                    for (int i = 0; i < d4.shop; i++) {
                        obtain(AbstractDungeon.player, returnRandomScreenlessRelic(AbstractRelic.RelicTier.SHOP), true);
                    }
                    for (int i = 0; i < d4.devilOnlyRelic; i++) {
                        obtain(AbstractDungeon.player, Utils.getRandomRelicRng(devilOnlyRelics), true);
                    }
                    d4.init();
                    AbstractDungeon.player.reorganizeRelics();
                    getScreen = false;
                }
            }
        }
    }

    private static boolean getScreen = false;

    public static AbstractRelic returnRandomScreenlessRelic(final AbstractRelic.RelicTier tier) {
        AbstractRelic tmpRelic;
        if (!getScreen) {
            tmpRelic= AbstractDungeon.returnRandomRelic(tier);
            if (Objects.equals(tmpRelic.relicId, "Bottled Flame") || Objects.equals(tmpRelic.relicId, "Bottled Lightning") || Objects.equals(tmpRelic.relicId, "Bottled Tornado")) {
                getScreen = true;
            }
        } else {
            tmpRelic = AbstractDungeon.returnRandomScreenlessRelic(tier);
        }
        return tmpRelic;
    }

    public static boolean obtain(AbstractPlayer p, String relicId, boolean canDuplicate) {
        AbstractRelic r = getRelic(relicId);
        if (r == null) return false;
        removeRelicString(relicId);
        Utils.removeFromPool(r);
        return obtain(p, r, canDuplicate, true);
    }

    public static boolean obtain(AbstractPlayer p, String relicId, boolean canDuplicate, boolean callOnEquip) {
        AbstractRelic r = getRelic(relicId);
        if (r == null) return false;
        removeRelicString(relicId);
        Utils.removeFromPool(r);
        return obtain(p, r, canDuplicate, callOnEquip);
    }


    public static boolean obtain(AbstractPlayer p, AbstractRelic r, boolean canDuplicate) {
        return obtain(p, r, canDuplicate, true);
    }

    public static boolean obtain(AbstractPlayer p, AbstractRelic r, boolean canDuplicate, boolean callOnEquip) {
        if (r == null) {
            return false;
        } else if (p.hasRelic(r.relicId) && !canDuplicate) {
            return false;
        } else {
            int slot = p.relics.size();
            r.makeCopy().instantObtain(p, slot, callOnEquip);
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

    private static void doDaily() {
        doDaily(null);
    }

    private static void doDaily(String specificDay) {
        File date = new File(".date");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String theDay = sdf.format(today);
        if (!theDay.equals(specificDay)) {
            return;
        }
        if (!date.exists()) {
            try {
                date.createNewFile();
                doGetRelic(new FileWriter(date), theDay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(date));
                String thisDay = reader.readLine();
                if (!theDay.equals(thisDay)) {
                    doGetRelic(new FileWriter(date), theDay);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void doGetRelic(FileWriter writer, String theDay) {
        try {
            writer.write(theDay);
            writer.flush();
            writer.close();
            List<AbstractRelic> all = new ArrayList<>();
//            all.addAll(inPoolRelics);
//            all.addAll(bossRelics);
//            all.addAll(devilOnlyRelics);
            all.add(new PokeGo());
            int n = new Random(Integer.parseInt(theDay)).nextInt(all.size());
            DailyIntro dailyIntro = new DailyIntro(all.get(n), 3);
            obtain(AbstractDungeon.player, dailyIntro, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.registerCustomReward(HEART, rewardSave -> Heart.getHeartById(rewardSave.id, rewardSave.amount), customReward -> new RewardSave(customReward.type.toString(), ((Heart)customReward).id, ((Heart)customReward).amount, 0));
    }
}
