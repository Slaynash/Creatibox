package fr.slaynash.creatibox.server;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import fr.slaynash.creatibox.common.entities.EntityManager;
import slaynash.sgengine.world3d.CollisionManager3d;

public class WorldManager extends Thread{
	
	private static final int TPS = 30;
	
	private static WorldManager instance = null;
	private static boolean running = false;
	
	public static void init() {
		instance = new WorldManager();
		instance.setName("SERVER_WORLDMANAGER");
		instance.setDaemon(true);
		CollisionManager3d.reload();
		
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1);
		Transform transform = new Transform();
		transform.origin.set(new Vector3f(new float[] {0,-1,0}));
		transform.setRotation(new Quat4f(0, 0, 0, 1));
		DefaultMotionState groundMotionState = new DefaultMotionState(transform);
		
		RigidBodyConstructionInfo constructionInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new javax.vecmath.Vector3f(0,0,0));
		
		RigidBody ground = new RigidBody(constructionInfo);
		CollisionManager3d.getDynamicWorld().addRigidBody(ground);
	}
	
	public void run() {
		running = true;
		CollisionManager3d.start();
		while(running) {
			long startTime = System.nanoTime()/1000000;
			try {
				CollisionManager3d.update();
				EntityManager.updateEntities();
			}
			catch(Exception e) {
				System.err.println("[WorldManager] An error occured while ticking world.");
				e.printStackTrace();
			}
			long delta = (1000/TPS)-((System.nanoTime()/1000000)-startTime);
			//System.out.println(delta);
			try { if(delta>0) Thread.sleep(delta); }
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public static void stopWorld() {
		running = false;
		CollisionManager3d.destroyDynamicWorld();
	}

	public static void startWorld() {
		if(!running) {
			instance.start();
		}
	}
	
}
