package com.avy.cflag.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;

public class PathFinder {
	
	private ArrayList<Direction> bestFullPath = new ArrayList<Direction>();
	private ArrayList<Direction> bestHalfPath = new ArrayList<Direction>();
	private ArrayList<Direction> curPath= new ArrayList<Direction>();;

	private int[][] visitedMatrix;
	private PlayImages[][] inputMatrix;

	private Point storedDst;
	
	public void findShortestPath(Point src){
		
		if(src.equals(storedDst)) { 
			printPath(curPath);
			if(curPath.size()>0 && (bestFullPath.size()==0 || curPath.size()<bestFullPath.size())) {
				bestFullPath = cloneList(curPath);
			}
			return;
		}
		setVisited(src, curPath.size());
		
		boolean blocked=true;
		for (Direction curDrc : Direction.values()) {
			Point nxtNode = new Point(0, 0);
			switch (curDrc) {
				case Up:
					nxtNode = new Point(src.x,src.y-1);
					break;
				case Right:
					nxtNode = new Point(src.x+1,src.y);
					break;
				case Down:
					nxtNode = new Point(src.x,src.y+1);
					break;
				case Left:
					nxtNode = new Point(src.x-1,src.y);
					break;
				default:
					break;
			}
			
			if(isPathExists(nxtNode) && isValidPos(nxtNode, curPath.size()+1)) {
				blocked=false;
				curPath.add(curDrc);
				findShortestPath(nxtNode);
				curPath.remove(curPath.size()-1);
			}
		}
		if(blocked) {
			printPath(curPath);
			if(curPath.size()>0 && (bestHalfPath.size()==0 || curPath.size()>bestHalfPath.size())) {
				bestHalfPath = cloneList(curPath);
			}
		}
		return;
	}
	
	public ArrayList<Direction>  cloneList(ArrayList<Direction> input) {
		ArrayList<Direction> output = new ArrayList<Direction>();
		for (Iterator<Direction> iterator = input.iterator(); iterator.hasNext();) {
			Direction direction = (Direction) iterator.next();
			output.add(direction);
		}
		return output;
	}
	
	public boolean isValidPos(Point input, int val) {
		if(val<=visitedMatrix[input.y][input.x]){
			return true;
		} else {
			return false;
		}
	}
	
	public void setVisited(Point input, int val) {
		visitedMatrix[input.y][input.x]=val;
	}
	
	public boolean isPathExists(Point curPos) {
		if((curPos.x>=0 && curPos.x < inputMatrix.length) && (curPos.y>=0 && curPos.y < inputMatrix.length)) {
			System.out.println(inputMatrix[curPos.y][curPos.x].name());
			if(inputMatrix[curPos.x][curPos.y]==PlayImages.Grass) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void fillVisitedMatrix() {
		for (int i = 0; i < visitedMatrix.length; i++) {
			for (int j = 0; j < visitedMatrix[i].length; j++) {
				visitedMatrix[i][j]=Integer.MAX_VALUE;
			}
		}
	}
	
	public void printPath(ArrayList<Direction> path) {
		for (Direction drc : path) {
			System.out.print(drc.name() + "->");
		}
		System.out.print("\n");
	}
	
	public ArrayList<Direction> extendedPath(ArrayList<Direction> input, Direction strtDirection) {
		ArrayList<Direction> output = new ArrayList<Direction>();
		Direction prevDirection = strtDirection;
		for (Direction curDirection : input) {
			output.add(curDirection);
			if(curDirection!=prevDirection) {
				output.add(curDirection);
			} 
			prevDirection = curDirection;
		}
		return output;
	}
	
	public ArrayList<Direction> findPath(PlayImages[][] lvlPlayField, Direction strtDirection, Point startPos, Point dstPoint) {
		this.storedDst = dstPoint;
		this.bestFullPath = new ArrayList<Direction>();
		this.bestHalfPath = new ArrayList<Direction>();
		this.curPath =  new ArrayList<Direction>();
		
		this.visitedMatrix = new int[lvlPlayField.length][lvlPlayField.length];
		this.inputMatrix = lvlPlayField;
		PlayUtils.printMatrix(inputMatrix);
		this.fillVisitedMatrix();
		this.findShortestPath(startPos);
		
		System.out.println("Final Path:");
//		if(bestFullPath.size()>0) {
//			printPath(bestFullPath);
			return extendedPath(bestFullPath,strtDirection);
//		} else {
//			printPath(bestHalfPath);
//			return extendedPath(bestHalfPath,strtDirection);
//		}
	}
}