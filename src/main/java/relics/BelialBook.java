package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mymod.IsaacMod;
import relics.abstracrt.BookSuit;
import relics.abstracrt.DevilInterface;

public class BelialBook extends BookSuit implements DevilInterface {
    public static final String ID = "BelialBook";
    public static final String IMG = "images/relics/BelialBook.png";
    public static final String DESCRIPTION = "三充能，每打一个怪物房间加一充能，满充能时右击在本场战斗增加5点力量。";

    public BelialBook() {
        super("BelialBook", new Texture(Gdx.files.internal("images/relics/BelialBook.png")), RelicTier.COMMON, LandingSound.CLINK, 3);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new BelialBook();
    }

    //右键开大
    public void onRightClick() {
        if (isUsable()) {
            this.flash();
            if (AbstractDungeon.getMonsters() != null){
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 5), 5));
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
            this.stopPulse();
            show();
            resetCharge();
        }
    }

    private boolean removed = false;

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (!removed) {
            removed = true;
            IsaacMod.devilRelics.remove(this.relicId);
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
