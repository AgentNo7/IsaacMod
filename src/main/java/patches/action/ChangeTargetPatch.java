package patches.action;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relics.PokeGo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType.THORNS;


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
            //忽略反伤
            if (info.type == THORNS) {
                return;
            }
            //怪物不打人，打宠物
            if (ChangeTargetPatch.target != null && target == AbstractDungeon.player && source.contains(info.owner)) {
                action.target = ChangeTargetPatch.target;
            }
            if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PokeGo) {
                        PokeGo pokeGo = (PokeGo) relic;
                        //宠物打怪
                        if (info.owner == pokeGo.pet) {
                            action.target = pokeGo.target;
                            //带壳寄生怪
                            if (action instanceof VampireDamageAction) {
                                action.target = info.owner;
                            }
                            if (action.target == null) {
                                try {
                                    Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
                                    method.setAccessible(true);
                                    method.invoke(action);
                                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            info.applyPowers(pokeGo.pet, action.target);
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
                        //宠物的动作
                        if (source != null && source == pokeGo.pet) {
                            //给怪上debuff
                            action.target = pokeGo.target;
                            //一些动作给自己
                            if (source == target || action instanceof GainBlockAction || action instanceof HealAction) {
                                action.target = source;
                            }
                            //不指定就跳过
                            if (action.target == null) {
                                try {
                                    Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
                                    method.setAccessible(true);
                                    method.invoke(action);
                                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //其他怪debuff到宠物上
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
            if (source instanceof SpireShield && target == AbstractDungeon.player) {
                return;
            }
            if (AbstractDungeon.player.hasRelic(PokeGo.ID)) {
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic instanceof PokeGo) {
                        PokeGo pokeGo = (PokeGo) relic;
                        if (source != null && source == pokeGo.pet) {
                            action.target = pokeGo.target;
                            if (source == target || action instanceof GainBlockAction || action instanceof HealAction) {
                                action.target = source;
                            }
                            if (action.target == null) {
                                try {
                                    Method method = AbstractGameAction.class.getDeclaredMethod("tickDuration");
                                    method.setAccessible(true);
                                    method.invoke(action);
                                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
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
