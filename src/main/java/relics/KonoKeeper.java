package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;

import java.util.Random;

public class KonoKeeper extends CustomRelic {
    public static final String ID = "KonoKeeper";
    public static final String IMG = "images/relics/konokeeper.png";
    public static final String DESCRIPTION = "进入普通怪物房间时，有 #b15% 的概率使得当前房间变成商店。";

    public static void show() {
        AbstractDungeon.player.getRelic("KonoKeeper").flash();
    }

    public KonoKeeper() {
        super("KonoKeeper", new Texture(Gdx.files.internal("images/relics/konokeeper.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new KonoKeeper();
    }

    /**
     * 有25%的概率使得普通敌人房间变成商店
     *
     * @param room
     */
    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (AbstractDungeon.floorNum > 1){
            AbstractRoom theRoom = AbstractDungeon.nextRoom.room;
            if ((theRoom instanceof MonsterRoom) && !(theRoom instanceof MonsterRoomBoss) && !(theRoom instanceof MonsterRoomElite)) {
                int rnd = new Random().nextInt(100);
                if (rnd < 25) {
                    AbstractDungeon.nextRoom.room = new ShopRoom();
                    show();
                }
            }
        }
    }

}
