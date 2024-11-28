package com.somemore.global.common.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class GeoUtils {

    private static final double EARTH_RADIUS = 6371.0;

    public static double[] calculateMaxMinCoordinates(double latitude, double longitude,
        double radius) {
        double latRad = Math.toRadians(latitude);
        double latDiff = radius / EARTH_RADIUS;
        double maxLatRad = latRad + latDiff;
        double minLatRad = latRad - latDiff;

        double maxLat = Math.toDegrees(maxLatRad);
        double minLat = Math.toDegrees(minLatRad);

        double lonDiff = radius / (EARTH_RADIUS * Math.cos(latRad));
        double maxLon = longitude + Math.toDegrees(lonDiff);
        double minLon = longitude - Math.toDegrees(lonDiff);

        return new double[]{minLat, minLon, maxLat, maxLon};
    }

}
