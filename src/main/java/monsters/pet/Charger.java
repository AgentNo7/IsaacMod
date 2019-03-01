package monsters.pet;

import actions.AnimateSuicideAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import monsters.Intent.Move;
import monsters.abstracrt.AbstractPet;

public class Charger extends AbstractPet {

    public static final String NAME;
    public static final String MOVE_NAME;

    private int attackDmg = 5;

    public static int ChargerAmount = 6;

    public static boolean[] ChargerAlive = new boolean[ChargerAmount];

    public int index = 0;
    public int imgIndex = 0;

    private static int total = 4;

    private static Texture[] imgs = {
            new Texture(Gdx.files.internal("images/monsters/Charger/Charger0.png")),
            new Texture(Gdx.files.internal("images/monsters/Charger/Charger1.png")),
            new Texture(Gdx.files.internal("images/monsters/Charger/Charger2.png")),
            new Texture(Gdx.files.internal("images/monsters/Charger/Charger3.png"))
    };


    public Charger(float x, float y) {
        super(NAME, "Charger", 10, -8.0F, 10.0F, 70, 70.0F, (String) null, x, y);
        this.setHp(10);
        this.img = imgs[0];
        this.damage.add(new DamageInfo(this, attackDmg));
        this.setMove(MOVE_NAME, (byte) Move.ATTACK.id, Intent.ATTACK, attackDmg);
    }

    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractMonster monster = getTarget();
        if (monster != null) {
            AbstractDungeon.actionManager.addToBottom(new AnimateSuicideAction(this, monster, 0.25F, 20));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    private int lasttime = -1;

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null) {
            int time = (int) (CardCrawlGame.playtime * 10);
            if (lasttime != time) {
                lasttime = time;
                imgIndex = (imgIndex + 1) % total;
                this.img = imgs[imgIndex];
            }
        }
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) Move.UNKNOWN.id, Intent.UNKNOWN);
    }

    @Override
    public void die() {
        ChargerAlive[index] = false;
        super.die();
    }

    @Override
    public void die(boolean triggerRelics) {
        ChargerAlive[index] = false;
        super.die(false);
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "冲锋怪";
            MOVE_NAME = "开冲！";
        } else {
            NAME = "Charger";
            MOVE_NAME = "Dash!";
        }
    }
}