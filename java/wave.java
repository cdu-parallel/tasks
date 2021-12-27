import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

class Main {
    public static long[] array;
    public static int end;
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the array size: ");

        int size = in.nextInt();
        array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }

        int processors = Runtime.getRuntime().availableProcessors();
        int delta = Math.max((int) Math.ceil(Math.log(size) / Math.log(processors)), 1);
        int wave = (int) Math.ceil(Math.log(size) / Math.log(2));
        int middle = size / 2 + size % 2;
        end = size;
        List<Future> futures = new ArrayList<>();

        for (int i = 0; i < wave; i++) {
            System.out.println("Wave: " + i);
            List<Integer> indices = getListOfIndices(end, delta);
            ExecutorService executor = Executors.newFixedThreadPool((int) Math.ceil(Math.log(end) / Math.log(2)));
            futures.clear();

            for (int j = 0; j < indices.size() - 1; j++) {
                futures.add(executor.submit(new WaveThread(indices.get(j), indices.get(j + 1))));
            }

            executor.shutdown();
            while (!futures.stream().allMatch(future -> {
                try {
                    return future.get() == null;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return false;
            })) {
                executor.awaitTermination(100, TimeUnit.MILLISECONDS);
            }

            end = middle;
            middle = end / 2 + end % 2;
            delta = Math.max((int) Math.ceil(Math.log(end) / Math.log(processors)), 1);
            System.out.println();
        }

        System.out.println("Result: " + array[0]);
    }

    private static List<Integer> getListOfIndices(int size, int delta) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < size; i += delta) {
            indices.add(i);
        }

        indices.add(size);
        return indices;
    }
}

class WaveThread implements Runnable {
    private int start, end;
    public WaveThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        calculateSum();
        System.out.println("Thread '" + Thread.currentThread().getName() + "' finished");
    }

    private void calculateSum() {
        for (int i = start; i < end; i++) {
            synchronized (Main.array) {
                if (i < Main.end - i - 1) {
                    Main.array[i] += Main.array[Main.end - i - 1];
                }
            }
        }
    }
}
