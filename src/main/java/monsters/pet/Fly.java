package monsters.pet;

import actions.AnimateSuicideAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class Fly extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private int attackDmg = 5;

    private int attackBase = 5;

    public static int flyAmount = 20;

    public static boolean[] flyAlive = new boolean[flyAmount];

    public int index = 0;

    public Fly(float x, float y) {
        super(NAME, "Fly", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = new Texture(Gdx.files.internal("images/monsters/HushFly.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    private int liveTurn = 0;

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractMonster monster = getTarget();
        if (monster != null) {
            liveTurn++;
            if (liveTurn >= 3) {
                AbstractDungeon.actionManager.addToBottom(new AnimateSuicideAction(this, monster, 0.25F, 20));
            } else {
                this.damage.get(0).applyPowers(this, monster);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
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
    }

    @Override
    protected void getMove(int i) {
        if (liveTurn >= 2) {
            this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
        } else {
            this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
        }
    }

    @Override
    protected Texture getAttackIntent() {
        return super.getAttackIntent();
    }

    @Override
    public void die() {
        flyAlive[index] = false;
        super.die();
    }

    @Override
    public void die(boolean triggerRelics) {
        flyAlive[index] = false;
        super.die(false);
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "苍蝇";
            MOVE_NAME = "摸你一下";
        } else {
            NAME = "Fly";
            MOVE_NAME = "attack";
        }
    }
}