package patches.action;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Arrays;

public class DamageAllEnemiesActionPatch {

    @SpirePatch(clz = DamageAllEnemiesAction.class, method = "update")
    public static class DamageAllPatch {
        @SpireInsertPatch(rloc = 40, localvars = {"i", "temp"})
        public static void Insert(DamageAllEnemiesAction action, @ByRef int[] i, @ByRef int[] temp) {
            int size = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            if (action.damage.length != size) {
                int[] newDamage = Arrays.copyOf(action.damage, size);
                if (action.damage.length >= 1) {
                    for (int j = action.damage.length; j < newDamage.length; j++) {
                        newDamage[j] = action.damage[action.damage.length - 1];
                    }
                }
                action.damage = newDamage;
            }
            if (temp[0] != size) {
                temp[0] = size;
            }
            if (i[0] >= temp[0]) {
                i[0] = temp[0] - 1;
            }
        }
    }
}
