package kw.maj.test;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.kw.gdx.sign.SignListener;

public class SquareGroup extends Group {
    private int num;
    private SignListener<Integer> signListener;
    public SquareGroup(int num){
        this.num = num;
        Group group = CocosResource.loadFile("cocos/num.json");
        setSize(group.getWidth(),group.getHeight());
        addActor(group);
        Label numlabel = group.findActor("numlabel");
        numlabel.setText(num);
        addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                signListener.sign(num);
            }
        });
    }

    public int getNum() {
        return num;
    }

    public void setListener(SignListener<Integer> integerSignListener) {
        this.signListener = integerSignListener;
    }
}
