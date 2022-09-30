package cn.caicai.concurrent.condition;

/**
 * 括号问题
 *
 * 给定一个 N 代表括号嵌套层数
 *
 * 每一个生产者 输出 "("
 * 每一个消费者 输出 ")"
 *
 * 最后检查输出的括号格式是否正确来判断程序是否正确
 *
 * */
public class BracketsProblem {

    int count;
    public static int N = 4;
    StringBuilder stringBuilder = new StringBuilder();

    private FIFOLock lock = new FIFOLock();
    private FIFOLock.CV p = lock.createCV();
    private FIFOLock.CV c = lock.createCV();


    void product() throws InterruptedException {

        lock.lock();

        while (count == N) {
            p.await();
        }

        count++;
        System.out.print("(");
        stringBuilder.append("(");

        c.signal();

        lock.unlock();
    }

    void consume() throws InterruptedException {

        lock.lock();

        while (count == 0) {
            c.await();
        }
        count--;
        System.out.print(")");
        stringBuilder.append(")");

        p.signal();

        lock.unlock();

    }

    public static void main(String[] args) throws InterruptedException {
        final BracketsProblem demo = new BracketsProblem();
        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                try {
                    demo.product();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer1");

        Thread producer2 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                try {
                    demo.product();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Producer2");

        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    demo.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer1");

        producer1.start();
        producer2.start();
        consumer1.start();

        producer1.join();
        producer2.join();
        consumer1.join();

        //-----------------------------------
        // 检查括号是否正确
        //-----------------------------------
        StringBuilder stringBuilder = demo.stringBuilder;
        String result = stringBuilder.toString();
        int count = 0;

        for (int i = 0; i < result.length();i++) {
            if (result.charAt(i) == '(') count++;
            if (result.charAt(i) == ')') count--;

            if (count > BracketsProblem.N || count < 0) {
                System.out.println("\ncount:"+count);
                throw new RuntimeException("bug");
            }
        }
        System.out.println("\nOK");
    }
}

