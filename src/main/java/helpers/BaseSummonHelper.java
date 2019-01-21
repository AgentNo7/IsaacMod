package helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BaseSummonHelper {
    public BaseSummonHelper() {
    }

    public static void summonMinion(AbstractMonster monster) {
        BasePlayerMinionHelper.addMinion(AbstractDungeon.player, monster);
    }
}