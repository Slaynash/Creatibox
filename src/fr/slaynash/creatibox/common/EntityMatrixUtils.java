package fr.slaynash.creatibox.common;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class EntityMatrixUtils {
	
	public static final float RAD_TO_DEG = (float) (180f/Math.PI);
	
	public static Matrix4f createEntityTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Vector3f getRotation(Matrix4f mat){
		float yaw, pitch, roll;
        if (mat.m00 == 1.0f)
        {
            yaw = (float) Math.atan2(mat.m02, mat.m23);
            pitch = 0;
            roll = 0;

        }else if (mat.m00 == -1.0f)
        {
            yaw = (float) Math.atan2(mat.m02, mat.m23);
            pitch = 0;
            roll = 0;
        }else 
        {

            yaw = (float) Math.atan2(mat.m20,mat.m00);
            pitch = (float) Math.asin(mat.m10);
            roll = (float) Math.atan2(mat.m12,mat.m11);
        }
        return new Vector3f(-roll, -yaw, pitch);
	}

	public static Vector3f getRotation(javax.vecmath.Matrix4f mat){
		float yaw, pitch, roll;
        if (mat.m00 == 1.0f)
        {
            yaw = (float) Math.atan2(mat.m02, mat.m23);
            pitch = 0;
            roll = 0;

        }else if (mat.m00 == -1.0f)
        {
            yaw = (float) Math.atan2(mat.m02, mat.m23);
            pitch = 0;
            roll = 0;
        }else 
        {

            yaw = (float) Math.atan2(mat.m20,mat.m00);
            pitch = (float) Math.asin(mat.m10);
            roll = (float) Math.atan2(mat.m12,mat.m11);
        }
        return new Vector3f(-roll, -yaw, pitch);
	}
}
