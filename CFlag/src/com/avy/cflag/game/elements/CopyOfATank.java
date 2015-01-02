package com.avy.cflag.game.elements;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.EnumStore.AntiTankState;
import com.avy.cflag.game.EnumStore.BulletState;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.PlayUtils;

public class CopyOfATank {

	private Bullet tankBullet;
	private Point tankPos;
	private Direction tankDirection;
	private AntiTankState tankState;
	private int tankStateTime;

	public CopyOfATank() {
		tankBullet = new Bullet();
		tankPos = new Point(0, 0);
		tankDirection = Direction.Up;
		tankState = AntiTankState.Exploded;
		tankStateTime = 0;
	}

	@Override
	public CopyOfATank clone() {
		final CopyOfATank newATank = new CopyOfATank();
		newATank.tankBullet = tankBullet;
		newATank.tankPos = tankPos;
		newATank.tankDirection = tankDirection;
		newATank.tankState = tankState;
		newATank.tankStateTime = tankStateTime;
		return newATank;
	}

	public void fireTank(final LTank lTank) {
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {
			final PlayImages lvlPlayField[][] = lTank.getLvlPlayField();
			tankStateTime = 0;
			switch (tankBullet.getCurBulletState()) {
				case Exploded:
					tankBullet.resetBullet();
					tankState = AntiTankState.Exploded;
					break;
				case HitTank:
					tankBullet.resetBullet();
					tankState = AntiTankState.HitTank;
					break;
				case Fired:
				case InMotion:
					if (tankBullet.getCurBulletState() == BulletState.Fired) {
						tankState = AntiTankState.Firing;
						tankBullet.createBullet(tankPos, tankDirection);
					} else {
						tankBullet.createBullet();
					}

					final Point curBltPos = tankBullet.getCurBulletPos();
					final Point nxtBltPos = PlayUtils.getNextPosition(curBltPos, tankBullet.getCurBulletDirection());
					PlayImages nxtPosObj = PlayImages.OutOfBounds;
					if (PlayUtils.positionWithinBounds(nxtBltPos)) {
						nxtPosObj = lvlPlayField[nxtBltPos.x][nxtBltPos.y];
					}
					switch (nxtPosObj) {
						case Hero_D:
						case Hero_L:
						case Hero_U:
						case Hero_R:
							tankBullet.setCurBulletState(BulletState.HitTank);
							break;
						case Brick:
							
							break;
						case Rubber:
						case Flag:
						case MBlock:
						case MMirror_D:
						case MMirror_L:
						case MMirror_R:
						case MMirror_U:
						case RMirror_D:
						case RMirror_L:
						case RMirror_R:
						case RMirror_U:
						case Steel:
						case Villain_D:
						case Villain_L:
						case Villain_R:
						case Villain_U:
						case OutOfBounds:
							tankBullet.setCurBulletState(BulletState.Exploded);
							break;
						default:
							tankBullet.setCurBulletState(BulletState.InMotion);
							break;
					}
					tankBullet.setCurBulletPos(nxtBltPos);
				default:
					break;
			}
		}
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
