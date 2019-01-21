package patches.power;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Buffer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.cards.blue.Buffer",
        method = "use",
        paramtypez = {AbstractPlayer.class, AbstractMonster.class}
)
public class BufferAddPatch {
    public BufferAddPatch() {
    }

    public static void Replace(Buffer buffer, AbstractPlayer player, AbstractMonster monster) {
        AbstractDungeon.player.powers.add(0, new BufferPower(AbstractDungeon.player, buffer.magicNumber));
    }
}
