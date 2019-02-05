package monsters.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;
import relics.MomsKnife;

public class MomsKnifePet extends AbstractPet {

    public boolean LclickStart;
    public boolean Lclick;

    public static final String NAME;

    public int miliTimeTake = 0;

    public boolean clickFinish = false;

    private MomsKnife momsKnife;

    public MomsKnifePet(float x, float y, relics.MomsKnife momsKnife) {
        super(NAME, "MomsKnife", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10000000);
        this.img = new Texture(Gdx.files.internal("images/monsters/MomsKnife.png"));
        this.momsKnife = momsKnife;
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
            NAME = "妈刀";
        } else {
            NAME = "Succubus";
        }
    }

    public void onRightClick() {

    }

    @Override
    public void update() {
        super.update();
        if (this.LclickStart && InputHelper.justReleasedClickLeft) {
            if (this.hb.hovered) {
                this.Lclick = true;
            }
            this.LclickStart = false;
        }
        if ((this.hb != null) && ((this.hb.hovered) && (InputHelper.justClickedLeft))) {
            this.LclickStart = true;
            clickFinish = false;
            miliTimeTake = (int) (CardCrawlGame.playtime * 100);
        }
        if ((this.Lclick)) {
            this.Lclick = false;
            this.onRightClick();
            miliTimeTake = (int) (CardCrawlGame.playtime * 100) - miliTimeTake;
            clickFinish = true;
        }
    }

    @Override
    public void createIntent() {

    }

    @Override
    public void showHealthBar() {
        this.hideHealthBar();
    }
}