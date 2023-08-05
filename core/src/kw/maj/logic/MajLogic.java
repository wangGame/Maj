package kw.maj.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.sign.SignListener;

import kw.maj.constant.Constant;
import kw.maj.data.MajData;
import kw.maj.group.SelectGroup;
import kw.maj.group.UserMajGroup;
import kw.maj.user.IUser;

public class MajLogic {
    private Array<IUser> iUsers;
    private int zhuangjia;
    private Array<Integer>[] arrays;
    private int userEnterIndex;
    private int currentDesk;
    private Group rootView;
    private  MajData data;
    private int disCard[];

    public MajLogic(Group group){
        this.rootView = group;
        iUsers = new Array<>();
        disCard = new int[4];
    }
    public void initData(){
        this.data = new MajData();
        this.arrays = data.genUserPai();
        //庄家
        this.zhuangjia = data.shuaiNum();
    }


    public void userEnter(){
        this.userEnterIndex = this.zhuangjia;
        this.currentDesk = this.zhuangjia;
        //用户进入游戏
        for (int i = 0; i < 4; i++) {
            rootView.addAction(Actions.delay(i*0.3f,Actions.run(new Runnable() {
                @Override
                public void run() {
                    int userId = (userEnterIndex++ % 4);
                    IUser iUser = new IUser(userId,arrays[userId]);
                    iUsers.add(iUser);
                    //展示图标
                    Group panelPlayer = rootView.findActor("Panel_Player");
                    Group faceFrame = panelPlayer.findActor("face_frame_" + userId);
                    Image imageHeader = faceFrame.findActor("Image_Header");
                    imageHeader.setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture("cocos/GameLayer/im_defaulthead_"+iUser.getSex()+".png"))));
                    if (((zhuangjia-1) % 4) == userId) {
                        startGame();
                    }
                }
            })));
        }
    }


    int selectQueNum = 0;

    public void startGame(){
        initUserPaiView();
        initUserIconAnimation();
        for (IUser iUser : iUsers) {
            //选择  万条筒
            if (iUser.getUserId() != 0){
                rootView.addAction(Actions.delay((float) (3* Math.random()),Actions.run(()->{
                    iUser.select();
                    queAdd();
                })));
            }else {
                //展示按钮
//                queAdd();
                showWTT();
            }
        }
    }

    private void showWTT() {
        String path[] = {"mingmah_46.png","mingmah_45.png","mingmah_44.png"};
        final Table table = new Table(){{
            int index = 0;
            for (String s : path) {
                SelectGroup image = new SelectGroup(s,index,new SignListener(){
                    @Override
                    public void sign(Object o) {
                        if (o instanceof Integer) {
                            int o1 = (int) o;
                            iUsers.get(0).setDaQue(o1);
                            queAdd();
                            Actor selectTable = rootView.findActor("selectTable");
                            if (selectTable!=null){
                                selectTable.remove();
                            }
                        }
                    }
                });
                add(image);
            }
            pack();
        }};
        table.setName("selectTable");
        rootView.addActor(table);
    }


    public void queAdd(){
        selectQueNum++;
        if (selectQueNum >= 4){
            gameSendPai();
        }
    }


    private void gameSendPai() {
        //庄家开始
        int moCard = data.faPai();
        if (moCard == -2){
            System.out.println("-----------流局----------");
            return;
        }
        currentDesk = (currentDesk)% 4;
//        System.out.println(currentDesk);
        Group panelGame = rootView.findActor("Panel_Game");
        Group playPanel = panelGame.findActor("PlayerPanel_" + currentDesk);
        String path = null;
        if (currentDesk == 0){
            Group recvHandCard = playPanel.findActor("RecvHandCard_" + currentDesk);
            path = "cocos/GameLayer/Mahjong/2/handmah_" + (((moCard & Constant.MASK_COLOR) >> 4)+1)+""+ (moCard & Constant.MASK_VALUE) + ".png";
            recvHandCard.clear();
            recvHandCard.setVisible(true);
            Image image = new Image(Asset.getAsset().getTexture(path));
            recvHandCard.addActor(image);
            IUser iUser = iUsers.get(currentDesk);
            iUser.addCard(moCard);

        } else if (currentDesk == 1){
            path =  "cocos/GameLayer/Mahjong/hand_left.png";
            Image recvHandCard = playPanel.findActor("RecvCard_" + currentDesk);
            recvHandCard.setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture(path))));
            recvHandCard.setVisible(true);
            IUser iUser = iUsers.get(currentDesk);
            iUser.addCard(moCard);
        }else if (currentDesk == 2){
            path =  "cocos/GameLayer/Mahjong/hand_top.png";
            Image recvHandCard = playPanel.findActor("RecvCard_" + currentDesk);
            recvHandCard.setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture(path))));
            recvHandCard.setVisible(true);
            IUser iUser = iUsers.get(currentDesk);
            iUser.addCard(moCard);
        }else if (currentDesk == 3){
            path =  "cocos/GameLayer/Mahjong/hand_right.png";
            Image recvHandCard = playPanel.findActor("RecvCard_" + currentDesk);
            recvHandCard.setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture(path))));
            recvHandCard.setVisible(true);
            IUser iUser = iUsers.get(currentDesk);
            iUser.addCard(moCard);
        }
        IUser iUser = iUsers.get(currentDesk);
        iUser.analy(moCard, currentDesk);
        int analy = iUser.outCard();
        int userTemp = currentDesk;
        for (int i = 0; i < 3; i++) {
            userTemp = (++userTemp) % 4;
            IUser iUser1 = iUsers.get(userTemp);
            int analyCode = iUser1.analy(analy, currentDesk);
            if (analyCode == 1){
                int i1 = data.faPai();
                int analy1 = iUser1.analy(i1, userTemp);

                break;
            }
        }

        //假装分析
        rootView.addAction(Actions.delay(0.3F, Actions.run(() -> {
            if (currentDesk == 0) {
                Group recvHandCard = playPanel.findActor("RecvHandCard_" + currentDesk);
                recvHandCard.setVisible(false);
                updateImage();
            } else {
                Image recvHandCard = playPanel.findActor("RecvCard_" + currentDesk);
                recvHandCard.setVisible(false);
            }
            int cardIndex= analy;
            Group actor = panelGame.findActor("DiscardCard_" + currentDesk);
            String pathTexture = null;
            if (currentDesk == 0) {
                pathTexture = "cocos/GameLayer/Mahjong/2/mingmah_" + (((cardIndex & Constant.MASK_COLOR) >> 4) + 1) + "" + (cardIndex & Constant.MASK_VALUE) + ".png";
            } else if (currentDesk == 1) {
                pathTexture = "cocos/GameLayer/Mahjong/3/mingmah_" + (((cardIndex & Constant.MASK_COLOR) >> 4) + 1) + "" + (cardIndex & Constant.MASK_VALUE) + ".png";
            } else if (currentDesk == 2) {
                pathTexture = "cocos/GameLayer/Mahjong/2/mingmah_" + (((cardIndex & Constant.MASK_COLOR) >> 4) + 1) + "" + (cardIndex & Constant.MASK_VALUE) + ".png";
            } else if (currentDesk == 3) {
                pathTexture = "cocos/GameLayer/Mahjong/1/mingmah_" + (((cardIndex & Constant.MASK_COLOR) >> 4) + 1) + "" + (cardIndex & Constant.MASK_VALUE) + ".png";
            }
            Image image = new Image(Asset.getAsset().getTexture(pathTexture));
            actor.addActor(image);
            int i1 = disCard[currentDesk];
            int i2 = i1 % 11;
            int i3 = i1 / 11;
            float distance = 45;
            float offy = 10;
            if (currentDesk == 0) {
                image.setX(i2 * distance);
                image.setY(i3*(distance+offy));
                image.setScale(0.6f);
                image.toBack();
            }
            if (currentDesk == 1){
                image.setY(i2 * 35);
                image.setX(i3*(distance+offy+5));
                image.setScale(0.5f);
                image.toBack();
            }
            if (currentDesk == 2){
                image.setX(i2 * distance);
                image.setY(-i3*(distance+offy)+60);
                image.setScale(0.6f);
                image.toFront();
            }
            if (currentDesk == 3){
                image.setY(i2 * 35);
                image.setX(-i3*(distance+offy+5));
                image.setScale(0.5f);
                image.toBack();
            }
            disCard[currentDesk]++;
            currentDesk ++;
            gameSendPai();
        })));

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

    public void updateImage(){
        if (currentDesk!=0)return;
        int userId = 0;
        IUser iUser = iUsers.get(userId);
        Array<Integer> userHandPai = iUser.getUserHandPai();
        Group playerGame = rootView.findActor("Panel_Game");
        Group handCard_0 = playerGame.findActor("HandCard_"+userId);
        handCard_0.clearChildren();
        float x  = 0;
        for (Integer integer : userHandPai) {
            int cbData = integer;
            String path = "cocos/GameLayer/Mahjong/2/handmah_" + (((cbData & Constant.MASK_COLOR) >> 4)+1)+""+ (cbData & Constant.MASK_VALUE) + ".png";
            UserMajGroup image = new UserMajGroup(path);
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
    }

    private void initUserPaiView() {
        for (IUser iUser : iUsers) {
            Array<Integer> userHandPai = iUser.getUserHandPai();
            int userId = iUser.getUserId();
            if (userId == 0){
                Group playerGame = rootView.findActor("Panel_Game");
                Group handCard_0 = playerGame.findActor("HandCard_"+userId);
                float x  = 0;
                for (Integer handCardIndex : userHandPai) {
                    int cbData = handCardIndex;
                    String path = "cocos/GameLayer/Mahjong/2/handmah_" + (((cbData & Constant.MASK_COLOR) >> 4)+1)+""+ (cbData & Constant.MASK_VALUE) + ".png";
                    UserMajGroup image = new UserMajGroup(path);
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
