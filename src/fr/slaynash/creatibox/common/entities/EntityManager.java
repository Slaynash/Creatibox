package fr.slaynash.creatibox.common.entities;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import fr.slaynash.communication.rudp.RUDPClient;
import fr.slaynash.creatibox.common.BytesUtils;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;

public class EntityManager {
	
	private static List<EntityDef> entityDefList = new ArrayList<EntityDef>();
	private static List<Entity> entityList = new ArrayList<Entity>();
	private static int entityNumber = 0;
	
	public static EntityDef createEntityDef(String entityRaw) {
		String[] data = entityRaw.split(" ");
		if(data[0].equals("prop")) {
			return new EntityPropDef(data[1]);
		}
		else return null;
	}
	
	public static void registerEntityDef(EntityDef entity) {
		for(EntityDef ent:entityDefList) if(ent.getName().equals(entity.getName())) {
			System.err.println(LogTimer.getTimeForLog()+"[EntityManager] Unable to register entity "+entity.getName()+": This name is already used.");
			return;
		}
		System.out.println(LogTimer.getTimeForLog()+"[EntityManager] Registering entity with name "+entity.getName());
		entityDefList.add(entity);
	}
	
	public static EntityDef getEntityDef(String entityName) {
		synchronized (entityDefList) {
			for(EntityDef entity:entityDefList) if(entity.getName().equals(entityName)) return entity;
		}
		return null;
	}
	
	/**
	 * <b><u>! Server only !</u></b>
	 * @param entityName
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param angX
	 * @param angY
	 * @param angZ
	 * @return
	 */
	public static Entity createEntity(String entityName, float posX, float posY, float posZ, float angX, float angY, float angZ) {
		synchronized (entityList) {
			for(EntityDef entity:entityDefList) if(entity.getName().equals(entityName)) {
				Entity e = entity.createEntity((entityNumber+=1), posX, posY, posZ, angX, angY, angZ);
				entityList.add(e);
				return e;
			}
		}
		System.err.println(LogTimer.getTimeForLog()+"[EntityManager] Unable to find entity with name "+entityName);
		return null;
	}
	
	//Client
	public static Entity createEntity(int id, String entityName, float posX, float posY, float posZ, float angX, float angY, float angZ) {
		System.out.println("Creating entity "+entityName+" at "+posX+"_"+posY+"_"+posZ);
		synchronized (entityList) {
			for(EntityDef entity:entityDefList) if(entity.getName().equals(entityName)) {
				Entity e = entity.createEntity(id, posX, posY, posZ, angX, angY, angZ);
				entityList.add(e);
				return e;
			}
		}
		System.err.println(LogTimer.getTimeForLog()+"[EntityManager] Unable to find entity with name "+entityName);
		return null;
	}
	
	public static void updateEntities() {
		synchronized (entityList) {
			for(Entity entity:entityList) entity.update();
		}
	}
	
	public static void renderEntities3D() {
		synchronized (entityList) {
			for(Entity entity:entityList) entity.render3D();
		}
	}
	
	public static void renderEntitiesVR(int eye) {
		synchronized (entityList) {
			for(Entity entity:entityList) entity.renderVR(eye);
		}
	}

	public static Entity getEntity(int entityID) {
		synchronized (entityList) {
			for(Entity entity:entityList) if(entity.getId() == entityID) return entity;
		}
		return null;
	}

	public static void clear() {
		synchronized (entityList) {
			entityList.clear();
		}
	}

	public static void sendListToUser(RUDPClient rudp) {
		synchronized (entityList) {
			for(Entity entity:entityList) {
				byte[] name = entity.getDef().getName().getBytes(StandardCharsets.UTF_8);
				byte[] packet = new byte[29+name.length];
				
				packet[0] = GameValues.packets.ADD_ENTITY_ONCONNECT;
				byte[] idb = BytesUtils.toBytes(entity.getId());
				byte[] px = BytesUtils.toBytes(entity.getPosX());
				byte[] py = BytesUtils.toBytes(entity.getPosX());
				byte[] pz = BytesUtils.toBytes(entity.getPosX());
				byte[] rx = BytesUtils.toBytes(entity.getAngX());
				byte[] ry = BytesUtils.toBytes(entity.getAngX());
				byte[] rz = BytesUtils.toBytes(entity.getAngX());
				
				

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
				
				for(int i=0;i<name.length;i++) packet[29+i] = name[i];
				
				rudp.sendReliablePacket(packet);
			}
		}
	}

	public static boolean removeEntity(int entityId) {
		synchronized (entityList) {
			for(int i=0;i<entityList.size();i++) {
				if(entityList.get(i).getId() == entityId) {
					entityList.get(i).destroy();
					entityList.remove(i);
					return true;
				}
			}
		}
		return false;
	}
}
