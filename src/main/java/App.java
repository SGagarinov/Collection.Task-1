import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class App {

    private static ArrayBlockingQueue aQueue = new ArrayBlockingQueue(100);
    private static ArrayBlockingQueue bQueue = new ArrayBlockingQueue(100);
    private static ArrayBlockingQueue cQueue = new ArrayBlockingQueue(100);

    public static final String[] alphabet = {"a", "b", "c"};

    public static void main(String[] args) throws InterruptedException {

        int length = 100_000;
        int size = 10_000;

        Thread generatedThread = new Thread(() -> {

            for (int i = 0; i < size; i++) {
                try {
                    aQueue.put(generateText(String.join("", alphabet), length));
                } catch (InterruptedException e) {
                    return;
                }

                try {
                    bQueue.put(generateText(String.join("", alphabet), length));
                } catch (InterruptedException e) {
                    return;
                }

                try {
                    cQueue.put(generateText(String.join("", alphabet), length));
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        generatedThread.start();

        int[] maxSize = new int[3];
        String[] maxString = new String[3];

        Thread aCountThread = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                try {
                    String str = aQueue.take().toString();
                    int sizeA = str.split("a").length;

                    if (sizeA > maxSize[0]) {
                        maxSize[0] = sizeA;
                        maxString[0] = str;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread bCountThread = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                try {
                    String str = bQueue.take().toString();
                    int sizeB = str.split("a").length;

                    if (sizeB > maxSize[1]) {
                        maxSize[1] = sizeB;
                        maxString[1] = str;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread cCountThread = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                try {
                    String str = cQueue.take().toString();
                    int sizeC = str.split("a").length;

                    if (sizeC > maxSize[2]) {
                        maxSize[2] = sizeC;
                        maxString[2] = str;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        aCountThread.start();
        bCountThread.start();
        cCountThread.start();

        aCountThread.join();
        bCountThread.join();
        cCountThread.join();
        generatedThread.join();

        System.out.println("Максимальная строка с символом 'a' содержит " + maxSize[0] + " вхождений символа");
        System.out.println("Максимальная строка с символом 'b' содержит " + maxSize[1] + " вхождений символа");
        System.out.println("Максимальная строка с символом 'c' содержит " + maxSize[2] + " вхождений символа");

        System.out.println("Максимальная строка с символом 'a':\n" + maxString[0]);
        System.out.println("Максимальная строка с символом 'b':\n" + maxString[1]);
        System.out.println("Максимальная строка с символом 'c':\n" + maxString[2]);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
