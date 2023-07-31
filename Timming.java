public class Timming {
    public static void main() {
        long startTime = System.nanoTime();
        // your code in here
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }
}
