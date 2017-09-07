package collection;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> {
	private final int MAX_CACHE_SIZE;
	private final float DEFAULT_LOAD_FACTOR = 0.75f;
	LinkedHashMap<K, V> map;

	public LRUCache(int cacheSize) {
		MAX_CACHE_SIZE = cacheSize;
		// 根据cacheSize和加载因子计算hashmap的capactiy，+1确保当达到cacheSize上限时不会触发hashmap的扩容。
		int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
		map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTOR, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > MAX_CACHE_SIZE;
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}

	public synchronized void put(K key, V value) {
		map.put(key, value);
	}

	public synchronized V get(K key) {
		return map.get(key);
	}

	public synchronized void remove(K key) {
		map.remove(key);
	}

	public synchronized int size() {
		return map.size();
	}

	public synchronized void clear() {
		map.clear();
	}

	public static void main(String[] args) {
		LRUCache<Integer, String> lru = new LRUCache<Integer, String>(5);
		lru.put(1, "11");
		lru.put(2, "11");
		lru.put(3, "11");
		lru.put(4, "11");
		lru.put(5, "11");
		System.out.println(lru.toString());
		lru.put(6, "66");
		lru.get(2);
		lru.put(7, "77");
		lru.get(4);
		System.out.println(lru.toString());
		System.out.println();
	}
}