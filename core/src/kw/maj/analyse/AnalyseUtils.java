package kw.maj.analyse;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import kw.maj.constant.Constant;
import kw.maj.model.ChiModel;
import kw.maj.model.GangModel;
import kw.maj.model.PengModel;
import kw.maj.user.IUser;

public class AnalyseUtils {
    public static Array<Integer> analyseGangCard(Array<Integer> handCard){
//        int arr[][] = new int[4][4];
//        for (int i = 0; i < arr.length; i++) {
//            for (int i1 = 0; i1 < arr[0].length; i1++) {
//                arr[i][i1] = 0;
//            }
//        }
//        Array<Integer> gangArray = new Array<>();
//        for (Integer integer : handCard) {
//            int cardType = getCardType(integer);
//            int cardValue = getCardValue(integer);
//            arr[cardType-1][cardValue]++;
//            if (arr[cardType-1][cardValue] == 4){
//                gangArray.add(integer);
//            }
//        }
//        return gangArray;


        ArrayMap<Integer,Integer> arr = new ArrayMap<>();
        Array<Integer> gangArray = new Array<>();
        for (Integer integer : handCard) {
            if (arr.containsKey(integer)) {
                int integer1 = arr.get(integer);
                integer1 ++;
                arr.put(integer,integer1);
                if (integer1 == 4){
                    gangArray.add(integer);
                }
            }else {
                arr.put(integer,1);
            }
        }
        return gangArray;
    }

    public static int getCardType(int cardData){
        return ((cardData & Constant.MASK_COLOR) >> 4)+1;
    }


    public static int getReCardType(int cardData){
        return ((cardData & Constant.MASK_COLOR) >> 4)+1;
    }


    public static int getCardValue(int cardData){
        return (cardData & Constant.MASK_VALUE);
    }

    public static Array<Integer> analysePengCard(Array<Integer> handCard) {
        ArrayMap<Integer,Integer> arr = new ArrayMap<>();
        Array<Integer> pengArray = new Array<>();
        for (Integer integer : handCard) {
            if (arr.containsKey(integer)) {
                int integer1 = arr.get(integer);
                integer1 ++;
                arr.put(integer,integer1);
            }else {
                arr.put(integer,1);
            }
        }
        for (ObjectMap.Entry<Integer, Integer> integerIntegerEntry : arr) {
            Integer key = integerIntegerEntry.key;
            Integer value = integerIntegerEntry.value;
            if (value==3){
                pengArray.add(key);
            }
        }
        return pengArray;
    }

    /**
     * 这个主要是找手上自己的牌，可以组成吃的状态，  用户如果发牌 的时候   只需要关系剩下的牌，
     *
     *
     * 对于杠默认会保留
     *
     * 对于碰后面需要进行杠判断
     *
     * 对于吃  可能会剩下其他的 ，对于吃还需要在进行分析
     * @param userHandPai
     * @return
     */
    public static Array<int[]> analyseChiCard(Array<Integer> userHandPai) {
        userHandPai.sort();
        int [][] tempArr = new int[5][10] ;
        for (Integer integer : userHandPai) {
            int cardValue = getCardValue(integer);
            int cardType = getCardType(integer);
            tempArr[cardType][cardValue] = 0;
            if (cardType==4)continue;
            tempArr[cardType][cardValue] = integer;
        }

        Array<int[]> array = new Array<>();
        for (int i = 0; i < 4; i++) {
            int[] ints = tempArr[i];
            for (int i1 = 0; i1 < ints.length-2; i1++) {
                for (int i2 = i1+1; i2 < ints.length-1; i2++) {
                    if (ints[i2]<=0)continue;
                    if (ints[i2-1]<=0)continue;
                    if (ints[i2+1]<=0)continue;
                    array.add(new int[]{ints[i2-1],ints[i2],ints[i2+1]});
                }
            }
        }
        return array;
    }

