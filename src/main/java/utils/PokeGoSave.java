package utils;

import java.io.Serializable;

public class PokeGoSave implements Serializable{
    public String className;
    public Integer slot;

    public PokeGoSave(String className, Integer slot) {
        this.className = className;
        this.slot = slot;
    }
}
