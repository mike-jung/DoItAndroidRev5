package org.techtown.tutorial.graphic;

import android.graphics.Matrix;
import android.graphics.Path;

/**
 * An oriented bounding box
 */
public class OrientedBoundingBox {
    public final float squareness;

    public final float width;
    public final float height;

    public final float orientation; 

    public final float centerX;
    public final float centerY;

    OrientedBoundingBox(float angle, float cx, float cy, float w, float h) {
        orientation = angle;
        width = w;
        height = h;
        centerX = cx;
        centerY = cy;
        float ratio = w / h;
        if (ratio > 1) {
            squareness = 1 / ratio;
        } else {
            squareness = ratio;
        }
    }

    /**
     * Currently used for debugging purpose only.
     *
     * @hide
     */
    public Path toPath() {
        Path path = new Path();
        float[] point = new float[2];
        point[0] = -width / 2;
        point[1] = height / 2;
        Matrix matrix = new Matrix();
        matrix.setRotate(orientation);
        matrix.postTranslate(centerX, centerY);
        matrix.mapPoints(point);
        path.moveTo(point[0], point[1]);

        point[0] = -width / 2;
        point[1] = -height / 2;
        matrix.mapPoints(point);
        path.lineTo(point[0], point[1]);

        point[0] = width / 2;
        point[1] = -height / 2;
        matrix.mapPoints(point);
        path.lineTo(point[0], point[1]);

        point[0] = width / 2;
        point[1] = height / 2;
        matrix.mapPoints(point);
        path.lineTo(point[0], point[1]);

        path.close();

        return path;
    }
}
