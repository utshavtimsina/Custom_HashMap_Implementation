/**
 * @Author: UTSAB
 * @For: Assignment
 */
public class Exercise03 {

    public static void main(String[] args) {
        MyMap<String, Integer> map = new MyHashMap<String, Integer>();//Initializing a new map of size 4
        /**
         * using old example values from book
         */
        //Added into a hashMap of size 4
        map.put("Smith", 30);
        map.put("Anderson", 31);
        // adds into a new hashTable of size 8
        map.put("Lewis", 29);
        map.put("Cook", 29);
        //Assigns a new value 65 which previously was 30 to smith
        map.put("Smith", 65);
        //Added into a new hashTable of size 16
        map.put("Utsab", 65);
        map.put("Ashish", 65);

        System.out.println("Entries in map: " + map);

        System.out.println("The age for " + "Lewis is " + map.get("Lewis"));

        System.out.println("Is Smith in the map? " + map.containsKey("Smith"));
        System.out.println("Is age 33 in the map? " + map.containsValue(33));

        map.remove("Smith");
        System.out.println("Entries in map: " + map);

        map.clear();
        System.out.println("Entries in map: " + map);
    }

    static class MyHashMap<K, V> implements MyMap<K, V> {
        // Define the default hash table size. Must be a power of 2
        private static int DEFAULT_INITIAL_CAPACITY = 4;

        // Define the maximum hash table size. 1 << 30 is same as 2^30
        private static int MAXIMUM_CAPACITY = 1 << 30;

        // Current hash table capacity. Capacity is a power of 2
        private int capacity;

        // Define default load factor
        private static float DEFAULT_MAX_LOAD_FACTOR = 0.5f;

        // Specify a load factor used in the hash table
        private float loadFactorThreshold;

        // The number of entries in the map
        private int size = 0;

        // Hash table is an array with each cell that is a linked list
        MyMap.Entry<K, V>[] map;

        /** Construct a map with the default capacity and load factor */
        public MyHashMap() {
            this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
        }

        /**
         * Construct a map with the specified initial capacity and default load
         * factor
         */
        public MyHashMap(int initialCapacity) {
            this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
        }

        /**
         * Construct a map with the specified initial capacity and load factor
         */
        @SuppressWarnings("unchecked")
        public MyHashMap(int initialCapacity, float loadFactorThreshold) {
            if (initialCapacity > MAXIMUM_CAPACITY)
                this.capacity = MAXIMUM_CAPACITY;
            else
                this.capacity = trimToPowerOf2(initialCapacity);

            this.loadFactorThreshold = loadFactorThreshold;
            /**
             * Creating a new Map with Size
             */
            map = new MyMap.Entry[capacity];
        }

        @Override
        /** Remove the entries from this map */
        public void clear() {
            size = 0;
            removeEntries();
        }

        @Override
        /** Return true if the specified key is in the map */
        public boolean containsKey(K key) {
            int i = hash(key.hashCode());
            while (map[i] != null) {
                if (map[i].getKey().equals(key))
                    return true;
                i = (i + hash2(i)) % capacity;
            }

            return false;
        }

        @Override
        /** Return true if this map contains the value */
        public boolean containsValue(V value) {
            for (int i = 0; i < capacity; i++) {
                if (map[i] != null) {
                    if (map[i].getValue().equals(value))
                        return true;
                }
            }

            return false;
        }

        @Override
        /** Return a set of entries in the map */
        public java.util.Set<MyMap.Entry<K, V>> entrySet() {
            java.util.Set<MyMap.Entry<K, V>> set = new java.util.HashSet<MyMap.Entry<K, V>>();

            for (int i = 0; i < capacity; i++) {
                if (map[i] != null) {
                    set.add(map[i]);
                }
            }

            return set;
        }

        @Override
        /** Return the value that matches the specified key */
        public V get(K key) {
            int i = hash(key.hashCode());
            while (map[i] != null) {
                if (map[i].getKey().equals(key))
                    return map[i].getValue();
                i = (i + hash2(i)) % capacity;
            }

            return null;
        }

        @Override
        /** Return true if this map contains no entries */
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        /** Return a set consisting of the keys in this map */
        public java.util.Set<K> keySet() {
            java.util.Set<K> set = new java.util.HashSet<K>();

            for (int i = 0; i < capacity; i++) {
                if (map[i] != null) {
                    set.add(map[i].getKey());
                }
            }

            return set;
        }

