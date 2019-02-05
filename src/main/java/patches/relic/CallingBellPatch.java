package patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.CallingBell;

public class CallingBellPatch {

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.relics.CallingBell",
            method = "onEquip"
    )
    public static class atPreBattlePatch {
        public atPreBattlePatch() {
        }

        @SpireInsertPatch(loc = 44)
        public static void Insert(CallingBell callingBell) {
            callingBell.update();
        }
    }
}
