package room;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.TeleportEvent;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import replayTheSpire.ReplayTheSpireMod;

public class HidenRoom extends AbstractRoom{
        private MapRoomNode teleDest;

        public HidenRoom(MapRoomNode teleDest) {
            this.teleDest = teleDest;
            this.phase = RoomPhase.EVENT;
            this.mapSymbol = "PTL";
            this.mapImg = ReplayTheSpireMod.portalIcon;
            this.mapImgOutline = ReplayTheSpireMod.portalBG;
        }

        public void onPlayerEntry() {
            AbstractDungeon.overlayMenu.proceedButton.hide();
            this.event = new TeleportEvent(this.teleDest);
            this.event.onEnterRoom();
        }

    @Override
    public AbstractCard.CardRarity getCardRarity(int i) {
        return null;
    }
}
