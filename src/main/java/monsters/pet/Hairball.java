package monsters.pet;

import actions.FeedHairballAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class Hairball extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private int attackDmg = 3;

    public int index = 0;

    public Hairball(float x, float y) {
        super(NAME, "Hairball", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = new Texture(Gdx.files.internal("images/monsters/Hairball.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractMonster monster = getTarget();
        if (monster != null) {
            this.damage.get(0).applyPowers(this, monster);
            AbstractDungeon.actionManager.addToBottom(new FeedHairballAction(monster, this.damage.get(0), 2));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    protected Texture getAttackIntent() {
        return super.getAttackIntent();
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "嗝屁猫的毛球";
            MOVE_NAME = "甩大力";
        } else {
            NAME = "Guppy's Hairball";
            MOVE_NAME = "Swing it";
        }
    }
}