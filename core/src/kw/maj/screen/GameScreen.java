package kw.maj.screen;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.BaseGame;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.sign.SignListener;

import kw.maj.SquareGroup;

@ScreenResource("cocos/gameview.json")
public class GameScreen extends BaseScreen {
    private Label showLabel;
    private int peopleNum;

    private int peopleValue[];

    public GameScreen(BaseGame game) {
        super(game);
        this.peopleValue = new int[4];
    }

    @Override
    public void show() {
        super.show();
        showLabel = rootView.findActor("showLabel");
        Table table = new Table(){{
            for (int i = 0; i < 10; i++) {
                SquareGroup squareGroup = new SquareGroup(i+1);
                add(squareGroup);
                squareGroup.setListener(new SignListener<Integer>(){
                    @Override
                    public void sign(Integer integer) {
                        super.sign(integer);
                        if (thinking)return; //电脑思考  不可以打扰
                        peopleValue[peopleNum%4] = integer;
                        System.out.println(peopleNum +"                   "+integer);
                        showLabel.setText(integer);
                        sendEndOption(integer);
                    }
                });
            }
            pack();
        }};
        addActor(table);
    }

    boolean thinking = false;
    public void sendEndOption(int x){
        thinking = true;

        stage.addAction(Actions.delay(1,Actions.run(()->{
            peopleNum ++;
            int check = check((int) x);
            System.out.println("check：  "+check);
            if (check != -1){
                int index = 0;
                for (int i : peopleValue) {
                    System.out.print(i+"   ");
                }
                System.out.println();
                for (int i : peopleValue) {
                    System.out.print(index+"  ");
                    index++;
                }
                System.out.println();
                peopleNum = check;
            }
            if (peopleNum % 4 == 0){
//                thinking = false;
             peopleNum = 0;
//                return;
            }

            double v = Math.random() * 9  + 1;
            showLabel.setText((int)v);
            peopleValue[peopleNum] = (int)v;
            System.out.println(peopleNum +"-------------------"+(int)v);
            sendEndOption((int)v);


        })));
    }

    private int check(int v){
        for (int i = 0; i < 3; i++) {
            if(peopleValue[(i+peopleNum)%4] == v){
                return (i+peopleNum)%4;
            }
        }
        return -1;
    }
}
