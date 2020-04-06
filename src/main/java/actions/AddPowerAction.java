package actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * Created by aqiod on 2019/3/8.
 */
public class AddPowerAction extends AbstractGameAction {

    private AbstractCreature owner;
    private AbstractPower power;

    public AddPowerAction(AbstractCreature owner, AbstractPower power) {
        this.owner = owner;
        this.power = power;
    }

    @Override
    public void update() {
        if (owner != null && power != null) {
            owner.addPower(power);
        }
        this.isDone = true;
    }
}
