package kw.maj.user;

import com.badlogic.gdx.utils.Array;

import kw.maj.model.ChiModel;

public class IUser {
    private Array<Integer> userHandPai;
    private int userId;

    private Array<ChiModel> chiModels;

    private int sex;

    public IUser(int userId,Array<Integer> userHandPai){
        this.userId = userId;
        this.userHandPai = userHandPai;
        this.sex = (int) (Math.random() * 2);
    }
    //发牌

    public void sendPai(int sendPai){
        userHandPai.add(sendPai);
        //现在需要出牌

    }

    public int getSex() {
        return sex;
    }

    //分析   用户出完牌，会进行分析，用户是否可以有碰吃等操作
    public void analy(){

    }

    public Array<Integer> getUserHandPai() {
        return userHandPai;
    }

    public int getUserId() {
        return userId;
    }
}
