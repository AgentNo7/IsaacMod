package patches.room;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static utils.Utils.getRandomRelicRng;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.rooms.AbstractRoom",
        method = "addRelicToRewards",
        paramtypez = {RelicTier.class}
)
public class RelicRewardPatch {
    public RelicRewardPatch() {
    }

    public static void Replace(AbstractRoom room, RelicTier tier) {
        if (AbstractDungeon.relicRng.randomBoolean(0.15F)) {
            CustomRelic relic = getRandomRelicRng();
            if (relic != null && !AbstractDungeon.player.hasRelic(relic.relicId)) {
                room.rewards.add(new RewardItem(relic));
            } else {
                room.rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(tier)));
            }
        } else {
            room.rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(tier)));
        }
    }
}
