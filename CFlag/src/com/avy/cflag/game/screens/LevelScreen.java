package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.utils.LevelScore;
import com.avy.cflag.game.Utils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class LevelScreen extends BackScreen {

	private final int colCnt=10;
	private final int rowCnt=4;
	private final int numButWidth=40;
	private final int numButHeight=40;

	private final int perPageLvlCnt = colCnt * rowCnt;
	private float rowGap = 0;
	private float colGap = 0;
	
	private final int numDclty = 5;
	
	private final int dcltyLvlCnt[] = new int[numDclty];
	private final int dcltyGrpCnt[] = new int[numDclty];

	private Image dcltyButtonUp[]= new Image[numDclty];
	private Image dcltyButtonDown[]= new Image[numDclty];
	private Image dcltyStr[]= new Image[numDclty];
	private Group dcltyButtonPageGroup[]= new Group[numDclty];
	private Image dcltyPageFrame[]= new Image[numDclty];
	private Group dcltyNumPageGroup[][] = new Group[numDclty][];
	private Group dcltyNumGroup[]= new Group[numDclty];
	private Group dcltyButtonGroup; 
	
	private final TextureAtlas levelAtlas;
	private final BitmapFont scoreFont;

	private Image midButtonUp;
	private Image midButtonDown;
	private Group midButtonGroup;

	private Image leftButtonUp;
	private Image leftButtonDown;
	private Group leftButtonGroup;

	private Image rightButtonUp;
	private Image rightButtonDown;
	private Group rightButtonGroup;

	private Image titleStr;
	private Image playAgainStr;
	private Image playStr;

	private int selectDclty;
	private int curPage;
	private int selectedLevel;
	
	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;
	
	private boolean printLevelData;
	
	private Image thumbnail;
	private Image clickedNumButton;
	
	public LevelScreen(CFlagGame game) {
		super(game, true, true, false);
		dcltyLvlCnt[0] = 136;
		dcltyLvlCnt[1] = 137;
		dcltyLvlCnt[2] = 138;
		dcltyLvlCnt[3] = 139;
		dcltyLvlCnt[4] = 140;
		
		for (int i = 0; i < dcltyGrpCnt.length; i++) {
			dcltyGrpCnt[i] = dcltyLvlCnt[i]/perPageLvlCnt + (dcltyLvlCnt[i]%perPageLvlCnt>0?1:0);
			dcltyNumPageGroup[i] = new Group[dcltyGrpCnt[i]];
		}

		levelAtlas = g.createImageAtlas("levelselect");
		scoreFont = g.createFont("salsa", 13);
		
		selectDclty = 0;
		curPage=0;
		
		printLevelData = false;
		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);
		g.setFont(scoreFont);

		midButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		midButtonUp.setPosition((bottomBar.getWidth() - midButtonUp.getWidth()) / 2, bottomBar.getY() + (bottomBar.getHeight() - midButtonUp.getHeight()) / 2);
		midButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		midButtonDown.setPosition(midButtonUp.getX(), midButtonUp.getY());
		midButtonDown.setVisible(false);

		final int sideButtonMargin = 18;
		leftButtonUp = new Image(g.getFlipTexRegion("lefttributtonup"));
		leftButtonUp.setPosition(sideButtonMargin, bottomBar.getY() + (bottomBar.getHeight() - leftButtonUp.getHeight()) / 2);
		leftButtonDown = new Image(g.getFlipTexRegion("lefttributtondown"));
		leftButtonDown.setPosition(leftButtonUp.getX(), leftButtonUp.getY());
		leftButtonDown.setVisible(false);

		rightButtonUp = new Image(g.getFlipTexRegion("righttributtonup"));
		rightButtonUp.setPosition(bottomBar.getWidth() - rightButtonUp.getWidth() - sideButtonMargin, bottomBar.getY() + (bottomBar.getHeight() - rightButtonUp.getHeight()) / 2);
		rightButtonDown = new Image(g.getFlipTexRegion("righttributtondown"));
		rightButtonDown.setPosition(rightButtonUp.getX(), rightButtonUp.getY());
		rightButtonDown.setVisible(false);

		g.setImageAtlas(levelAtlas);

		titleStr = new Image(g.getFlipTexRegion("titlestr"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		playStr = new Image(g.getFlipTexRegion("playstr"));
		playStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playAgainStr = new Image(g.getFlipTexRegion("playagainstr"));
		playAgainStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
	
		midButtonGroup = new Group();
		midButtonGroup.addActor(midButtonUp);
		midButtonGroup.addActor(midButtonDown);
		midButtonGroup.addActor(playStr);
		midButtonGroup.addActor(playAgainStr);
		midButtonGroup.setVisible(false);

		leftButtonGroup = new Group();
		leftButtonGroup.addActor(leftButtonUp);
		leftButtonGroup.addActor(leftButtonDown);
		if(curPage>0)
			leftButtonGroup.setVisible(true);
		else
			leftButtonGroup.setVisible(false);
		
		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		if(curPage<dcltyGrpCnt[selectDclty]-1)
			rightButtonGroup.setVisible(true);
		else
			rightButtonGroup.setVisible(false);
		
		stage.addActor(titleStr);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);
		stage.addActor(argbFull);
		
		dcltyButtonGroup = new Group();
		
		for (int dcltyNo = 0; dcltyNo < numDclty; dcltyNo++) {
			
			Difficulty dclty = Utils.getDifficultyByIdx(dcltyNo);
			
			dcltyButtonUp[dcltyNo] = new Image(g.getFlipTexRegion("buttonup"));
			
			if(dcltyNo==0)
				dcltyButtonUp[dcltyNo].setPosition((topBar.getWidth() - (dcltyButtonUp[dcltyNo].getWidth()*5))/2,topBar.getHeight() + 20);
			else
				dcltyButtonUp[dcltyNo].setPosition(dcltyButtonUp[dcltyNo-1].getX()+dcltyButtonUp[dcltyNo-1].getWidth()-1,dcltyButtonUp[dcltyNo-1].getY());
			
			dcltyButtonDown[dcltyNo] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() + "buttondown"));
			dcltyButtonDown[dcltyNo].setPosition(dcltyButtonUp[dcltyNo].getX(), dcltyButtonUp[dcltyNo].getY());
			dcltyButtonDown[dcltyNo].setVisible(false);
			
			dcltyStr[dcltyNo] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() +"str"));
			dcltyStr[dcltyNo].setPosition(dcltyButtonUp[dcltyNo].getX(), dcltyButtonUp[dcltyNo].getY());
			dcltyPageFrame[dcltyNo] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() +"frame"));
			dcltyPageFrame[dcltyNo].setPosition(20, dcltyButtonUp[dcltyNo].getY()+dcltyButtonUp[dcltyNo].getHeight());
			dcltyPageFrame[dcltyNo].setSize(topBar.getWidth()-40, bottomBar.getY()-dcltyPageFrame[dcltyNo].getY() - 20);
			dcltyPageFrame[dcltyNo].getColor().a = 0.5f;
			
			dcltyButtonPageGroup[dcltyNo] = new Group();
			dcltyButtonPageGroup[dcltyNo].addActor(dcltyButtonUp[dcltyNo]);
			dcltyButtonPageGroup[dcltyNo].addActor(dcltyButtonDown[dcltyNo]);
			dcltyButtonPageGroup[dcltyNo].addActor(dcltyStr[dcltyNo]);
			dcltyButtonPageGroup[dcltyNo].setVisible(true);
			dcltyButtonPageGroup[dcltyNo].setName(Integer.toString(dcltyNo));
			
			dcltyButtonPageGroup[dcltyNo].addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					Group tmp = (Group)event.getListenerActor();
					int tmpLvlNo = Integer.parseInt(tmp.getName());
					
					if(!dcltyButtonDown[tmpLvlNo].isVisible()){
						for (int i = 0; i < numDclty; i++) {
							if(i==tmpLvlNo){
								dcltyButtonDown[i].addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
								dcltyPageFrame[i].addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
								curPage=0;
								selectDclty=i;
								dcltyNumPageGroup[i][curPage].setVisible(true);
								dcltyNumGroup[i].addAction(sequence(alpha(0), visible(true), alpha(1f)));
								if(curPage>0)
									leftButtonGroup.setVisible(true);
								else
									leftButtonGroup.setVisible(false);
								
								if(curPage<dcltyGrpCnt[selectDclty]-1)
									rightButtonGroup.setVisible(true);
								else
									rightButtonGroup.setVisible(false);
							} else {
								dcltyButtonDown[i].addAction(sequence(fadeOut(0.2f), visible(false)));
								dcltyPageFrame[i].addAction(sequence(fadeOut(0.2f), visible(false)));
								dcltyNumGroup[i].addAction(sequence(fadeOut(0.2f), visible(false)));
							}
						}
					}
					return true;
				}
			});
			
			colGap = (dcltyPageFrame[dcltyNo].getWidth() - colCnt*numButWidth)/(colCnt+1);
			rowGap = (dcltyPageFrame[dcltyNo].getHeight() - rowCnt*numButHeight)/(rowCnt+1);
			dcltyNumGroup[dcltyNo] = new Group();
			int l=1;
			for (int i = 0; i < dcltyNumPageGroup[dcltyNo].length; i++) {
				dcltyNumPageGroup[dcltyNo][i] = new Group();
				for (int j = 0; j < rowCnt; j++) {
					for (int k = 0; k < colCnt; k++) {
						if(l<=dcltyLvlCnt[dcltyNo]){
							Group numButtonGroup = new Group();
							Image numButtonUp = new Image(g.getFlipTexRegion("numbuttonup"));
							numButtonUp.setPosition(dcltyPageFrame[dcltyNo].getX()+colGap+k*(numButtonUp.getWidth()+colGap), dcltyPageFrame[dcltyNo].getY()+rowGap+j*(numButtonUp.getHeight()+rowGap));
							numButtonUp.setName("buttonUp");
							
							Image numButtonDown = new Image(g.getFlipTexRegion("numbuttondown"));
							numButtonDown.setPosition(numButtonUp.getX(), numButtonUp.getY());
							numButtonDown.setVisible(false);
							numButtonDown.setName("buttonDown");
							
							numButtonGroup.addActor(numButtonUp);
							numButtonGroup.addActor(numButtonDown);
							numButtonGroup.setName(Integer.toString(l-1));
			
							char numStr[] = Integer.toString(l).toCharArray();
							
							Image numStr1=null, numStr2=null, numStr3=null;
							float numStrLen=0;
							
							if(numStr.length>0){
								numStr1 = new Image(g.getFlipTexRegion(""+numStr[0]));
								numStrLen=numStr1.getWidth();
							}
							if(numStr.length>1){
								numStr2 = new Image(g.getFlipTexRegion(""+numStr[1]));
								numStrLen = numStrLen + numStr2.getWidth();
							}
							if(numStr.length>2) { 
								numStr3 = new Image(g.getFlipTexRegion(""+numStr[2]));
								numStrLen = numStrLen + numStr3.getWidth();
							}
							
							if(numStr.length>0){
								numStr1.setPosition(numButtonUp.getX()+(numButtonUp.getWidth()-numStrLen)/2, numButtonUp.getY()+(numButtonUp.getHeight()-numStr1.getHeight())/2);
								numButtonGroup.addActor(numStr1);
							}
							if(numStr.length>1){
								numStr2.setPosition(numStr1.getX()+numStr1.getWidth(),numStr1.getY());
								numButtonGroup.addActor(numStr2);
							}
							if(numStr.length>2){
								numStr3.setPosition(numStr2.getX()+numStr2.getWidth(),numStr2.getY());
								numButtonGroup.addActor(numStr3);
							}
							
							dcltyNumPageGroup[dcltyNo][i].addActor(numButtonGroup);
			
							numButtonGroup.addListener(new InputListener() {
								@Override
								public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
									Group tmp = (Group)event.getListenerActor();
									Image buttonDown = (Image)tmp.findActor("buttonDown");
									buttonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
									return true;
								}
								
								@Override
								public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
									Group tmp = (Group)event.getListenerActor();
									clickedNumButton = (Image)tmp.findActor("buttonDown");
									clickedNumButton.addAction(sequence(fadeOut(0.2f), visible(false)));
									selectedLevel = Integer.parseInt(tmp.getName());
									swingOutThumbnail();
								}
							});
						}
						l++;
					}
				}
				
				if(dcltyNo==selectDclty && i==curPage)
					dcltyNumPageGroup[dcltyNo][i].setVisible(true);
				else
					dcltyNumPageGroup[dcltyNo][i].setVisible(false);
				
				dcltyNumGroup[dcltyNo].addActor(dcltyNumPageGroup[dcltyNo][i]);

				if(dcltyNo==selectDclty){
					dcltyButtonDown[dcltyNo].setVisible(true);
					dcltyPageFrame[dcltyNo].setVisible(true);
					dcltyNumGroup[dcltyNo].setVisible(true);
				} else {
					dcltyNumGroup[dcltyNo].setVisible(false);
					dcltyPageFrame[dcltyNo].setVisible(false);
				}
			}
			dcltyButtonGroup.addActor(dcltyButtonPageGroup[dcltyNo]);
			stage.addActor(dcltyPageFrame[dcltyNo]);
			stage.addActor(dcltyNumGroup[dcltyNo]);
		}
		stage.addActor(dcltyButtonGroup);

		midButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				midButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				midButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
					}
				})));
			}
		});

		leftButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				leftButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				leftButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				swipeRight();
			}
		});

		rightButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				rightButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				rightButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				swipeLeft();
			}
		});

		if (MemStore.gameOPTS.isSwipeMove()) {
			stage.addListener(new DragListener() {
				@Override
				public void dragStart(InputEvent event, float x, float y, int pointer) {
					dragStartX = x;
					super.dragStart(event, x, y, pointer);
				}

				@Override
				public void dragStop(InputEvent event, float x, float y, int pointer) {
					super.dragStop(event, x, y, pointer);
					dragEndX = x;
					if (dragStartX > dragEndX) {
						swipeLeft();
					} else if (dragStartX < dragEndX) {
						swipeRight();
					}
				}
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(printLevelData){
						if(!(event.getTarget().getName()!=null && event.getTarget().getName().equalsIgnoreCase("thumbnail")))
							swingInThumbnail();
					}
					return true;
				}
				
				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					if (keycode == Keys.BACK||keycode==Keys.ESCAPE) {
						if(!printLevelData){
							argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
								@Override
								public void run() {
									game.setScreen(new MenuScreen(game));
								}
							})));
						} else{
							swingInThumbnail();
						}
					}
					return true;
				}
			});
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
//		if (printLevelData) {
//			final LevelScore gScore = MemStore.userSCORE.getScores(selectedLevel);
//			final Level lvl = new Level();
//			lvl.loadLevel(selectedLevel);
//			batch.begin();
//			final String lvlDetails = "Level No      : " + (selectedLevel) + "\nLevel Name : " + lvl.getLvlName() + "\nDifficulty    : " + lvl.getLvlDclty().name() + "\n";
//			String scoreDetails = "";
//			if (gScore != null) {
//				scoreDetails = "Moves Played : " + gScore.getMoves() + "\nShots Fired    : " + gScore.getShots() + "\nHint Used       : " + (gScore.isHintUsed() ? "Yes" : "No");
//			} else {
//				scoreDetails = "Moves Played : 0\nShots Fired    : 0 \nHint Used       : No";
//			}
//			g.drawMultiLineString(lvlDetails, 200, 433, Color.BLACK);
//			g.drawMultiLineString(scoreDetails, 600, 433, Color.BLACK);
//			batch.end();
//		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (levelAtlas != null) {
			levelAtlas.dispose();
		}
		if (scoreFont != null) {
			scoreFont.dispose();
		}
	}

	public void swipeLeft(){
		if (!dragInProgress && rightButtonGroup.isVisible()){
			if(curPage<dcltyGrpCnt[selectDclty]-1){
				dcltyNumPageGroup[selectDclty][curPage].addAction(sequence());
				dcltyNumPageGroup[selectDclty][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(-game.getSrcWidth(), dcltyNumPageGroup[selectDclty][curPage].getY(), 1f), visible(false)));
	
				dcltyNumPageGroup[selectDclty][curPage+1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectDclty][curPage+1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
	
					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curPage++;
			}
		}
		if (curPage >= dcltyGrpCnt[selectDclty] - 1) {
			rightButtonGroup.setVisible(false);
		}
		if (curPage > 0) {
			leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}
	
	public void swipeRight(){
		if (!dragInProgress && leftButtonGroup.isVisible()){
			if(curPage>0){
				dcltyNumPageGroup[selectDclty][curPage].addAction(sequence());
				dcltyNumPageGroup[selectDclty][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(game.getSrcWidth(), dcltyNumPageGroup[selectDclty][curPage].getY(), 1f), visible(false)));
	
				dcltyNumPageGroup[selectDclty][curPage-1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectDclty][curPage-1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
	
					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curPage--;
			}
		}
		if (curPage <= 0) {
			leftButtonGroup.setVisible(false);
		}
		if (curPage < dcltyGrpCnt[selectDclty]-1) {
			rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}
	
	public void swingOutThumbnail(){
		thumbnail = new Image(g.getThumbTexRegion(1));
		thumbnail.setPosition(clickedNumButton.getX(), clickedNumButton.getY());
		thumbnail.setOrigin(thumbnail.getWidth() / 2, thumbnail.getHeight() / 2);
		thumbnail.setName("thumbnail");
		float tempWidth = thumbnail.getWidth();
		float tempHeight = thumbnail.getHeight();
		thumbnail.setSize(clickedNumButton.getWidth(),clickedNumButton.getHeight());
		stage.addActor(thumbnail);
		thumbnail.addAction(parallel(forever(rotateBy(1f)),sizeTo(tempWidth,tempHeight,1f),moveTo((topBar.getWidth()-tempWidth)/2, (480-tempHeight)/2,1f,swingOut)));
		
		dcltyNumGroup[selectDclty].addAction(alpha(0.1f));
		leftButtonGroup.addAction(alpha(0f));
		rightButtonGroup.addAction(alpha(0f));

		dcltyNumGroup[selectDclty].setTouchable(Touchable.disabled);
		dcltyButtonGroup.setTouchable(Touchable.disabled);
		leftButtonGroup.setTouchable(Touchable.disabled);
		rightButtonGroup.setTouchable(Touchable.disabled);
		
		playStr.addAction(visible(true));
		playAgainStr.addAction(visible(false));
		midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
		
		printLevelData=true;
	}
	
	public void swingInThumbnail(){
		thumbnail.setOrigin(clickedNumButton.getWidth() / 2, clickedNumButton.getWidth() / 2);
		thumbnail.clearActions();
		thumbnail.addAction(sequence(parallel(repeat(40,rotateBy(-1f)),sizeTo(clickedNumButton.getWidth(),clickedNumButton.getHeight(),1f),moveTo(clickedNumButton.getX(),clickedNumButton.getY(),1f,swingIn)),fadeOut(0.2f),visible(false), run(new Runnable() {
			@Override
			public void run() {
				thumbnail.remove();
			}
		})));
		dcltyNumGroup[selectDclty].addAction(alpha(1f));
		leftButtonGroup.addAction(alpha(1f));
		rightButtonGroup.addAction(alpha(1f));

		dcltyNumGroup[selectDclty].setTouchable(Touchable.enabled);
		dcltyButtonGroup.setTouchable(Touchable.enabled);
		leftButtonGroup.setTouchable(Touchable.enabled);
		rightButtonGroup.setTouchable(Touchable.enabled);
		
		playStr.addAction(visible(false));
		playAgainStr.addAction(visible(false));
		midButtonGroup.addAction(sequence(fadeOut(1f), visible(false), alpha(1f)));
		printLevelData=false;
	}
}
