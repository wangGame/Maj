package kw.maj.screen;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kw.gdx.BaseGame;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;

import kw.maj.logic.MajLogic;

@ScreenResource("cocos/gameview.json")
public class MajScreen extends BaseScreen {
    private MajLogic logic;
    public MajScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        initData();
        initGameView();
    }

    private void initGameView() {
        //每个用户的初始值为0
        Group panelPlayer = findActor("Panel_Player");
        for (int i = 0; i < 4; i++) {
            Group faceFrame = panelPlayer.findActor("face_frame_" + i);
            Label textScore = faceFrame.findActor("Text_Score");
            textScore.setText(0);
        }
        //剩余牌的数量

        Group panelBg = findActor("Panel_ Bg");
        Label textLeftCard = panelBg.findActor("Text_LeftCard");
        textLeftCard.setText(0);
    }
    //   2
    //1     3
    //   0
    private void initData() {
        logic = new MajLogic(stage.getRoot());
        logic.initData();
        logic.userEnter();
    }
}
