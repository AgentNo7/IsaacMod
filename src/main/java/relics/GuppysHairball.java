package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import helpers.BaseSummonHelper;
import monsters.pet.Hairball;
import relics.abstracrt.DevilRelic;
import utils.Point;
import utils.Utils;

public class GuppysHairball extends DevilRelic {
    public static final String ID = "GuppysHairball";
    public static final String IMG = "images/relics/GuppysHairball.png";
    public static final String DESCRIPTION = "战斗开始召唤一个猫球，猫球杀死敌人时,猫球力量加2，每场战斗最多一次。";

    public GuppysHairball() {
        super("GuppysHairball", new Texture(Gdx.files.internal("images/relics/GuppysHairball.png")), RelicTier.UNCOMMON, LandingSound.CLINK);
        counter = 0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new GuppysHairball();
    }

    public boolean addedPower = false;

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        addedPower = false;
        Point center = new Point(AbstractDungeon.player.hb.x - 1200, AbstractDungeon.player.hb_y + 170);
        double angle = 0;
        Point point = Utils.getCirclePoint(center, angle, 150);
        Hairball hairball = new Hairball((float) point.x, (float) point.y);
//        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(hairball, false));
        BaseSummonHelper.summonMinion(hairball);
        if (counter > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(hairball, hairball, new StrengthPower(hairball, counter), counter));
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
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
