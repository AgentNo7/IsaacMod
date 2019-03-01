package patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

public class CombatRewardScreenPatch {

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.screens.CombatRewardScreen",
            method = "open",
            paramtypez = {String.class}
    )
    public static class CombatRewardScreenOpenPatch {
        public CombatRewardScreenOpenPatch() {
        }

        public static SpireReturn Prefix(CombatRewardScreen screen, String setLabel) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                screen.setupItemReward();
                screen.rewards.clear();
                for (RewardItem rewardItem : AbstractDungeon.getCurrRoom().rewards) {
                    if (rewardItem.relic == null) {
                        screen.rewards.add(rewardItem);
                    }
                }
                AbstractDungeon.player.reorganizeRelics();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

//    @SpirePatch(
//            cls = "com.megacrit.cardcrawl.rooms.AbstractRoom",
//            method = "addRelicToRewards",
//            paramtypez = {AbstractRelic.RelicTier.class}
//    )
//    public static class atBattleStartPatch {
//        public atBattleStartPatch() {
//        }
//
//        public static void Replace(AbstractRoom buffer, AbstractRelic.RelicTier tier) {
//            buffer.rewards.add(new RewardItem(new Cauldron()));
//        }
//    }
}
