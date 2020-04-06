package relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mymod.IsaacMod;
import relics.abstracrt.ClickableRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyIntro extends ClickableRelic {
    public static final String ID = "DailyIntro";
    public static final String IMG = "images/relics/test.png";
    public static final String DESCRIPTION = "每日特效，第一局游戏获得三个相同的遗物，右击使用。";

    private boolean used = false;

    public DailyIntro() {
        this(null, 6);
    }

    private AbstractRelic relic;
    private int num;

    public DailyIntro(AbstractRelic relic, int num) {
        super("DailyIntro", new Texture(Gdx.files.internal("images/relics/test.png")), RelicTier.SPECIAL, LandingSound.CLINK);
        this.relic = relic;
        this.num = num;
        this.tips.clear();
        this.description = getUpdatedDescription();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + this.num + this.DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new DailyIntro(relic, num);
    }

    @Override
    public void onRightClick() {
        if (!used) {
            used = true;
            List<AbstractRelic> relics = new ArrayList<>();
            if (relic != null && num != 0) {
                for (int i = 0; i < num; i++) {
                    relics.add(relic);
                }
            }
            IsaacMod.obtainRelics = relics;
            IsaacMod.removeRelics = Collections.singletonList(this.relicId);
        }
    }
}
