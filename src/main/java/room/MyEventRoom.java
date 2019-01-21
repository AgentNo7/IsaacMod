package room;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.EventRoom;
import events.HidenRoomEvent;

public class MyEventRoom extends EventRoom {

    public MyEventRoom() {
        super();
        this.event = new HidenRoomEvent();
    }

    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.event = new HidenRoomEvent();
        this.event.onEnterRoom();
    }

    private boolean used = false;

    @Override
    public void update() {
        super.update();
        if (!used) {
            this.event = new HidenRoomEvent();
            this.event.onEnterRoom();
        }
    }
}
