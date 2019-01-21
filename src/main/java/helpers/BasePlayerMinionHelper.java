package helpers;


import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import patches.player.PlayerAddFieldsPatch;

import java.util.Objects;

public class BasePlayerMinionHelper {
    public BasePlayerMinionHelper() {
    }

    public static MonsterGroup getMinions(AbstractPlayer player) {
        return (MonsterGroup) PlayerAddFieldsPatch.f_minions.get(player);
    }

    public static void changeMaxMinionAmount(AbstractPlayer player, int newMax) {
        PlayerAddFieldsPatch.f_maxMinions.set(player, newMax);
    }

    public static boolean addMinion(AbstractPlayer player, AbstractMonster minionToAdd) {
        MonsterGroup minions = (MonsterGroup)PlayerAddFieldsPatch.f_minions.get(player);
        int maxMinions = (Integer)PlayerAddFieldsPatch.f_maxMinions.get(player);
        if (minions.monsters.size() == maxMinions) {
            return false;
        } else {
            minionToAdd.init();
            minionToAdd.usePreBattleAction();
            minionToAdd.showHealthBar();
            minions.add(minionToAdd);
            return true;
        }
    }

    public static boolean removeMinion(AbstractPlayer player, AbstractMonster minionToRemove) {
        return ((MonsterGroup)PlayerAddFieldsPatch.f_minions.get(player)).monsters.remove(minionToRemove);
    }

    public static boolean hasMinions(AbstractPlayer player) {
        return ((MonsterGroup)PlayerAddFieldsPatch.f_minions.get(player)).monsters.size() > 0;
    }

    public static int getMaxMinions(AbstractPlayer player) {
        return (Integer)PlayerAddFieldsPatch.f_maxMinions.get(player);
    }

    public static void clearMinions(AbstractPlayer player) {
        MonsterGroup minions = new MonsterGroup(new AbstractMonster[]{});
        minions.monsters.removeIf(Objects::isNull);
        PlayerAddFieldsPatch.f_minions.set(player, minions);
    }
}