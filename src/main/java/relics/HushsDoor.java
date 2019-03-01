package relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import helpers.MinionHelper;
import helpers.SummonHelper;
import monsters.LittleHush;
import monsters.Monstro;
import monsters.pet.Fly;
import patches.player.PlayerDamagePatch;
import powers.BlankCardPower;
import powers.FlightPower;
import relics.abstracrt.ClickableRelic;
import relics.abstracrt.ResurrectRelic;
import rewards.BlackHeart;
import rewards.SoulHeart;
import room.MonsterRoomMyBoss;
import screen.BloodShopScreen;
import utils.Invoker;
import utils.Point;
import utils.SoulHeartSave;
import utils.Utils;

import static patches.ui.JudasPatch.haveJudas;
import static patches.ui.SoulHeartPatch.blackHeart;
import static patches.ui.SoulHeartPatch.soulHeart;

public class HushsDoor extends ClickableRelic implements CustomSavable<SoulHeartSave> {
    public static final String ID = "HushsDoor";
    public static final String IMG = "images/relics/HushsDoor.png";
    public static final String DESCRIPTION = "通往Hush（凹凸）的关键。右击可切换是否挑战。";

    public static boolean toHush = true;

    public static boolean isOn = false;

    @Override
    public void onRightClick() {
        toHush = !toHush;
        if (toHush) {
            this.img = ImageMaster.loadImage("images/relics/HushsDoor.png");
        } else {
            this.img = ImageMaster.loadImage("images/relics/HushsDoorBroken.png");
        }
    }

    private static boolean talk = true;

    public HushsDoor() {
        super("HushsDoor", new Texture(Gdx.files.internal(toHush ? "images/relics/HushsDoor.png" : "images/relics/HushsDoorBroken.png")), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new HushsDoor();
    }

    /**
     * 把心脏变成凹凸
     */
    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (enteredBloodShop) {
            AbstractDungeon.shopScreen = new ShopScreen();
            enteredBloodShop = false;
            isOn = false;
        }
        makeHeartToHush();
//        makeAllToHush();
        makeBossToMonstro();
    }

    @Override
    public void onEquip() {
        super.onEquip();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
    }

    private void makeHeartToHush() {
        if (AbstractDungeon.floorNum < 53) {
            return;
        }
        if (toHush && AbstractDungeon.nextRoom.room instanceof MonsterRoomBoss) {
            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new LittleHush(-50.0F, 30.0F)));
            AbstractDungeon.lastCombatMetricKey = "LittleHush";
        }
    }

    private void makeAllToHush() {
        if (AbstractDungeon.nextRoom.room instanceof MonsterRoom) {
//            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new LittleHush(-50.0F, 30.0F)));
//            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new Nemesis()));
            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new TheGuardian()));

//            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new Monstro(-50.0F, 30.0F)));
            AbstractDungeon.lastCombatMetricKey = "LittleHush";
        }
    }

    private void makeBossToMonstro() {
        if (AbstractDungeon.nextRoom.room instanceof MonsterRoomBoss && isMonstro) {
            if (CardCrawlGame.dungeon instanceof Exordium) {
                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new Monstro(-50.0F, 0.0F)));
            } else if (CardCrawlGame.dungeon instanceof TheCity) {
                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new AbstractMonster[]{new Monstro(-150.0F, 0.0F), new Monstro(150.0F, 0.0F, 1)}));
            } else if (CardCrawlGame.dungeon instanceof TheBeyond) {
                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new AbstractMonster[]{new Monstro(-300.0F, 0.0F, 0.5F)
                        , new Monstro(-100.0F, 0.0F, 0.5F, 1)
                        , new Monstro(100F, 0.0F, 0.5F)
                        , new Monstro(300.0F, 0.0F, 0.5F, 1)
                }));
            }
            AbstractDungeon.lastCombatMetricKey = "Monstro";
        }
    }

    private boolean change = false;

    private boolean open = false;

    private boolean enteredBloodShop = false;

    public static int guppyCount = 0;
    public static int bookCount = 0;

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.currMapNode != null) {
            if (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
                AbstractChest chest = ((TreasureRoomBoss) AbstractDungeon.getCurrRoom()).chest;
                if (chest != null && chest.isOpen) {
                    open = true;
                }
                if (open && ((TreasureRoomBoss) AbstractDungeon.getCurrRoom()).choseRelic) {
                    open = false;
                    ((TreasureRoomBoss) AbstractDungeon.getCurrRoom()).choseRelic = false;
                    change = true;
                }
//                if (open && chest != null && !chest.isOpen) {
//                    open = false;
//                    change = true;
//                }
            }
        }
        if (change) {
            change = false;
            //是否无伤
            int chanceToDevil = counter == -3 ? 100 : 33;
            if (AbstractDungeon.aiRng.random(0, 99) < chanceToDevil) {
                AbstractDungeon.shopScreen = new BloodShopScreen();
                AbstractRoom shopRoom = new ShopRoom();
                AbstractDungeon.currMapNode.setRoom(shopRoom);
                shopRoom.onPlayerEntry();
                AbstractDungeon.shopScreen.open();
                enteredBloodShop = true;
            }
        }
