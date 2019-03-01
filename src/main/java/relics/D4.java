package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import relics.abstracrt.ChargeableRelic;
import relics.abstracrt.DevilInterface;

import java.lang.reflect.Field;

public class D4 extends ChargeableRelic {
    public static final String ID = "D4";
    public static final String IMG = "images/relics/D4.png";
    public static final String DESCRIPTION = "六充能，满充能时右击可以roll自身所有遗物，每个遗物将替换为随机同稀有度的遗物（特殊遗物算做稀有遗物，复制D4无效，战斗中使用可能会导致bug）。";

    public D4() {
        super("D4", new Texture(Gdx.files.internal("images/relics/D4.png")), RelicTier.RARE, LandingSound.CLINK, 6);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new D4();
    }

    public boolean roll = false;

    private boolean doRoll = false;

    public int devilOnlyRelic = 0;
    public int rare = 0;
    public int uncommon = 0;
    public int common = 0;
    public int special = 0;
    public int boss = 0;
    public int shop = 0;
    public int starter = 0;
    public int deprecated = 0;

    //右键开roll
    public void onRightClick() {
        if (counter >= maxCharge) {
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof HushsDoor || relic instanceof D4 || relic instanceof Circlet || relic instanceof NineLifeCat) {
                    continue;
                }
                if (relic instanceof DevilInterface) {
                    devilOnlyRelic++;
                } else {
                    switch (relic.tier) {
                        case RARE:
                            rare++;
                            break;
                        case UNCOMMON:
                            uncommon++;
                            break;
                        case COMMON:
                            common++;
                            break;
                        case SPECIAL:
                            special++;
                            break;
                        case BOSS:
                            boss++;
                            break;
                        case SHOP:
                            shop++;
                            break;
                        case STARTER:
                            starter++;
                        case DEPRECATED:
                            deprecated++;
                        default:
                            break;
                    }
                }
            }
            roll = true;
            doRoll = true;
            counter = 0;
            this.pulse = false;
        }
    }

    public void init() {
        devilOnlyRelic = 0;
        rare = 0;
        uncommon = 0;
        common = 0;
        special = 0;
        boss = 0;
        shop = 0;
        starter = 0;
        deprecated = 0;
    }

    @Override
    public void onVictory() {
        super.onVictory();
    }

    @Override
    public void update() {
        super.update();
        if (doRoll && AbstractDungeon.getMonsters() != null) {
            Field isHidden = null;
            try {
                isHidden = ProceedButton.class.getDeclaredField("isHidden");
                isHidden.setAccessible(true);
                boolean hide = isHidden.getBoolean(AbstractDungeon.overlayMenu.proceedButton);
                if (!hide && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    doRoll = false;
                    AbstractDungeon.overlayMenu.proceedButton.hide();
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
