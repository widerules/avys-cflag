package com.avy.cflag.game.elements;

import static com.avy.cflag.game.EnumStore.FixedObjects;
import static com.avy.cflag.game.EnumStore.StreamObjects;
import static com.avy.cflag.game.EnumStore.TunnelObjects;
import static com.avy.cflag.game.MemStore.lvlFieldLEN;

import com.avy.cflag.base.Point;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.EnumStore.AntiTankState;
import com.avy.cflag.game.EnumStore.BulletState;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.FirePathState;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.EnumStore.TankState;
import com.avy.cflag.game.PlayUtils;

public class LTank {

	private Point curTankPos;
	private Direction curTankDirection;
	private Bullet curTankBullet;
	private PlayImages[][] lvlBaseField;
	private PlayImages[][] lvlPlayField;

	private TankState curTankState;
	private TankState storedTankState;
	private int tankStateTime;

	private ATank aTankPrev;
	private ATank aTankCur;

	private int tankMoves;
	private int tankShots;

	private int undoChangeCount;
	private boolean uiStateChanged;

	public LTank() {
	};

	public LTank(final Level inLvl) {
		lvlPlayField = inLvl.getLvlPlayField();
		lvlBaseField = inLvl.getLvlBaseField();
		curTankPos = inLvl.getTankOrigPos();
		curTankDirection = Direction.Up;
		curTankState = TankState.Moving;
		tankStateTime = 0;
		curTankBullet = new Bullet();
		aTankPrev = new ATank();
		aTankCur = new ATank();
		storedTankState = TankState.Moving;
		tankMoves = 0;
		tankShots = 0;
		undoChangeCount = 0;
		uiStateChanged = false;
	}

