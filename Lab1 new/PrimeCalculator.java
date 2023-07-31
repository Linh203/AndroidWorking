import java.util.ArrayList;
import java.util.List;


public class PrimeCalculator  implements Runnable{
    private int start;
    private int end;
    private List<Integer> primes = new ArrayList<>();

    public PrimeCalculator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

     // Kiểm tra xem một số có phải là số nguyên tố hay không
     private boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
    // Tính toán số nguyên tố và lưu vào danh sách primes
    public void run() {
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
    }
}
