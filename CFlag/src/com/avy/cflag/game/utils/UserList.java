package com.avy.cflag.game.utils;

import java.util.ArrayList;

public class UserList {
	private int currentUser;
	private ArrayList<UserOptions> userOptionsList;

	public UserList() {
		currentUser = -1;
		userOptionsList = new ArrayList<UserOptions>();
	}

	public int getUserCount() {
		return userOptionsList.size();
	}

	public int getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(int currentUser) {
		this.currentUser = currentUser;
	}

	public ArrayList<UserOptions> getUserOptionsList() {
		return userOptionsList;
	}

	public void setUserOptionsList(ArrayList<UserOptions> userOptionsList) {
		this.userOptionsList = userOptionsList;
	}

	public UserOptions getCurrentUserOptions() {
		return userOptionsList.get(currentUser);
	}

	public void setCurrentUserOptions(UserOptions userOptions) {
		userOptionsList.set(currentUser, userOptions);
	}

	public UserOptions getUserOptionsByIdx(int idx) {
		return userOptionsList.get(idx);
	}

	public boolean isUserExists(String userName) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(userName)) {
				return true;
			}
		}
		return false;
	}

	public int getUserIndex(String userName) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(userName)) {
				return i;
			}
		}
		return -1;
	}

	public UserOptions getUserOptions(int index) {
		return userOptionsList.get(index);
	}

	public UserOptions addUser(String userName) {
		UserOptions userOpt = new UserOptions(userName);
		if (!isUserExists(userName)) {
			addUser(userOpt);
			return userOpt;
		} else {
			return getUserOptionsByIdx(currentUser);
		}
	}

	public void addUser(UserOptions inGameOpts) {
		userOptionsList.add(inGameOpts);
		currentUser = userOptionsList.size() - 1;
	}

	public void deleteUser(UserOptions inGameOpts) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(inGameOpts.getUserName())) {
				userOptionsList.remove(i);
				if (getUserCount() <= 0)
					currentUser = -1;
				else
					currentUser = (currentUser + 1 + getUserCount()) % getUserCount();
				return;
			}
		}
	}

	public void deleteUser(int idx) {
		if (getUserCount() > 0 && idx >= 0)
			userOptionsList.remove(idx);
		if (getUserCount() <= 0)
			currentUser = -1;
		else
			currentUser = (currentUser + 1 + getUserCount()) % getUserCount();
	}

	public void updateUser(UserOptions inGameOpts) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(inGameOpts.getUserName())) {
				userOptionsList.set(i, inGameOpts.clone());
				currentUser = i;
				return;
			}
		}
	}
}