    public static void userSendCardOrNa(int newCard, IUser iUser, boolean isCurrentUser){
        int cardType = getCardType(newCard);
        int cardValue = getCardValue(newCard);
        //检测杠
        boolean gang = false;
        PengModel pengModelTemp = null;
        Array<Integer> userHandPai = iUser.getUserHandPai();
        Array<PengModel> pengModels = iUser.getPengModels();
        for (PengModel pengModel : pengModels) {
            int cardData = pengModel.getCardData();
            if (cardData == newCard) {
                //杠
                if (isCurrentUser){
                    pengModelTemp = pengModel;
                    gang = true;
                }
            }
        }
        if (gang) {
            pengModels.removeValue(pengModelTemp, false);
            GangModel gmodel = new GangModel();
            gmodel.setPublicCard(!isCurrentUser);
            gmodel.setCardData(newCard);
            iUser.addGang(gmodel);
            return;
        }

        int [][] tempArr = new int[5][10] ;
        for (Integer integer : userHandPai) {
            int cardValueTemp = getCardValue(integer);
            int cardTypeTemp = getCardType(integer);
            tempArr[cardTypeTemp][cardValueTemp] = 0;
            if (cardTypeTemp==4)continue;
            tempArr[cardTypeTemp][cardValueTemp] ++;
        }

        if (tempArr[cardType][cardValue] == 2) {
            //碰
            PengModel model = new PengModel();
            model.setPublicCard(!isCurrentUser);
            model.setCardData(newCard);
            iUser.addPeng(model);
            return;
        }

        //吃
        if (cardType!=4){
            //
//            if (cardValue==1) {
//                if (tempArr[cardType][cardValue+1] == newCard+1) {
//                    if (tempArr[cardType][cardValue+2] == newCard+2) {
//                        //chi
//                        ChiModel model = new ChiModel();
//                        model.setPublicCard(!isCurrentUser);
//                        Array<Integer>arrayTem = new Array<>();
//                        arrayTem.add(newCard,newCard+1,newCard+2);
//                        model.setPaiId(arrayTem);
//                        iUser.addChi(model);
//                        return;
//                    }
//                }
//            }else if (cardValue == 9){
//                if (tempArr[cardType][cardValue-1] == newCard-1) {
//                    if (tempArr[cardType][cardValue-2] == newCard-2) {
//                        //chi
//                        ChiModel model = new ChiModel();
//                        model.setPublicCard(!isCurrentUser);
//                        Array<Integer>arrayTem = new Array<>();
//                        arrayTem.add(newCard,newCard-1,newCard-2);
//                        model.setPaiId(arrayTem);
//                        iUser.addChi(model);
//                        return;
//                    }
//                }
//            }else {
                if (cardValue+2<=9){
                    if (tempArr[cardType][cardValue+1] == newCard+1) {
                        if (tempArr[cardType][cardValue+2] == newCard+2) {
                            //chi

                            ChiModel model = new ChiModel();
                            model.setPublicCard(!isCurrentUser);
                            Array<Integer>arrayTem = new Array<>();
                            arrayTem.add(newCard,newCard+1,newCard+2);
                            model.setPaiId(arrayTem);
                            iUser.addChi(model);
                            return;
                        }
                    }
                }
                if (cardValue - 2 >=1) {
                    if (tempArr[cardType][cardValue - 1] == newCard - 1) {
                        if (tempArr[cardType][cardValue - 2] == newCard - 2) {
                            //chi

                            ChiModel model = new ChiModel();
                            model.setPublicCard(!isCurrentUser);
                            Array<Integer> arrayTem = new Array<>();
                            arrayTem.add(newCard, newCard - 1, newCard - 2);
                            model.setPaiId(arrayTem);
                            iUser.addChi(model);
                            return;
                        }
                    }
                }
                if (cardValue-1>=1 && cardValue+1<=9) {
                    if (tempArr[cardType][cardValue - 1] == newCard - 1) {
                        if (tempArr[cardType][cardValue + 1] == newCard + 1) {
                            //chi

                            ChiModel model = new ChiModel();
                            model.setPublicCard(!isCurrentUser);
                            Array<Integer> arrayTem = new Array<>();
                            arrayTem.add(newCard, newCard - 1, newCard + 1);
                            model.setPaiId(arrayTem);
                            iUser.addChi(model);
                            return;
                        }
                    }
                }

//            }
        }

        //
//        Array<ChiModel> chiModels = iUser.getChiModels();
//        for (ChiModel chiModel : chiModels) {
//            if (chiModel.getType() == cardType) {
//
//            }
//        }
    }

    public static void outFenxi(IUser iUser) {
        Array<Integer> canOption = iUser.getCanOption();
        Array<Integer> temp = new Array<>(canOption);

        Array<Integer> array= new Array<>();
        int [][] tempArr = new int[5][10] ;
        for (Integer integer : canOption) {
            int cardValueTemp = getCardValue(integer);
            int cardTypeTemp = getCardType(integer);
            tempArr[cardTypeTemp][cardValueTemp] = 0;
            if (cardTypeTemp==4)continue;
            tempArr[cardTypeTemp][cardValueTemp] ++;
            if (tempArr[cardTypeTemp][cardValueTemp] == 2) {
                array.add(integer);
            }
        }


        Array<Integer> aa = new Array<>();
        for (int[] ints : tempArr) {
            for (int i = 0; i < ints.length-1; i++) {
                if (ints[i] == ints[i+1]) {

                }
            }
        }

    }


    /**
     * Index转成牌
     * @param cbCardIndex
     * @return
     */
    int switchToCardData(int cardType,int cardValue) {
        return (((cbCardIndex / 9) << 4)
                | (cbCardIndex % 9 + 1));;
    }
}
