public class test {
    public static void main(String[] args) {
        int limit = 1000000;
        long sum = 0;

        for (int i = 2; i <= limit; i++) {
            if (isPrime(i)) {
                sum += i;
            }
        }

        System.out.println("Tổng của số nguyên tố từ 1 đến " + limit + " là: " + sum);
}
public static boolean isPrime(int number) {
    if (number <= 1) {
        return false;
    }

    for (int i = 2; i <= Math.sqrt(number); i++) {
        if (number % i == 0) {
            return false;
        }
    }

    return true;
}
}
