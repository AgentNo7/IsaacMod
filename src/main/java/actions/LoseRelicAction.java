package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import relics.ForgetMeNow;

public class LoseRelicAction extends AbstractGameAction {
    private String relic;

    public LoseRelicAction(String relic) {
        this.duration = 0.0F;
        this.relic = relic;
    }

    public void update() {
        AbstractDungeon.player.loseRelic(relic);
        if (ForgetMeNow.ID.equals(relic)) {
            ForgetMeNow.used = false;
        }
        this.tickDuration();
        this.isDone = true;
    }
}
