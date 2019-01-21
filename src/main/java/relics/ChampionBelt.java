package relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;

public class ChampionBelt extends CustomRelic {
    public static final String ID = "ChampionBelt";
    public static final String IMG = "images/relics/ChampionBelt.png";
    public static final String DESCRIPTION = "在每回合开始时获得 [E] 。进入普通敌人房间时有35%的概率变成精英房间";

    public ChampionBelt() {
        super("ChampionBelt", new Texture(Gdx.files.internal("images/relics/ChampionBelt.png")), RelicTier.BOSS, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new ChampionBelt();
    }

    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        if (counter == -3) {
            AbstractDungeon.nextRoom.room = new MonsterRoomElite();
        } else {
            AbstractRoom theRoom = AbstractDungeon.nextRoom.room;
            if ((theRoom instanceof MonsterRoom) && !(theRoom instanceof MonsterRoomBoss) && !(theRoom instanceof MonsterRoomElite)) {
                int rnd = AbstractDungeon.mapRng.random(0, 99);
                if (rnd < 35) {
                    AbstractDungeon.nextRoom.room = new MonsterRoomElite();
                    counter = -3;
                }
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite && counter == -3) {
            AbstractDungeon.actionManager.addToTop(new AnimateJumpAction(AbstractDungeon.player));
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        counter = -1;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
    }

    @Override
    public void update() {
        super.update();
    }
}
