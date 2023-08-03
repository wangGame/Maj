package kw.maj.group;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;

public class UserMajGroup extends Group {
    Image image;
    private boolean isSelect;
    public UserMajGroup(String path,Runnable runnable){
        image = new Image(Asset.getAsset().getTexture(path));
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        image.setTouchable(Touchable.disabled);
        addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                runnable.run();
            }
        });
    }

    public void resetPosition() {
        this.isSelect = false;
        image.setY(0);
    }

    public void setSelect() {
        this.isSelect = true;
        image.setY(50);
    }
}
