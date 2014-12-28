package com.avy.cflag.game;

import static com.avy.cflag.game.MemStore.lvlFieldLEN;
import static com.avy.cflag.game.MemStore.playImageScaledLEN;
import static com.avy.cflag.game.MemStore.pltfrmStartPOS;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;

public class PlayUtils {
	public static Difficulty getDifficultyByVal(final int inDcltyValue) {
		Difficulty outDclty = null;

		for (final Difficulty curDclty : Difficulty.values()) {
			if (curDclty.dcltyValue == inDcltyValue) {
				outDclty = curDclty;
			}
		}
		return outDclty;
	}

	public static Difficulty getDifficultyByIdx(final int indexValue) {
		return getDifficultyByVal((int) Math.round(Math.pow(2, indexValue)) * 10);
	}

	public static PlayImages getPlayImage(final int inFieldId) {
		PlayImages outPlayObject = null;

		for (final PlayImages curPlayObject : PlayImages.values()) {
			if (curPlayObject.id == inFieldId) {
				outPlayObject = curPlayObject;
			}
		}
		return outPlayObject;
	}

	public static Point getCenterPixPos(final Point inPos) {
		final Point outPoint = new Point();
		outPoint.x = pltfrmStartPOS.x + (inPos.x * playImageScaledLEN.x) + (playImageScaledLEN.x / 2);
		outPoint.y = pltfrmStartPOS.y + (inPos.y * playImageScaledLEN.y) + (playImageScaledLEN.y / 2);
		return outPoint;
	}

	public static PlayImages turnTank(final Direction inDirection) {
		PlayImages outTank = PlayImages.Hero_U;
		switch (inDirection) {
		case Up:
			outTank = PlayImages.Hero_U;
			break;
		case Right:
			outTank = PlayImages.Hero_R;
			break;
		case Down:
			outTank = PlayImages.Hero_D;
			break;
		case Left:
			outTank = PlayImages.Hero_L;
			break;
		}
		return outTank;
	}

	public static PlayImages destroyTank(final PlayImages curTank) {
		PlayImages outTank = PlayImages.Villain_U;
		switch (getDirection(curTank)) {
		case Up:
			outTank = PlayImages.DVillain_U;
			break;
		case Right:
			outTank = PlayImages.DVillain_R;
			break;
		case Down:
			outTank = PlayImages.DVillain_D;
			break;
		case Left:
			outTank = PlayImages.DVillain_L;
			break;
		}
		return outTank;
	}

	public static Direction getDirection(final PlayImages curObj) {
		Direction outDirection = Direction.Up;
		final String objName = curObj.name();
		if (objName.contains("_U")) {
			outDirection = Direction.Up;
		}
		if (objName.contains("_R")) {
			outDirection = Direction.Right;
		}
		if (objName.contains("_D")) {
			outDirection = Direction.Down;
		}
		if (objName.contains("_L")) {
			outDirection = Direction.Left;
		}
		return outDirection;
	}
	
	public static boolean isOpposite(Direction drc1, Direction drc2){
		return Math.abs(drc1.ordinal()-drc2.ordinal())==2;
	}

	public static Point getNextPosition(final Point curPos, final Direction moveDirection) {
		final Point nxtPos = new Point();

		switch (moveDirection) {
		case Up:
			nxtPos.x = curPos.x;
			nxtPos.y = curPos.y - 1;
			break;
		case Right:
			nxtPos.x = curPos.x + 1;
			nxtPos.y = curPos.y;
			break;
		case Down:
			nxtPos.x = curPos.x;
			nxtPos.y = curPos.y + 1;
			break;
		case Left:
			nxtPos.x = curPos.x - 1;
			nxtPos.y = curPos.y;
			break;
		}
		return nxtPos;
	}

	public static boolean positionWithinBounds(final Point curPoint) {
		return (curPoint.x >= 0) && (curPoint.x < lvlFieldLEN.x) && (curPoint.y >= 0) && (curPoint.y < lvlFieldLEN.y);
	}

	public static boolean inBoundsRect(final Point event, final int x, final int y, final int width, final int height) {
		return (event.x > x) && (event.x < ((x + width) - 1)) && (event.y > y) && (event.y < ((y + height) - 1));
	}

	public static boolean inBoundsRect(final Point event, final Point posOnScreen, final Point imgLen) {
		return (event.x > posOnScreen.x) && (event.x < ((posOnScreen.x + imgLen.x) - 1)) && (event.y > posOnScreen.y) && (event.y < ((posOnScreen.y + imgLen.y) - 1));
	}

	public static boolean inBoundsPolygon(final Point p, final Point[] polygon) {
		double minX = polygon[0].x;
		double maxX = polygon[0].x;
		double minY = polygon[0].y;
		double maxY = polygon[0].y;
		for (int i = 1; i < polygon.length; i++) {
			final Point q = polygon[i];
			minX = Math.min(q.x, minX);
			maxX = Math.max(q.x, maxX);
			minY = Math.min(q.y, minY);
			maxY = Math.max(q.y, maxY);
		}

		if ((p.x < minX) || (p.x > maxX) || (p.y < minY) || (p.y > maxY)) {
			return false;
		}

		boolean inside = false;
		for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
			if (((polygon[i].y > p.y) != (polygon[j].y > p.y)) && (p.x < ((((polygon[j].x - polygon[i].x) * (p.y - polygon[i].y)) / (polygon[j].y - polygon[i].y)) + polygon[i].x))) {
				inside = !inside;
			}
		}
		return inside;
	}

	public static boolean inBoundsRect(final Point event, final int topLeftX, final int topLeftY, final int topRightX, final int topRightY, final int bottomLeftX, final int bottomLeftY,
			final int bottomRightX, final int bottomRightY) {
		final Point polygon[] = new Point[4];
		polygon[0] = new Point(topLeftX, topLeftY);
		polygon[1] = new Point(topRightX, topRightY);
		polygon[2] = new Point(bottomLeftX, bottomLeftY);
		polygon[3] = new Point(bottomRightX, bottomRightY);
		return inBoundsPolygon(new Point(event.x, event.y), polygon);
	}

	@SuppressWarnings("rawtypes")
	public static Object resizeArray(final Object oldArray, final int newSize) {
		final int oldSize = java.lang.reflect.Array.getLength(oldArray);
		final Class elementType = oldArray.getClass().getComponentType();
		final Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
		final int preserveLength = Math.min(oldSize, newSize);

		if (preserveLength > 0) {
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		}

		return newArray;
	}
}
