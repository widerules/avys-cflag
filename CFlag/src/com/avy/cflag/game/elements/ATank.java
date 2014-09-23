package com.avy.cflag.game.elements;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.MemStore.AntiTankState;
import com.avy.cflag.game.MemStore.BulletState;
import com.avy.cflag.game.MemStore.Direction;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.Utils;

public class ATank {

	private Bullet tankBullet;
	private Point tankPos;
	private Direction tankDirection;
	private AntiTankState tankState;
	private int tankStateTime;

	public ATank() {
		tankBullet = new Bullet();
		tankPos = new Point(0, 0);
		tankDirection = Direction.Up;
		tankState = AntiTankState.Exploded;
		tankStateTime = 0;
	}

	public ATank clone() {
		ATank newATank = new ATank();
		newATank.tankBullet = tankBullet;
		newATank.tankPos = tankPos;
		newATank.tankDirection = tankDirection;
		newATank.tankState = tankState;
		newATank.tankStateTime = tankStateTime;
		return newATank;
	}

	public void fireTank(LTank lTank) {
		if (tankStateTime > 0) {
			tankStateTime--;
		} else {
			PlayImages lvlPlayField[][] = lTank.getLvlPlayField();
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

					Point curBltPos = tankBullet.getCurBulletPos();
					Point nxtBltPos = Utils.getNextPosition(curBltPos, tankBullet.getCurBulletDirection());
					PlayImages nxtPosObj = PlayImages.OutOfBounds;
					if (Utils.positionWithinBounds(nxtBltPos))
						nxtPosObj = lvlPlayField[nxtBltPos.x][nxtBltPos.y];
					switch (nxtPosObj) {
						case Hero_D:
						case Hero_L:
						case Hero_U:
						case Hero_R:
							tankBullet.setCurBulletState(BulletState.HitTank);
							break;
						case Brick:
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

	public void setTankBullet(Bullet tankBullet) {
		this.tankBullet = tankBullet;
	}

	public Point getTankPos() {
		return tankPos;
	}

	public void setTankPos(Point tankPos) {
		this.tankPos = tankPos;
	}

	public Direction getTankDirection() {
		return tankDirection;
	}

	public void setTankDirection(Direction tankDirection) {
		this.tankDirection = tankDirection;
	}

	public AntiTankState getTankState() {
		return tankState;
	}

	public void setTankState(AntiTankState tankState) {
		this.tankState = tankState;
	}

	public int getTankStateTime() {
		return tankStateTime;
	}

	public void setTankStateTime(int tankStateTime) {
		this.tankStateTime = tankStateTime;
	}

}
