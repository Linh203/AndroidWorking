class MyThread implements Runnable {
    /**
     * Luồng
     */
    private Thread t;

    /**
     * Tên luồng
     */
    String name;

    /**
     * constructor
     */
    MyThread(String _name) {
        this.name = _name;
        System.out.println("running MyThread constructor " + name);
    }

    /**
     * implement interface của Runnable
     */
    public void run() {
        System.out.println("running MyThread " + name);
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

public class ThreadDemo {
    public static void main(String[] args) {
        // khai báo instance của class MyThread
        MyThread mt1 = new MyThread("Thread1");
        mt1.start();

        // khai báo instance của class MyThread
        MyThread mt2 = new MyThread("Thread2");
        mt2.start();

    }
}
