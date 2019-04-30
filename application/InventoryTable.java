package application;


public class InventoryTable {

    private class Node {
        private String key;
        private String location;
        private String date;
        private Node next;

        // Initialize a hashNode with given key and value
        // Key cannot be null, or else throw IllegalNullKeyException
        private Node(String key, String location, String date) {
            if (key != null) {

                this.key = key;
                this.location = location;
                this.date = date;
                this.next = null;
            }
        }
    }

    private int capacity;
    private double loadFactorThreshold;
    private int size;
    private Node[] table;

    // Constructor
    InventoryTable() {
        this(0.5, 11);
    }

    public InventoryTable(double loadFactorThreshold, int capacity) {
        this.capacity = capacity;
        this.loadFactorThreshold = loadFactorThreshold;
        table = new Node[capacity];
        size = 0;
    }

    public int hash(String key) {
        return (int) Math.abs(key.hashCode() % capacity);
    }


    public String[] get(String key) {
        // TODO Auto-generated method stub
        int index = hash(key);
        Node cur = table[index];
        while (cur.next != null) {
            if (cur.key.equals(key)) {
                String[] result = {cur.location, cur.date};
                return result;
            }
            cur = cur.next;
        }
        return null;
    }

 
    public void add(String key, String location, String date) {
        // TODO Auto-generated method stub
        insertHelper(key, location, date, table);

        if (getLoadFactor() > loadFactorThreshold) {// if LF threshold is reached, resize and rehash
            this.capacity = 2 * capacity + 1;
            size = 0;
            Node[] newtable = new Node[capacity];// expand array
            for (Node node : table) {
                Node cur = node;
                while (cur != null) {
                    insertHelper(cur.key, cur.location, cur.date, newtable);
                    cur = cur.next;
                }
            }
            this.table = newtable.clone();
        }

    }

    private void insertHelper(String key, String location, String date, Node[] table) {
        Node newnode = new Node(key, location, date);
        if (contains(key)) {
            return;
        }
        if (table[hash(key)] == null) {
            table[hash(key)] = newnode;
            size++;
        } else {
            Node cur = table[hash(key)];
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = newnode;
            size++;
        }
    }

    private double getLoadFactor() {
        // TODO Auto-generated method stub
        return (double) size / capacity;
    }


    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return size == 0;
    }


    public int getSize() {
        // TODO Auto-generated method stub
        return this.size;
    }



    public boolean remove(String key) {
        // TODO Auto-generated method stub
        
        if (key == null) {
            return false;
        } else {
            int index = hash(key);
            if (table[index] == null) {
                // empty linkedlist
                return false;
            }

            Node cur = table[index];
            if (cur.key.equals(key)) {
                // remove head
                table[index] = cur.next;
                size--;
                return true;
            }
            while (cur.next != null) {
                if (cur.next.key.equals(key) && cur.next.next != null) {
                    // remove middle
                    cur.next = cur.next.next;
                    size--;
                    return true;
                } else if (cur.next.key.equals(key) && cur.next.next == null) {
                    // remove tail
                    cur.next = null;
                    size--;
                    return true;
                }
                cur = cur.next;
            }
            return false;
        }

    }

    

    public boolean contains(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return false;
        } else {
            Node cur = table[hash(key)];
            while (cur != null) {
                if (cur.key.equals(key)) {
                    return true;
                }
                cur = cur.next;
            }
        }
        return false;
    }
}
