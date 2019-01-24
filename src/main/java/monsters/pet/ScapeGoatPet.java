package monsters.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;
import patches.action.ChangeTargetPatch;
import relics.PunchingBag;

public class ScapeGoatPet extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    PunchingBag punchingBag;

    public ScapeGoatPet(float x, float y, PunchingBag punchingBag) {
        super(NAME, "ScapeGoat", 10, -8.0F, 10.0F, 70, 70.0F, null, x, y);
        this.setHp(punchingBag.counter);
        this.punchingBag = punchingBag;
        this.img = new Texture(Gdx.files.internal("images/monsters/ScapeGoat.png"));
        this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
    }

    @Override
    public void takeTurn() {
        if (ChangeTargetPatch.target == this) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, MOVE_NAME, 1.0f, 2.0f));
        }
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
    }

    @Override
    protected Texture getAttackIntent() {
        return super.getAttackIntent();
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        punchingBag.counter -= info.output;
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "棉花怪";
            MOVE_NAME = "呜呜呜";
        } else {
            NAME = "Scape Goat";
            MOVE_NAME = "Please, don't.";
        }
    }
}