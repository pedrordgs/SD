package Lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class Sistema {
    private Map<Integer,Musica> musicas;
    private Map<String,User> users;
    private ReentrantLock lock;
    private DownLock dlock;

    public Sistema() {
        this.musicas = new HashMap<>();
        this.users = new HashMap<>();
        this.dlock=new DownLock();
        this.lock=new ReentrantLock();
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }

    public void dlock() {
        this.dlock.downLock();
    }

    public void dunlock() {
        this.dlock.downUnlock();
    }

    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(Map<Integer, Musica> musicas) {
        this.musicas = musicas;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public Musica getMusica(int id){
        Musica m = null;
        if (this.musicas.containsKey(id)){
            m=this.musicas.get(id);
        }
        return m;
    }

    public int addMusica(Musica m){
        int id = this.musicas.size();
        m.setId(id);
        this.musicas.put(m.getId(),m);
        return id;
    }

    public void addUser (User u){
        this.lock.lock();
        if (this.users.containsKey(u.getUsername())) return;
        this.users.put(u.getUsername(),u);
        this.lock.unlock();
    }

    public boolean dadosCorretos (String username, String password){
        return this.users.containsKey(username) && this.users.get(username).getPassword().equals(password);
    }

    public void download (int id){
        this.lock.lock();
        Musica m = this.musicas.get(id);
        int d = m.getNumeroDownloads();
        m.setNumeroDownloads(++d);
        this.musicas.put(id,m);
        this.lock.unlock();
    }

    public List<Musica> procura (String etiqueta){
        return this.musicas.values().stream().filter(m-> m.getEtiquetas().contains(etiqueta)).collect(Collectors.toList());
    }

    public boolean existeMusica(Musica m){
        return this.musicas.values().stream().anyMatch(c -> c.getTitulo().equals(m.getTitulo()) && c.getInterprete().equals(m.getInterprete()));
    }
}