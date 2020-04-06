package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import relics.abstracrt.ClickableRelic;

import java.util.Random;

public class BloodDonationBag extends ClickableRelic {
    public static final String ID = "BloodDonationBag";
    public static final String IMG = "images/relics/BloodDonationBag.png";
    public static final String DESCRIPTION = "右击扣一血，获得10金币（20%），5币（30%），1金币（50%）。boss房。";

    private boolean RclickStart;
    private boolean Rclick;

    public static boolean canUse = true;

    public BloodDonationBag() {
        super("BloodDonationBag", new Texture(Gdx.files.internal("images/relics/BloodDonationBag.png")), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BloodDonationBag();
    }

    //右键卖血
    public void onRightClick() {
        if (!canUse) {
            return;
        }
        if (AbstractDungeon.player.hasRelic("Charity")) {
            AbstractDungeon.player.currentHealth -= 5;
        }
        if (AbstractDungeon.player.currentHealth <= 1) {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
        } else {
            AbstractDungeon.player.currentHealth -= 1;
            AbstractDungeon.player.healthBarUpdatedEvent();
            AbstractDungeon.effectList.add(new StrikeEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 1));
            if (AbstractDungeon.player.currentHealth <= 0) {
                AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
            }
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                relic.onLoseHp(1);
            }
        }
        int rnd = new Random().nextInt(100);
        if (rnd < 20) {
            AbstractDungeon.player.gainGold(10);
        } else if (rnd < 50) {
            AbstractDungeon.player.gainGold(5);
        } else {
            AbstractDungeon.player.gainGold(1);
        }
        show();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        canUse = !(room instanceof MonsterRoomBoss) && !(room instanceof TreasureRoomBoss);
    }
}
