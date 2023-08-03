package kw.maj.model;

public class PengModel {

    //杠  碰
    private int pengType;
    private int paiId;

    public void setPaiId(int paiId) {
        this.paiId = paiId;
    }

    public void setPengType(int pengType) {
        this.pengType = pengType;
    }

    public int getPaiId() {
        return paiId;
    }

    public int getPengType() {
        return pengType;
    }
}
