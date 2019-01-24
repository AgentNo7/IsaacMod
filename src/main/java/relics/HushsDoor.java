package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
import helpers.BasePlayerMinionHelper;
import helpers.BaseSummonHelper;
import monsters.LittleHush;
import monsters.pet.Fly;
import powers.BlankCardPower;
import powers.FlightPower;
import relics.abstracrt.ClickableRelic;
import room.MonsterRoomMyBoss;
import screen.BloodShopScreen;
import utils.Point;
import utils.Utils;

import java.math.BigDecimal;

public class HushsDoor extends ClickableRelic {
    public static final String ID = "HushsDoor";
    public static final String IMG = "images/relics/HushsDoor.png";
    public static final String DESCRIPTION = "通往Hush（凹凸）的关键。右击可切换是否挑战。";

    private static boolean toHush = true;

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
    }

    private AbstractCard lastCard = null;

    /**
     * 猫套效果
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        //guppyCount >= 3 ||
        if ( Utils.areGuppy()) {
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
        x = new BigDecimal(Float.toString(AbstractDungeon.player.drawX - 1300));
        y = new BigDecimal(Float.toString(AbstractDungeon.player.drawY - 100));
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
    }

    private static BigDecimal x;
    private static BigDecimal y;

    @Override
    public void onVictory() {
        super.onVictory();
        if (GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat == 0) {
            counter = -3;
        }
        BasePlayerMinionHelper.clearMinions(AbstractDungeon.player);
    }

    public static void spawnFly() {
        int i = 0;
        for (; i < Fly.flyAlive.length; i++) {
            if (!Fly.flyAlive[i]) {
                Point center = new Point(x.doubleValue(), y.doubleValue());
                double angle = 2 * Math.PI * i / Fly.flyAmount;
                Point point = Utils.getCirclePoint(center, angle, 250);
                Fly fly = new Fly((float) point.x, (float) point.y);
                BaseSummonHelper.summonMinion(fly);
                fly.index = i;
                if (i != Fly.flyAlive.length - 1) {
                    Fly.flyAlive[i] = true;
                }
                break;
            }
        }
    }
}
