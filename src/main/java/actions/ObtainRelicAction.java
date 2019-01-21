package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static mymod.IsaacMod.obtain;

public class ObtainRelicAction extends AbstractGameAction {
    private AbstractRelic relic;

    private boolean canDuplicate;

    public ObtainRelicAction(AbstractRelic relic, boolean canDuplicate) {
        this.duration = 0.0F;
        this.relic = relic;
        this.canDuplicate = canDuplicate;
    }

    public ObtainRelicAction(AbstractRelic relic) {
        this(relic, false);


    }

    public void update() {
        obtain(AbstractDungeon.player, relic.makeCopy(), canDuplicate);
        this.tickDuration();
        this.isDone = true;
    }
}
