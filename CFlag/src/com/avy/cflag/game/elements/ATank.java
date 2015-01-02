package com.avy.cflag.game.elements;

import com.avy.cflag.base.Point;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.EnumStore.AntiTankState;
import com.avy.cflag.game.EnumStore.BulletState;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.PlayUtils;

public class ATank {

	private Bullet tankBullet;
	private Point tankPos;
	private Direction tankDirection;
	private AntiTankState tankState;
	private int tankStateTime;
	private Point curPosOnIce;
	private Direction curOnIceDrc;

	public ATank() {
		tankBullet = new Bullet();
		tankPos = new Point(0, 0);
		tankDirection = Direction.Up;
		tankState = AntiTankState.Exploded;
		tankStateTime = 0;
		curPosOnIce = new Point(0, 0);
		curOnIceDrc = Direction.Up;
	}

	@Override
	public ATank clone() {
		final ATank newATank = new ATank();
		newATank.tankBullet = tankBullet;
		newATank.tankPos = tankPos;
		newATank.tankDirection = tankDirection;
		newATank.tankState = tankState;
		newATank.tankStateTime = tankStateTime;
		newATank.curPosOnIce = curPosOnIce;
		newATank.curOnIceDrc = curOnIceDrc;
		return newATank;
	}

	public boolean fireTank(final LTank lTank) {
		boolean stateChanged = false;
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {
			if (tankBullet.getCurBulletState() == BulletState.Exploded) {
				tankBullet.resetBullet();
				tankState = AntiTankState.Exploded;
			} else if (tankBullet.getCurBulletState() == BulletState.HitHero) { 
				tankBullet.resetBullet();
				tankState = AntiTankState.HitTank;
			} else {

				if (tankBullet.getCurBulletState() == BulletState.Fired) {
					tankStateTime = 0;
					curPosOnIce = new Point(0,0);
					curOnIceDrc = Direction.Up;
					tankState = AntiTankState.Firing;
					tankBullet.createBullet(tankPos, tankDirection);
				} else {
					tankBullet.createBullet();
				}
				final PlayImages lvlBaseField[][] = lTank.getLvlBaseField();
				final PlayImages lvlPlayField[][] = lTank.getLvlPlayField();

				final Point curBltPos = tankBullet.getCurBulletPos();
				Point nxtBltPos = PlayUtils.getNextPosition(curBltPos, tankBullet.getCurBulletDirection());
				PlayImages nxtPosObj = PlayImages.OutOfBounds;
				if (PlayUtils.positionWithinBounds(nxtBltPos)) {
					nxtPosObj = lvlPlayField[nxtBltPos.x][nxtBltPos.y];
				}
				final PlayImages curPosObj = lvlPlayField[curBltPos.x][curBltPos.y];
				final PlayImages curPosBaseObj = lvlBaseField[curBltPos.x][curBltPos.y];
				final BulletState curBulletState = tankBullet.getCurBulletState();
				final Direction curBulletDirection = tankBullet.getCurBulletDirection();
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
								tankBullet.setCurBulletState(BulletState.Exploded);
								isMoved = true;
								break;
							case Ice:
							case ThinIce:
								lvlPlayField[nxtBltPos.x][nxtBltPos.y] = curPosObj;
								tankBullet.resetBullet();
								tankState = AntiTankState.ObjOnIce;
								curPosOnIce = nxtBltPos;
								curOnIceDrc = curBulletDirection;
								tankStateTime = 0;
								isMoved = true;
								break;
							case Water:
								if (curBulletState == BulletState.MoveBlock) {
									lvlBaseField[nxtBltPos.x][nxtBltPos.y] = PlayImages.Bridge;
									lvlPlayField[nxtBltPos.x][nxtBltPos.y] = PlayImages.Bridge;
								}
								tankBullet.setCurBulletState(BulletState.Exploded);
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
								nxtBltPos = lTank.getTargetTunnelLocation(nxtPosObj, nxtBltPos);
								lvlPlayField[curBltPos.x][curBltPos.y] = curPosBaseObj;
								lvlPlayField[nxtBltPos.x][nxtBltPos.y] = curPosObj;
								tankBullet.setCurBulletState(BulletState.Exploded);
								isMoved = true;
								break;
							default:
								tankBullet.setCurBulletState(BulletState.Exploded);
								break;
						}
						if (isMoved) {
							stateChanged = true;
							if (curPosObj == curPosBaseObj) {
								lvlBaseField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
								lvlPlayField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
							} else if (curPosBaseObj == PlayImages.ThinIce) {
								lvlBaseField[curBltPos.x][curBltPos.y] = PlayImages.Water;
							} else {
								lvlPlayField[curBltPos.x][curBltPos.y] = curPosBaseObj;
							}
						}
						break;
					case HitRMirror:
						stateChanged = true;
						lvlPlayField[curBltPos.x][curBltPos.y] = lTank.getRotatedMirror(curPosObj);
						tankBullet.setCurBulletState(BulletState.Exploded);
						break;
					case HitBrick:
						stateChanged = true;
						lvlBaseField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
						lvlPlayField[curBltPos.x][curBltPos.y] = PlayImages.Grass;
						tankBullet.setCurBulletState(BulletState.Exploded);
						break;
					case HitSteel:
					case HitDTank:
						tankBullet.setCurBulletState(BulletState.Exploded);
						break;
					case HitTank:
						stateChanged = true;
						lvlPlayField[curBltPos.x][curBltPos.y] = PlayUtils.destroyTank(curPosObj);
						tankBullet.setCurBulletState(BulletState.Exploded);
						break;
					case EnterMMirror:
						tankBullet.setCurBulletDirection(lTank.getMirrorReflectDirecion(curPosObj, curBulletDirection));
						nxtBltPos = curBltPos;
						tankBullet.setCurBulletState(BulletState.ExitMMirror);
						break;
					case EnterRMirror:
						tankBullet.setCurBulletDirection(lTank.getMirrorReflectDirecion(curPosObj, curBulletDirection));
						nxtBltPos = curBltPos;
						tankBullet.setCurBulletState(BulletState.ExitRMirror);
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
								tankBullet.setCurBulletState(BulletState.HitHero);
								break;
							case DVillain_D:
							case DVillain_L:
							case DVillain_R:
							case DVillain_U:
								tankBullet.setCurBulletState(BulletState.HitDTank);
								break;
							case Villain_U:
								if (curBulletDirection == Direction.Down) {
									tankBullet.setCurBulletState(BulletState.HitTank);
								} else {
									tankBullet.setCurBulletState(BulletState.MoveTank);
								}
								break;
							case Villain_R:
								if (curBulletDirection == Direction.Left) {
									tankBullet.setCurBulletState(BulletState.HitTank);
								} else {
									tankBullet.setCurBulletState(BulletState.MoveTank);
								}
								break;
							case Villain_D:
								if (curBulletDirection == Direction.Up) {
									tankBullet.setCurBulletState(BulletState.HitTank);
								} else {
									tankBullet.setCurBulletState(BulletState.MoveTank);
								}
								break;
							case Villain_L:
								if (curBulletDirection == Direction.Right) {
									tankBullet.setCurBulletState(BulletState.HitTank);
								} else {
									tankBullet.setCurBulletState(BulletState.MoveTank);
								}
								break;
							case Brick:
								tankBullet.setCurBulletState(BulletState.HitBrick);
								break;
							case Rubber:
								tankBullet.setCurBulletState(BulletState.PassCrystal);
								break;
							case MBlock:
								tankBullet.setCurBulletState(BulletState.MoveBlock);
								break;
							case MMirror_D:
								if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Down)) {
									tankBullet.setCurBulletState(BulletState.MoveMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterMMirror);
								}
								break;
							case MMirror_L:
								if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Down)) {
									tankBullet.setCurBulletState(BulletState.MoveMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterMMirror);
								}
								break;
							case MMirror_R:
								if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Up)) {
									tankBullet.setCurBulletState(BulletState.MoveMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterMMirror);
								}
								break;
							case MMirror_U:
								if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Up)) {
									tankBullet.setCurBulletState(BulletState.MoveMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterMMirror);
								}
								break;
							case RMirror_D:
								if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Down)) {
									tankBullet.setCurBulletState(BulletState.HitRMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterRMirror);
								}
								break;
							case RMirror_L:
								if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Down)) {
									tankBullet.setCurBulletState(BulletState.HitRMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterRMirror);
								}
								break;
							case RMirror_R:
								if ((curBulletDirection == Direction.Right) || (curBulletDirection == Direction.Up)) {
									tankBullet.setCurBulletState(BulletState.HitRMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterRMirror);
								}
								break;
							case RMirror_U:
								if ((curBulletDirection == Direction.Left) || (curBulletDirection == Direction.Up)) {
									tankBullet.setCurBulletState(BulletState.HitRMirror);
								} else {
									tankBullet.setCurBulletState(BulletState.EnterRMirror);
								}
								break;
							case Steel:
								tankBullet.setCurBulletState(BulletState.HitSteel);
								break;
							case OutOfBounds:
								tankBullet.setCurBulletState(BulletState.Exploded);
								break;
							default:
								tankBullet.setCurBulletState(BulletState.InMotion);
								break;
						}

						switch (tankBullet.getCurBulletState()) {
							case HitSteel:
							case MoveMirror:
							case MoveBlock:
							case HitBrick:
							case HitTank:
							case MoveTank:
								Sounds.blast.mixPlay();
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
				tankBullet.setCurBulletPos(nxtBltPos);
			}
		}
		return stateChanged;
	}

	public boolean slideObjOnIce(final LTank lTank) {
		boolean isMoved = false;
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {

			final PlayImages lvlBaseField[][] = lTank.getLvlBaseField();
			final PlayImages lvlPlayField[][] = lTank.getLvlPlayField();

			Point nxtMovePos = PlayUtils.getNextPosition(curPosOnIce, curOnIceDrc);
			final PlayImages curPosObj = lvlPlayField[curPosOnIce.x][curPosOnIce.y];
			final PlayImages curPosBaseObj = lvlBaseField[curPosOnIce.x][curPosOnIce.y];
			PlayImages nxtPosObj = PlayImages.OutOfBounds;
			if (PlayUtils.positionWithinBounds(nxtMovePos)) {
				nxtPosObj = lvlPlayField[nxtMovePos.x][nxtMovePos.y];
			} else {
				tankState = AntiTankState.Exploded;
			}

			switch (nxtPosObj) {
				case Grass:
				case Bridge:
					lvlPlayField[nxtMovePos.x][nxtMovePos.y] = curPosObj;
					tankState = AntiTankState.Exploded;
					isMoved = true;
					break;
				case Stream_D:
				case Stream_L:
				case Stream_R:
				case Stream_U:
					lvlPlayField[nxtMovePos.x][nxtMovePos.y] = curPosObj;
					tankState = AntiTankState.Exploded;
					isMoved = true;
					break;
				case Ice:
				case ThinIce:
					lvlPlayField[nxtMovePos.x][nxtMovePos.y] = curPosObj;
					tankState = AntiTankState.ObjOnIce;
					tankStateTime = 0;
					isMoved = true;
					break;
				case Water:
					if (curPosObj == PlayImages.MBlock) {
						lvlBaseField[nxtMovePos.x][nxtMovePos.y] = PlayImages.Bridge;
						lvlPlayField[nxtMovePos.x][nxtMovePos.y] = PlayImages.Bridge;
					}
					tankState = AntiTankState.Exploded;
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
					nxtMovePos = lTank.getTargetTunnelLocation(nxtPosObj, nxtMovePos);
					lvlPlayField[curPosOnIce.x][curPosOnIce.y] = curPosBaseObj;
					lvlPlayField[nxtMovePos.x][nxtMovePos.y] = curPosObj;
					tankState = AntiTankState.Exploded;
					isMoved = true;
					break;
				default:
					break;
			}
			if (isMoved) {
				if (curPosBaseObj == PlayImages.ThinIce) {
					lvlBaseField[curPosOnIce.x][curPosOnIce.y] = PlayImages.Water;
					lvlPlayField[curPosOnIce.x][curPosOnIce.y] = PlayImages.Water;
				} else {
					lvlPlayField[curPosOnIce.x][curPosOnIce.y] = curPosBaseObj;
				}
				if (tankState == AntiTankState.ObjOnIce) {
					curPosOnIce = nxtMovePos;
				}
			}
		}
		return isMoved;
	}

	public Bullet getTankBullet() {
		return tankBullet;
	}

	public void setTankBullet(final Bullet tankBullet) {
		this.tankBullet = tankBullet;
	}

	public Point getTankPos() {
		return tankPos;
	}

	public void setTankPos(final Point tankPos) {
		this.tankPos = tankPos;
	}

	public Direction getTankDirection() {
		return tankDirection;
	}

	public void setTankDirection(final Direction tankDirection) {
		this.tankDirection = tankDirection;
	}

	public AntiTankState getTankState() {
		return tankState;
	}

	public void setTankState(final AntiTankState tankState) {
		this.tankState = tankState;
	}

	public int getTankStateTime() {
		return tankStateTime;
	}

	public void setTankStateTime(final int tankStateTime) {
		this.tankStateTime = tankStateTime;
	}

}
