package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import powers.GuppysCollarPower;
import relics.abstracrt.DevilRelic;

public class GuppysCollar extends DevilRelic {
    public static final String ID = "GuppysCollar";
    public static final String IMG = "images/relics/GuppysCollar.png";
    public static final String DESCRIPTION = "死亡时有50%的概率一血复活。";

    public GuppysCollar() {
        super("GuppysCollar", new Texture(Gdx.files.internal("images/relics/GuppysCollar.png")), RelicTier.COMMON, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysCollar();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (!(room instanceof MonsterRoom)) {
            AbstractDungeon.player.powers.add(new GuppysCollarPower(AbstractDungeon.player, 1));
        }
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GuppysCollarPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (AbstractDungeon.player.getRelic(this.relicId) == this) {
            HushsDoor.guppyCount++;
        }
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
