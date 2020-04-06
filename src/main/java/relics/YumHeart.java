package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import relics.abstracrt.ChargeableRelic;

public class YumHeart extends ChargeableRelic {
    public static final String ID = "YumHeart";
    public static final String IMG = "images/relics/YumHeart.png";
    public static final String DESCRIPTION = "四充能，每打一个怪物房间加一充能，满充能时右击回复25血。";

    public YumHeart() {
        super("YumHeart", new Texture(Gdx.files.internal("images/relics/YumHeart.png")), RelicTier.RARE, LandingSound.CLINK, 4);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new YumHeart();
    }

    //右键开大
    public void onRightClick() {
        if (isUsable()) {
            AbstractDungeon.player.heal(25, true);
            this.stopPulse();
            resetCharge();
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
    }
}
