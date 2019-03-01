package relics;

import actions.LoseRelicAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import relics.abstracrt.ClickableRelic;

import java.util.ArrayList;

public class ForgetMeNow extends ClickableRelic {
    public static final String ID = "ForgetMeNow";
    public static final String IMG = "images/relics/ForgetMeNow.png";
    public static final String DESCRIPTION = "一次性遗物，右击回到本层的第一层（在boss宝箱会直接下层）。";

    public static boolean used = false;

    public ForgetMeNow() {
        super("ForgetMeNow", new Texture(Gdx.files.internal("images/relics/ForgetMeNow.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new ForgetMeNow();
    }

    //右键使用
    public void onRightClick() {
        if (!used) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
                return;
            }
            used = true;
            if (AbstractDungeon.floorNum < 55) {
                CardCrawlGame.dungeon = getDungeon(AbstractDungeon.floorNum, AbstractDungeon.player);
            } else {
                CardCrawlGame.dungeon = getDungeon(CardCrawlGame.nextDungeon, AbstractDungeon.player);
            }
            CardCrawlGame.music.fadeOutBGM();
            CardCrawlGame.music.fadeOutTempBGM();
            AbstractDungeon.fadeOut();
            AbstractDungeon.topLevelEffects.clear();
            AbstractDungeon.actionManager.actions.clear();
            AbstractDungeon.effectList.clear();
            AbstractDungeon.effectsQueue.clear();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.dungeonMapScreen.open(true);
            AbstractDungeon.floorNum = AbstractDungeon.floorNum - AbstractDungeon.floorNum % 17;
        }
    }

    public AbstractDungeon getDungeon(int floorNum, AbstractPlayer p) {
        switch (floorNum / 17) {
            case 0:
                return new Exordium(p, new ArrayList<>());
            case 1:
                return new TheCity(p, AbstractDungeon.specialOneTimeEventList);
            case 2:
                return new TheBeyond(p, AbstractDungeon.specialOneTimeEventList);
            case 3:
                return new TheEnding(p, AbstractDungeon.specialOneTimeEventList);
            default:
                return null;
        }
    }

    public AbstractDungeon getDungeon(String key, AbstractPlayer p) {
        byte var4 = -1;
        switch (key.hashCode()) {
            case -1887678253:
                if (key.equals("Exordium")) {
                    var4 = 0;
                }
                break;
            case 313705820:
                if (key.equals("TheCity")) {
                    var4 = 1;
                }
                break;
            case 791401920:
                if (key.equals("TheBeyond")) {
                    var4 = 2;
                }
                break;
            case 884969688:
                if (key.equals("TheEnding")) {
                    var4 = 3;
                }
        }

        switch (var4) {
            case 0:
                ArrayList<String> emptyList = new ArrayList<>();
                return new Exordium(p, emptyList);
            case 1:
                return new TheCity(p, AbstractDungeon.specialOneTimeEventList);
            case 2:
                return new TheBeyond(p, AbstractDungeon.specialOneTimeEventList);
            case 3:
                return new TheEnding(p, AbstractDungeon.specialOneTimeEventList);
            default:
                return null;
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (used) {
            AbstractDungeon.actionManager.addToBottom(new LoseRelicAction(ForgetMeNow.ID));
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        used = false;
    }

    @Override
    public void update() {
        super.update();
    }
}
