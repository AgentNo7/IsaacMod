package patches.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
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
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "preBattlePrep"
    )
    public static class PreBattlePatch {
        public PreBattlePatch() {
        }

        public static void Postfix(AbstractPlayer _instance) {
//            BasePlayerMinionHelper.changeMaxMinionAmount(_instance, 100);//(Integer) PlayerAddFieldsPatch.f_baseMinions.get(_instance)
            BasePlayerMinionHelper.clearMinions(_instance);
        }
    }

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

            MonsterGroup minions = PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
            switch (AbstractDungeon.getCurrRoom().phase) {
                case COMBAT:
                    if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) {
                        minions.update();
                        minions.showIntent();
                    }
            }

        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.characters.AbstractPlayer",
            method = "render"
//            paramtypes = {SpriteBatch.class}
    )
    public static class RenderPatch {
        public RenderPatch() {
        }

        public static void Prefix(AbstractPlayer _instance, SpriteBatch sb) {
            MonsterGroup minions = PlayerAddFieldsPatch.f_minions.get(AbstractDungeon.player);
            switch (AbstractDungeon.getCurrRoom().phase) {
                case COMBAT:
                    if (BasePlayerMinionHelper.hasMinions(AbstractDungeon.player)) {
                        minions.render(sb);
                    }
            }
        }
    }

}
