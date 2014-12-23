package com.avy.cflag.game;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL;

import java.util.Arrays;
import java.util.List;

import com.avy.cflag.base.AcraMap;
import com.avy.cflag.base.AnimActor;
import com.avy.cflag.base.AnimDrawable;
import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.ImageRegion;
import com.avy.cflag.base.Point;
import com.avy.cflag.game.utils.UserList;
import com.avy.cflag.game.utils.UserOptions;
import com.avy.cflag.game.utils.UserScore;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

public class MemStore {

	public static AcraMap acraMap;
	public static final Point lvlFieldLEN = new Point(16, 16);
	public static final int lvlFieldDataLEN = lvlFieldLEN.x * lvlFieldLEN.y;
	public static final int lvlNameLEN = 31;
	public static final int lvlHintLEN = 256;
	public static final int lvlAuthorLEN = 31;
	public static final int lvlDcltyLEN = 2;
	public static final int lvlLEN = lvlFieldDataLEN + lvlNameLEN + lvlHintLEN + lvlAuthorLEN + lvlDcltyLEN;

	public static enum Difficulty {
		Novice(10), Easy(20), Medium(40), Hard(80), Deadly(160);

		public final int dcltyValue;

		Difficulty(int inDcltyValue) {
			this.dcltyValue = inDcltyValue;
		}

		public static int length() {
			return Difficulty.values().length;
		}
	};

	public static byte[][] lvlDataPerDCLTY = new byte[Difficulty.length()][];
	public static int lvlCntPerDCLTY[] = new int[Difficulty.length()];

	public static UserList userLIST = null;
	public static UserScore curUserSCORE = null;
	public static UserOptions curUserOPTS = null;

	public static Point pltfrmStartPOS = null;
	public static Point pltfrmLEN = null;
	public static final Point playImageOrigLEN = new Point(30, 30);
	public static Point playImageScaledLEN = null;

	public static enum PlayImages {
		Grass(0, 1f, NORMAL), Hero_U(1, 0.2f, LOOP), Flag(2, 0.2f, LOOP_PINGPONG), Water(3, 0.2f, LOOP_PINGPONG), Steel(4, 1f, NORMAL), MBlock(5, 1f, NORMAL), Brick(6, 0.2f, NORMAL), Villain_U(7, 0.21f, LOOP), Villain_R(8, 0.22f, LOOP), Villain_D(9, 0.23f,
				LOOP), Villain_L(10, 0.24f, LOOP), MMirror_U(11, 0.25f, LOOP), MMirror_R(12, 0.26f, LOOP), MMirror_D(13, 0.27f, LOOP), MMirror_L(14, 0.28f, LOOP), Stream_U(15, 0.1f, LOOP), Stream_R(16, 0.1f, LOOP), Stream_D(17, 0.1f, LOOP), Stream_L(18, 0.1f,
				LOOP), Rubber(19, 1f, NORMAL), RMirror_U(20, 0.16f, LOOP), RMirror_R(21, 0.17f, LOOP), RMirror_D(22, 0.18f, LOOP), RMirror_L(23, 0.19f, LOOP), Ice(24, 1f, NORMAL), ThinIce(25, 1f, NORMAL), Bridge(26, 0.2f, LOOP_PINGPONG), Tunnel_0(64, 0.21f,
				LOOP), Tunnel_1(66, 0.22f, LOOP), Tunnel_2(68, 0.23f, LOOP), Tunnel_3(70, 0.24f, LOOP), Tunnel_4(72, 0.25f, LOOP), Tunnel_5(74, 0.26f, LOOP), Tunnel_6(76, 0.27f, LOOP), Tunnel_7(78, 0.28f, LOOP), Hero_R(82, 0.2f, LOOP), Hero_D(83, 0.2f, LOOP), Hero_L(
				84, 0.2f, LOOP), DVillain_U(85, 1f, NORMAL), DVillain_R(86, 1f, NORMAL), DVillain_D(87, 1f, NORMAL), DVillain_L(88, 1f, NORMAL), OutOfBounds(100, 1f, NORMAL);

		public final int id;
		public final float animSpeed;
		public final PlayMode animType;
		public Array<ImageRegion> texRegions;

		PlayImages(int inObjId, float animSpeed, PlayMode animType) {
			this.id = inObjId;
			this.animSpeed = animSpeed;
			this.animType = animType;
			texRegions = null;
		}

		public static void load(Graphics g) {
			for (PlayImages playObject : PlayImages.values()) {
				playObject.texRegions = g.getFlipTexRegions(playObject.name().toLowerCase());
			}
		}

		public AnimActor getAnimActor() {
			return new AnimActor(new AnimDrawable(new Animation(animSpeed, texRegions, animType)));
		}
	}

	public static enum Direction {
		Up, Right, Down, Left
	};

	public static enum GameState {
		FadeIn, FadeOut, Ready, Running, Paused, Dead, Drowned, Won, Menu, Hint, Restart, NextLevel, Quit
	}

	public static enum BulletState {
		Fired, InMotion, MoveTank, HitTank, HitBrick, HitDTank, PassCrystal, MoveBlock, HitRMirror, EnterRMirror, ExitRMirror, MoveMirror, EnterMMirror, ExitMMirror, HitSteel, BlockExitsTunnel, Exploded
	}

	public static enum TankState {
		OnStream, OnIce, OnWater, OnThinIce, OnTunnel, Firing, Moving, ShotDead, MissFire, CurATankFired, PrevATankFired, Blocked, BothATankFired, ReachedFlag
	}

	public static enum AntiTankState {
		MissFire, Fired, Firing, Exploded, HitTank
	}

	public static enum FirePathState {
		ATankFound, Clear, Blocked
	}

	public static List<PlayImages> fixedObjects = Arrays.asList(PlayImages.Steel, PlayImages.MBlock, PlayImages.Brick, PlayImages.DVillain_U, PlayImages.DVillain_R, PlayImages.DVillain_D, PlayImages.DVillain_L, PlayImages.Villain_U, PlayImages.Villain_R,
			PlayImages.Villain_D, PlayImages.Villain_L, PlayImages.MMirror_U, PlayImages.MMirror_R, PlayImages.MMirror_D, PlayImages.MMirror_L, PlayImages.Rubber, PlayImages.RMirror_U, PlayImages.RMirror_R, PlayImages.RMirror_D, PlayImages.RMirror_L);

	public static List<PlayImages> streamObjects = Arrays.asList(PlayImages.Stream_U, PlayImages.Stream_R, PlayImages.Stream_D, PlayImages.Stream_L);
	public static List<PlayImages> tunnelObjects = Arrays.asList(PlayImages.Tunnel_0, PlayImages.Tunnel_1, PlayImages.Tunnel_2, PlayImages.Tunnel_3, PlayImages.Tunnel_4, PlayImages.Tunnel_5, PlayImages.Tunnel_6, PlayImages.Tunnel_7);

}
