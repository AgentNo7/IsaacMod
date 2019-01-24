package patches.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.PokeGo;

import java.util.ArrayList;
import java.util.List;


public class ChangeTargetPatch {
    public ChangeTargetPatch() {
    }

    public static AbstractCreature target = null;
    public static List<AbstractCreature> source = new ArrayList<>();

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.actions.AbstractGameAction",
            method = "setValues",
            paramtypez = {AbstractCreature.class, DamageInfo.class}
    )
    public static class ChangeDamageTarget {
        public ChangeDamageTarget() {
        }

        public static void Postfix(AbstractGameAction action, AbstractCreature target, DamageInfo info) {
            if (ChangeTargetPatch.target != null && target == AbstractDungeon.player && source.contains(info.owner)) {
                action.target = ChangeTargetPatch.target;
            }
            if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PokeGo) {
                        PokeGo pokeGo = (PokeGo) relic;
                        //宠物打人
                        if (info.owner == pokeGo.pet) {
                            action.target = pokeGo.target;
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.actions.AbstractGameAction",
            method = "setValues",
            paramtypez = {AbstractCreature.class, AbstractCreature.class, int.class}
    )
    public static class ChangeBuffTarget {
        public ChangeBuffTarget() {
        }

        public static void Postfix(AbstractGameAction action, AbstractCreature target, AbstractCreature source, int amount) {
            if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PokeGo) {
                        PokeGo pokeGo = (PokeGo) relic;
                        if (source == pokeGo.pet) {
                            //排除给自己的buff
                            if (target != source) {
                                //给怪上debuff
//                                if (pokeGo.target != null) {
                                action.target = pokeGo.target;
//                                } else {
//                                    action.target = null;
//                                }
                            }
                        }
                        //其他怪打到宠物上
                        else if (pokeGo.pet != null && pokeGo.pet.currentHealth > 0 && target == AbstractDungeon.player && ChangeTargetPatch.source.contains(source)) {
                            action.target = pokeGo.pet;
                        }
                    }
                }
            }
        }
    }


    @SpirePatch(
            cls = "com.megacrit.cardcrawl.actions.AbstractGameAction",
            method = "setValues",
            paramtypez = {AbstractCreature.class, AbstractCreature.class}
    )
    public static class ChangeBuff2Target {
        public ChangeBuff2Target() {
        }

        public static void Postfix(AbstractGameAction action, AbstractCreature target, AbstractCreature source) {
            if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PokeGo) {
                        PokeGo pokeGo = (PokeGo) relic;
                        if (source == pokeGo.pet) {
                            if (target != source) {
                                action.target = pokeGo.target;
                            }
                        }
                        if (pokeGo.pet != null && pokeGo.pet.currentHealth > 0 && ChangeTargetPatch.source.contains(source) && target == AbstractDungeon.player) {
                            action.target = pokeGo.pet;
                        }
                    }
                }
            }
        }
    }


}
