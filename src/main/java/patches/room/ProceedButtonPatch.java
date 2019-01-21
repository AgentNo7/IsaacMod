package patches.room;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.CallingBell;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import screen.BloodShopScreen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static relics.HushsDoor.isOn;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.ui.buttons.ProceedButton",
        method = "update",
        paramtypez = {}
)
public class ProceedButtonPatch {
    @SpireInsertPatch(
            loc = 129
    )
    public static void Insert(ProceedButton button) {
        //反射私有方法
        try {
            if (AbstractDungeon.shopScreen instanceof BloodShopScreen) {
                if (AbstractDungeon.player.hasRelic(CallingBell.ID) && !isOn) {
                    isOn = true;
                    AbstractDungeon.gridSelectScreen.hide();
                    AbstractDungeon.shopScreen.open();
                } else {
                    if (Settings.isDemo) {
                        Method tickDuration = ProceedButton.class.getDeclaredMethod("goToDemoVictoryRoom");
                        tickDuration.setAccessible(true);
                        tickDuration.invoke(AbstractDungeon.overlayMenu.proceedButton);
                    } else {
                        Method goToNextDungeon = ProceedButton.class.getDeclaredMethod("goToNextDungeon", AbstractRoom.class);
                        goToNextDungeon.setAccessible(true);
                        goToNextDungeon.invoke(AbstractDungeon.overlayMenu.proceedButton, new TreasureRoomBoss());
                    }
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
