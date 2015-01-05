package com.avy.cflag.game.elements;

import com.avy.cflag.base.Point;
import com.avy.cflag.base.Rect;
import com.avy.cflag.game.EnumStore.BulletState;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.ExplodeState;
import com.avy.cflag.game.PlayUtils;

public class Bullet {

	private Direction curBulletDirection;
	private Rect curBulletRect;
	private Point curBulletPos;
	private BulletState curBulletState;
	private Point explodePos;
	private ExplodeState explodeState;
	
	public Bullet() {
		curBulletDirection = Direction.Up;
		curBulletRect = new Rect(0, 0, 0, 0);
		curBulletState = BulletState.Fired;
		curBulletPos = new Point(0, 0);
		explodePos = new Point(0, 0);
		explodeState = ExplodeState.Off;
	}

	@Override
	public Bullet clone() {
		final Bullet newBullet = new Bullet();
		newBullet.curBulletDirection = curBulletDirection;
		newBullet.curBulletPos = curBulletPos;
		newBullet.curBulletRect = curBulletRect;
		newBullet.curBulletState = curBulletState;
		newBullet.explodePos = explodePos;
		newBullet.explodeState = ExplodeState.Off;
		return newBullet;
	}

	public void resetBullet() {
		curBulletDirection = Direction.Up;
		curBulletRect = new Rect(0, 0, 0, 0);
		curBulletState = BulletState.Fired;
		curBulletPos = new Point(0, 0);
		explodePos = new Point(0, 0);
	}

	public void createBullet(final Point bulletPos, final Direction bulletDirection) {
		curBulletPos = bulletPos;
		curBulletDirection = bulletDirection;
		createBullet();
	}

