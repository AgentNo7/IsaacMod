package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import monsters.abstracrt.AbstractPet;

public class AnimateMoveAction extends AbstractGameAction {
    private boolean called = false;

    private float targetX, targetY;

    private Float time;

    public AnimateMoveAction(AbstractPet owner, AbstractCreature target, float time) {
        this.setValues(null, owner, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.targetX = target.drawX;
        this.targetY = target.drawY;
        this.time = time;
    }

    public AnimateMoveAction(AbstractPet owner, AbstractCreature target) {
        this.setValues(null, owner, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.targetX = target.drawX;
        this.targetY = target.drawY;
    }

    public void update() {
        if (!this.called) {
            AbstractPet p = (AbstractPet) this.source;
            p.moveTimer = time == null ? 2.0F : time;
            p.speedX = (targetX - p.drawX) / p.moveTimer;
            p.speedY = (targetY - p.drawY) / p.moveTimer;
            this.called = true;
        }

        this.tickDuration();
    }


}
