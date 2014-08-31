package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class TempLevelScreen extends BackScreen {

	private final int colCnt=10;
	private final int rowCnt=4;
	private final int numButWidth=40;
	private final int numButHeight=40;

	private final int perPageLvlCnt = colCnt * rowCnt;
	
	private final int noviceLvlCnt = 100;
	private final int easyLvlCnt = 100;
	private final int mediumLvlCnt = 100;
	private final int hardLvlCnt = 50;
	private final int deadlyLvlCnt = 50;

	private final int noviceGrpCnt = noviceLvlCnt/perPageLvlCnt + (noviceLvlCnt%perPageLvlCnt>0?1:0);
	private final int easyGrpCnt = easyLvlCnt/perPageLvlCnt + (easyLvlCnt%perPageLvlCnt>0?1:0);
	private final int mediumGrpCnt = mediumLvlCnt/perPageLvlCnt + (mediumLvlCnt%perPageLvlCnt>0?1:0);
	private final int hardGrpCnt = hardLvlCnt/perPageLvlCnt + (hardLvlCnt%perPageLvlCnt>0?1:0);
	private final int deadlyGrpCnt = deadlyLvlCnt/perPageLvlCnt + (deadlyLvlCnt%perPageLvlCnt>0?1:0);
	
	private float rowGap = 0;
	private float colGap = 0;
	
	private final TextureAtlas levelAtlas;
	private final BitmapFont scoreFont;

	private Image noviceButtonUp;
	private Image noviceButtonDown;
	private Image noviceStr;
	private Group noviceButtonGroup;
	private Image noviceFrame;
	private Group noviceNumPageGroup[] = new Group[noviceGrpCnt];
	private Group noviceNumGroup;
	
	private Image easyButtonUp;
	private Image easyButtonDown;
	private Image easyStr;
	private Group easyButtonGroup;
	private Image easyFrame;
	private Group easyNumPageGroup[] = new Group[easyGrpCnt];
	private Group easyNumGroup;

	private Image mediumButtonUp;
	private Image mediumButtonDown;
	private Image mediumStr;
	private Group mediumButtonGroup;
	private Image mediumFrame;
	private Group mediumNumPageGroup[] = new Group[mediumGrpCnt];
	private Group mediumNumGroup;

	private Image hardButtonUp;
	private Image hardButtonDown;
	private Image hardStr;
	private Group hardButtonGroup;
	private Image hardFrame;
	private Group hardNumPageGroup[] = new Group[hardGrpCnt];
	private Group hardNumGroup;

	private Image deadlyButtonUp;
	private Image deadlyButtonDown;
	private Image deadlyStr;
	private Group deadlyButtonGroup;
	private Image deadlyFrame;
	private Group deadlyNumPageGroup[] = new Group[deadlyGrpCnt];
	private Group deadlyNumGroup;

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

	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;
	private boolean printText;
	
	public TempLevelScreen(CFlagGame game) {
		super(game, true, true, false);

		levelAtlas = g.createImageAtlas("levelselect");
		scoreFont = g.createFont("salsa", 13);
		
		printText = true;
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
		
		noviceButtonUp = new Image(g.getFlipTexRegion("buttonup"));
		noviceButtonUp.setPosition((topBar.getWidth() - (noviceButtonUp.getWidth()*5))/2,topBar.getHeight() + 20);
		noviceButtonDown = new Image(g.getFlipTexRegion("novicebuttondown"));
		noviceButtonDown.setPosition(noviceButtonUp.getX(), noviceButtonUp.getY());
		noviceStr = new Image(g.getFlipTexRegion("novicestr"));
		noviceStr.setPosition(noviceButtonUp.getX(), noviceButtonUp.getY());
		noviceFrame = new Image(g.getFlipTexRegion("noviceframe"));
		noviceFrame.setPosition(20, noviceButtonUp.getY()+noviceButtonUp.getHeight());
		noviceFrame.setSize(topBar.getWidth()-40, bottomBar.getY()-noviceFrame.getY() - 20);
		noviceFrame.getColor().a = 0.5f;
		
		noviceButtonGroup = new Group();
		noviceButtonGroup.addActor(noviceButtonUp);
		noviceButtonGroup.addActor(noviceButtonDown);
		noviceButtonGroup.addActor(noviceStr);
		noviceButtonGroup.setVisible(true);
		
		easyButtonUp = new Image(g.getFlipTexRegion("buttonup"));
		easyButtonUp.setPosition(noviceButtonUp.getX()+noviceButtonUp.getWidth()-1,noviceButtonUp.getY());
		easyButtonDown = new Image(g.getFlipTexRegion("easybuttondown"));
		easyButtonDown.setPosition(easyButtonUp.getX(), easyButtonUp.getY());
		easyButtonDown.setVisible(false);
		easyStr = new Image(g.getFlipTexRegion("easystr"));
		easyStr.setPosition(easyButtonUp.getX(), easyButtonUp.getY());
		easyFrame = new Image(g.getFlipTexRegion("easyframe"));
		easyFrame.setPosition(20, easyButtonUp.getY()+easyButtonUp.getHeight());
		easyFrame.setSize(topBar.getWidth()-40, bottomBar.getY()-easyFrame.getY() - 20);
		easyFrame.getColor().a = 0.5f;
		easyFrame.setVisible(false);
		
		easyButtonGroup = new Group();
		easyButtonGroup.addActor(easyButtonUp);
		easyButtonGroup.addActor(easyButtonDown);
		easyButtonGroup.addActor(easyStr);
		easyButtonGroup.setVisible(true);

		mediumButtonUp = new Image(g.getFlipTexRegion("buttonup"));
		mediumButtonUp.setPosition(easyButtonUp.getX()+easyButtonUp.getWidth()-1,easyButtonUp.getY());
		mediumButtonDown = new Image(g.getFlipTexRegion("mediumbuttondown"));
		mediumButtonDown.setPosition(mediumButtonUp.getX(), mediumButtonUp.getY());
		mediumButtonDown.setVisible(false);
		mediumStr = new Image(g.getFlipTexRegion("mediumstr"));
		mediumStr.setPosition(mediumButtonUp.getX(), mediumButtonUp.getY());
		mediumFrame = new Image(g.getFlipTexRegion("mediumframe"));
		mediumFrame.setPosition(20, mediumButtonUp.getY()+mediumButtonUp.getHeight());
		mediumFrame.setSize(topBar.getWidth()-40, bottomBar.getY()-mediumFrame.getY() - 20);
		mediumFrame.getColor().a = 0.5f;
		mediumFrame.setVisible(false);
		
		mediumButtonGroup = new Group();
		mediumButtonGroup.addActor(mediumButtonUp);
		mediumButtonGroup.addActor(mediumButtonDown);
		mediumButtonGroup.addActor(mediumStr);
		mediumButtonGroup.setVisible(true);

		hardButtonUp = new Image(g.getFlipTexRegion("buttonup"));
		hardButtonUp.setPosition(mediumButtonUp.getX()+mediumButtonUp.getWidth()-1,mediumButtonUp.getY());
		hardButtonDown = new Image(g.getFlipTexRegion("hardbuttondown"));
		hardButtonDown.setPosition(hardButtonUp.getX(), hardButtonUp.getY());
		hardButtonDown.setVisible(false);
		hardStr = new Image(g.getFlipTexRegion("hardstr"));
		hardStr.setPosition(hardButtonUp.getX(), hardButtonUp.getY());
		hardFrame = new Image(g.getFlipTexRegion("hardframe"));
		hardFrame.setPosition(20, hardButtonUp.getY()+hardButtonUp.getHeight());
		hardFrame.setSize(topBar.getWidth()-40, bottomBar.getY()-hardFrame.getY() - 20);
		hardFrame.getColor().a = 0.5f;
		hardFrame.setVisible(false);
		
		hardButtonGroup = new Group();
		hardButtonGroup.addActor(hardButtonUp);
		hardButtonGroup.addActor(hardButtonDown);
		hardButtonGroup.addActor(hardStr);
		hardButtonGroup.setVisible(true);

		deadlyButtonUp = new Image(g.getFlipTexRegion("buttonup"));
		deadlyButtonUp.setPosition(hardButtonUp.getX()+hardButtonUp.getWidth()-1,hardButtonUp.getY());
		deadlyButtonDown = new Image(g.getFlipTexRegion("deadlybuttondown"));
		deadlyButtonDown.setPosition(deadlyButtonUp.getX(), deadlyButtonUp.getY());
		deadlyButtonDown.setVisible(false);
		deadlyStr = new Image(g.getFlipTexRegion("deadlystr"));
		deadlyStr.setPosition(deadlyButtonUp.getX(), deadlyButtonUp.getY());
		deadlyFrame = new Image(g.getFlipTexRegion("deadlyframe"));
		deadlyFrame.setPosition(20, deadlyButtonUp.getY()+deadlyButtonUp.getHeight());
		deadlyFrame.setSize(topBar.getWidth()-40, bottomBar.getY()-deadlyFrame.getY() - 20);
		deadlyFrame.getColor().a = 0.5f;
		deadlyFrame.setVisible(false);
		
		deadlyButtonGroup = new Group();
		deadlyButtonGroup.addActor(deadlyButtonUp);
		deadlyButtonGroup.addActor(deadlyButtonDown);
		deadlyButtonGroup.addActor(deadlyStr);
		deadlyButtonGroup.setVisible(true);

		midButtonGroup = new Group();
		midButtonGroup.addActor(midButtonUp);
		midButtonGroup.addActor(midButtonDown);
		midButtonGroup.addActor(playStr);
		midButtonGroup.addActor(playAgainStr);
		midButtonGroup.setVisible(false);

		leftButtonGroup = new Group();
		leftButtonGroup.addActor(leftButtonUp);
		leftButtonGroup.addActor(leftButtonDown);
		leftButtonGroup.setVisible(false);

		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		rightButtonGroup.setVisible(false);

		stage.addActor(titleStr);
		stage.addActor(noviceButtonGroup);
		stage.addActor(noviceFrame);
		stage.addActor(easyButtonGroup);
		stage.addActor(easyFrame);
		stage.addActor(mediumButtonGroup);
		stage.addActor(mediumFrame);
		stage.addActor(hardButtonGroup);
		stage.addActor(hardFrame);
		stage.addActor(deadlyButtonGroup);
		stage.addActor(deadlyFrame);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);
		stage.addActor(argbFull);
		
		colGap = (noviceFrame.getWidth() - colCnt*numButWidth)/(colCnt+1);
		rowGap = (noviceFrame.getHeight() - rowCnt*numButHeight)/(rowCnt+1);
		noviceNumGroup = new Group();
		int l=0;
		for (int i = 0; i < noviceNumPageGroup.length; i++) {
			noviceNumPageGroup[i] = new Group();
			for (int j = 0; j < rowCnt; j++) {
				for (int k = 0; k < colCnt; k++) {
					Group numButtonGroup = new Group();
					Image numButtonUp = new Image(g.getFlipTexRegion("numbuttonup"));
					numButtonUp.setPosition(noviceFrame.getX()+colGap+k*(numButtonUp.getWidth()+colGap), noviceFrame.getY()+rowGap+j*(numButtonUp.getHeight()+rowGap));
					numButtonUp.setName("buttonUp");
					
					Image numButtonDown = new Image(g.getFlipTexRegion("numbuttondown"));
					numButtonDown.setPosition(numButtonUp.getX(), numButtonUp.getY());
					numButtonDown.setVisible(false);
					numButtonDown.setName("buttonDown");
					
					numButtonGroup.addActor(numButtonUp);
					numButtonGroup.addActor(numButtonDown);
					numButtonGroup.setName(Integer.toString(l));
	
					char numStr[] = Integer.toString(l+1).toCharArray();
					
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
						numStr3 = new Image(g.getFlipTexRegion(""+numStr[1]));
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
					
					noviceNumPageGroup[i].addActor(numButtonGroup);
	
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
							Image buttonDown = (Image)tmp.findActor("buttonDown");
							buttonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
						}
					});
					
					l++;
				}
			}
			if(i!=0) 
				noviceNumPageGroup[i].setVisible(false);
			noviceNumGroup.addActor(noviceNumPageGroup[i]);
		}
		
		stage.addActor(noviceNumGroup);
		
		noviceButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!noviceButtonDown.isVisible()){
					noviceButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
					easyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));

					noviceFrame.addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
					easyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
				}
				return true;
			}
		});
		easyButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!easyButtonDown.isVisible()){
					noviceButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
					mediumButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));

					noviceFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyFrame.addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
					mediumFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
				}
				return true;
			}
		});
		mediumButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!mediumButtonDown.isVisible()){
					noviceButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
					hardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));

					noviceFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumFrame.addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
					hardFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
				}
				return true;
			}
		});
		hardButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!hardButtonDown.isVisible()){
					noviceButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
					deadlyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));

					noviceFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardFrame.addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
					deadlyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
				}
				return true;
			}
		});
		deadlyButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!deadlyButtonDown.isVisible()){
					noviceButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));

					noviceFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					easyFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					mediumFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					hardFrame.addAction(sequence(fadeOut(0.2f), visible(false)));
					deadlyFrame.addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
				}
				return true;
			}
		});
		
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
//				swipeRight();
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
//				swipeLeft();
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
//						swipeLeft();
					} else if (dragStartX < dragEndX) {
//						swipeRight();
					}
				}

				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					if (keycode == Keys.BACK) {
						argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
							@Override
							public void run() {
								game.setScreen(new MenuScreen(game));
							}
						})));
					}
					return true;
				}
			});
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
//		if (selectedThumb >= 0 && printText && totalThumbs > 0) {
//			final LevelScore gScore = MemStore.userSCORE.getScores(selectedThumb);
//			final Level lvl = new Level();
//			lvl.loadLevel(selectedThumb + 1);
//			batch.begin();
//			final String lvlDetails = "Level No      : " + (selectedThumb+1) + "\nLevel Name : " + lvl.getLvlName() + "\nDifficulty    : " + lvl.getLvlDclty().name() + "\n";
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

