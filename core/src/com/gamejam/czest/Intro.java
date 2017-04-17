package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamejam.czest.entities.SideTile;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;

/**
 * Created by bartek on 15.04.17.
 */
public class Intro
{
    private com.gamejam.czest.screens.GameplayScreen screen;

    private Stage stage;
    private IntroState state;

    private boolean playerShotCoal;

    private Color clearColor = new Color(201f/255f, 203f/255f, 198f/255f, 1);

    public Intro()
    {

    }

    public void init(SpriteBatch spriteBatch, final com.gamejam.czest.screens.GameplayScreen screen)
    {
        Assets.instance.sounds.backgroundMusic.stop();

        this.screen = screen;
        playerShotCoal = false;

        Viewport viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, spriteBatch);
        state = IntroState.INITIAL;

        Gdx.input.setInputProcessor(stage);


        Table rootTable = new Table();
        //rootTable.setDebug(true);
        rootTable.setFillParent(true);
        rootTable.addListener(new InputListener()
        {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                if(state == IntroState.CONTENT_TYPING || state == IntroState.TUTORIAL_TYPING)
                {
                    Table rootTable = (Table) stage.getActors().get(0);
                    TypingLabel label = (TypingLabel) rootTable.getCells().get(1).getActor();
                    label.skipToTheEnd();
                }
                else if(state.isSpaceToContinueActive())
                {
                    state = state.getNextState();
                    initUI(state);

                    if(state == IntroState.PLAYER_MOVING_LEFT)
                        screen.initIntroEnemy();
                }
                return true;
            }
        });

        rootTable.add().expandX();
        rootTable.row();
        rootTable.add().expand();
        rootTable.row();
        rootTable.add().expandX();

        stage.addActor(rootTable);

        stage.setKeyboardFocus(rootTable);

        initUI(state);
    }

    private void initUI(IntroState state)
    {
        Table rootTable = (Table) stage.getActors().get(0);

        if(!state.isUIvisible())
        {
            rootTable.setVisible(false);
            return;
        }
        else rootTable.setVisible(true);

        rootTable.getCells().get(0).setActor(setUpTitleLabel()).expandX().center();
        if(state == IntroState.CONTENT_TYPING || state == IntroState.TUTORIAL_TYPING)
            rootTable.getCells().get(1).setActor(setUpContentLabel(state)).center().expandY().width(600);

        if(state.isSpaceToContinueActive())
            rootTable.getCells().get(2).setActor(setUpTouchTheScreenPrompt()).expand();
        else
            rootTable.getCells().get(2).getActor().setVisible(false);
    }

    private TypingLabel setUpContentLabel(IntroState state)
    {
        TypingLabel label = null;
        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.instance.fonts.introContent, Color.WHITE);
        if(state == IntroState.CONTENT_TYPING)
        {
            label = new TypingLabel("The world was invaded by {COLOR=PINK}aliens{CLEARCOLOR}.\n " +
                    "Now the only way to survive is to {JUMP}{COLOR=BLACK}throw coal{CLEARCOLOR}{ENDJUMP} at them...\n "// +
                    //"Fortunately you can {SHAKE}{COLOR=BROWN}dig it up{CLEARCOLOR}{ENDSHAKE} by getting close to the {COLOR=BLACK}black ore{CLEARCOLOR}"
                    , labelStyle);
            label.setAlignment(Align.center);
            label.setWrap(true);
            label.setTypingListener(new TypingListener()
            {
                @Override
                public void event(String event) {}
                @Override
                public void end()
                {
                    Intro.this.state = Intro.this.state.getNextState();
                    initUI(Intro.this.state);
                }
                @Override
                public String replaceVariable(String variable) {return null;}
                @Override
                public void onChar(Character ch) {}
            });
        }
        else if(state == IntroState.TUTORIAL_TYPING)
        {
            label = new TypingLabel("Use {COLOR=BLUE}left{CLEARCOLOR} and {COLOR=BLUE}right{CLEARCOLOR} arrow to {WAVE}move{ENDWAVE} + {COLOR=BLUE}spacebar{CLEARCOLOR} to {JUMP}{COLOR=BLACK}shoot coal{CLEARCOLOR}{ENDJUMP}.\n " +
                    "If out of {COLOR=BLACK}ammo{CLEARCOLOR} - {COLOR=BROWN}{SHAKE}dig it{ENDSHAKE}{CLEARCOLOR} from the side.\n " +
                    "Good Luck!", labelStyle);
            label.setAlignment(Align.center);
            label.setWrap(true);
            label.setTypingListener(new TypingListener()
            {
                @Override
                public void event(String event) {}
                @Override
                public void end()
                {
                    Intro.this.state = Intro.this.state.getNextState();
                    initUI(Intro.this.state);
                }
                @Override
                public String replaceVariable(String variable) {return null;}
                @Override
                public void onChar(Character ch) {}
            });
        }
        return label;
    }

    private Label setUpTitleLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.instance.fonts.titleBig, Color.WHITE);
        Label titleLabel = new Label("[PINK]Alien[BLACK]Coal[BLUE]Shooter", labelStyle);
        return titleLabel;
    }

    private Label setUpTouchTheScreenPrompt()
    {
        SequenceAction sequenceAction = new SequenceAction(
                Actions.fadeOut(1f), Actions.fadeIn(1f));

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceAction);


        Label.LabelStyle labelStyle = new Label.LabelStyle(Assets.instance.fonts.credits, Color.WHITE);
        Label promptLabel = new Label("Press any key to continue", labelStyle);

        promptLabel.addAction(repeatAction);

        return promptLabel;
    }

    public void render()
    {
        stage.draw();
    }

    public void update(float delta)
    {
        stage.act(delta);

        if(state == IntroState.PLAYER_MOVING_LEFT)
        {
            com.gamejam.czest.entities.Player player = screen.getPlayer();
            player.queueMove(SideTile.Side.LEFT);

            if(!playerShotCoal && player.getBounds().x + player.getBounds().width/2f <= screen.getViewport().getWorldWidth() * Constants.INTRO.PLAYER_MIN_WORLD_PERCENTAGE_POS_TO_SHOOT)
            {
                player.queueShot(Constants.INTRO.COAL_SPEED_X, Constants.INTRO.COAL_SPEED_Y);
                playerShotCoal = true;
            }

            if(player.getBounds().x + player.getBounds().width/2f <= screen.getViewport().getWorldWidth()/2f)
            {
                //player.getBounds().x = screen.getViewport().getWorldWidth()/2f - player.getBounds().width/2f;
                state = IntroState.GAMEPLAY_START;
            }
        }
    }

    public boolean isBackgroundAllowedToMove()
    {
        return state == IntroState.PLAYER_MOVING_LEFT || state == IntroState.GAMEPLAY_START;
    }

    public boolean isIntroFinished()
    {
        return state == IntroState.GAMEPLAY_START;
    }


    public enum IntroState
    {
        INITIAL, CONTENT_TYPING, CONTENT_FINISHED, TUTORIAL_TYPING, TUTORIAL_FINISHED, PLAYER_MOVING_LEFT, GAMEPLAY_START;

        public IntroState getNextState()
        {
            if(this == GAMEPLAY_START) return GAMEPLAY_START;
            else return IntroState.values()[this.ordinal()+1];
        }

        public boolean isSpaceToContinueActive()
        {
            return this == INITIAL || this == CONTENT_FINISHED || this == TUTORIAL_FINISHED;
        }

        public boolean isUIvisible()
        {
            return (this != PLAYER_MOVING_LEFT && this != GAMEPLAY_START);
        }
    }
}
