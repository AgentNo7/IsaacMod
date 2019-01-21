package monsters.minions;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import monsters.Hush;
import monsters.Intent.Move;

public class HushFly extends CustomMonster {

    public static final String NAME;
    public static final String MOVE_NAME;

    private int attackDmg = 4;

    public Hush source;
    public int index;

    public HushFly(float x, float y) {
        super(NAME, "HushFly", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = new Texture(Gdx.files.internal("images/monsters/HushFly.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
//        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.05F));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo) this.damage.get(0), AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void usePreBattleAction() {
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 2)));
//        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
    }

    private boolean addPower = false;

    @Override
    public void update() {
        super.update();
        if (!addPower) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1)));
            addPower = true;
        }
    }

    @Override
    protected void getMove(int i) {
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    public void die() {
        source.flyAlive[index] = false;
        super.die();
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "死寂苍蝇";
            MOVE_NAME = "摸你一下";
        } else {
            NAME = "HushFly";
            MOVE_NAME = "attack";
        }
    }
}
