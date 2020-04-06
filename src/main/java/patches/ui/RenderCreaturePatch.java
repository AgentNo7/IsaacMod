package patches.ui;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import utils.Invoker;

public class RenderCreaturePatch {

    public static String atlasUrl = null;
    public static String skeletonUrl = null;
    public static boolean haveJudas = false;

    public static Skeleton isDeliriousSkeleton = null;

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loadAnimation"
    )
    public static class loadAnimation {
        public loadAnimation() {
        }

        public static void Postfix(AbstractCreature creature, String atlasUrl, String skeletonUrl, float scale) {
            if (creature instanceof AbstractPlayer) {
                RenderCreaturePatch.atlasUrl = atlasUrl;
                RenderCreaturePatch.skeletonUrl = skeletonUrl;
            }
        }
    }

    @SpirePatch(
            clz = Skeleton.class,
            method = "setColor"
    )
    public static class setColorPatch {
        public setColorPatch() {
        }

        public static void Postfix(Skeleton skeleton, final Color color1) {
            if (haveJudas) {
                Skeleton skeleton1 = Invoker.getField(AbstractDungeon.player, "skeleton");

                if (AbstractDungeon.player != null && skeleton1 == skeleton) {
                    if (color1 != Color.BLACK) {
                        skeleton.setColor(Color.BLACK);
                    }
                }
            }
        }
    }

}
