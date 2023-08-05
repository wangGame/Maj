package kw.maj.user;

import com.badlogic.gdx.utils.Array;

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
    private Array<OtherMode> otherModes;
    private Array<Integer> canOption;

    private int sex;

    public IUser(int userId,Array<Integer> userHandPai){
        chiModels = new Array<>();
        gangModels = new Array<>();
        pengModels = new Array<>();
        otherModes = new Array<>();
        canOption = new Array<>();
        canOption.addAll(userHandPai);
        this.userId = userId;
        this.userHandPai = userHandPai;
        this.sex = (int) (Math.random() * 2);
        userAnaly();
    }
    //发牌

    public void userAnaly(){
        Array<Integer> gangArray = AnalyseUtils.analyseGangCard(userHandPai);
        for (Integer integer : gangArray) {
            GangModel model = new GangModel();
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
            model.setPublicCard(false);
            model.setCardData(integer);
        }

        Array<int[]> ints = AnalyseUtils.analyseChiCard(userHandPai);
        for (int[] anInt : ints) {
            ChiModel model = new ChiModel();
            model.setPublicCard(false);
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


    public void sendPai(int sendPai){
        userHandPai.add(sendPai);
        //现在需要出牌

    }

    public int getSex() {
        return sex;
    }

    //分析   用户出完牌，会进行分析，用户是否可以有碰吃等操作
    public int analy(int i, int currentDesk){
        AnalyseUtils.userSendCardOrNa(i,this,currentDesk == userId);
        if (canOption.size == 0){
            System.out.println("--------------------------------");
        }

        AnalyseUtils.outFenxi(this);

        Integer integer = canOption.removeIndex(0);
        userHandPai.removeValue(integer,false);
        return integer;
    }


    public Array<Integer> getUserHandPai() {
        return userHandPai;
    }

    public int getUserId() {
        return userId;
    }

    private int peng[] = new int[6];
    private int gang[] = new int[6];
    public void peng(){
        int pengTemp[] = new int[6];
        for (Integer integer : userHandPai) {
            pengTemp[integer]++;
            if (pengTemp[integer]==2){
                peng[integer] = 2;
            }
            if (pengTemp[integer] == 3){
                peng[integer] = 3;
            }
        }
    }

    public void chi(){

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

    public Array<ChiModel> getChiModels() {
        return chiModels;
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

    public Array<GangModel> getGangModels() {
        return gangModels;
    }

    public void addCard(int i) {
        userHandPai.add(i);
        canOption.add(i);
    }

    public Array<Integer> getCanOption() {
        return canOption;
    }
}
