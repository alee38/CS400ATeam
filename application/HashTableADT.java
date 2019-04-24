package application;

public interface HashTableADT<K, V> {
	public V get (K key);
	public void add(K key, V value);
	public V remove(K key);
	public boolean isEmpty();
	public int getSize();
}
