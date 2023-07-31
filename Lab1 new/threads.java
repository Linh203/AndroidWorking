
import java.math.BigInteger;
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

class MyThread implements Runnable {
    /**
     * Luồng
     */
    private Thread t;

    /**
     * Tên luồng
     */
    String name;

    Integer maxNum;

    Flag flag;

    /**
     * constructor
     */
    MyThread(String _name, Flag _flag, Integer _maxNum) {
        this.name = _name;
        System.out.println("running MyThread constructor " + name);
        this.flag = _flag;
        this.maxNum = _maxNum;
    }

    /**
     * implement interface của Runnable
     */
    public void run() {
        for(int i = 0; i < maxNum ; i++){
            // check cờ
            if (flag.isStop) {
                break;
            }
            System.out.println("Thread " + name + " : "+ i);
        }
        //vẫy cờ
        flag.setDone();
    }

    /**
     * Hàm start thread
     */
    public void start() {
        System.out.println("running MyThread " + name + "is starting");
        // kiểm tra xem thread đã được khởi tạo hay chưa nếu chưa thì khởi tạo
        if (t == null) {
            // truyền vào Runnable và tên của thread (tên thread không được trùng nhau)
            t = new Thread(this, name);
            t.start();
        }
    }
}

public class threads {
    Flag flag;
    public static void main(String[] args) {
       long startTime = System.nanoTime();

       Flag flag = new Flag();
        
        // khai báo instance của class MyThread
        MyThread mt1 = new MyThread("Thread1", flag, 4);
        mt1.start();

        // khai báo instance của class MyThread
        MyThread mt2 = new MyThread("Thread2", flag, 4);
        mt2.start();
        MyThread mt3 = new MyThread("Thread3", flag, 4);
        mt3.start();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }
}

/**
 * Cờ đánh dấu nhiệm vụ đã xong
 */
class Flag {
    Boolean isStop = false;
    Integer starttime = 0;
    Integer endtime = 0;

    public Flag(){
        starttime = (int) System.nanoTime();
    }
    
    public void setDone(){
        isStop = true;
        endtime = (int) System.nanoTime();
    }

    public Boolean isDone(){
        return isStop;
    }

    public Integer getDuration(){
        return Math.abs(endtime - starttime);
    }
}