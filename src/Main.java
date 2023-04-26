import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static final int SIZE = 100;
    public static int sizeForEnemyCoordinates = 100;
    public static int sizeForMyCoordinates = 100;
    public static int countMyShips = 10;
    public static int countEnemyShips = 10;

    public static String colorGreen = "\u001B[32m";
    public static String colorReset = "\u001B[0m";

    public static String myBorderField = "\u001B[52;30;46m|\u001B[0m";
    public static String myEmptyField = "\u001B[52;30;46m \u001B[0m";
    public static String enemyBorderField = "\u001B[52;30;43m|\u001B[0m";
    public static String enemyEmptyField = "\u001B[52;30;43m \u001B[0m";
    public static String enemyEmptyFieldForEnemy = " ";

    public static String liveShip = "\u001B[52;30;45m+\u001B[0m";
    public static String deadShip = "\u001B[52;30;41mX\u001B[0m";
    public static String enemyLiveShip = "+";

    public static String hitMyField = "\u001B[52;30;46m*\u001B[0m";
    public static String hitEnemyField = "\u001B[52;30;43m*\u001B[0m";

    public static String[] enemyFieldForMe = new String[SIZE];
    public static String[] myFieldForMe = new String[SIZE];
    public static String[] enemyFieldForEnemy = new String[SIZE];
    public static String[] myFieldForEnemy = new String[SIZE];

    public static int x;
    public static int y;

    public static int[][] environment = {{0, 0}, {-1, 0}, {-1, -1}, {0, -1},
            {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}};
    public static int[] myCoordinatesForEnemy = new int[sizeForMyCoordinates];
    public static int[] enemyCoordinatesForEnemy = new int[sizeForEnemyCoordinates];

    public static void clear() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public static void printFields() {
        clear();
        System.out.print(" ");
        for (int i = 0; i < 10; i++) {
            System.out.print(colorGreen + " " + i + colorReset);
        }
        System.out.print("          ");
        for (int i = 0; i < 10; i++) {
            System.out.print(colorGreen + " " + i + colorReset);
        }
        System.out.println("\r");

        for (int i = 0; i < 100; i = i + 10) {
            System.out.print(colorGreen + i / 10 + colorReset);
            for (int j = 0; j < 10; j++) {
                System.out.print(myBorderField);
                System.out.print(myFieldForMe[i + j]);
            }
            System.out.print(myBorderField);
            System.out.print("        ");
            System.out.print(colorGreen + i / 10 + colorReset);
            for (int j = 0; j < 10; j++) {
                System.out.print(enemyBorderField);
                System.out.print(enemyFieldForMe[i + j]);
            }
            System.out.print(enemyBorderField);
            System.out.println("\r");
        }
    }

    public static boolean checkBorder(String input) {
        String[] inputArr = input.split(",");
        y = Integer.parseInt(inputArr[0]);
        x = Integer.parseInt(inputArr[1]);

        return (y < 0 || y > 9 || x < 0 || x > 9);
    }

    public static boolean checkInput(String fieldForCheck, String[] arr) {

        int point = y * 10 + x;

        for (int[] i : environment) {
            if ((x + i[0]) < 0 || (x + i[0]) > 9 || (y + i[1]) < 0 || (y + i[1]) > 9) {
                continue;
            }
            if (arr[point + i[0] + i[1] * 10].equals(fieldForCheck)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static boolean checkShot(String fieldForCheck, String[] arrWhereCheck,
                                    String hit, String missed, String[] arrForChange) {
        int point = y * 10 + x;
        if (fieldForCheck.equals(arrWhereCheck[point])) {
            for (int[] i : environment) {
                if ((x + i[0]) < 0 || (x + i[0]) > 9 || (y + i[1]) < 0 || (y + i[1]) > 9) {
                    continue;
                }
                arrForChange[point + i[0] + i[1] * 10] = missed;
            }
            arrForChange[point] = hit;
            return true;

        } else arrForChange[point] = missed;
        return false;
    }

    public static void main(String[] args) throws Exception {
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            myCoordinatesForEnemy[i] = i;
            enemyCoordinatesForEnemy[i] = i;
        }
        Arrays.fill(myFieldForMe, myEmptyField);
        Arrays.fill(enemyFieldForMe, enemyEmptyField);
        Arrays.fill(enemyFieldForEnemy, enemyEmptyFieldForEnemy);
        Arrays.fill(myFieldForEnemy, enemyEmptyFieldForEnemy);

        printFields();

        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i < countMyShips + 1; i++) {
            System.out.print("Введите координаты своего " + i +
                    "-го корабля (вертикаль,горизонталь): ");
            String input = scanner.nextLine();

            if (checkBorder(input)) {
                i--;
                continue;
            }
            if (checkInput(myEmptyField, myFieldForMe)) {
                i--;
                continue;
            }

            myFieldForMe[y * 10 + x] = liveShip;

            printFields();
        }
        for (int i = 1; i < countEnemyShips + 1; i++) {
            int newShip = random.nextInt(sizeForEnemyCoordinates);
            x = enemyCoordinatesForEnemy[newShip] % 10;
            y = enemyCoordinatesForEnemy[newShip] / 10;
            if (checkInput(enemyEmptyFieldForEnemy, enemyFieldForEnemy)) {
                i--;
                continue;
            }
            enemyFieldForEnemy[enemyCoordinatesForEnemy[newShip]] = enemyLiveShip;
            System.out.println(enemyCoordinatesForEnemy[newShip]);
            sizeForEnemyCoordinates--;
            enemyCoordinatesForEnemy[newShip] = 1000;
            Arrays.sort(enemyCoordinatesForEnemy);
        }

        printFields();
        while (true) {

            System.out.print("Ваш ход! Введите координаты для удара (вертикаль,горизонталь): ");
            String input = scanner.nextLine();
            if (checkBorder(input)) {
                continue;
            }
            Thread.sleep(300);
            if (checkShot(enemyLiveShip, enemyFieldForEnemy, deadShip, hitEnemyField, enemyFieldForMe)) {
                System.out.println("\u001B[1;5;34m" + "Отличный выстрел! Корабль уничтожен!" + "\u001B[0m");
                Thread.sleep(400);
                countEnemyShips--;
            } else {
                System.out.println("Мимо!");
            }
            Thread.sleep(1800);
            printFields();
            if (countEnemyShips == 0) {
                System.out.println("\u001B[1;5;34m" + "Вы выиграли!!!" + "\u001B[0m");
                break;
            }

            System.out.println("Ход компьютера!");
            Thread.sleep(300);
            int newHit = random.nextInt(sizeForMyCoordinates);
            int coordinatesHit = myCoordinatesForEnemy[newHit];
            x = coordinatesHit % 10;
            y = coordinatesHit / 10;
            System.out.println("Компьютер ударил по координатам (вертикаль,горизонталь): "
                    + y + "," + x);

            if (myFieldForMe[coordinatesHit].equals(liveShip)) {
                for (int[] i : environment) {
                    if ((x + i[0]) < 0 || (x + i[0]) > 9 || (y + i[1]) < 0 || (y + i[1]) > 9) {
                        continue;
                    }
                    for (int j = 0; j < sizeForMyCoordinates; j++) {
                        if ((coordinatesHit + i[0] + i[1] * 10) == myCoordinatesForEnemy[j]) {
                            myCoordinatesForEnemy[j] = 1000;
                            sizeForMyCoordinates--;
                            break;
                        }
                    }

                }
                myFieldForMe[coordinatesHit] = deadShip;
                System.out.println("\u001B[1;5;31m" + "Ваш корабль уничтожен!" + "\u001B[0m");
                Thread.sleep(400);
                countMyShips--;
            } else {
                myFieldForMe[coordinatesHit] = hitMyField;
                myCoordinatesForEnemy[newHit] = 1000;
                sizeForMyCoordinates--;
                System.out.println("Мимо!");
            }
            Arrays.sort(myCoordinatesForEnemy);
            Thread.sleep(1500);
            printFields();
            if (countMyShips == 0) {
                System.out.println("\u001B[1;5;31m" + "Выиграл компьютер!" + "\u001B[0m");
                break;
            }

        }

    }
}