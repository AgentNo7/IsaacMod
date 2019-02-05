package relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
import helpers.MinionHelper;
import helpers.SummonHelper;
import monsters.LittleHush;
import monsters.Monstro;
import monsters.pet.Fly;
import powers.BlankCardPower;
import powers.FlightPower;
import relics.abstracrt.ClickableRelic;
import rewards.BlackHeart;
import rewards.SoulHeart;
import room.MonsterRoomMyBoss;
import screen.BloodShopScreen;
import utils.Point;
import utils.SoulHeartSave;
import utils.Utils;

import static patches.ui.SoulHeartPatch.blackHeart;
import static patches.ui.SoulHeartPatch.soulHeart;

public class HushsDoor extends ClickableRelic implements CustomSavable<SoulHeartSave> {
    public static final String ID = "HushsDoor";
    public static final String IMG = "images/relics/HushsDoor.png";
    public static final String DESCRIPTION = "通往Hush（凹凸）的关键。右击可切换是否挑战。";

    public static boolean toHush = true;

    public static boolean isOn = false;

    @Override
    protected void onRightClick() {
        toHush = !toHush;
        if (toHush) {
            this.img = ImageMaster.loadImage("images/relics/HushsDoor.png");
        } else {
            this.img = ImageMaster.loadImage("images/relics/HushsDoorBroken.png");
        }
    }

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
        guppyCount = 0;
        bookCount = 0;
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
            AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new LittleHush(-50.0F, 30.0F)));
            AbstractDungeon.lastCombatMetricKey = "LittleHush";
        }
    }

    private void makeBossToMonstro() {
        if (AbstractDungeon.nextRoom.room instanceof MonsterRoomBoss && AbstractDungeon.aiRng.randomBoolean(0.33F)) {
            if (CardCrawlGame.dungeon instanceof Exordium) {
                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new Monstro(-50.0F, 0.0F)));
            } else if (CardCrawlGame.dungeon instanceof TheCity || (AbstractDungeon.floorNum > 22 && AbstractDungeon.floorNum < 38)) {
                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new AbstractMonster[]{new Monstro(-150.0F, 0.0F), new Monstro(150.0F, 0.0F)}));
            }
//            else if (CardCrawlGame.dungeon instanceof TheBeyond) {
//                AbstractDungeon.nextRoom.room = new MonsterRoomMyBoss(new MonsterGroup(new AbstractMonster[]{new Monstro(-100.0F, 0.0F), new Monstro(-50.0F, 0.0F)}));
//            }
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
            }
        }
        if (change) {
            change = false;
            //是否无伤
            int chance = 3;//AbstractDungeon.floorNum > 20 ? 6 : 4;
            int chanceToDevil = counter == -3 ? 1 : chance;
            int rnd = AbstractDungeon.eventRng.random(1, chanceToDevil);
            if (rnd == 1) {
                AbstractDungeon.shopScreen = new BloodShopScreen();
                AbstractRoom shopRoom = new ShopRoom();
                AbstractDungeon.currMapNode.setRoom(shopRoom);
                shopRoom.onPlayerEntry();
                AbstractDungeon.shopScreen.open();
                enteredBloodShop = true;
            }
        }
        if (save != null && AbstractDungeon.getCurrRoom() != null) {
            int soul = save.soulHeartNum;
            int black = save.blackHeartNum;
            save = null;
            if (soul > 0 || black > 0) {
                for (int i = 0; i < soul; i++) {
                    AbstractDungeon.getCurrRoom().rewards.add(0, new SoulHeart());
                }
                for (int i = 0; i < black; i++) {
                    AbstractDungeon.getCurrRoom().rewards.add(0, new BlackHeart());
                }
                AbstractDungeon.combatRewardScreen.open();
            }
        }
    }

    private AbstractCard lastCard = null;

    private SoulHeartSave save = null;

    /**
     * 猫套效果
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        //guppyCount >= 3 ||
        if (Utils.areGuppy()) {
            spawnFly();
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        // || ( bookCount >= 3)
        if (Utils.areBookworm() && c.type == AbstractCard.CardType.ATTACK && lastCard != c) {
            int rnd = AbstractDungeon.aiRng.random(0, 99);
            if (rnd < 50) {
                lastCard = BlankCardPower.playAgain(c, m);
            }
        }
    }

    /**
     * 猫套效果
     */
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        counter = -2;
        //guppyCount >= 3 ||
        if (Utils.areGuppy()) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlightPower(AbstractDungeon.player, 1), 1));
        }
        for (int i = 0; i < Fly.flyAlive.length; i++) {
            Fly.flyAlive[i] = false;
        }
        x = AbstractDungeon.player.drawX;//new BigDecimal(Float.toString( - 1300));
        y = AbstractDungeon.player.drawY + 70;//new BigDecimal(Float.toString( - 100));
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
                if (i != Fly.flyAlive.length - 1) {
                    Fly.flyAlive[i] = true;
                }
                break;
            }
        }
    }

    @Override
    public SoulHeartSave onSave() {
        int soul = 0, black = 0;
        for (RewardItem rewardItem : AbstractDungeon.getCurrRoom().rewards) {
            if (rewardItem.img == SoulHeart.soulHeart && !rewardItem.isDone) {
                soul++;
            } else if (rewardItem.img == BlackHeart.blackHeart && !rewardItem.isDone) {
                black++;
            }
        }
        return new SoulHeartSave(soulHeart, blackHeart, soul, black);
    }

    @Override
    public void onLoad(SoulHeartSave save) {
        if (save != null) {
            soulHeart = save.soulHeart;
            blackHeart = save.blackHeart;
            if (save.soulHeartNum > 0 || save.blackHeartNum > 0) {
                this.save = save;
            }
        }
    }
}
