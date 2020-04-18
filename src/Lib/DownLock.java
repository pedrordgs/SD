package Lib;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DownLock {
    private ReentrantLock lock;
    private int downloads;
    private Queue<Condition> waiters;
    private static final int MAXDOWN = 2;

    public DownLock() {
        this.lock = new ReentrantLock();
        this.downloads = 0;
        this.waiters=new LinkedList<>();
    }

    public void downLock(){
        this.lock.lock();
        while (this.downloads==MAXDOWN){
            try {
                Condition wait = this.lock.newCondition();
                this.waiters.add(wait);
                wait.await();
            }
            catch (InterruptedException e){
                e.printStackTrace();
                this.lock.unlock();
            }
        }
        this.downloads++;
        this.lock.unlock();
    }

    public void downUnlock (){
        this.lock.lock();
        downloads--;
        if(downloads<MAXDOWN && !waiters.isEmpty()){
            this.waiters.remove().signal();
        }
        this.lock.unlock();
    }
}
