package patches.event;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import events.HidenRoomEvent;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.helpers.EventHelper",
        method = "getEvent"
)
public class AddEventPatch {
    public AddEventPatch() {
    }

    public static AbstractEvent Postfix(AbstractEvent event, String key) {
        //事件加入隐藏房
        if (AbstractDungeon.eventRng.randomBoolean(0.20F)) {
            return new HidenRoomEvent();
        }
        return event;
    }
}
