package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import relics.abstracrt.ClickableRelic;

public class MaYiHuaBei extends ClickableRelic {
    public static final String ID = "MaYiHuaBei";
    public static final String IMG = "images/relics/huabei.png";
    public static final String DESCRIPTION = "点击遗物获得 #b500 临时金额并失效，下次失去金币时将失去所有金币，进入boss房间遗物重新生效。";

    private boolean used = false;
    private boolean loseAll = true;

    public MaYiHuaBei() {
        super("MaYiHuaBei", new Texture(Gdx.files.internal("images/relics/huabei.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.Rclick = false;
        this.RclickStart = false;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MaYiHuaBei();
    }

    //右键点击获得500金币
    protected void onRightClick() {
        if (!used) {
            AbstractDungeon.player.gainGold(500);
            used = true;
            loseAll = true;
            show();
            this.pulse = false;
        }
    }

    //只在使用遗物后的第一次扣钱时触发
    @Override
    public void onLoseGold() {
        if (used && loseAll) {
            AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
            loseAll = false;
        }
    }

    /**
     * 见到boss重置遗物
     *
     * @param room
     */
    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof MonsterRoomBoss) {
            used = false;
            this.pulse = true;
            beginPulse();
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
