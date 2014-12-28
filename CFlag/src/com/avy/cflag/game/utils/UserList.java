package com.avy.cflag.game.utils;

import java.util.ArrayList;

public class UserList {
	private int currentUser;
	private final ArrayList<UserOptions> userOptionsList;

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

	public UserOptions getCurrentUserOptions() {
		if ((currentUser > -1) && (currentUser < getUserCount())) {
			return userOptionsList.get(currentUser);
		} else {
			return new UserOptions();
		}
	}

	public UserOptions getUserOptionsByIdx(final int idx) {
		if ((idx >= 0) && (idx < getUserCount())) {
			return userOptionsList.get(idx);
		} else {
			return new UserOptions();
		}
	}

	public boolean isUserExists(final String userName) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(userName)) {
				return true;
			}
		}
		return false;
	}

	public int getUserIndex(final String userName) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(userName)) {
				return i;
			}
		}
		return -1;
	}

	public UserOptions addUser(final String userName) {
		final UserOptions userOpt = new UserOptions(userName);
		if (!isUserExists(userName)) {
			addUser(userOpt);
			return userOpt;
		} else {
			return getUserOptionsByIdx(currentUser);
		}
	}

	public void addUser(final UserOptions inGameOpts) {
		userOptionsList.add(inGameOpts);
		currentUser = userOptionsList.size() - 1;
	}

	public void deleteUser(final int idx) {
		if ((getUserCount() > 0) && (idx >= 0)) {
			userOptionsList.remove(idx);
		}
		if (getUserCount() <= 0) {
			currentUser = -1;
		} else {
			currentUser = (currentUser + 1 + getUserCount()) % getUserCount();
		}
	}

	public void updateUser(final UserOptions inGameOpts) {
		for (int i = 0; i < userOptionsList.size(); i++) {
			if (userOptionsList.get(i).getUserName().equalsIgnoreCase(inGameOpts.getUserName())) {
				userOptionsList.set(i, inGameOpts.clone());
				currentUser = i;
				return;
			}
		}
	}
}
