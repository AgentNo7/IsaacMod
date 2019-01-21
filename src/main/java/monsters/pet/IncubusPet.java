package monsters.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class IncubusPet extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private relics.Incubus incubus;

    private int attackDmg = 0;

    public int index = 0;

    public IncubusPet(float x, float y, relics.Incubus incubus) {
        super(NAME, "Incubus", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = new Texture(Gdx.files.internal("images/monsters/Incubus.png"));
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
        this.incubus = incubus;
    }

    @Override
    public void takeTurn() {
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
    }

    @Override
    protected Texture getAttackIntent() {
        return super.getAttackIntent();
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "莉莉丝宝宝";
            MOVE_NAME = "黑暗朋友";
        } else {
            NAME = "Incubus";
            MOVE_NAME = "Dark friend";
        }
    }
}