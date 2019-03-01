package utils;

import rewards.Pill;

public class PillsDropSave {
    public int strength;
    public int dexterity;
    public int focus;
    public int maxHp;
    public Pill.Effect effect;

    public PillsDropSave(int strength, int dexterity, int focus, int maxHp, Pill.Effect effect) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.focus = focus;
        this.maxHp = maxHp;
        this.effect = effect;
    }
}
