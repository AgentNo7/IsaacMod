package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import monsters.abstracrt.AbstractPet;

public class AnimateSuicideAction extends AbstractGameAction {
    private boolean called = false;

    private float targetX, targetY;

    private Float time;

    private int damage;

    public AnimateSuicideAction(AbstractPet owner, AbstractCreature target, float time, int damage) {
        this.setValues(target, owner, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.targetX = target.drawX;
        this.targetY = target.drawY;
        this.time = time;
        this.damage = damage;
    }

    public AnimateSuicideAction(AbstractPet owner, AbstractCreature target) {
        this.setValues(target, owner, 0);
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
            p.target = target;
            p.isSuicide = true;
            p.suicideDamage = damage;
            this.called = true;
        }

        this.tickDuration();
    }


}
