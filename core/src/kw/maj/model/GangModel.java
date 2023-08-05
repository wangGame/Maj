package kw.maj.model;

public class GangModel {
    private boolean isPublicCard;
    private int cardData;

    public void setPublicCard(boolean publicCard) {
        isPublicCard = publicCard;
    }

    public void setCardData(int cardData) {
        this.cardData = cardData;
    }

    public boolean isPublicCard() {
        return isPublicCard;
    }

    public int getCardData() {
        return cardData;
    }
}
