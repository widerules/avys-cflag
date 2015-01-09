package com.avy.cflag.game.pathfinding;

import java.util.ArrayList;
import java.util.List;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.PlayUtils;

public class PathFinder {
	private final GridFinderOptions finderOpts;
	private NavigationGrid navGrid;
	private final AStarFinder finder;
	private GridCell[][] cells;
	private static final List<Direction> path = new ArrayList<Direction>();

	public PathFinder() {
		finderOpts = new GridFinderOptions(false, true, true, 1, 1);
		finder = new AStarFinder(finderOpts);
	}

	public void loadGrid(final PlayImages[][] lvlPlayField) {
		cells = new GridCell[lvlPlayField.length][lvlPlayField[0].length];

		for (int i = 0; i < lvlPlayField[0].length; i++) {
			for (int j = 0; j < lvlPlayField.length; j++) {
				boolean isWalkable = false;
				if ((lvlPlayField[i][j] == PlayImages.Grass) || lvlPlayField[i][j].name().startsWith("Hero")||(lvlPlayField[i][j] == PlayImages.Bridge)||(lvlPlayField[i][j] == PlayImages.Flag)) {
					isWalkable = true;
				}
				final GridCell cell = new GridCell(i, j, isWalkable);
				cells[i][j] = cell;
			}
		}
	}

	public ArrayList<Direction> findPath(final PlayImages[][] lvlPlayField, final Direction strtDirection, final Point strtPos, final Point dstPos) {
		List<Direction> outPut = new ArrayList<Direction>();

		loadGrid(lvlPlayField);
		navGrid = new NavigationGrid(cells);

		final GridCell strtCell = navGrid.getCell(strtPos.x, strtPos.y);
		final GridCell dstCell = navGrid.getCell(dstPos.x, dstPos.y);

		try {
			if (finder.findPath(strtCell, dstCell, navGrid)) {
				outPut = backtrace(strtCell, dstCell, strtDirection);
			}
		} catch (Exception e) {
			MemStore.acraMAP.put("FindPathStrtCell", strtCell.x + " : " + strtCell.y);
			MemStore.acraMAP.put("FindPathEndCell", dstCell.x + " : " + dstCell.y);
			throw new RuntimeException("Error in Path Finder");
		}

		return (ArrayList<Direction>) outPut;
	}

	public static List<Direction> backtrace(final GridCell strtNode, final GridCell endNode, final Direction strtDirection) {
		path.clear();
		GridCell curNode = null;
		GridCell prevNode = endNode;
		Direction curDrc, prevDrc = null;

		while ((prevNode.getParent() != null) && (prevNode != prevNode.getParent())) {
			curNode = prevNode.getParent();
			curDrc = PlayUtils.getDirectionFromPoints(new Point(curNode.x, curNode.y), new Point(prevNode.x, prevNode.y));
			if ((prevDrc != null) && (curDrc != prevDrc)) {
				path.add(0, prevDrc);
			}
			path.add(0, curDrc);
			prevNode = curNode;
			prevDrc = curDrc;
		}
		if ((prevDrc != null) && (strtDirection != prevDrc)) {
			path.add(0, prevDrc);
		}
		return path;
	}

}
