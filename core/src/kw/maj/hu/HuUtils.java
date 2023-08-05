package kw.maj.hu;

import com.badlogic.gdx.utils.Array;

import kw.maj.analyse.AnalyseUtils;
import kw.maj.constant.Constant;

public class HuUtils {
    boolean isShunZi[]={
            true, //筒子
            true, //万
            true, //条
            false //other
    } ;

    public boolean huPai(Array<Integer> handCard,int newCard){

        return false;
    }

    public boolean bianzhang(int newCard,Array<Integer> handCard){
        int cardType = AnalyseUtils.getCardType(newCard);
        int cardValue = AnalyseUtils.getCardValue(newCard);
        if (!isShunZi[cardType - 1]) {
            return false;//
        }
        if (cardValue==3 || cardValue ==7){

        }
        return false;
    }

}
