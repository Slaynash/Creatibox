package fr.slaynash.creatibox.common.entities;

import fr.slaynash.creatibox.common.GameValues;
import slaynash.sgengine.models.Renderable3dModel;
import slaynash.sgengine.models.utils.ModelManager;
import slaynash.text.utils.Localization;

public class EntityPropDef extends EntityDef {
	
	private String modelName = "";
	private String textureDiffuse = null;
	private String textureNormal = null;
	private String textureSpecular = null;
	
	private Renderable3dModel model;
	private EntityCollisionShape collisionType = EntityCollisionShape.NONE;
	private float[] bounds;

	public EntityPropDef(String name) {
		super(name);
	}

	@Override
	public void addParameter(String line) {
		if(line.startsWith("name ")) {
			String[] data = line.split(" ", 3);
			Localization.registerTranslation(data[1], getName(), data[2]);
		}
		else if(line.startsWith("model ")) {
			modelName = line.split(" ", 2)[1];
			if(!GameValues.isServer())
				model = ModelManager.loadObj(modelName, textureDiffuse, textureNormal, textureSpecular);
		}
		else if(line.startsWith("collisionShape ")) {
			String type = modelName = line.split(" ", 2)[1];
			if(type.equals("boxShape")) collisionType = EntityCollisionShape.BOX;
			else if(type.equals("cylinderShape")) collisionType = EntityCollisionShape.CYLINDER;
		}
		else if(line.startsWith("collisionBounds ")) {
			String[] data = line.split(" ");
			bounds = new float[data.length-1];
			for(int i=0;i<data.length-1;i++) {
				bounds[i] = Float.parseFloat(data[i+1]);
			}
		}
		else if(line.startsWith("textureDiffuse ")) {
			textureDiffuse = line.split(" ", 2)[1];
		}
		else if(line.startsWith("textureNormal ")) {
			textureDiffuse = line.split(" ", 2)[1];
		}
		else if(line.startsWith("textureSpecular ")) {
			textureDiffuse = line.split(" ", 2)[1];
		}
	}

	public String getModelName() {
		return modelName;
	}

	public String getTextureDiffuse() {
		return textureDiffuse;
	}

	public String getTextureNormal() {
		return textureNormal;
	}

	public String getTextureSpecular() {
		return textureSpecular;
	}

	public Renderable3dModel getModel() {
		return model;
	}

	public EntityCollisionShape getCollisionType() {
		return collisionType;
	}

	@Override
	public Entity createEntity(int id, float posX, float posY, float posZ, float angX, float angY, float angZ) {
		return new EntityProp(id, posX, posY, posZ, angX, angY, angZ, this);
	}

	public float[] getBounds() {
		return bounds;
	}
	
}
