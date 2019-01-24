package patches.damage;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import helpers.BasePlayerMinionHelper;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.cards.DamageInfo",
        method = "applyPowers",
        paramtypez = {AbstractCreature.class, AbstractCreature.class}
)
public class DamageInfoPatch {
    public DamageInfoPatch() {
    }

    public static SpireReturn Prefix(DamageInfo damageInfo, AbstractCreature owner, AbstractCreature target) {
        damageInfo.output = damageInfo.base;
        damageInfo.isModified = false;
        float tmp = /*EL:36*/damageInfo.output;
        if (!owner.isPlayer) {
            if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                final float mod = AbstractDungeon.player.getBlight(/*EL:42*/"DeadlyEnemies").effectFloat();
                tmp *= mod;
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            for (final AbstractPower p : owner.powers) {
                tmp = p.atDamageGive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            if (!(target == AbstractDungeon.player && (owner instanceof AbstractMonster && BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters.contains(owner)))) {
                for (final AbstractPower p : target.powers) {
                    tmp = p.atDamageReceive(tmp, damageInfo.type);
                    if (damageInfo.base != (int) tmp) {
                        damageInfo.isModified = true;
                    }
                }
            }
            for (final AbstractPower p : owner.powers) {
                tmp = p.atDamageFinalGive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }

            if (!(target == AbstractDungeon.player && (owner instanceof AbstractMonster && BasePlayerMinionHelper.getMinions(AbstractDungeon.player).monsters.contains(owner)))) {
                for (final AbstractPower p : target.powers) {
                    tmp = p.atDamageFinalReceive(tmp, damageInfo.type);
                    if (damageInfo.base != (int) tmp) {
                        damageInfo.isModified = true;
                    }
                }
            }
            damageInfo.output = MathUtils.floor(tmp);
            if (damageInfo.output < 0) {
                damageInfo.output = 0;
            }
        } else {
//            DamageInfo.logger.info(/*EL:90*/"Damage Info calculation for Player is still in test");
            for (final AbstractPower p : owner.powers) {
                tmp = p.atDamageGive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            for (final AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            for (final AbstractPower p : owner.powers) {
                tmp = p.atDamageFinalGive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            for (final AbstractPower p : target.powers) {
                tmp = p.atDamageFinalReceive(tmp, damageInfo.type);
                if (damageInfo.base != (int) tmp) {
                    damageInfo.isModified = true;
                }
            }
            damageInfo.output = MathUtils.floor(tmp);
            if (damageInfo.output < 0) {
                damageInfo.output = 0;
            }
        }
        return SpireReturn.Return(null);
    }
}
