package fr.slaynash.creatibox.common.entities;

import javax.vecmath.Matrix4f;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.EntityMatrixUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.server.Client;
import fr.slaynash.creatibox.server.Server;
import slaynash.sgengine.models.Renderable3dModel;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.world3d.CollisionManager3d;

public class EntityProp extends Entity {
	
	private Renderable3dModel model;
	
	private MotionState motionState;
	private RigidBody body;

	public EntityProp(int id, float posX, float posY, float posZ, float angX, float angY, float angZ, EntityPropDef def) {
		super(id, posX, posY, posZ, angX, angY, angZ, def);
		
		model = def.getModel();
	}
	
	public void initCollisionModel() {
		CollisionShape shape = null;
		if(((EntityPropDef)getDef()).getCollisionType() == EntityCollisionShape.BOX) {
			shape = new BoxShape(new javax.vecmath.Vector3f(((EntityPropDef)getDef()).getBounds()[0]*.5f , ((EntityPropDef)getDef()).getBounds()[1]*.5f, ((EntityPropDef)getDef()).getBounds()[2]*.5f));
		}
		else if(((EntityPropDef)getDef()).getCollisionType() == EntityCollisionShape.CYLINDER) {
			shape = new CylinderShape(new javax.vecmath.Vector3f(((EntityPropDef)getDef()).getBounds()[0]*.5f , ((EntityPropDef)getDef()).getBounds()[1]*.5f, ((EntityPropDef)getDef()).getBounds()[0]*.5f));
		}
		else {
			return;
		}
        Transform cubeTransform = new Transform();
        cubeTransform.origin.set(getPosX(), getPosY(), getPosZ());
        motionState = new DefaultMotionState(cubeTransform);
        float mass = 1;
        javax.vecmath.Vector3f fallInertia = new javax.vecmath.Vector3f(0, 0, 0);
        shape.calculateLocalInertia(mass, fallInertia);
        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(mass, motionState, shape, fallInertia);
        body = new RigidBody(fallRigidBodyCI);
        body.setFriction(1);
        CollisionManager3d.getDynamicWorld().addRigidBody(body);
	}

	@Override
	public void update() {
		if(body != null) {
			Transform transform = body.getWorldTransform(new Transform());
			setPosition(transform.origin.x, transform.origin.y, transform.origin.z);
			Matrix4f rotMat = transform.getMatrix(new Matrix4f());
			//rotMat.invert();
			Vector3f rot = EntityMatrixUtils.getRotation(rotMat);
			setRotation(rot.x, rot.y, rot.z);
		}
		
		
		
		byte[] packet = new byte[29];
		
		byte[] idb = BytesUtils.toBytes(getId());
		byte[] px = BytesUtils.toBytes(getPosX());
		byte[] py = BytesUtils.toBytes(getPosY());
		byte[] pz = BytesUtils.toBytes(getPosZ());
		byte[] rx = BytesUtils.toBytes(getAngX()*EntityMatrixUtils.RAD_TO_DEG);
		byte[] ry = BytesUtils.toBytes(getAngY()*EntityMatrixUtils.RAD_TO_DEG);
		byte[] rz = BytesUtils.toBytes(getAngZ()*EntityMatrixUtils.RAD_TO_DEG);
		packet[0] = GameValues.packets.ENTITY_UPDATE;
		packet[1] = idb[0];
		packet[2] = idb[1];
		packet[3] = idb[2];
		packet[4] = idb[3];
		
		packet[5] = px[0];
		packet[6] = px[1];
		packet[7] = px[2];
		packet[8] = px[3];

		packet[9] = py[0];
		packet[10] = py[1];
		packet[11] = py[2];
		packet[12] = py[3];

		packet[13] = pz[0];
		packet[14] = pz[1];
		packet[15] = pz[2];
		packet[16] = pz[3];
		

		packet[17] = rx[0];
		packet[18] = rx[1];
		packet[19] = rx[2];
		packet[20] = rx[3];

		packet[21] = ry[0];
		packet[22] = ry[1];
		packet[23] = ry[2];
		packet[24] = ry[3];

		packet[25] = rz[0];
		packet[26] = rz[1];
		packet[27] = rz[2];
		packet[28] = rz[3];
		synchronized (Server.getClients()) {
			for(Client client: Server.getClients()) client.getRudp().sendPacket(packet);
		}
	}

	@Override
	public void render3D() {
		ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(getPosX(), getPosY(), getPosZ()), getAngX(), getAngY(), getAngZ(), 1));
		model.render();
	}

	@Override
	public void renderVR(int eye) {
		ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(getPosX(), getPosY(), getPosZ()), getAngX(), getAngY(), getAngZ(), 1));
		model.renderVR(eye);
	}

	@Override
	public void destroy() {
		CollisionManager3d.getDynamicWorld().removeRigidBody(body);
		body = null;
	}
	
	//https://stackoverflow.com/a/32180454

}