//        if (save != null && AbstractDungeon.getCurrRoom() != null) {
//            int soul = save.soulHeartNum;
//            int black = save.blackHeartNum;
//            save = null;
//            if (soul > 0 || black > 0) {
//                for (int i = 0; i < soul; i++) {
//                    AbstractDungeon.getCurrRoom().rewards.add(0, new SoulHeart());
//                }
//                for (int i = 0; i < black; i++) {
//                    AbstractDungeon.getCurrRoom().rewards.add(0, new BlackHeart());
//                }
//                AbstractDungeon.combatRewardScreen.open();
//            }
//        }
        if (CardCrawlGame.dungeon != lastDungeon) {
            lastDungeon = CardCrawlGame.dungeon;
            if (!(lastDungeon instanceof TheBeyond)) {
                isMonstro = AbstractDungeon.aiRng.randomBoolean(0.15F);
                if (isMonstro && !(CardCrawlGame.dungeon instanceof TheEnding)) {
                    DungeonMap.boss = ImageMaster.loadImage("images/ui/map/monstro.png");
                    DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/monstro_outline.png");
                }
            } else {
                isMonstro = false;
            }
        }
        if (AbstractDungeon.floorNum == 0) {
            if (AbstractDungeon.overlayMenu != null && AbstractDungeon.overlayMenu.proceedButton != null && AbstractDungeon.combatRewardScreen != null) {
                if ("Select or Not?".equals(((String) Invoker.getField(AbstractDungeon.combatRewardScreen, "labelOverride")))) {
                    AbstractDungeon.overlayMenu.proceedButton.hide();
                }
            }
        }
    }

    public static AbstractDungeon lastDungeon = null;
    public static boolean isMonstro = false;

    private AbstractCard lastCard = null;

    private SoulHeartSave save = null;

    /**
     * 猫套效果
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        //guppyCount >= 3 ||
        if (AbstractDungeon.player.getRelic(HushsDoor.ID) != this) {
            return;
        }
        if (Utils.areGuppy()) {
            spawnFly();
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (AbstractDungeon.player.getRelic(HushsDoor.ID) != this) {
            return;
        }
        if (Utils.areBookworm() && c.type == AbstractCard.CardType.ATTACK && lastCard != c) {
            int rnd = AbstractDungeon.aiRng.random(0, 99);
            if (rnd < 50) {
                lastCard = BlankCardPower.playAgain(c, m);
            }
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (AbstractDungeon.player.currentHealth < 0) {
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            AbstractDungeon.player.currentHealth = 0;
            if (AbstractDungeon.player.currentBlock > 0) {
                AbstractDungeon.player.loseBlock();
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(AbstractDungeon.player.hb.cX - AbstractDungeon.player.hb.width / 2.0f + (float) Invoker.getField(AbstractDungeon.player, "BLOCK_ICON_X"), AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 2.0f + (float) Invoker.getField(AbstractDungeon.player, "BLOCK_ICON_Y")));
            }
        }
    }

    /**
     * 猫套效果
     */
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (talk) {
            talk = false;
            if (Settings.language == Settings.GameLanguage.ZHS) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "书套猫套失去遗物也保留效果啦！", 1.0f, 2.0f));
            }
        }
        if (AbstractDungeon.player.getRelic(HushsDoor.ID) != this) {
            return;
        }
        counter = -2;
        if (Utils.areGuppy()) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlightPower(AbstractDungeon.player, 1), 1));
        }
        for (int i = 0; i < Fly.flyAlive.length; i++) {
            Fly.flyAlive[i] = false;
        }
        x = AbstractDungeon.player.drawX;//new BigDecimal(Float.toString( - 1300));
        y = AbstractDungeon.player.drawY + 70;//new BigDecimal(Float.toString( - 100));
        if (haveJudas && !AbstractDungeon.player.hasRelic(JudasShadow.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 15), 15));
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
    }

    //    private static BigDecimal x;
//    private static BigDecimal y;
    private static double x;
    private static double y;

    @Override
    public void onVictory() {
        super.onVictory();
        if (AbstractDungeon.player.getRelic(HushsDoor.ID) != this) {
            return;
        }
        if (GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat == 0) {
            counter = -3;
        }
        MinionHelper.clearMinions(AbstractDungeon.player);
        int rnd = AbstractDungeon.aiRng.random(0, 99);
        if (rnd < 2) {
            AbstractDungeon.getCurrRoom().rewards.add(new SoulHeart());
        } else if (rnd < 3) {
            AbstractDungeon.getCurrRoom().rewards.add(new BlackHeart());
        }
    }

    public static void spawnFly() {
        int i = 0;
        for (; i < Fly.flyAlive.length; i++) {
            if (!Fly.flyAlive[i]) {
                Point center = new Point(x, y);
                double angle = 2 * Math.PI * i / Fly.flyAmount;
                Point point = Utils.getCirclePoint(center, angle, 200);
                Fly fly = new Fly(0, 0);
                fly.drawX = (float) point.x;
                fly.drawY = (float) point.y;
                SummonHelper.summonMinion(fly);
                fly.index = i;
//                if (i != Fly.flyAlive.length - 1) {
                Fly.flyAlive[i] = true;
//                }
                break;
            }
        }
    }

    @Override
    public SoulHeartSave onSave() {
        int soul = 0, black = 0;
//        for (RewardItem rewardItem : AbstractDungeon.getCurrRoom().rewards) {
//            if (rewardItem.img == SoulHeart.soulHeart && !rewardItem.isDone) {
//                soul++;
//            } else if (rewardItem.img == BlackHeart.blackHeart && !rewardItem.isDone) {
//                black++;
//            }
//        }
        return new SoulHeartSave(soulHeart, blackHeart, soul, black, bookCount, guppyCount);
    }

    @Override
    public void onLoad(SoulHeartSave save) {
        if (save != null) {
            soulHeart = save.soulHeart;
            blackHeart = save.blackHeart;
            bookCount = save.bookCount;
            guppyCount = save.guppyCount;
        }
        PlayerDamagePatch.resurrectRelics.clear();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof ResurrectRelic) {
                PlayerDamagePatch.resurrectRelics.add((ResurrectRelic) relic);
            }
        }

    }
}
