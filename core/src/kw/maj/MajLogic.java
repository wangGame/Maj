package kw.maj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;

import kw.maj.data.MajData;
import kw.maj.group.UserMajGroup;
import kw.maj.user.IUser;

public class MajLogic {
    private static final Integer MASK_COLOR = 0XF0;
    private static final int MASK_VALUE = 0X0F;
    private Array<IUser> iUsers;
    private int zhuangjia;
    private Array<Integer>[] arrays;
    private int currentIndex;
    private Group rootView;

    public MajLogic(Group group){
        this.rootView = group;
        iUsers = new Array<>();
    }
    public void initData(){
        MajData data = new MajData();
        this.arrays = data.genUserPai();
        //庄家
        zhuangjia = data.shuaiNum();
    }


    public void userEnter(){
        currentIndex = zhuangjia;
        //用户进入游戏
        for (int i = 0; i < 4; i++) {
            rootView.addAction(Actions.delay(i*0.3f,Actions.run(new Runnable() {
                @Override
                public void run() {
                    int userId = (currentIndex++ % 4);
                    IUser iUser = new IUser(userId,arrays[userId]);
                    iUsers.add(iUser);
                    //展示图标
                    Group panelPlayer = rootView.findActor("Panel_Player");
                    Group faceFrame = panelPlayer.findActor("face_frame_" + userId);
                    Image imageHeader = faceFrame.findActor("Image_Header");
                    imageHeader.setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture("cocos/GameLayer/im_defaulthead_"+iUser.getSex()+".png"))));
                    if (((zhuangjia-1) % 4) == userId) {
                        System.out.println("_--------------------------------");
                        startGame();

                    }
                }
            })));
        }
    }

    public void startGame(){
        initUserPaiView();
        initUserIconAnimation();
    }

    private void initUserIconAnimation() {
        for (int userId = 0; userId < 4; userId++) {
            Group panelPlayer = rootView.findActor("Panel_Player");
            Group faceFrame = panelPlayer.findActor("face_frame_" + userId);
            if (userId == 0){
                faceFrame.addAction(Actions.moveToAligned(80.f,250.f, Align.center,0.5f));
            }else if (userId == 1){
                faceFrame.addAction(Actions.moveToAligned(80.f,380,Align.center,0.5f));
            }else if (userId == 2){
                faceFrame.addAction(Actions.moveToAligned(1060.00f,640.00f,Align.center,0.5f));
            }else if (userId == 3){
                faceFrame.addAction(Actions.moveToAligned(1200.00f,380,Align.center,0.5f));
            }
        }
    }

    private void initUserPaiView() {
        for (IUser iUser : iUsers) {
            Array<Integer> userHandPai = iUser.getUserHandPai();
            int userId = iUser.getUserId();
            if (userId == 0){
                System.out.println("00000000000000000000");
                Group playerGame = rootView.findActor("Panel_Game");
                Group handCard_0 = playerGame.findActor("HandCard_"+userId);

                float x  = 0;
                for (Integer integer : userHandPai) {
//                    int cbData = indexToCard(integer);
                    int cbData = integer;
                    String path = "cocos/GameLayer/Mahjong/2/handmah_" + (((cbData & MASK_COLOR) >> 4)+1)+""+ (cbData & MASK_VALUE) + ".png";
                    FileHandle internal = Gdx.files.internal(path);
                    System.out.println(path +"        "+internal.exists());
                    UserMajGroup image = new UserMajGroup(path, new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    image.setX(x);
                    image.addListener(new OrdinaryButtonListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            guiWei(event);
                        }
                    });
                    x = x + image.getWidth();
                    handCard_0.addActor(image);
                }
            }else if (userId == 1){
                Group playerGame = rootView.findActor("Panel_Game");
                Group handCard_0 = playerGame.findActor("HandCard_"+userId);
                float y  = 0;
                for (Integer integer : userHandPai) {
                    String path =  "cocos/GameLayer/Mahjong/hand_left.png";
                    FileHandle internal = Gdx.files.internal(path);
                    System.out.println(path +"        "+internal.exists());
                    Image image = new Image(Asset.getAsset().getTexture(path));
                    image.setY(y);
                    y = y + image.getWidth()/2.0f;
                    handCard_0.addActor(image);
                }
            } else if (userId == 2){
                Group playerGame = rootView.findActor("Panel_Game");
                Group handCard_0 = playerGame.findActor("HandCard_"+userId);
                float x  = 0;
                for (Integer integer : userHandPai) {
                    String path =  "cocos/GameLayer/Mahjong/hand_top.png";
                    FileHandle internal = Gdx.files.internal(path);
                    System.out.println(path +"        "+internal.exists());
                    Image image = new Image(Asset.getAsset().getTexture(path));
                    image.setX(x);
                    x = x + image.getWidth();
                    handCard_0.addActor(image);
                }
            }else if (userId == 3){
                Group playerGame = rootView.findActor("Panel_Game");
                Group handCard_0 = playerGame.findActor("HandCard_"+userId);
                float y  = 0;
                for (Integer integer : userHandPai) {
                    String path =  "cocos/GameLayer/Mahjong/hand_right.png";
                    FileHandle internal = Gdx.files.internal(path);
                    System.out.println(path +"        "+internal.exists());
                    Image image = new Image(Asset.getAsset().getTexture(path));
                    image.setY(y);
                    y = y + image.getWidth()/2.0f;
                    handCard_0.addActor(image);
                }
            }
        }
    }

    public void guiWei(InputEvent event){
        Group playerGame = rootView.findActor("Panel_Game");
        Group handCard_0 = playerGame.findActor("HandCard_0");
        for (Actor child : handCard_0.getChildren()) {
            if (child instanceof UserMajGroup) {
                UserMajGroup userMajGroup = (UserMajGroup) child;
                userMajGroup.resetPosition();
            }
        }
        Actor target = event.getTarget();
        if (target != null){
            if (target instanceof UserMajGroup) {
                ((UserMajGroup)(target)).setSelect();
            }
        }
    }

    public int indexToCard(int cbCardIndex){
        return ((cbCardIndex / 9) << 4)
                | (cbCardIndex % 9 + 1);
    }
}
