package Lib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import Runnable.ServidorRunnable;

public class ThreadsNot {
    private List<ServidorRunnable> tnot;
    private ReentrantLock lock;

    public ThreadsNot() {
        this.tnot = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public List<ServidorRunnable> getTnot() {
        return tnot;
    }

    public void setTnot(List<ServidorRunnable> tnot) {
        this.tnot = tnot;
    }

    public void lock (){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }

    public void removeThread(ServidorRunnable s){
        this.tnot.remove(s);
    }

    public void addThread(ServidorRunnable s){
        this.tnot.add(s);
    }

    public void notificarTodos(String titulo, String inter, int id){
        this.tnot.forEach(t -> t.notificar(titulo,inter,id));
    }
}
