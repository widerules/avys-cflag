package com.avy.cflag.game.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link NavigationGraph} which is represented as a grid or a table. The nodes are accessible through (x, y) coordinates.
 * 
 * @author Xavier Guzman
 * 
 * @param <T>
 *            only classes extending {@link GridCell} can be used within this graph
 */
public class NavigationGrid {
	protected int width;
	protected int height;
	private final List<GridCell> neighbors = new ArrayList<GridCell>();

	/** The nodes contained in the grid. They are stored as Grid[x][y] */
	protected GridCell[][] nodes;

	/**
	 * Creates an grid with no nodes. If this constructor is used, make sure to call {@link NavigationGrid#setNodes(NavigationGridGraphNode[][])} before trying to make use of the grid cells.
	 */
	public NavigationGrid() {
		this(null);
	}

	public NavigationGrid(final GridCell[][] nodes) {
		if (nodes != null) {
			width = nodes.length;
			height = nodes[0].length;
		}
		this.nodes = nodes;
	}

	public GridCell getCell(final int x, final int y) {
		return contains(x, y) ? nodes[x][y] : null;
	};

	public void setCell(final int x, final int y, final GridCell cell) {
		if (contains(x, y)) {
			nodes[x][y] = cell;
		}
	}

	/**
	 * Determine whether the node at the given position is walkable.
	 * 
	 * @param x
	 *            - The x / column coordinate of the node.
	 * @param y
	 *            - The y / row coordinate of the node.
	 * @return true if the node at [x,y] is walkable, false if it is not walkable (or if [x,y] is not within the grid's limit)
	 */
	public boolean isWalkable(final int x, final int y) {
		return contains(x, y) && nodes[x][y].isWalkable();
	};

	/**
	 * Determine wether the given x,y pair is within the bounds of this grid
	 * 
	 * @param x
	 *            - The x / column coordinate of the node.
	 * @param y
	 *            - The y / row coordinate of the node.
	 * @return true if the (x,y) is within the boundaries of this grid
	 */
	public boolean contains(final int x, final int y) {
		return ((x >= 0) && (x < width)) && ((y >= 0) && (y < height));
	};

	/**
	 * Set whether the node on the given position is walkable.
	 * 
	 * @param x
	 *            - The x / column coordinate of the node.
	 * @param y
	 *            - The y / row coordinate of the node.
	 * @param walkable
	 *            - Whether the position is walkable.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the coordinate is not inside the grid.
	 */
	public void setWalkable(final int x, final int y, final boolean walkable) {
		nodes[x][y].setWalkable(walkable);
	};

	public List<GridCell> getNeighbors(final GridCell cell) {
		return null;
	}

	/**
	 * Get the neighbors of the given node.
	 * 
	 * <pre>
	 *     offsets      diagonalOffsets:
	 *  +---+---+---+    +---+---+---+
	 *  |   | 0 |   |    | 4 |   | 5 |
	 *  +---+---+---+    +---+---+---+
	 *  | 3 |   | 1 |    |   |   |   |
	 *  +---+---+---+    +---+---+---+
	 *  |   | 2 |   |    | 6 |   | 7 |
	 *  +---+---+---+    +---+---+---+
	 * </pre>
	 * 
	 * @param node
	 * @param opt
	 */
	public List<GridCell> getNeighbors(final GridCell node, final GridFinderOptions opt) {
		final GridFinderOptions options = opt;
		final boolean allowDiagonal = options.allowDiagonal;
		final boolean dontCrossCorners = options.dontCrossCorners;
		final int yDir = options.isYDown ? -1 : 1;
		final int x = node.getX(), y = node.getY();
		neighbors.clear();
		boolean s0 = false, d0 = false, s1 = false, d1 = false, s2 = false, d2 = false, s3 = false, d3 = false;

		// up
		if (isWalkable(x, y + yDir)) {
			neighbors.add(nodes[x][y + yDir]);
			s0 = true;
		}
		// right
		if (isWalkable(x + 1, y)) {
			neighbors.add(nodes[x + 1][y]);
			s1 = true;
		}
		// down
		if (isWalkable(x, y - yDir)) {
			neighbors.add(nodes[x][y - yDir]);
			s2 = true;
		}
		// left
		if (isWalkable(x - 1, y)) {
			neighbors.add(nodes[x - 1][y]);
			s3 = true;
		}

		if (!allowDiagonal) {
			return neighbors;
		}

		if (dontCrossCorners) {
			d0 = s3 && s0;
			d1 = s0 && s1;
			d2 = s1 && s2;
			d3 = s2 && s3;
		} else {
			d0 = s3 || s0;
			d1 = s0 || s1;
			d2 = s1 || s2;
			d3 = s2 || s3;
		}

		// up left
		if (d0 && this.isWalkable(x - 1, y + yDir)) {
			neighbors.add(nodes[x - 1][y + yDir]);
		}
		// up right
		if (d1 && this.isWalkable(x + 1, y + yDir)) {
			neighbors.add(nodes[x + 1][y + yDir]);
		}
		// down right
		if (d2 && this.isWalkable(x + 1, y - yDir)) {
			neighbors.add(nodes[x + 1][y - yDir]);
		}
		// down left
		if (d3 && this.isWalkable(x - 1, y - yDir)) {
			neighbors.add(nodes[x - 1][y - yDir]);
		}

		return neighbors;
	}

	public float getMovementCost(final GridCell node1, final GridCell node2, final GridFinderOptions opt) {

		if (node1 == node2) {
			return 0;
		}

		final GridFinderOptions options = opt;
		final GridCell cell1 = node1, cell2 = node2;
		return (cell1.x == cell2.x) || (cell1.y == cell2.y) ? options.orthogonalMovementCost : options.diagonalMovementCost;
	}

	public boolean isWalkable(final GridCell node) {
		final GridCell c = node;
		return isWalkable(c.x, c.y);
	};

	public GridCell[][] getNodes() {
		return nodes;
	}

	public void setNodes(final GridCell[][] nodes) {
		this.nodes = nodes;
		width = nodes.length;
		height = nodes[0].length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public boolean lineOfSight(final GridCell from, final GridCell to) {
		if ((from == null) || (to == null)) {
			return false;
		}

		final GridCell node = from, neigh = to;
		int x1 = node.getX(), y1 = node.getY();
		final int x2 = neigh.getX(), y2 = neigh.getY();
		final int dx = Math.abs(x1 - x2);
		final int dy = Math.abs(y1 - y2);
		final int xinc = (x1 < x2) ? 1 : -1;
		final int yinc = (y1 < y2) ? 1 : -1;

		int error = dx - dy;

		for (int n = dx + dy; n > 0; n--) {
			if (!isWalkable(x1, y1)) {
				return false;
			}
			final int e2 = 2 * error;
			if (e2 > -dy) {
				error -= dy;
				x1 += xinc;
			}
			if (e2 < dx) {
				error += dx;
				y1 += yinc;
			}
		}

		return true;

	}
}
