package kw.maj.model;

import com.badlogic.gdx.utils.Array;

public class ChiModel {
    private Array<Integer> paiId;
    private int type;//万  饼等

    private boolean isPublicCard;

    public boolean isPublicCard() {
        return isPublicCard;
    }

    public void setPublicCard(boolean publicCard) {
        isPublicCard = publicCard;
    }

    public void setPaiId(Array<Integer> paiId) {
        this.paiId = paiId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Array<Integer> getPaiId() {
        return paiId;
    }

    public int getType() {
        return type;
    }
}
