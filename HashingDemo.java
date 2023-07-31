
import java.security.MessageDigest;

class HashingWorker {
   public static String sha256(final String base) {
      try {
         final MessageDigest digest = MessageDigest.getInstance("SHA-256");
         final byte[] hash = digest.digest(base.getBytes("UTF-8"));
         final StringBuilder hexString = new StringBuilder();
         for (int i = 0; i < hash.length; i++) {
            final String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1)
               hexString.append('0');
            hexString.append(hex);
         }
         return hexString.toString();
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }
   }
}

public class HashingDemo {

   public static void main(String args[]) {
      // get input command line argument
      System.out.println("Nhập string cần hash:");

      String input = System.console().readLine();

      String output = HashingWorker.sha256(input);
      System.out.println("input: " + input);
      System.out.println("output: " + output);
   }
}