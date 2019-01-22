package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rewards.chests.LargeChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import relics.abstracrt.DevilRelic;

public class GuppysTail extends DevilRelic {
    public static final String ID = "GuppysTail";
    public static final String IMG = "images/relics/GuppysTail.png";
    public static final String DESCRIPTION = "进入宝箱房间时，把小宝箱变成大宝箱。";

    public static void show() {
        AbstractDungeon.player.getRelic("GuppysTail").flash();
    }

    public GuppysTail() {
        super("GuppysTail", new Texture(Gdx.files.internal("images/relics/GuppysTail.png")), RelicTier.COMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysTail();
    }

    /**
     * 进入宝箱房间时，把小宝箱变成大宝箱。
     *
     */
    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() instanceof TreasureRoom) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            AbstractChest chest = ((TreasureRoom)room).chest;
            if (!(chest == null) && !(chest instanceof LargeChest)) {
                ((TreasureRoom)room).chest = new LargeChest();
            }
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
        }
    }
}