	public void createBullet() {
		final Point curPixPos = PlayUtils.getCenterPixPos(curBulletPos);
		explodePos = new Point(0,0);

		switch (curBulletState) {
			case Fired:
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y - 15, curPixPos.x + 3, curPixPos.y - 10);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y + 10, curPixPos.x + 3, curPixPos.y + 15);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x + 10, curPixPos.y - 3, curPixPos.x + 15, curPixPos.y + 3);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x - 15, curPixPos.y - 3, curPixPos.x - 10, curPixPos.y + 3);
						break;
				}
				break;
			case HitBrick:
				explodeState = ExplodeState.BreakOn;
				explodePos = curPixPos;
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y + 10, curPixPos.x + 3, curPixPos.y + 15);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y - 15, curPixPos.x + 3, curPixPos.y - 10);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x - 15, curPixPos.y - 3, curPixPos.x - 10, curPixPos.y + 3);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x + 10, curPixPos.y - 3, curPixPos.x + 15, curPixPos.y + 3);
						break;
				}
				break;
			case HitDTank:
				explodeState = ExplodeState.BlastOn;
				switch (curBulletDirection) {
					case Up:
						explodePos = new Point(curPixPos.x, curPixPos.y + 15);
						break;
					case Down:
						explodePos = new Point(curPixPos.x, curPixPos.y - 15);
						break;
					case Right:
						explodePos = new Point(curPixPos.x-15, curPixPos.y);
						break;
					case Left:
						explodePos = new Point(curPixPos.x+15, curPixPos.y);
						break;
				}
				break;
			case HitTank:
				explodeState = ExplodeState.BurnOn;
				explodePos = curPixPos;
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y + 10, curPixPos.x + 3, curPixPos.y + 15);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y - 15, curPixPos.x + 3, curPixPos.y - 10);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x - 15, curPixPos.y - 3, curPixPos.x - 10, curPixPos.y + 3);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x + 10, curPixPos.y - 3, curPixPos.x + 15, curPixPos.y + 3);
						break;
				}
				break;
			case InMotion:
				switch (curBulletDirection) {
					case Up:
					case Down:
						curBulletRect = new Rect(curPixPos.x - 2, curPixPos.y - 13, curPixPos.x + 2, curPixPos.y + 13);
						break;
					case Right:
					case Left:
						curBulletRect = new Rect(curPixPos.x - 13, curPixPos.y - 2, curPixPos.x + 13, curPixPos.y + 2);
						break;
				}
				break;
			case MoveBlock:
			case MoveTank:
			case MoveMirror:
				explodeState = ExplodeState.BlastOn;
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 7, curPixPos.y + 14, curPixPos.x + 7, curPixPos.y + 16);
						explodePos = new Point(curPixPos.x, curPixPos.y + 15);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 7, curPixPos.y - 16, curPixPos.x + 7, curPixPos.y - 14);
						explodePos = new Point(curPixPos.x, curPixPos.y - 15);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x - 16, curPixPos.y - 7, curPixPos.x - 14, curPixPos.y + 7);
						explodePos = new Point(curPixPos.x-15, curPixPos.y);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x + 14, curPixPos.y - 7, curPixPos.x + 16, curPixPos.y + 7);
						explodePos = new Point(curPixPos.x+15, curPixPos.y);
						break;
				}
				break;
			case PassCrystal:
				curBulletRect = new Rect(0, 0, 0, 0);
				break;
			case HitSteel:
				explodeState = ExplodeState.BlastOn;
				switch (curBulletDirection) {
					case Up:
						explodePos = new Point(curPixPos.x, curPixPos.y + 15);
						break;
					case Down:
						explodePos = new Point(curPixPos.x, curPixPos.y - 15);
						break;
					case Right:
						explodePos = new Point(curPixPos.x-15, curPixPos.y);
						break;
					case Left:
						explodePos = new Point(curPixPos.x+15, curPixPos.y);
						break;
				}
				curBulletRect = new Rect(0, 0, 0, 0);
				break;
			case EnterMMirror:
			case EnterRMirror:
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y, curPixPos.x + 3, curPixPos.y + 13);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y - 13, curPixPos.x + 3, curPixPos.y);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x - 13, curPixPos.y - 3, curPixPos.x, curPixPos.y + 3);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x, curPixPos.y - 3, curPixPos.x + 13, curPixPos.y + 3);
						break;
				}
				break;
			case ExitMMirror:
			case ExitRMirror:
				switch (curBulletDirection) {
					case Up:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y - 13, curPixPos.x + 3, curPixPos.y);
						break;
					case Down:
						curBulletRect = new Rect(curPixPos.x - 3, curPixPos.y, curPixPos.x + 3, curPixPos.y + 13);
						break;
					case Right:
						curBulletRect = new Rect(curPixPos.x, curPixPos.y - 3, curPixPos.x + 13, curPixPos.y + 3);
						break;
					case Left:
						curBulletRect = new Rect(curPixPos.x - 13, curPixPos.y - 3, curPixPos.x, curPixPos.y + 3);
						break;
				}
				break;
			default:
				break;
		}
	}

	public Direction getCurBulletDirection() {
		return curBulletDirection;
	}

	public void setCurBulletDirection(final Direction curBulletDirection) {
		this.curBulletDirection = curBulletDirection;
	}

	public Rect getCurBulletRect() {
		return curBulletRect;
	}

	public void setCurBulletRect(final Rect curBulletRect) {
		this.curBulletRect = curBulletRect;
	}

	public Point getCurBulletPos() {
		return curBulletPos;
	}

	public void setCurBulletPos(final Point curBulletPos) {
		this.curBulletPos = curBulletPos;
	}

	public BulletState getCurBulletState() {
		return curBulletState;
	}

	public void setCurBulletState(final BulletState curBulletState) {
		this.curBulletState = curBulletState;
	}
	
	public void setExplodePos(Point explodePos) {
		this.explodePos = explodePos;
	}
	
	public Point getExplodePos() {
		return explodePos;
	}
	
	public ExplodeState getExplodeState() {
		return explodeState;
	}
	
	public void setExplodeState(ExplodeState explodeState) {
		this.explodeState = explodeState;
	}

}
