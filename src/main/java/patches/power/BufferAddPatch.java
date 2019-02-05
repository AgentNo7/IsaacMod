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
        if (AbstractDungeon.player.powers != null && AbstractDungeon.player.powers.size() > 0 && AbstractDungeon.player.powers.get(0) != null && AbstractDungeon.player.powers.get(0).ID.equals(BufferPower.POWER_ID)) {
            AbstractDungeon.player.powers.get(0).amount += buffer.magicNumber;
        } else {
            AbstractDungeon.player.powers.add(0, new BufferPower(AbstractDungeon.player, buffer.magicNumber));
        }
    }
}
