package relics;

import actions.LoseRelicAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.abstracrt.ClickableRelic;

public class ForgetMeNow extends ClickableRelic {
    public static final String ID = "ForgetMeNow";
    public static final String IMG = "images/relics/ForgetMeNow.png";
    public static final String DESCRIPTION = "一次性遗物，右击回到本层的第一层（在boss宝箱会直接下层）。";

    public static boolean used = false;

    public ForgetMeNow() {
        super("ForgetMeNow", new Texture(Gdx.files.internal("images/relics/ForgetMeNow.png")), RelicTier.RARE, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new ForgetMeNow();
    }

    //右键使用
    protected void onRightClick() {
        if (!used) {
            used = true;
            CardCrawlGame.music.fadeOutBGM();
            CardCrawlGame.music.fadeOutTempBGM();
            AbstractDungeon.fadeOut();
            AbstractDungeon.isDungeonBeaten = true;
            AbstractDungeon.overlayMenu.proceedButton.hide();
            AbstractDungeon.dungeonMapScreen.closeInstantly();
            AbstractDungeon.getCurrRoom().onPlayerEntry();
            AbstractDungeon.floorNum = AbstractDungeon.floorNum - AbstractDungeon.floorNum % 17;
        }
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (used) {
            AbstractDungeon.actionManager.addToBottom(new LoseRelicAction(ForgetMeNow.ID));
            used = false;
        }
    }

    @Override
    public void onEquip() {
        super.onEquip();
        used = false;
    }

    @Override
    public void update() {
        super.update();
    }
}
