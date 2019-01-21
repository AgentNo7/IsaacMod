package patches.player;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import helpers.BasePlayerMinionHelper;

public class PlayerMethodPatches {
    public PlayerMethodPatches() {
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.core.AbstractCreature",
            method = "updatePowers"
    )
    public static class UpdatePowersPatch {
        public UpdatePowersPatch() {
        }

        public static void Postfix(AbstractCreature _instance) {
            if (_instance instanceof AbstractPlayer) {
                (PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.updatePowers();
                });
            }

        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.core.AbstractCreature",
            method = "applyStartOfTurnPowers"
    )
    public static class ApplyStartOfTurnPowersPatch {
        public ApplyStartOfTurnPowersPatch() {
        }

        public static void Postfix(AbstractCreature _instance) {
            if (_instance instanceof AbstractPlayer) {
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.applyStartOfTurnPowers();
                });
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.loseBlock();
                });
            }

        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.core.AbstractCreature",
            method = "applyStartOfTurnPostDrawPowers"
    )
    public static class ApplyStartOfTurnPostDrawPowersPatch {
        public ApplyStartOfTurnPostDrawPowersPatch() {
        }

        public static void Postfix(AbstractCreature _instance) {
            if (_instance instanceof AbstractPlayer) {
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.applyStartOfTurnPostDrawPowers();
                });
            }

        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.core.AbstractCreature",
            method = "applyTurnPowers"
    )
    public static class ApplyTurnPowersPatch {
        public ApplyTurnPowersPatch() {
        }

        public static void Postfix(AbstractCreature _instance) {
            if (_instance instanceof AbstractPlayer) {
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.applyTurnPowers();
                });
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.core.AbstractCreature",
            method = "applyEndOfTurnTriggers"
    )
    public static class EndOfTurnPatch {
        public EndOfTurnPatch() {
        }

        public static void Prefix(AbstractCreature _instance) { //postfix
            if (_instance instanceof AbstractPlayer) {
                BaseMod.logger.info("----------- Minion Before Attacking --------------");
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.takeTurn();
                });
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.applyEndOfTurnTriggers();
                });
                ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.forEach((monster) -> {
                    monster.powers.forEach((power) -> {
                        power.atEndOfRound();
                    });
                });
            }

        }
    }

//    @SpirePatch(
//            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
//            method = "preBattlePrep"
//    )
//    public static class PreBattlePatch {
//        public PreBattlePatch() {
//        }
//
//        public static void Postfix(AbstractPlayer _instance) {
//            BasePlayerMinionHelper.changeMaxMinionAmount(_instance, 100);//(Integer) PlayerAddFieldsPatch.f_baseMinions.get(_instance)
//            BasePlayerMinionHelper.clearMinions(_instance);
//        }
//    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "update"
    )
    public static class UpdatePatch {
        public UpdatePatch() {
        }

        public static void Postfix(AbstractPlayer player) {
            if (AbstractPlayerClickField.RclickStart.get(player) && InputHelper.justReleasedClickRight) {
                if (player.hb.hovered) {
                    AbstractPlayerClickField.Rclick.set(player, true);
                }
                AbstractPlayerClickField.RclickStart.set(player, false);
            }
            if ((player.hb != null) && ((player.hb.hovered) && (InputHelper.justClickedRight))) {
                AbstractPlayerClickField.RclickStart.set(player, true);
            }
            if (AbstractPlayerClickField.Rclick.get(player)) {
                AbstractPlayerClickField.Rclick.set(player, false);
                //右击动作
                AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(player));
            }

            MonsterGroup minions = (MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                switch (AbstractDungeon.getCurrRoom().phase) {
                    case COMBAT:
                        if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) {
                            minions.update();
                            minions.showIntent();
                        }
                }
            }

        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "render",
            paramtypes = {"com.badlogic.gdx.graphics.g2d.SpriteBatch"}
    )
    public static class RenderPatch {
        public RenderPatch() {
        }

        public static void Prefix(AbstractPlayer _instance, SpriteBatch sb) {
            MonsterGroup minions = (MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
            if (AbstractDungeon.getCurrRoom() != null && (AbstractDungeon.getCurrRoom() instanceof MonsterRoom || AbstractDungeon.getCurrRoom() instanceof EventRoom)) {
                switch (AbstractDungeon.getCurrRoom().phase) {
                    case COMBAT:
                        if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) {
                            minions.render(sb);
                        }
                }
            }

        }
    }

//    @SpirePatch(
//            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
//            method = "damage",
//            paramtypes = {"com.megacrit.cardcrawl.cards.DamageInfo"}
//    )
//    public static class DamagePatch {
//        public DamagePatch() {
//        }
//
////        public static SpireReturn Prefix(AbstractPlayer _instance, DamageInfo info) {
////            boolean attackingMonster = true;
//////            if (info.owner instanceof AbstractMonster) {
//////                AbstractMonster owner = (AbstractMonster) info.owner;
//////                attackingMonster = checkAttackMonsterIntent(owner.intent);
//////            }
////
////            if (attackingMonster) {
////                AbstractDungeon.actionManager.addToBottom(new DamageAction(MonsterHelper.getTarget((AbstractMonster) info.owner), info, AbstractGameAction.AttackEffect.NONE));
////                return SpireReturn.Return((Object) null);
////            } else {
////                return SpireReturn.Continue();
////            }
////        }
//
////        private static boolean checkAttackMonsterIntent(AbstractMonster.Intent intent) {
////            return intent == MonsterIntentEnum.ATTACK_MINION || intent == MonsterIntentEnum.ATTACK_MINION_BUFF || intent == MonsterIntentEnum.ATTACK_MINION_DEBUFF || intent == MonsterIntentEnum.ATTACK_MINION_DEFEND;
////        }
//
//        private static void damageFriendlyMonster(DamageInfo info) {
//            MonsterGroup minions = (MonsterGroup) kobting.friendlyminions.patches.PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
//            int randomMinionIndex = AbstractDungeon.aiRng.random(minions.monsters.size() - 1);
//            AbstractFriendlyMonster minion = (AbstractFriendlyMonster) ((MonsterGroup) kobting.friendlyminions.patches.PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.get(randomMinionIndex);
//            info.applyPowers(info.owner, minion);
//            AbstractDungeon.actionManager.addToBottom(new DamageAction((AbstractCreature) ((MonsterGroup) PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player)).monsters.get(randomMinionIndex), info, AbstractGameAction.AttackEffect.NONE));
//        }
//    }

//    @SpirePatch(
//            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
//            method = "initializeClass"
//    )
//    public static class InitializePatch {
//        public InitializePatch() {
//        }
//
//        public static void Prefix(AbstractPlayer _instance) {
//            BasePlayerMinionHelper.clearMinions(_instance);
//        }
//    }
}
