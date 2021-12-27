import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static int[] array;
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the array size: ");

        int size = in.nextInt();
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }

        int processors = Runtime.getRuntime().availableProcessors();
        List<Part> list = new ArrayList<>();
        int delta = Math.max((int) Math.ceil(Math.log(size) / Math.log(processors)), 1);
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < size; i += delta) {
            indices.add(i);
        }

        indices.add(size);
        for (int i = 0; i < indices.size() - 1; i++) {
            list.add(new Part(indices.get(i), indices.get(i + 1)));
        }

        for (Thread thread : list) {
            thread.start();
        }

        long result = 0L;
        for (Part thread : list) {
            result += thread.getPartialSum();
        }

        System.out.println("Result: " + result);
    }
}

class Part extends Thread {
    private int start, end;
    private long partial;
    private boolean flag;
    public Part(int start, int end) {
        this.start = start;
        this.end = end;
        partial = 0L;
        flag = false;
    }

    @Override
    public synchronized void run() {
        for (int i = start; i < end; i++) {
            partial += Main.array[i];
        }

        flag = true;
        System.out.println("Thread '" + this.getName() + "' finished");
        notifyAll();
    }

    public synchronized long getPartialSum() throws InterruptedException {
        while (!flag) {
            wait();
        }

        return partial;
    }
}
