
package service;

public class TransactionBuffer {

    private long[] buffer = new long[3];
    private int count = 0;
    private int in = 0;
    private int out = 0;

    public void add(long transaction) throws InterruptedException {
        synchronized (buffer) {
            while (count == buffer.length) {
                buffer.wait();
            }

            buffer[in] = transaction;
            in = (in + 1) % buffer.length;
            count++;

            System.out.println("Added: " + transaction);

            buffer.notifyAll();
        }
    }

    public long take() throws InterruptedException {
        synchronized (buffer) {
            while (count == 0) {
                buffer.wait();
            }

            long transaction = buffer[out];
            out = (out + 1) % buffer.length;
            count--;

            buffer.notifyAll();

            return transaction;
        }
    }
}
