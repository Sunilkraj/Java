package learn.collection.CustomHashMap;

class Entry<K,V>{
	private K key = null;
	private V value = null;
	private Entry<K,V> next = null;
	
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public Entry<K, V> getNext() {
		return next;
	}
	public void setNext(Entry<K, V> next) {
		this.next = next;
	}	
}

public class CustomHashMap <K, V>{
	private int initialCapacity = 16;
	private Entry <K,V> table [] = null;
	
	public CustomHashMap() {
		this.table = new Entry[initialCapacity];
	} 
	
	public CustomHashMap(int capacity) {
		this.initialCapacity = capacity;
		this.table = new Entry[capacity];
	}
	
	private int index(K key) {
		if(key == null){
            return 0;
        }
		return Math.abs(key.hashCode() % initialCapacity);
	}
	
	public void put(K key, V value) {
		int index = index(key);
		
		Entry<K,V> newEntry = new Entry<K, V>();
		newEntry.setKey(key);
		newEntry.setValue(value);
		newEntry.setNext(null);
		
		if (table[index] == null) {
			table[index] = newEntry;
		} else {
			Entry <K,V> prevNode = null;
			Entry <K,V> currNode = table[index];
			
			while (currNode != null) {
				if (currNode.getKey().equals(key)) {
					currNode.setValue(value);
					break;
				}
				prevNode = currNode;
				currNode = currNode.getNext();
			}
			if (prevNode != null) {
				prevNode.setNext(newEntry);
			}
		}
	}
	
	public V get(K key) {
		V value = null;
		int index = index(key);
		Entry<K, V> e = table[index];
		
		while (e != null) {
			if (e.getKey().equals(key)) {
				value = e.getValue();
				break;
			}
			e = e.getNext();
		}
		return value;
	}
	
	public void remove(K key){
        int index = index(key);
        Entry<K, V> previous = null;
        Entry<K, V> entry = table[index];
        while (entry != null){
            if(entry.getKey().equals(key)){
                if(previous == null){
                    entry = entry.getNext();
                    table[index] = entry;
                    return;
                }else {
                    previous.setNext(entry.getNext());
                    return;
                }
            }
            previous = entry;
            entry = entry.getNext();
        }
    }
	
	 public void display(){
	        for(int i = 0; i < initialCapacity; i++){
	            if(table[i] != null){
	                Entry<K, V> currentNode = table[i];
	                while (currentNode != null){
	                    System.out.println(String.format("Key is %s and value is %s", currentNode.getKey(), currentNode.getValue()));
	                    currentNode = currentNode.getNext();
	                }
	            }
	        }
	    }

}
