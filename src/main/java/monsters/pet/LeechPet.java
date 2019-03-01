package monsters.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FadingPower;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class LeechPet extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private int attackDmg = 6;

    public LeechPet(float x, float y) {
        super(NAME, "Leech", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = new Texture(Gdx.files.internal("images/monsters/Leech.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FadingPower(this, 6)));
    }

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractMonster monster = getTarget();
        if (monster != null) {
            this.damage.get(0).applyPowers(this, monster);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.player.heal(1, true);
        } else {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void getMove(int i) {
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "水蛭";
            MOVE_NAME = "吸！";
        } else {
            NAME = "Leech";
            MOVE_NAME = "Blood Suck!";
        }
    }
}