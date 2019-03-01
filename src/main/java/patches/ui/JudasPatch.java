package patches.ui;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import utils.Invoker;

public class JudasPatch {

    public static boolean haveJudas = false;

    @SpirePatch(
            clz = Skeleton.class,
            method = "setColor"
    )
    public static class setColorPatch {
        public setColorPatch() {
        }

        public static void Postfix(Skeleton skeleton, final Color color1) {
            if (!haveJudas) {
                return;
            }
            Skeleton skeleton1 = Invoker.getField(AbstractDungeon.player, "skeleton");

            if (AbstractDungeon.player != null && skeleton1 == skeleton) {
                if (color1 != Color.BLACK) {
                    skeleton.setColor(Color.BLACK);
                }
            }
        }
    }
}
