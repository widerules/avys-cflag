package com.avy.cflag.game.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A generic implementation of A* that works on any {@link NavigationGraph} instance.
 * 
 * @author Xavier Guzman
 * 
 * @param <GridCell>
 *            a class implementing {@link NavigationNode}
 */
public class AStarFinder {

	private GridFinderOptions defaultOptions;
	BHeap<GridCell> openList;
	public int jobId;

	public AStarFinder(final GridFinderOptions opt) {
		defaultOptions = opt;
		openList = new BHeap<GridCell>(new Comparator<GridCell>() {
			@Override
			public int compare(final GridCell o1, final GridCell o2) {
				if ((o1 == null) || (o2 == null)) {
					if (o1 == o2) {
						return 0;
					}
					if (o1 == null) {
						return -1;
					} else {
						return 1;
					}

				}
				return (int) (o1.getF() - o2.getF());
			}
		});
	}

	public boolean findPath(final GridCell startNode, final GridCell endNode, final NavigationGrid graph) {
		if (jobId == Integer.MAX_VALUE) {
			jobId = 0;
		}
		final int job = ++jobId;

		GridCell node, neighbor;
		final List<GridCell> neighbors = new ArrayList<GridCell>();
		float ng;

		startNode.setG(0);
		startNode.setF(0);

		// push the start node into the open list
		openList.clear();
		openList.add(startNode);
		startNode.setParent(null);
		startNode.setOpenedOnJob(job);

		while (openList.size > 0) {

			// pop the position of node which has the minimum 'f' value.
			node = openList.pop();
			node.setClosedOnJob(job);

			// if reached the end position, construct the path and return it
			if (node == endNode) {
				return true;
			}

			// get neighbors of the current node
			neighbors.clear();
			neighbors.addAll(graph.getNeighbors(node, defaultOptions));
			for (int i = 0, l = neighbors.size(); i < l; ++i) {
				neighbor = neighbors.get(i);

				if ((neighbor.getClosedOnJob() == job) || !graph.isWalkable(neighbor)) {
					continue;
				}

				// get the distance between current node and the neighbor and calculate the next g score
				ng = node.getG() + graph.getMovementCost(node, neighbor, defaultOptions);

				// check if the neighbor has not been inspected yet, or can be reached with smaller cost from the current node
				if ((neighbor.getOpenedOnJob() != job) || (ng < neighbor.getG())) {
					final float prevf = neighbor.getF();
					neighbor.setG(ng);

					neighbor.setH(calculateManhattanDistance(neighbor, endNode));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setParent(node);

					if (neighbor.getOpenedOnJob() != job) {
						openList.add(neighbor);
						neighbor.setOpenedOnJob(job);
					} else {
						// the neighbor can be reached with smaller cost.
						// Since its f value has been updated, we have to update its position in the open list
						openList.updateNode(neighbor, neighbor.getF() - prevf);
					}
				}
			}
		}

		// fail to find the path
		return false;
	}

	public float calculateManhattanDistance(final GridCell from, final GridCell to) {
		final GridCell c1 = from, c2 = to;
		return Math.abs(c2.x - c1.x) + Math.abs(c2.y - c1.y);
	}

}
