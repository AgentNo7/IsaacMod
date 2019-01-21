package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SwapRelicAction extends AbstractGameAction {
    private int relic1;
    private int relic2;
    private  AbstractRelic relic11;
    private  AbstractRelic relic22;

    public SwapRelicAction(int relic1, AbstractRelic relic11,  int relic2, AbstractRelic relic22) {
        this.duration = 0.0F;
        this.relic1 = relic1;
        this.relic2 = relic2;
        this.relic11 = relic11;
        this.relic22 = relic22;
    }

    public void update() {
        AbstractDungeon.player.relics.set(relic1, relic22.makeCopy());
        AbstractDungeon.player.relics.set(relic2, relic11.makeCopy());
        this.tickDuration();
        this.isDone = true;
    }
}
