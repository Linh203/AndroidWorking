import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int start = 1;
        int end = 1000000;
        int numThreads = 3;

        List<Thread> threads = new ArrayList<>();
        List<PrimeCalculator> calculators = new ArrayList<>();
        List<Integer> primes = new ArrayList<>();

        int range = (end - start + 1) / numThreads; // phần số nguyên khi chia đều

        for (int i = 0; i < numThreads; i++) {
            int threadStart = start + i * range;
            int threadEnd = (i == numThreads - 1) ? end : threadStart + range - 1;

            PrimeCalculator calculator = new PrimeCalculator(threadStart ,threadEnd);
            calculators.add(calculator);

            Thread thread = new Thread(calculator);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            try {
                thread.join(); // đợi tất cả các threads hoàn thành
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Thread thread : threads) {
            try {
                thread.join(); // đợi tất cả các threads hoàn thành
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Tính tổng của danh sách primes
        long sum = 0;
        for (int prime : primes) {
            sum += prime;
        }

        System.out.println("Tổng số nguyên tố từ 1 đến 1000000 là: " + sum);

}

}
