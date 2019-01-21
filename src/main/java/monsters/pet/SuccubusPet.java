package monsters.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class SuccubusPet extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private relics.Succubus succubus;

    private int attackDmg = 1;

    public int index = 0;

    public SuccubusPet(float x, float y, relics.Succubus succubus) {
        super(NAME, "Succubus", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(100);
        this.img = new Texture(Gdx.files.internal("images/monsters/Succubus.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
        this.succubus = succubus;
    }

    @Override
    public void takeTurn() {
    }

    @Override
    protected void getMove(int i) {
    }

    @Override
    protected Texture getAttackIntent() {
        return super.getAttackIntent();
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "魅魔";
            MOVE_NAME = "助力后援";
        } else {
            NAME = "Succubus";
            MOVE_NAME = "need my help?";
        }
    }
}