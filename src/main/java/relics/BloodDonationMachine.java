package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import relics.abstracrt.ClickableRelic;

import java.util.Random;

public class BloodDonationMachine extends ClickableRelic {
    public static final String ID = "BloodDonationMachine";
    public static final String IMG = "images/relics/BloodDonationMachine.png";
    public static final String DESCRIPTION = "右击扣一血，获得20金币（10%），10金币（30%）,5金币（30%），1金币（30%），使用时有3%的概率被卖爆，然后加7血上限或者获得卖血袋。boss房。";

    public static boolean canUse = true;

    public static boolean broken = false;

    public static boolean isBag = false;

    public BloodDonationMachine() {
        super("BloodDonationMachine", new Texture(Gdx.files.internal("images/relics/BloodDonationMachine.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BloodDonationMachine();
    }

    //右键卖血
    protected void onRightClick() {
        if (broken || !canUse) {
            this.img = ImageMaster.loadImage("images/relics/BrokenBloodDonationMachine.png");
            return;
        }
        int bang = AbstractDungeon.aiRng.random(0, 99);
        if (bang < 3) {
            int blood = AbstractDungeon.aiRng.random(0, 99);
            if (blood < 50) {
                AbstractDungeon.player.increaseMaxHp(7, true);
            } else {
                isBag = true;
            }
            show();
            broken = true;
            this.img = ImageMaster.loadImage("images/relics/BrokenBloodDonationMachine.png");
            return;
        }
        AbstractDungeon.player.currentHealth -= 1;
        if (AbstractDungeon.player.hasRelic("Charity")) {
            AbstractDungeon.player.currentHealth -= 5;
        }
        AbstractDungeon.player.healthBarUpdatedEvent();
        AbstractDungeon.effectList.add(new StrikeEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 1));
        if (AbstractDungeon.player.currentHealth <= 0) {
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
        }
        int rnd = new Random().nextInt(100);
        if (rnd < 10) {
            AbstractDungeon.player.gainGold(20);
        } else if (rnd < 40) {
            AbstractDungeon.player.gainGold(10);
        } else if (rnd < 70) {
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
        if (broken) {
            this.img = ImageMaster.loadImage("images/relics/BrokenBloodDonationMachine.png");
        }
        canUse = !(room instanceof MonsterRoomBoss) && !(room instanceof TreasureRoomBoss);
    }
}
