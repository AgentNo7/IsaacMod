package patches.player;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import relics.abstracrt.ResurrectRelic;

import java.util.ArrayList;
import java.util.List;

import static patches.ui.SoulHeartPatch.blackHeart;
import static patches.ui.SoulHeartPatch.soulHeart;

public class PlayerDamagePatch {

    public static List<ResurrectRelic> resurrectRelics = new ArrayList<>();

    public static Comparator comparator = new Comparator();


    @SpirePatch(
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "damage"
    )
    public static class DeathPatch {
        public DeathPatch() {
        }

        public static void Postfix(AbstractPlayer player, DamageInfo info) {

        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class damagePatch {
        public damagePatch() {
        }

        @SpireInsertPatch(
                rloc = 83,
                localvars = {"damageAmount"}
        )
        public static void Insert(AbstractPlayer player, final DamageInfo info, @ByRef int[] damageAmount) { //damageAmount 必定>0 //1769 - 1686
            if (blackHeart > 0) {
                if (damageAmount[0] > blackHeart % 10) {
                    blackHeart = blackHeart <= damageAmount[0] ? 0 : blackHeart - damageAmount[0];
                    if (AbstractDungeon.getMonsters() != null) {
                        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(player, DamageInfo.createDamageMatrix(40, false), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
                    }
                    } else {
                    blackHeart -= damageAmount[0];
                }
                damageAmount[0] = 0;
            } else if (soulHeart > 0) {
                soulHeart = soulHeart <= damageAmount[0] ? 0 : soulHeart - damageAmount[0];
                damageAmount[0] = 0;
            }
        }

        @SpireInsertPatch(
                rloc = 126
        )
        public static SpireReturn Insert2(AbstractPlayer player, final DamageInfo info) { ///*SL:1819*/if (player.currentHealth < 1) { //rloc = 133
            if (player.currentHealth < 1) {
                if (resurrectRelics.size() > 0 && !AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID)) {
                    resurrectRelics.sort(comparator);
                    for (ResurrectRelic resurrectRelic : resurrectRelics) {
                        if (resurrectRelic.canResurrect()) {
                            player.currentHealth = 0;
                            player.heal(resurrectRelic.onResurrect());
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, resurrectRelic));
                            return SpireReturn.Return(null);
                        }
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    public static class Comparator implements java.util.Comparator<ResurrectRelic> {

        @Override
        public int compare(ResurrectRelic o1, ResurrectRelic o2) {
            return o1.priority - o2.priority;
        }
    }
}
