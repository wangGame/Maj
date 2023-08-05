package kw.maj.group;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.sign.SignListener;

public class SelectGroup extends Group {
    private int index;
    public SelectGroup(String path, int index, SignListener runnable){
        Image image = new Image(Asset.getAsset().getTexture("cocos/GameLayer/Mahjong/2/"+path));
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        this.index = index;
        addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                runnable.sign(index);
            }
        });
    }
}
