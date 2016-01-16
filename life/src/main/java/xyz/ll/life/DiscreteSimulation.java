package xyz.ll.life;

import java.util.Random;

/**
 * Created by lucas-cleto on 1/16/16.
 */
public class DiscreteSimulation {

    private static final int[] DEL_X = {-1,  0,  1, -1,  0,  1, -1,  0,  1};
    private static final int[] DEL_Y = {-1, -1, -1,  0,  0,  0,  1,  1,  1};

    private static int[] shuffleArray(int[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }

    private static void plot(int[][] map, int[][] individuals) {
        System.out.println();
        System.out.println();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != -1) {
                    System.out.printf("%03d ", map[i][j]);
                } else {
                    System.out.print("... ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < individuals.length; i++) {
            System.out.printf("(%03d) ", i);
            for (int j = 0; j < individuals[i].length; j++) {
                System.out.print(individuals[i][j] + " ");
            }
            if (i % 2 == 0) {
                System.out.print("        ");
            } else {
                System.out.println();
            }
        }

        System.out.println();
        System.out.println();

        try {
            Thread.sleep(5 * 1000);
        } catch (Exception e) {}
    }

    private static boolean compatible(int[][] individuals, int a, int b) {
        int sum = 0;
        for (int i = 0; i < individuals[a].length; i++) {
            if (individuals[a][i] != individuals[b][i]) {
                sum++;
            }
        }
        return sum < 3;
    }

    public static void main(String[] args) {
        int[][] individuals = new int[20][5];
        int[][] pos = new int[20][2];
        int[][] map = new int[25][25];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = -1;
            }
        }

        Random random = new Random(0l);
        for (int i = 0; i < pos.length; i++) {
            while (true) {
                int x = random.nextInt(map.length);
                int y = random.nextInt(map[x].length);
                if (map[x][y] == -1) {
                    map[x][y] = i;
                    pos[i][0] = x;
                    pos[i][1] = y;
                    break;
                }
            }
        }

        plot(map, individuals);

        for (int t = 1; true; t++) {
            individualsLoop : for (int i = 0; i < pos.length; i++) {
                //scanning the surroundings
                int startX = (pos[i][0] - 3) < 0 ? 0 : (pos[i][0] - 3);
                int endX = (pos[i][0] + 3) > map.length ? map.length : (pos[i][0] + 3);
                for (int x = startX; x < endX; x++) {
                    int startY = (pos[i][1] - 3) < 0 ? 0 : (pos[i][1] - 3);
                    int endY = (pos[i][1] + 3) > map[x].length ? map[x].length : (pos[i][1] + 3);
                    for (int y = startY; y < endY; y++) {
                        //looking for a pair
                        if (map[x][y] != -1 && map[x][y] != i && compatible(individuals, i, map[x][y])) {
                            //get the pair
                            int pair = map[x][y];

                            //choose the ancestor for each half of dna
                            int begin, end;
                            if (random.nextBoolean()) {
                                begin = 0;
                                end = individuals[i].length / 2;
                            } else {
                                begin = individuals[i].length / 2;
                                end = individuals[i].length;
                            }

                            //merge the dna
                            for (int k = begin; k < end; k++) {
                                individuals[i][k] = individuals[pair][k];
                            }

                            //potential mutation
                            if (random.nextInt(100) == 0) {
                                int mutation = random.nextInt(individuals[i].length);
                                individuals[i][mutation] = individuals[i][mutation] == 0 ? 1 : 0;
                            }

                            //move to the new position
                            if (random.nextInt(10) == 0) {
                                int[] mov = new int[9];
                                for (int j = 0; j < 9; j++) {
                                    mov[j] = j;
                                }
                                mov = shuffleArray(mov);
                                for (int j = 0; j < 9; j++) {
                                    int xf = pos[i][0] + DEL_X[mov[j]];
                                    int yf = pos[i][1] + DEL_Y[mov[j]];

                                    if (xf < 0) {
                                        xf = 0;
                                    }
                                    if (xf > map.length - 1) {
                                        xf = map.length - 1;
                                    }
                                    if (yf < 0) {
                                        yf = 0;
                                    }
                                    if (yf > map[xf].length - 1) {
                                        yf = map[xf].length - 1;
                                    }

                                    if (map[xf][yf] == -1 || map[xf][yf] == i) {
                                        map[pos[i][0]][pos[i][1]] = -1;

                                        pos[i][0] = xf;
                                        pos[i][1] = yf;

                                        map[pos[i][0]][pos[i][1]] = i;
                                    }
                                }
                            }

                            //continue
                            continue individualsLoop;
                        }
                    }
                }
            }

            if (t % 100 == 0) {
                System.out.println();
                plot(map, individuals);
            } else {
                System.out.print(".");
            }
        }
    }
}
