package actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReviveMonsterAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import monsters.minions.HushFly;

import java.util.Iterator;

@Deprecated
public class SpawnLittleFlyAction extends AbstractGameAction {
    public static final float pos0X = 210.0F;
    public static final float pos0Y = 50.0F;
    public static final float pos1X = -220.0F;
    public static final float pos1Y = 90.0F;
    private static final float pos2X = 180.0F;
    private static final float pos2Y = 320.0F;
    private static final float pos3X = -250.0F;
    private static final float pos3Y = 310.0F;

    public SpawnLittleFlyAction(AbstractMonster monster) {
        this.source = monster;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            int count = 0;
            Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var2.hasNext()) {
                AbstractMonster m = (AbstractMonster)var2.next();
                if (m != this.source) {
                    if (m.isDying) {
                        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, m, new MinionPower(this.source)));
                        AbstractDungeon.actionManager.addToTop(new ReviveMonsterAction(m, this.source, false));
                        if (AbstractDungeon.player.hasRelic("Philosopher's Stone")) {
                            m.addPower(new StrengthPower(m, 1));
                        }

                        if (ModHelper.isModEnabled("Lethality")) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, 3), 3));
                        }

                        if (ModHelper.isModEnabled("Time Dilation")) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SlowPower(m, 0)));
                        }

                        this.tickDuration();
                        return;
                    }

                    ++count;
                }
            }

            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new HushFly(-220.0F + count, 90.0F), true));
//            if (count == 1) {
//                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new HushFly(-220.0F, 90.0F), true));
//            } else if (count == 2) {
//                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new HushFly(180.0F, 320.0F), true));
//            } else if (count == 3) {
//                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new HushFly(-250.0F, 310.0F), true));
//            }
        }

        this.tickDuration();
    }
}
