package kw.maj.user;

import com.badlogic.gdx.utils.Array;

import java.awt.event.ItemEvent;
import java.util.Random;

import kw.maj.analyse.AnalyseUtils;
import kw.maj.model.ChiModel;
import kw.maj.model.GangModel;
import kw.maj.model.PengModel;

/**
 * 牌面：另一个游戏是纪录某种牌面的个数，我是通过数组的方式存储所有，那么就需要每次改变对自己的牌进行分析
 *
 * 分析过程：
 * - 牌的个数是4  杠  碰
 * - 分析胡牌
 *  创建临时的牌面 14张
 *  - 分析数目
 *   -单调判断
 */
public class IUser {
    private Array<Integer> userHandPai;
    private int userId;
    private Array<ChiModel> chiModels;
    private Array<GangModel> gangModels;
    private Array<PengModel> pengModels;
    private Array<Integer> canOption;
    private int sex;
    private int daQue = 0; //选择打雀

    public IUser(int userId,Array<Integer> userHandPai){
        chiModels = new Array<>();
        gangModels = new Array<>();
        pengModels = new Array<>();
        canOption = new Array<>();
        canOption.addAll(userHandPai);
        this.userId = userId;
        this.userHandPai = userHandPai;
        this.sex = (int) (Math.random() * 2);
    }
    //发牌

    public void userAnaly(){
        Array<Integer> gangArray = AnalyseUtils.analyseGangCard(userHandPai);
        for (Integer integer : gangArray) {
            GangModel model = new GangModel();
            int cardData = model.getCardData();
            int cardType = AnalyseUtils.getCardType(cardData);
            if (cardType == daQue)break;
            model.setPublicCard(false);
            model.setCardData(integer);
            gangModels.add(model);
            for (int i = 0; i < 4; i++) {
                canOption.removeValue(model.getCardData(),false);
            }
        }
        Array<Integer> pengArray = AnalyseUtils.analysePengCard(userHandPai);
        for (Integer integer : pengArray) {
            PengModel model = new PengModel();
            int cardData = model.getCardData();
            int cardType = AnalyseUtils.getCardType(cardData);
            if (cardType == daQue)break;
            model.setPublicCard(false);
            model.setCardData(integer);
        }

        Array<int[]> ints = AnalyseUtils.analyseChiCard(userHandPai);
        for (int[] anInt : ints) {
            ChiModel model = new ChiModel();
            model.setPublicCard(false);
            int modelType = model.getType();
            if (modelType == daQue)break;
            Array<Integer> array= new Array<>();
            for (int i : anInt) {
                array.add(i);
            }
            int cardType = AnalyseUtils.getCardType(anInt[0]);
            model.setType(cardType);
            model.setPaiId(array);
            chiModels.add(model);
        }
    }

    public int getSex() {
        return sex;
    }

    //分析   用户出完牌，会进行分析，用户是否可以有碰吃等操作
    public int analy(int i, int currentDesk){
        return AnalyseUtils.userSendCardOrNa(i,this,currentDesk == userId);
    }

    public int outCard(){
        Array<Integer> array = AnalyseUtils.outFenxi(this);
        for (Integer integer : canOption) {
            int cardType = AnalyseUtils.getCardType(integer);
            if (cardType == daQue){
                userHandPai.removeValue(integer,false);
                canOption.removeValue(integer,false);
                return integer;
            }
        }
        Integer integer  = 0 ;
         if (array.size<0){
             integer = canOption.removeIndex(0);
             userHandPai.removeValue(integer,false);
         }else {
             integer = array.get(0);
             canOption.removeValue(integer,false);
             userHandPai.removeValue(integer,false);
         }
        return integer;
    }


    public Array<Integer> getUserHandPai() {
        return userHandPai;
    }

    public int getUserId() {
        return userId;
    }

    public void addGang(GangModel model){
        gangModels.add(model);
        if (model.isPublicCard())return;
        for (int i = 0; i < 4; i++) {
            canOption.removeValue(model.getCardData(),false);
        }
    }

    public Array<PengModel> getPengModels() {
        return pengModels;
    }

    public void addPeng(PengModel model) {
        pengModels.add(model);
        if (model.isPublicCard())return;
        for (int i = 0; i < 3; i++) {
            canOption.removeValue(model.getCardData(),false);
        }
    }

    public void addChi(ChiModel model) {
        chiModels.add(model);
        if (model.isPublicCard()) {
            for (Integer integer : model.getPaiId()) {
                canOption.removeValue(integer,false);
            }
        }
    }

    public void addCard(int i) {
        userHandPai.add(i);
        canOption.add(i);
    }

    public Array<Integer> getCanOption() {
        return canOption;
    }

    public void select() {
        daQue = (int)((Math.random() * 3) + 1);
        userAnaly();
    }

    public void setDaQue(int daQue) {
        this.daQue = daQue;
    }
}
