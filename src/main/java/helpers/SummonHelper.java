package helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import relics.BFFS;

public class SummonHelper {
    public SummonHelper() {
    }

    public static void summonMinion(AbstractMonster monster) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(BFFS.ID)) {
            monster.addPower(new StrengthPower(monster,  5));
        }
        boolean canNotLose = AbstractDungeon.getCurrRoom().cannotLose;
        MinionHelper.addMinion(AbstractDungeon.player, monster);
        AbstractDungeon.getCurrRoom().cannotLose = canNotLose;
    }
}