package com.example.j14016_m.framework.math;

import android.opengl.Matrix;


public class Vector3 {
	private static final float[] matrix = new float[16];
	private static final float[] inVec = new float[4];
	private static final float[] outVec = new float[4];
	public float x, y, z;

	public Vector3() {
	}

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/*
	public Vector3(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public Vector3 cpy() {
		return new Vector3(x, y, z);
	}
	*/
	
	public Vector3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/*
	public Vector3 set(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		return this;
	}
	*/

	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3 add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	/*
	public Vector3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}


	public Vector3 sub(Vector3 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public Vector3 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}
	*/


	public float len() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 nor() {
		float len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
			this.z /= len;
		}
		return this;
	}

	/*
	public Vector3 rotate(float angle, float axisX, float axisY, float axisZ) {
		inVec[0] = x;
		inVec[1] = y;
		inVec[2] = z;
		inVec[3] = 1;
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
		x = outVec[0];
		y = outVec[1];
		z = outVec[2];
		return this;
	}
	
	public float dist(Vector3 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return (float) Math.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	
	public float dist(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return (float) Math.sqrt(distX * distX + distY * distY + distZ * distZ);
	}
	*/

	public float distSquared(Vector3 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return distX * distX + distY * distY + distZ * distZ;
	}

	/*
	public float distSquared(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return distX * distX + distY * distY + distZ * distZ;
	}
	*/

	public void zero() {
		x = 0;
		y = 0;
		z = 0;
	}




	////////////////////////////////////////////////////////////////////
	public static Vector3 normal(Vector3 v1, Vector3 v2, Vector3 v3) {
		Vector3 a = sub(v2, v1);
		Vector3 b = sub(v3, v1);
		Vector3 c = cross(a, b);
		c.nor();
		return c;
	}

	public static Vector3 sub(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	public static Vector3 add(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}


	/*
	public Vector3 cross(Vector3 b) {
		this.x = this.y * b.z - this.z * b.y;
		this.y = this.z * b.x - this.x * b.z;
		this.z = this.x * b.y - this.y * b.x;

		return this;
	}
	*/

	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		float x = v1.y * v2.z - v1.z * v2.y;
		float y = v1.z * v2.x - v1.x * v2.z;
		float z = v1.x * v2.y - v1.y * v2.x;
		return new Vector3(x, y, z);
	}

	static public float dot(Vector3 v1, Vector3 v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}


}
