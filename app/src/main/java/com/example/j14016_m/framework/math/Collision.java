package com.example.j14016_m.framework.math;

public class Collision {

    public static boolean intersectSegmentTriangle(Vector3 p, Vector3 q, Vector3 a, Vector3 b, Vector3 c, float uvwt[]) {

        Vector3 ab = Vector3.sub(b, a);
        Vector3 ac = Vector3.sub(c, a);
        Vector3 qp = Vector3.sub(p, q);

        Vector3 n = Vector3.cross(ab, ac);

        float d = Vector3.dot(qp, n);
        if(d <= 0.0f) return false;

        Vector3 ap = Vector3.sub(p, a);
        uvwt[3] = Vector3.dot(ap, n);
        if(uvwt[3] < 0.0f) return false;
        if(uvwt[3] > d) return false;

        Vector3 e = Vector3.cross(qp, ap);
        uvwt[1] = Vector3.dot(ac, e);
        if(uvwt[1] < 0.0f || uvwt[1] > d) return false;

        uvwt[2] = -Vector3.dot(ab, e);
        if(uvwt[2] < 0.0f || uvwt[1] + uvwt[2] > d) return false;

        float ood = 1.0f / d;
        uvwt[3] *= ood;
        uvwt[1] *= ood;
        uvwt[2] *= ood;
        uvwt[0] = 1.0f - uvwt[1] - uvwt[2];

        return true;

    }


    public static boolean intersectRayTriangle(Vector3 p, Vector3 q, Vector3 a, Vector3 b, Vector3 c, float uvwt[]) {

        Vector3 ab = Vector3.sub(b, a);
        Vector3 ac = Vector3.sub(c, a);
        Vector3 qp = Vector3.sub(p, q);

        Vector3 n = Vector3.cross(ab, ac);

        float d = Vector3.dot(qp, n);
        if(d <= 0.0f) return false;

        Vector3 ap = Vector3.sub(p, a);
        uvwt[3] = Vector3.dot(ap, n);
        if(uvwt[3] < 0.0f) return false;
        //if(uvwt[3] > d) return false;

        Vector3 e = Vector3.cross(qp, ap);
        uvwt[1] = Vector3.dot(ac, e);
        if(uvwt[1] < 0.0f || uvwt[1] > d) return false;

        uvwt[2] = -Vector3.dot(ab, e);
        if(uvwt[2] < 0.0f || uvwt[1] + uvwt[2] > d) return false;

        float ood = 1.0f / d;
        uvwt[3] *= ood;
        uvwt[1] *= ood;
        uvwt[2] *= ood;
        uvwt[0] = 1.0f - uvwt[1] - uvwt[2];

        return true;

    }



}