//	public void swipeLeft() {
//		if (!dragInProgress && rightButtonGroup.isVisible()) {
//			if (curPage < totalPages) {
//				thumbsGroup[curPage].addAction(sequence(run(new Runnable() {
//					@Override
//					public void run() {
//						dragInProgress = true;
//					}
//				}), moveTo(-game.getSrcWidth(), thumbsGroup[curPage].getY(), 1f), visible(false)));
//
//				thumbsGroup[curPage + 1].addAction(sequence(alpha(0), moveTo(0, thumbsGroup[curPage + 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
//
//					@Override
//					public void run() {
//						dragInProgress = false;
//					}
//				})));
//				curPage++;
//			}
//			if (curPage >= totalPages - 1) {
//				rightButtonGroup.setVisible(false);
//			}
//			if (curPage > 0) {
//				leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
//			}
//			if (curPage != Math.floor(selectedThumb / (THUMBS_PER_COL * THUMBS_PER_ROW))) {
//				printText = false;
//				midButtonGroup.addAction(sequence(fadeOut(1f), visible(false)));
//			} else {
//				printText = true;
//				midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
//			}
//		}
//	}
//
//	public void swipeRight() {
//		if (!dragInProgress && leftButtonGroup.isVisible()) {
//			if (curPage > 0) {
//				thumbsGroup[curPage].addAction(sequence(run(new Runnable() {
//					@Override
//					public void run() {
//						dragInProgress = true;
//					}
//				}), moveTo(game.getSrcWidth(), thumbsGroup[curPage].getY(), 1f), visible(false)));
//
//				thumbsGroup[curPage - 1].addAction(sequence(alpha(0), moveTo(0, thumbsGroup[curPage - 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
//					@Override
//					public void run() {
//						dragInProgress = false;
//					}
//				})));
//				curPage--;
//			}
//			if (curPage <= 0) {
//				leftButtonGroup.setVisible(false);
//			}
//			if (curPage < totalPages) {
//				rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
//			}
//			if (curPage != Math.floor(selectedThumb / (THUMBS_PER_COL * THUMBS_PER_ROW))) {
//				printText = false;
//				midButtonGroup.addAction(sequence(fadeOut(1f), visible(false)));
//			} else {
//				printText = true;
//				midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
//			}
//		}
//	}
}