        @Override
        /** Add an entry (key, value) into the map */
        public V put(K key, V value) {
            if (get(key) != null) { // The key is already in the map
                int i = hash(key.hashCode());
                while (map[i] != null) {
                    //if the key already exists in a map then set new value
                    if (map[i].getKey().equals(key)) {
                        V oldValue = map[i].getValue();
                        map[i].value = value;
                        return oldValue;// Mandatory return so returning old Value here..
                    }
                    i = (i + hash2(i)) % capacity;//finding next empty index in case of hash collision using double hashing
                }
            }


            // Check load factor  with map size
            if (size >= capacity * loadFactorThreshold) {
                //Throwing Exception here
                if (capacity == MAXIMUM_CAPACITY)
                    throw new RuntimeException("Exceeding maximum capacity");
                //Readding previous hashed elements to a new hashTable
                rehash();
            }

            int i = hash(key.hashCode());
            //Finding next empty index i in which value can be added
            while (map[i] != null) {
                i = (i + hash2(i)) % capacity;
            }
            map[i] = new MyMap.Entry<K, V>(key, value);
            size++;
            return value;
        }

        @Override
        /** Remove the entries for the specified key */
        public void remove(K key) {
            int i = hash(key.hashCode());
            while (map[i] != null) {
                if (map[i].getKey().equals(key)) {
                    size--;
                    map[i] = null;//flag this as empty
                }
                i = (i + hash2(i)) % capacity;
            }
        }

        @Override
        /** Return the number of entries in this map */
        public int size() {
            return size;
        }

        @Override
        /** Return a set consisting of the values in this map */
        public java.util.Set<V> values() {
            java.util.Set<V> set = new java.util.HashSet<V>();

            for (int i = 0; i < capacity; i++) {
                if (map[i] != null) {
                    set.add(map[i].getValue());
                }
            }

            return set;
        }

        /** Hash function */
        private int hash(int hashCode) {
            return supplementalHash(hashCode) & (capacity - 1);
        }

        /** Hash2 function */
        private int hash2(int k) {
            return 7 - k % 7;
        }

        /** Ensure the hashing is evenly distributed */
        private static int supplementalHash(int h) {
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }

        /** Return a power of 2 for initialCapacity */
        private int trimToPowerOf2(int initialCapacity) {
            int capacity = 1;
            while (capacity < initialCapacity) {
                capacity <<= 1;
            }

            return capacity;
        }

        /** Remove all entries from each bucket */
        private void removeEntries() {
            for (int i = 0; i < capacity; i++) {
                if (map[i] != null) {
                    map[i] = null;
                }
            }
        }

        /** Rehash the map */
        @SuppressWarnings("unchecked")
        private void rehash() {
            java.util.Set<Entry<K, V>> set = entrySet(); // Get entries
            capacity <<= 1; // Double capacity
            map = new MyMap.Entry[capacity];
            size = 0; // Reset size to 0
            //Adding the old Map content to a new one
            for (Entry<K, V> entry : set) {
                put(entry.getKey(), entry.getValue()); // Store to new table
            }
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("[");

            for (int i = 0; i < capacity; i++) {
                if (map[i] != null)
                    builder.append(map[i]);
            }

            builder.append("]");
            return builder.toString();
        }
    }

    interface MyMap<K, V> {
        /** Remove all of the entries from this map */
        public void clear();

        /** Return true if the specified key is in the map */
        public boolean containsKey(K key);

        /** Return true if this map contains the specified value */
        public boolean containsValue(V value);

        /** Return a set of entries in the map */
        public java.util.Set<Entry<K, V>> entrySet();

        /** Return the first value that matches the specified key */
        public V get(K key);

        /** Return true if this map contains no entries */
        public boolean isEmpty();

        /** Return a set consisting of the keys in this map */
        public java.util.Set<K> keySet();

        /** Add an entry (key, value) into the map */
        public V put(K key, V value);

        /** Remove the entries for the specified key */
        public void remove(K key);

        /** Return the number of mappings in this map */
        public int size();

        /** Return a set consisting of the values in this map */
        public java.util.Set<V> values();

        /** Define inner class for Entry */
        public static class Entry<K, V> {
            K key;
            V value;

            public Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }

            @Override
            public String toString() {
                return "[" + key + ", " + value + "]";
            }
        }
    }

}
