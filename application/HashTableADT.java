package application;

public interface HashTableADT {
	String get (String key);
	void add(String key, String location, String date);
	boolean remove(String key);
	boolean isEmpty();
	int getSize();
}
