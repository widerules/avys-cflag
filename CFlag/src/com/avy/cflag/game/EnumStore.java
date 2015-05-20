package com.avy.cflag.game;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL;

import java.util.Arrays;
import java.util.List;

import com.avy.cflag.base.AnimActor;
import com.avy.cflag.base.AnimDrawable;
import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.ImageRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

public class EnumStore {

	public static enum Difficulty {
		Novice(10), Easy(20), Medium(40), Hard(80), Deadly(160);

		public final int dcltyValue;

		Difficulty(final int inDcltyValue) {
			dcltyValue = inDcltyValue;
		}

		public static int length() {
			return Difficulty.values().length;
		}
	};

	public static enum PlayImages {
		Grass(0, 1f, NORMAL), Hero_U(1, 0.2f, LOOP), Flag(2, 0.2f, LOOP_PINGPONG), Water(3, 0.2f, LOOP_PINGPONG), Steel(4, 1f, NORMAL), MBlock(5, 1f, NORMAL), Brick(6, 0.2f, NORMAL), Villain_U(7,
				0.21f, LOOP), Villain_R(8, 0.22f, LOOP), Villain_D(9, 0.23f, LOOP), Villain_L(10, 0.24f, LOOP), MMirror_U(11, 0.25f, LOOP), MMirror_R(12, 0.26f, LOOP), MMirror_D(13, 0.27f, LOOP), MMirror_L(
				14, 0.28f, LOOP), Stream_U(15, 0.1f, LOOP), Stream_R(16, 0.1f, LOOP), Stream_D(17, 0.1f, LOOP), Stream_L(18, 0.1f, LOOP), Rubber(19, 1f, NORMAL), RMirror_U(20, 0.16f, LOOP), RMirror_R(
				21, 0.17f, LOOP), RMirror_D(22, 0.18f, LOOP), RMirror_L(23, 0.19f, LOOP), Ice(24, 1f, NORMAL), ThinIce(25, 1f, NORMAL), Bridge(26, 0.2f, LOOP_PINGPONG), Tunnel_0(64, 0.21f, LOOP), Tunnel_1(
				66, 0.22f, LOOP), Tunnel_2(68, 0.23f, LOOP), Tunnel_3(70, 0.24f, LOOP), Tunnel_4(72, 0.25f, LOOP), Tunnel_5(74, 0.26f, LOOP), Tunnel_6(76, 0.27f, LOOP), Tunnel_7(78, 0.28f, LOOP), Hero_R(
				82, 0.2f, LOOP), Hero_D(83, 0.2f, LOOP), Hero_L(84, 0.2f, LOOP), DVillain_U(85, 1f, NORMAL), DVillain_R(86, 1f, NORMAL), DVillain_D(87, 1f, NORMAL), DVillain_L(88, 1f, NORMAL), OutOfBounds(
				100, 1f, NORMAL);

		public final int id;
		public final float animSpeed;
		public final PlayMode animType;
		public Array<ImageRegion> texRegions;

		PlayImages(final int inObjId, final float animSpeed, final PlayMode animType) {
			id = inObjId;
			this.animSpeed = animSpeed;
			this.animType = animType;
			texRegions = null;
		}

		public static void load(final Graphics g) {
			for (final PlayImages playObject : PlayImages.values()) {
				playObject.texRegions = g.getFlipYTexRegions(playObject.name().toLowerCase());
			}
		}

		public AnimActor getAnimActor() {
			final AnimActor temp = new AnimActor(new AnimDrawable(new Animation(animSpeed, texRegions, animType)));
			temp.setName(name());
			return temp;
		}
	}

	public static enum Direction {
		Up, Right, Down, Left
	};

	public static enum GameState {
		FadeIn, FadeOut, Ready, Running, Paused, Dead, Drowned, AwardStart, AwardEnd, Won, Menu, Hint, Restart, NextLevel, Quit
	}

	public static enum BulletState {
		Fired, InMotion, MoveTank, HitTank, HitHero, HitBrick, HitDTank, PassCrystal, MoveBlock, HitRMirror, EnterRMirror, ExitRMirror, MoveMirror, EnterMMirror, ExitMMirror, HitSteel, BlockExitsTunnel, Exploded, Drowned
	}

	public static enum ExplodeState {
		Off, BreakOn, Break, BlastOn, Blast, BlastMoveOn, BlastDown, BlastUp, BlastLeft, BlastRight, DrownOn, Drown, DrownDieOn, DrownDie, DieOn, Die, BurnOn, Burn 
	}

	public static enum TankState {
		OnStream, OnIce, ObjOnIce, OnWater, OnThinIce, OnTunnel, Firing, Moving, ShotDead, CurATankFired, PrevATankFired, Blocked, BothATankFired, ReachedFlag
	}

	public static enum AntiTankState {
		Fired, Firing, Exploded, HitTank, ObjOnIce
	}

	public static enum FirePathState {
		ATankFound, Clear, Blocked
	}

	public static List<PlayImages> FixedObjects = Arrays.asList(PlayImages.Steel, PlayImages.MBlock, PlayImages.Brick, PlayImages.DVillain_U, PlayImages.DVillain_R, PlayImages.DVillain_D,
			PlayImages.DVillain_L, PlayImages.Villain_U, PlayImages.Villain_R, PlayImages.Villain_D, PlayImages.Villain_L, PlayImages.MMirror_U, PlayImages.MMirror_R, PlayImages.MMirror_D,
			PlayImages.MMirror_L, PlayImages.Rubber, PlayImages.RMirror_U, PlayImages.RMirror_R, PlayImages.RMirror_D, PlayImages.RMirror_L);

	public static List<PlayImages> StreamObjects = Arrays.asList(PlayImages.Stream_U, PlayImages.Stream_R, PlayImages.Stream_D, PlayImages.Stream_L);
	public static List<PlayImages> TunnelObjects = Arrays.asList(PlayImages.Tunnel_0, PlayImages.Tunnel_1, PlayImages.Tunnel_2, PlayImages.Tunnel_3, PlayImages.Tunnel_4, PlayImages.Tunnel_5,
			PlayImages.Tunnel_6, PlayImages.Tunnel_7);
	
	public static enum Medals {
		None, Bronze, Silver, Gold
	}
}
