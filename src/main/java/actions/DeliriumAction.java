package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import monsters.Delirium;

/**
 * Created by aqiod on 2019/3/22.
 */
public class DeliriumAction extends AbstractGameAction {

    private boolean hasBuilt = false;
    public Delirium delirium;

    public DeliriumAction(Delirium delirium) {
        this.delirium = delirium;
    }

    public void update() {
        if (!this.hasBuilt) {
            GenericEventDialog.show();
            this.hasBuilt = true;
        }

        delirium.imageEventText.update();
        if (!GenericEventDialog.waitForInput && !this.isDone) {
            if (GenericEventDialog.selectedOption == 1) {
                delirium.setWeaken(true);
                delirium.maxHealth = delirium.currentHealth = 6666;
                delirium.healthBarUpdatedEvent();
            }
            delirium.imageEventText.clear();
            this.isDone = true;
        }

    }

}
