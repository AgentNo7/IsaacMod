package rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;

public abstract class Heart extends CustomReward{

    public String id;

    public int amount;

    public Heart(Texture icon, String text, RewardType type, String id) {
        super(icon, text, type);
        this.id = id;
    }

    public static Heart getHeartById(String id, int amount) {
        if (BlackHeart.ID.equals(id)) {
            return new BlackHeart(amount);
        }
        if (SoulHeart.ID.equals(id)) {
            return new SoulHeart(amount);
        }
        return null;
    }
}
