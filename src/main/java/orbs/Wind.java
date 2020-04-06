package orbs;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

/**
 * Created by Keeper on 2019/3/27.
 */
public class Wind extends CustomOrb {

    public static final String ORB_ID = "Wind";
    public static final String IMG = "images/orbs/Wind.png";
    public static final String NAME;
    public static final String[] DESC;

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            NAME = "风冷元素";
            DESC = new String[]{
                    "#y被动： 在你的回合开始时，获得抽一牌 。 NL #y激发： 抽 ",
                    " 牌。 NL 风冷元素 充能球不受 #y集中 影响。"
            };
        } else {
            NAME = "Wind";
            DESC = new String[]{
                    "#yPassive: At the start of turn, draw 1 card . NL #yEvoke: draw",
                    " cards . NL Wind is unaffected by #yFocus."
            };
        }
    }

    public Wind(){
        super(ORB_ID, NAME, 1, 2, "", "", IMG);
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, basePassiveAmount));
    }

    @Override
    public void onEvoke() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, baseEvokeAmount));
    }

    @Override
    public void updateDescription() {
        this.applyFocus();
        this.description = DESC[0] + this.evokeAmount + DESC[1];
    }

    @Override
    public AbstractOrb makeCopy() {
        return new Wind();
    }

    @Override
    public void playChannelSFX() {
    }

    @Override
    public void applyFocus() {
    }


}
