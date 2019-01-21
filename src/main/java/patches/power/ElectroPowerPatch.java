package patches.power;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.blue.Electrodynamics;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;

@SpirePatch(
        cls = "com.megacrit.cardcrawl.cards.blue.Electrodynamics",
        method = "use",
        paramtypez = {AbstractPlayer.class, AbstractMonster.class}
)
public class ElectroPowerPatch {
    public ElectroPowerPatch() {
    }

    public static void Replace(Electrodynamics electrodynamics, AbstractPlayer player, AbstractMonster monster) {
        if (!player.hasPower("Electrodynamics") && !player.hasPower(powers.ElectroPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ElectroPower(player)));
        }

        for(int i = 0; i < electrodynamics.magicNumber; ++i) {
            AbstractOrb orb = new Lightning();
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(orb));
        }
    }
}
