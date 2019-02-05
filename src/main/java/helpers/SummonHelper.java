package helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SummonHelper {
    public SummonHelper() {
    }

    public static void summonMinion(AbstractMonster monster) {
        MinionHelper.addMinion(AbstractDungeon.player, monster);
    }
}