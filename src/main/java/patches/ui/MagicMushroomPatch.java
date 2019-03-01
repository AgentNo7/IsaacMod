package patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class MagicMushroomPatch {

    public static String atlasUrl = null;
    public static String skeletonUrl = null;


    @SpirePatch(
            clz = AbstractCreature.class,
            method = "loadAnimation"
    )
    public static class loadAnimation {
        public loadAnimation() {
        }

        public static void Postfix(AbstractCreature creature, String atlasUrl, String skeletonUrl, float scale) {
            if (creature instanceof AbstractPlayer) {
                MagicMushroomPatch.atlasUrl = atlasUrl;
                MagicMushroomPatch.skeletonUrl = skeletonUrl;
            }
        }
    }
}
