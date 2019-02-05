package patches.damage;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helpers.MinionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.monsters.AbstractMonster",
        method = "calculateDamage",
        paramtypez = {int.class}
)
public class AbstractMonsterPatch {
    public AbstractMonsterPatch() {
    }

    public static SpireReturn Prefix(AbstractMonster abstractMonster, int dmg) {
        final AbstractPlayer target = AbstractDungeon.player;
        float tmp = /*EL:1238*/dmg;
        /*SL:1241*/if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
            final float mod = AbstractDungeon.player.getBlight(/*EL:1242*/"DeadlyEnemies").effectFloat();
            /*SL:1243*/tmp *= mod;
        }
        /*SL:1247*/for (final AbstractPower p : abstractMonster.powers) {
            /*SL:1248*/tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL);
        }

        if (!MinionHelper.getMinions(AbstractDungeon.player).monsters.contains(abstractMonster)) {
            /*SL:1254*/for (final AbstractPower p : target.powers) {
                /*SL:1255*/tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);
            }
        }
        try {
            Method method = AbstractMonster.class.getDeclaredMethod("applyBackAttack");
            method.setAccessible(true);
            Boolean b = (Boolean) method.invoke(abstractMonster);
            if (b) {
                /*SL:1260*/tmp = (int)(tmp * 1.5f);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
//        /*SL:1259*/if (abstractMonster.applyBackAttack()) {
//            /*SL:1260*/tmp = (int)(tmp * 1.5f);
//        }
        /*SL:1264*/for (final AbstractPower p : abstractMonster.powers) {
            /*SL:1265*/tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL);
        }
        if (!MinionHelper.getMinions(AbstractDungeon.player).monsters.contains(abstractMonster)) {
            /*SL:1269*/
            for (final AbstractPower p : target.powers) {
                /*SL:1270*/
                tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL);
            }
        }
        /*SL:1274*/dmg = MathUtils.floor(tmp);
        /*SL:1275*/if (dmg < 0) {
            /*SL:1276*/dmg = 0;
        }
//        /*SL:1280*/abstractMonster.intentDmg = dmg;
        try {
            Field intentDmg = AbstractMonster.class.getDeclaredField("intentDmg");
            intentDmg.setAccessible(true);
            intentDmg.set(abstractMonster, dmg);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return SpireReturn.Return(null);
    }

}