	@Override
	public LTank clone() {
		final LTank newLTank = new LTank();
		newLTank.curTankDirection = curTankDirection;
		newLTank.curTankPos = curTankPos;
		newLTank.curTankState = curTankState;

		newLTank.lvlBaseField = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];
		newLTank.lvlPlayField = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];

		for (int x = 0; x < lvlFieldLEN.x; x++) {
			for (int y = 0; y < lvlFieldLEN.y; y++) {
				newLTank.lvlBaseField[x][y] = lvlBaseField[x][y];
				newLTank.lvlPlayField[x][y] = lvlPlayField[x][y];
			}
		}

		newLTank.storedTankState = storedTankState;
		newLTank.tankMoves = tankMoves;
		newLTank.tankShots = tankShots;
		newLTank.tankStateTime = tankStateTime;
		newLTank.aTankCur = aTankCur.clone();
		newLTank.aTankPrev = aTankPrev.clone();
		newLTank.curTankBullet = curTankBullet.clone();
		return newLTank;
	}

	public boolean turnTank(final Direction turnDirection) {
		boolean turnTank = false;
		final PlayImages curTankObj = lvlPlayField[curTankPos.x][curTankPos.y];
		final PlayImages nxtTankObj = PlayUtils.turnTank(turnDirection);
		switch (turnDirection) {
		case Up:
			turnTank = curTankObj != PlayImages.Hero_U;
			break;
		case Right:
			turnTank = curTankObj != PlayImages.Hero_R;
			break;
		case Down:
			turnTank = curTankObj != PlayImages.Hero_D;
			break;
		case Left:
			turnTank = curTankObj != PlayImages.Hero_L;
			break;
		}

		if (turnTank) {
			curTankDirection = turnDirection;
			lvlPlayField[curTankPos.x][curTankPos.y] = nxtTankObj;
		}
		return turnTank;
	}

	public boolean moveTank(Direction moveDirection) {
		boolean isMoved = false;
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {

			boolean stateChangedForUndo = false;
			uiStateChanged = false;
			Direction onStreamDirection = Direction.Up;
			TankState tmpTankState = TankState.Moving;
			if (curTankState == TankState.OnStream) {
				if (isPosOnFire(aTankPrev, curTankPos)) {
					tmpTankState = TankState.PrevATankFired;
				}
			}

			final PlayImages curBaseObj = lvlBaseField[curTankPos.x][curTankPos.y];
			final PlayImages curTankObj = lvlPlayField[curTankPos.x][curTankPos.y];
			TankState nxtTankState = curTankState;
			Point nxtTankPos = curTankPos;

			if (curTankState == TankState.OnTunnel) {
				nxtTankPos = getTargetTunnelLocation(curBaseObj, curTankPos);
				lvlPlayField[curTankPos.x][curTankPos.y] = curBaseObj;
				lvlPlayField[nxtTankPos.x][nxtTankPos.y] = curTankObj;
				tankStateTime = 0;
				nxtTankState = TankState.Moving;
				curTankPos = nxtTankPos;
				isMoved = true;
				stateChangedForUndo = true;
				uiStateChanged = true;
			} else if (!turnTank(moveDirection)) {

				if (curTankState == TankState.OnStream) {
					moveDirection = PlayUtils.getDirection(curBaseObj);
				}

				nxtTankPos = PlayUtils.getNextPosition(curTankPos, moveDirection);

				if (PlayUtils.positionWithinBounds(nxtTankPos)) {
					final PlayImages nxtPosObj = lvlPlayField[nxtTankPos.x][nxtTankPos.y];
					if (!FixedObjects.contains(nxtPosObj)) {
						lvlPlayField[curTankPos.x][curTankPos.y] = curBaseObj;
						lvlPlayField[nxtTankPos.x][nxtTankPos.y] = curTankObj;

						if (TunnelObjects.contains(nxtPosObj)) {
							tankStateTime = 4;
							stateChangedForUndo = true;
							nxtTankState = TankState.OnTunnel;
						} else if (StreamObjects.contains(nxtPosObj)) {
							tankStateTime = 1;
							nxtTankState = TankState.OnStream;
							onStreamDirection = PlayUtils.getDirection(nxtPosObj);
							if (curTankState != nxtTankState) {
								stateChangedForUndo = true;
							}
						} else if (nxtPosObj == PlayImages.Ice) {
							tankStateTime = 2;
							nxtTankState = TankState.OnIce;
						} else if (nxtPosObj == PlayImages.ThinIce) {
							lvlBaseField[nxtTankPos.x][nxtTankPos.y] = PlayImages.Water;
						} else if (nxtPosObj == PlayImages.Water) {
							tankStateTime = 0;
							nxtTankState = TankState.OnWater;
							stateChangedForUndo = true;
						} else if (nxtPosObj == PlayImages.Flag) {
							tankStateTime = 0;
							nxtTankState = TankState.ReachedFlag;
							stateChangedForUndo = true;
						} else {
							tankStateTime = 0;
							nxtTankState = TankState.Moving;
							stateChangedForUndo = true;
						}

						curTankPos = nxtTankPos;
						isMoved = true;
						uiStateChanged = true;
					} else {
						tankStateTime = 0;
						nxtTankState = TankState.Blocked;
					}
				}
			} else {
				stateChangedForUndo = true;
				uiStateChanged = true;
			}

			if (((nxtTankState == TankState.OnStream) && (onStreamDirection != moveDirection)) || ((nxtTankState != TankState.OnStream) && (nxtTankState != TankState.Blocked))) {
				if (isPosOnFire(aTankCur, curTankPos)) {
					if (tmpTankState == TankState.PrevATankFired) {
						nxtTankState = TankState.BothATankFired;
					} else {
						nxtTankState = TankState.CurATankFired;
					}
				}
			}
			if (tmpTankState == TankState.PrevATankFired) {
				if (nxtTankState == TankState.Blocked) {
					aTankCur = aTankPrev;
					nxtTankState = TankState.CurATankFired;
				} else {
					storedTankState = nxtTankState;
					nxtTankState = TankState.PrevATankFired;
				}
			}

			curTankState = nxtTankState;
			if (stateChangedForUndo) {
				undoChangeCount++;
			}
		}

		return isMoved;
	}

	public void fireATankCur() {
		if (aTankCur.getTankState() != AntiTankState.HitTank) {
			aTankCur.fireTank(this);
		} else {
			curTankState = TankState.ShotDead;
		}
	}

	public void fireATankPrev() {
		if (aTankPrev.getTankState() != AntiTankState.Exploded) {
			aTankPrev.fireTank(this);
		} else {
			curTankState = storedTankState;
		}
	}

	public void fireBothATanks() {
		if (aTankPrev.getTankState() != AntiTankState.Exploded) {
			aTankPrev.fireTank(this);
		} else {
			if (aTankCur.getTankState() != AntiTankState.HitTank) {
				aTankCur.fireTank(this);
			} else {
				curTankState = TankState.ShotDead;
			}
		}
	}

	public void fireTank() {
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {
			boolean stateChanged = false;
			if (curTankBullet.getCurBulletState() == BulletState.Exploded) {
				curTankBullet.resetBullet();
				curTankState = TankState.Moving;
			} else {

				if (curTankBullet.getCurBulletState() == BulletState.Fired) {
					tankStateTime = 3;
					curTankState = TankState.Firing;
					curTankBullet.createBullet(new Point(curTankPos.x, curTankPos.y), curTankDirection);
				} else {
					curTankBullet.createBullet();
				}

				final Point curBltPos = curTankBullet.getCurBulletPos();
				Point nxtBltPos = PlayUtils.getNextPosition(curBltPos, curTankBullet.getCurBulletDirection());
				PlayImages nxtPosObj = PlayImages.OutOfBounds;
				if (PlayUtils.positionWithinBounds(nxtBltPos)) {
					nxtPosObj = lvlPlayField[nxtBltPos.x][nxtBltPos.y];
				}
				final PlayImages curPosObj = lvlPlayField[curBltPos.x][curBltPos.y];
				final PlayImages curPosBaseObj = lvlBaseField[curBltPos.x][curBltPos.y];
				final BulletState curBulletState = curTankBullet.getCurBulletState();
				final Direction curBulletDirection = curTankBullet.getCurBulletDirection();
				switch (curBulletState) {
				case MoveBlock:
				case MoveMirror:
				case MoveTank:
					boolean isMoved = false;
					switch (nxtPosObj) {
					case Grass:
					case Bridge:
					case Stream_D:
					case Stream_L:
					case Stream_R:
					case Stream_U:
						lvlPlayField[nxtBltPos.x][nxtBltPos.y] = curPosObj;
						curTankBullet.setCurBulletState(BulletState.Exploded);
						if(StreamObjects.contains(lvlBaseField[curTankPos.x][curTankPos.y]) && curTankDirection==PlayUtils.getDirection(lvlBaseField[curTankPos.x][curTankPos.y])) {
							curTankState=TankState.OnStream;
							curTankBullet.resetBullet();
						}
						isMoved = true;
						break;
					case Water:
						if (curBulletState == BulletState.MoveBlock) {
							lvlBaseField[nxtBltPos.x][nxtBltPos.y] = PlayImages.Bridge;
							lvlPlayField[nxtBltPos.x][nxtBltPos.y] = PlayImages.Bridge;
						}
						curTankBullet.setCurBulletState(BulletState.Exploded);
						isMoved = true;
						break;
					case Tunnel_0:
					case Tunnel_1:
					case Tunnel_2:
					case Tunnel_3:
					case Tunnel_4:
					case Tunnel_5:
					case Tunnel_6:
					case Tunnel_7:
						nxtBltPos = getTargetTunnelLocation(nxtPosObj, nxtBltPos);
						lvlPlayField[curBltPos.x][curBltPos.y] = curPosBaseObj;
						lvlPlayField[nxtBltPos.x][nxtBltPos.y] = curPosObj;
						curTankBullet.setCurBulletState(BulletState.Exploded);
						isMoved = true;
						break;
					default:
						curTankBullet.setCurBulletState(BulletState.Exploded);
						break;
					}
					if (isMoved) {
						stateChanged = true;
						if (curPosObj == curPosBaseObj) {
							lvlBaseField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
							lvlPlayField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
						} else {
							lvlPlayField[curBltPos.x][curBltPos.y] = curPosBaseObj;
						}
					}
					break;
				case HitRMirror:
					stateChanged = true;
					lvlPlayField[curBltPos.x][curBltPos.y] = getRotatedMirror(curPosObj);
					curTankBullet.setCurBulletState(BulletState.Exploded);
					break;
				case HitBrick:
					stateChanged = true;
					lvlBaseField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
					lvlPlayField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
					curTankBullet.setCurBulletState(BulletState.Exploded);
					break;
				case HitSteel:
				case HitDTank:
					curTankBullet.setCurBulletState(BulletState.Exploded);
					break;
				case HitTank:
					stateChanged = true;
					lvlPlayField[curBltPos.x][curBltPos.y] = PlayUtils.destroyTank(curPosObj);
					curTankBullet.setCurBulletState(BulletState.Exploded);
					break;
				case EnterMMirror:
					curTankBullet.setCurBulletDirection(getMirrorReflectDirecion(curPosObj, curBulletDirection));
					nxtBltPos = curBltPos;
					curTankBullet.setCurBulletState(BulletState.ExitMMirror);
					break;
				case EnterRMirror:
					curTankBullet.setCurBulletDirection(getMirrorReflectDirecion(curPosObj, curBulletDirection));
					nxtBltPos = curBltPos;
					curTankBullet.setCurBulletState(BulletState.ExitRMirror);
					break;
				case InMotion:
				case ExitMMirror:
				case Fired:
				case ExitRMirror:
				case PassCrystal:
					switch (nxtPosObj) {
					case Hero_U:
					case Hero_D:
					case Hero_L:
					case Hero_R:
						curTankBullet.setCurBulletState(BulletState.Exploded);
						curTankState = TankState.ShotDead;
						break;
					case DVillain_D:
					case DVillain_L:
					case DVillain_R:
					case DVillain_U:
						curTankBullet.setCurBulletState(BulletState.HitDTank);
						break;
					case Villain_U:
						if (curBulletDirection == Direction.Down) {
							curTankBullet.setCurBulletState(BulletState.HitTank);
						} else {
							curTankBullet.setCurBulletState(BulletState.MoveTank);
						}
						break;
					case Villain_R:
						if (curBulletDirection == Direction.Left) {
							curTankBullet.setCurBulletState(BulletState.HitTank);
						} else {
							curTankBullet.setCurBulletState(BulletState.MoveTank);
						}
						break;
					case Villain_D:
						if (curBulletDirection == Direction.Up) {
							curTankBullet.setCurBulletState(BulletState.HitTank);
						} else {
							curTankBullet.setCurBulletState(BulletState.MoveTank);
						}
						break;
					case Villain_L:
						if (curBulletDirection == Direction.Right) {
							curTankBullet.setCurBulletState(BulletState.HitTank);
						} else {
							curTankBullet.setCurBulletState(BulletState.MoveTank);
						}
						break;
					case Brick:
						curTankBullet.setCurBulletState(BulletState.HitBrick);
						break;
					case Rubber:
						curTankBullet.setCurBulletState(BulletState.PassCrystal);
						break;
					case MBlock:
						curTankBullet.setCurBulletState(BulletState.MoveBlock);
						break;
					case MMirror_D:
						if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Down)) {
							curTankBullet.setCurBulletState(BulletState.MoveMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterMMirror);
						}
						break;
					case MMirror_L:
						if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Down)) {
							curTankBullet.setCurBulletState(BulletState.MoveMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterMMirror);
						}
						break;
					case MMirror_R:
						if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Up)) {
							curTankBullet.setCurBulletState(BulletState.MoveMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterMMirror);
						}
						break;
					case MMirror_U:
						if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Up)) {
							curTankBullet.setCurBulletState(BulletState.MoveMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterMMirror);
						}
						break;
					case RMirror_D:
						if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Down)) {
							curTankBullet.setCurBulletState(BulletState.HitRMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterRMirror);
						}
						break;
					case RMirror_L:
						if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Down)) {
							curTankBullet.setCurBulletState(BulletState.HitRMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterRMirror);
						}
						break;
					case RMirror_R:
						if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Up)) {
							curTankBullet.setCurBulletState(BulletState.HitRMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterRMirror);
						}
						break;
					case RMirror_U:
						if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Up)) {
							curTankBullet.setCurBulletState(BulletState.HitRMirror);
						} else {
							curTankBullet.setCurBulletState(BulletState.EnterRMirror);
						}
						break;
					case Steel:
						curTankBullet.setCurBulletState(BulletState.HitSteel);
						break;
					case OutOfBounds:
						curTankBullet.setCurBulletState(BulletState.Exploded);
						break;
					default:
						curTankBullet.setCurBulletState(BulletState.InMotion);
						break;
					}

					switch (curTankBullet.getCurBulletState()) {
					case HitSteel:
					case MoveMirror:
					case MoveBlock:
					case HitBrick:
					case HitTank:
					case MoveTank:
						Sounds.blast.play();
						break;
					case HitRMirror:
					case EnterRMirror:
					case EnterMMirror:
						Sounds.shoot.play();
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
				curTankBullet.setCurBulletPos(nxtBltPos);
			}
			if (stateChanged) {
				undoChangeCount++;
				if (isPosOnFire(aTankCur, curTankPos)) {
					curTankState = TankState.CurATankFired;
				}
			}
		}
	}

	public Point getTargetTunnelLocation(final PlayImages tunnelObj, final Point curPoint) {
		Point nxtPoint = curPoint;
		for (int i = 0; i < lvlFieldLEN.x; i++) {
			for (int j = 0; j < lvlFieldLEN.y; j++) {
				if (lvlPlayField[i][j] == tunnelObj) {
					final Point tmpPoint = new Point(i, j);
					if (!tmpPoint.equals(curPoint)) {
						nxtPoint = tmpPoint;
						return nxtPoint;
					}
				}
			}
		}
		return nxtPoint;
	}

	public boolean isPosOnFire(final ATank aTank, final Point curPos) {
		boolean onFire = false;
		int tankRightLen = 100;
		int tankLeftLen = 100;
		int tankDownLen = 100;
		int tankUpLen = 100;
		int tempLen = 1;
		int x = 0, y = 0;

		for (x = curPos.x + 1, tempLen = 1; x < lvlFieldLEN.x; x++, tempLen++) {
			final PlayImages curObj = lvlPlayField[x][curPos.y];
			final FirePathState curPathState = checkPathPosition(curObj, Direction.Right);
			if (curPathState == FirePathState.ATankFound) {
				tankRightLen = tempLen;
				onFire = true;
				break;
			} else if (curPathState == FirePathState.Blocked) {
				break;
			}
		}
		for (x = curPos.x - 1, tempLen = 1; x >= 0; x--, tempLen++) {
			final PlayImages curObj = lvlPlayField[x][curPos.y];
			final FirePathState curPathState = checkPathPosition(curObj, Direction.Left);
			if (curPathState == FirePathState.ATankFound) {
				tankLeftLen = tempLen;
				onFire = true;
				break;
			} else if (curPathState == FirePathState.Blocked) {
				break;
			}
		}
		for (y = curPos.y + 1, tempLen = 1; y < lvlFieldLEN.y; y++, tempLen++) {
			final PlayImages curObj = lvlPlayField[curPos.x][y];
			final FirePathState curPathState = checkPathPosition(curObj, Direction.Down);
			if (curPathState == FirePathState.ATankFound) {
				tankDownLen = tempLen;
				onFire = true;
				break;
			} else if (curPathState == FirePathState.Blocked) {
				break;
			}
		}
		for (y = curPos.y - 1, tempLen = 1; y >= 0; y--, tempLen++) {
			final PlayImages curObj = lvlPlayField[curPos.x][y];
			final FirePathState curPathState = checkPathPosition(curObj, Direction.Up);
			if (curPathState == FirePathState.ATankFound) {
				tankUpLen = tempLen;
				onFire = true;
				break;
			} else if (curPathState == FirePathState.Blocked) {
				break;
			}
		}
		if (onFire) {
			if ((tankRightLen < tankLeftLen) && (tankRightLen < tankDownLen) && (tankRightLen < tankUpLen)) {
				aTank.setTankPos(new Point(curPos.x + tankRightLen, curPos.y));
				aTank.setTankDirection(Direction.Left);
			} else if ((tankLeftLen < tankDownLen) && (tankLeftLen < tankUpLen)) {
				aTank.setTankPos(new Point(curPos.x - tankLeftLen, curPos.y));
				aTank.setTankDirection(Direction.Right);
			} else if (tankDownLen < tankUpLen) {
				aTank.setTankPos(new Point(curPos.x, curPos.y + tankDownLen));
				aTank.setTankDirection(Direction.Up);
			} else {
				aTank.setTankPos(new Point(curPos.x, curPos.y - tankUpLen));
				aTank.setTankDirection(Direction.Down);
			}
			aTank.setTankState(AntiTankState.Fired);
		}
		return onFire;
	}

	public FirePathState checkPathPosition(final PlayImages curObj, final Direction curDirection) {
		FirePathState newState = FirePathState.Blocked;

		switch (curObj) {
		case Bridge:
		case Grass:
		case Ice:
		case ThinIce:
		case Water:
		case Stream_D:
		case Stream_L:
		case Stream_R:
		case Stream_U:
			newState = FirePathState.Clear;
			break;
		case Villain_D:
			if (curDirection == Direction.Up) {
				newState = FirePathState.ATankFound;
			} else {
				newState = FirePathState.Blocked;
			}
			break;
		case Villain_R:
			if (curDirection == Direction.Left) {
				newState = FirePathState.ATankFound;
			} else {
				newState = FirePathState.Blocked;
			}
			break;
		case Villain_U:
			if (curDirection == Direction.Down) {
				newState = FirePathState.ATankFound;
			} else {
				newState = FirePathState.Blocked;
			}
			break;
		case Villain_L:
			if (curDirection == Direction.Right) {
				newState = FirePathState.ATankFound;
			} else {
				newState = FirePathState.Blocked;
			}
			break;
		default:
			newState = FirePathState.Blocked;
			break;
		}
		return newState;
	}

	public Direction getMirrorReflectDirecion(final PlayImages curBaseObj, final Direction curBulletDirection) {
		Direction newDirection = curBulletDirection;
		switch (curBulletDirection) {
		case Up:
			if (curBaseObj.name().contains("_D")) {
				newDirection = Direction.Right;
			}
			if (curBaseObj.name().contains("_L")) {
				newDirection = Direction.Left;
			}
			break;
		case Down:
			if (curBaseObj.name().contains("_R")) {
				newDirection = Direction.Right;
			}
			if (curBaseObj.name().contains("_U")) {
				newDirection = Direction.Left;
			}
			break;
		case Left:
			if (curBaseObj.name().contains("_D")) {
				newDirection = Direction.Down;
			}
			if (curBaseObj.name().contains("_R")) {
				newDirection = Direction.Up;
			}
			break;
		case Right:
			if (curBaseObj.name().contains("_U")) {
				newDirection = Direction.Up;
			}
			if (curBaseObj.name().contains("_L")) {
				newDirection = Direction.Down;
			}
			break;
		}
		return newDirection;
	}

	public PlayImages getRotatedMirror(final PlayImages curRMirror) {
		PlayImages newRMirror = null;
		switch (curRMirror) {
		case RMirror_U:
			newRMirror = PlayImages.RMirror_R;
			break;
		case RMirror_D:
			newRMirror = PlayImages.RMirror_L;
			break;
		case RMirror_L:
			newRMirror = PlayImages.RMirror_U;
			break;
		case RMirror_R:
			newRMirror = PlayImages.RMirror_D;
			break;
		default:
			break;
		}
		return newRMirror;
	}

	public void incrementTankMoves() {
		tankMoves++;
	}

	public void incrementTankShots() {
		tankShots++;
	}

	public PlayImages[][] getLvlBaseField() {
		return lvlBaseField;
	}

	public void setLvlBaseField(final PlayImages[][] lvlBaseField) {
		this.lvlBaseField = lvlBaseField;
	}

	public PlayImages[][] getLvlPlayField() {
		return lvlPlayField;
	}

	public void setLvlPlayField(final PlayImages[][] lvlPlayField) {
		this.lvlPlayField = lvlPlayField;
	}

	public Direction getCurTankDirection() {
		return curTankDirection;
	}

	public void setCurTankDirection(final Direction curTankDirection) {
		this.curTankDirection = curTankDirection;
	}

	public TankState getCurTankState() {
		return curTankState;
	}

	public void setCurTankState(final TankState curTankState) {
		this.curTankState = curTankState;
	}

	public int getTankMoves() {
		return tankMoves;
	}

	public void setTankMoves(final int tankMoves) {
		this.tankMoves = tankMoves;
	}

	public Point getCurTankPos() {
		return curTankPos;
	}

	public void setCurTankPos(final Point curTankPos) {
		this.curTankPos = curTankPos;
	}

	public Bullet getCurTankBullet() {
		return curTankBullet;
	}

	public void setCurTankBullet(final Bullet curTankBullet) {
		this.curTankBullet = curTankBullet;
	}

	public ATank getaTankPrev() {
		return aTankPrev;
	}

	public void setaTankPrev(final ATank aTankPrev) {
		this.aTankPrev = aTankPrev;
	}

	public ATank getaTankCur() {
		return aTankCur;
	}

	public void setaTankCur(final ATank aTankCur) {
		this.aTankCur = aTankCur;
	}

	public int getStateChangeCount() {
		return undoChangeCount;
	}

	public void setStateChangeCount(final int stateChangeCount) {
		undoChangeCount = stateChangeCount;
	}

	public int getTankShots() {
		return tankShots;
	}

	public void setTankShots(final int tankShots) {
		this.tankShots = tankShots;
	}

	public boolean isUiStateChanged() {
		return uiStateChanged;
	}

	public void setUiStateChanged(final boolean uiStateChanged) {
		this.uiStateChanged = uiStateChanged;
	}
}
