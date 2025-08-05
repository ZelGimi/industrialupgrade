package com.denfop.world.vein.noise;

import net.minecraft.util.RandomSource;

import java.util.*;
import java.util.List;

import static com.denfop.world.vein.noise.Center.generateCenters;

public class PerlinNoiseViewer {
    private static   int maxValue = 4;
    private static final int WIDTH = 128*maxValue;
    private static final int HEIGHT = 128*maxValue;
    private static Point decodeToPoint(long encoded) {
        return new Point(getX(encoded), getY(encoded));
    }
    private static int getX(long encoded) {
        return (int) (encoded >> 32);
    }

    private static int getY(long encoded) {
        return (int) encoded;
    }
    private static long encode(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }


    private static boolean hasNeighborInSet(int x, int y, Set<Long> set) {
        return set.contains(encode(x - 1, y)) ||
                set.contains(encode(x + 1, y)) ||
                set.contains(encode(x, y - 1)) ||
                set.contains(encode(x, y + 1));
    }

    private static boolean hasNeighborInSetNear(int x, int y, Set<Long> set) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (set.contains(encode(x + dx, y + dy))) return true;
            }
        }
        return false;
    }

    public static List<ShellCluster> createClusters(RandomSource random) {
        int maxCenters = 512 * maxValue  * maxValue * 2;
        double minDist = 3;
        ArrayList<Center> centers = generateCenters(maxCenters, minDist, WIDTH, HEIGHT);
        double maxShellRadius = 8;
        double maxShellRadiusSq = maxShellRadius * maxShellRadius;

        List<ShellCluster> clusters = new LinkedList<>();

        class Pixel {
            short x, y;
            double dist;
            Pixel(int x, int y, double dist) {
                this.x = (short) x;
                this.y = (short) y;
                this.dist = dist;
            }
        }

        Map<Integer, List<Pixel>> centerPixels = new HashMap<>();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double minDistSq = Double.MAX_VALUE;
                int nearestIndex = -1;

                for (int i = 0; i < centers.size(); i++) {
                    Center c = centers.get(i);
                    double dx = x - c.x;
                    double dy = y - c.y;
                    double distSq = dx * dx + dy * dy;

                    if (distSq < minDistSq) {
                        minDistSq = distSq;
                        nearestIndex = i;
                    }
                }


                double randomShellDist = random.nextDouble() * maxShellRadius;
                if (minDistSq <= randomShellDist * randomShellDist) {
                    List<Pixel> list = centerPixels.computeIfAbsent(nearestIndex, k -> new LinkedList<>());
                    list.add(new Pixel(x, y, Math.sqrt(minDistSq)));
                }
            }
        }

         for (Map.Entry<Integer, List<Pixel>> entry : centerPixels.entrySet()) {
            List<Pixel> pixels = new ArrayList<>(entry.getValue());
            pixels.sort(Comparator.comparingDouble(p -> p.dist));

            ShellCluster cluster = new ShellCluster();
            clusters.add(cluster);

            double percentGray = (random.nextInt(10) + 1) / 10.0;
            double percentLightGray = (random.nextInt(8) + 1) / 10.0;
            double percentLightGray1 = (random.nextInt(5) + 1) / 10.0;

            int count = 0;
             Set<Long> blacks = new HashSet<>();
             Set<Long> grays = new HashSet<>();
             Set<Long> lightGrays = new HashSet<>();

             for (Pixel p : pixels) {
                 long encoded = encode(p.x, p.y);
                 if (count == 0) {
                     cluster.blacks.add(new Point(p.x, p.y));
                     blacks.add(encoded);
                 } else if (count < 25) {
                     if (hasNeighborInSet(p.x, p.y, blacks)) {
                         if (random.nextDouble() < percentGray) {
                             cluster.grays.add(new Point(p.x, p.y));
                             grays.add(encoded);
                         }
                     } else if (hasNeighborInSet(p.x, p.y, grays)) {
                         if (random.nextDouble() < percentLightGray) {
                             cluster.lightGrays.add(new Point(p.x, p.y));
                             lightGrays.add(encoded);
                         }
                     } else if (hasNeighborInSet(p.x, p.y, blacks)) {
                         if (hasNeighborInSetNear(p.x, p.y, grays)) {
                             if (random.nextDouble() < percentLightGray1) {
                                 cluster.lightGrays.add(new Point(p.x, p.y));
                                 lightGrays.add(encoded);
                             }
                         }
                     }
                 }

                 count++;
            }
        }

        return clusters;
    }
    public static ShellCluster createVolcanoClusters(RandomSource random) {
        int maxCenters = 1;
        double minDist = 1;
        ArrayList<Center> centers = generateCenters(maxCenters, minDist, 24, 24);
        double maxShellRadius = 4;
        List<ShellCluster> clusters = new LinkedList<>();
        class Pixel {
            short x, y;
            double dist;
            Pixel(int x, int y, double dist) {
                this.x = (short) x;
                this.y = (short) y;
                this.dist = dist;
            }
        }

        Map<Integer, List<Pixel>> centerPixels = new HashMap<>();

        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                double minDistSq = Double.MAX_VALUE;
                int nearestIndex = -1;

                for (int i = 0; i < centers.size(); i++) {
                    Center c = centers.get(i);
                    double dx = x - c.x;
                    double dy = y - c.y;
                    double distSq = dx * dx + dy * dy;

                    if (distSq < minDistSq) {
                        minDistSq = distSq;
                        nearestIndex = i;
                    }
                }


                double randomShellDist = random.nextDouble() * maxShellRadius;
                if (minDistSq <= randomShellDist * randomShellDist) {
                    List<Pixel> list = centerPixels.computeIfAbsent(nearestIndex, k -> new LinkedList<>());
                    list.add(new Pixel(x, y, Math.sqrt(minDistSq)));
                }
            }
        }

        for (Map.Entry<Integer, List<Pixel>> entry : centerPixels.entrySet()) {
            List<Pixel> pixels = new ArrayList<>(entry.getValue());
            pixels.sort(Comparator.comparingDouble(p -> p.dist));

            ShellCluster cluster = new ShellCluster();
            clusters.add(cluster);
            for (Pixel p : pixels) {
                cluster.point = new Point(p.x, p.y);
                break;

            }
        }

        return clusters.get(0);
    }
}
