package application;

public interface HashTableADT<K, V> {
	V get (K key);
	void add();
	boolean remove();
	boolean isEmpty();
	int getSize();
}
