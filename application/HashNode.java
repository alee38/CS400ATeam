package application;

// Inner class for the hash table
class HashNode {
    String key;
    String location;
    String date;
    //private Node next;

    // Initialize a hashNode with given key and value
    // Key cannot be null, or else throw IllegalNullKeyException
    HashNode(String key, String location, String date) {
        if (key != null) {

            this.key = key;
            this.location = location;
            this.date=date;
            //this.next = null;
        }
    }
}
