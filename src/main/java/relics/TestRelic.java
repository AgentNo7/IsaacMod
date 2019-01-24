package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TestRelic extends CustomRelic {
    public static final String ID = "TestRelic";
    public static final String IMG = "images/relics/test.png";
    public static final String DESCRIPTION = "开局999力量999缓冲，测试遗物，如遇到请不要拿。";

    public static void show() {
        AbstractDungeon.player.getRelic("TestRelic").flash();
    }

    public TestRelic() {
        super("TestRelic", new Texture(Gdx.files.internal("images/relics/test.png")), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TestRelic();
    }

    @Override
    public void atBattleStart() {
//        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 9), 9));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BufferPower(AbstractDungeon.player, 999), 999));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 999), 999));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 999), 999));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
//        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
//            m.die();
//        }
    }

//    @Override
//    public void onEnterRoom(AbstractRoom room) {
//        if (AbstractDungeon.floorNum > 1) {
////            if (theRoom instanceof EventRoom) {
////                EventRoom eventRoom = new EventRoom();
////                eventRoom.event = new HidenRoomEvent();
//            AbstractDungeon.nextRoom.room = new MyEventRoom();
////            }
//        }
//        AbstractDungeon.nextRoom.room.event = new HidenRoomEvent();
//        room.monsters = null;
//        room.event = new HidenRoomEvent();
//    }
//
//    @Override
//    public void justEnteredRoom(AbstractRoom room) {
//        room.event = new HidenRoomEvent();
//    }
//
//    private HidenRoomEvent hidenRoomEvent = new HidenRoomEvent();
//
//    private int floor = 0;

//    @Override
//    public void update() {
//        super.update();
//        if (AbstractDungeon.floorNum != floor) {
//            hidenRoomEvent = new HidenRoomEvent();
//            floor = AbstractDungeon.floorNum;
//        }
//        if (AbstractDungeon.floorNum > 1) {
//            AbstractDungeon.getCurrRoom().event = hidenRoomEvent;
//        }
//    }
}
