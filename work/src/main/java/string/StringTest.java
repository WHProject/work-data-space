package string;

import java.util.Random;

public class StringTest {

	private static final String base = " base string. ";
	private static final int count = 2000000;

	public static void stringTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		String test = new String(base);
		for (int i = 0; i < count / 100; i++) {
			test = test + " add ";
		}
		end = System.currentTimeMillis();
		System.out.println((end - begin) + " millis has elapsed when used String. ");
	}

	public static void stringBufferTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		StringBuffer test = new StringBuffer(base);
		for (int i = 0; i < count; i++) {
			test = test.append(" add ");
		}
		end = System.currentTimeMillis();
		System.out.println((end - begin) + " millis has elapsed when used StringBuffer. ");
	}

	public static void stringBuilderTest() {
		long begin, end;
		begin = System.currentTimeMillis();
		StringBuilder test = new StringBuilder(base);
		for (int i = 0; i < count; i++) {
			test = test.append(" add ");
		}
		end = System.currentTimeMillis();
		System.out.println((end - begin) + " millis has elapsed when used StringBuilder. ");
	}

	public static void performanceTest() {
		stringTest();
		stringBufferTest();
		stringBuilderTest();
	}

	public static void stringStorageTest() {
		/**
		 * 情景一：字符串池 JAVA虚拟机(JVM)中存在着一个字符串池，其中保存着很多String对象; 并且可以被共享使用，因此它提高了效率。
		 * 由于String类是final的，它的值一经创建就不可改变。
		 * 字符串池由String类维护，我们可以调用intern()方法来访问字符串池。
		 */
		String s1 = "abc";
		// ↑ 在字符串池创建了一个对象
		String s2 = "abc";
		// ↑ 字符串pool已经存在对象“abc”(共享),所以创建0个对象，累计创建一个对象
		System.out.println("s1 == s2 : " + (s1 == s2));
		// ↑ true 指向同一个对象，
		System.out.println("s1.equals(s2) : " + (s1.equals(s2)));
		// ↑ true 值相等
		// ↑------------------------------------------------------over

		/**
		 * 情景二：关于new String("")
		 * 
		 */
		String s31 = "abc";
		String s3 = new String("abc");
		// ↑ 创建了两个对象，一个存放在字符串池中，一个存在与堆区中；
		// ↑ 还有一个对象引用s3存放在栈中
		String s4 = new String("abc");
		// ↑ 字符串池中已经存在“abc”对象，所以只在堆中创建了一个对象
		System.out.println("s3 == s4 : " + (s3 == s4));
		// ↑false s3和s4栈区的地址不同，指向堆区的不同地址；
		System.out.println("s3.equals(s4) : " + (s3.equals(s4)));
		// ↑true s3和s4的值相同
		System.out.println("s1 == s3 : " + (s1 == s3));
		// ↑false 存放的地区多不同，一个栈区，一个堆区
		System.out.println("s1.equals(s3) : " + (s1.equals(s3)));
		// ↑true 值相同
		System.out.println("s3 == s31 : " + (s3 == s31));
		// ↑false s31对象在PermGen space中，s3对象在Heap space中，二者不是一个对象
		// ↑------------------------------------------------------over

		/**
		 * 情景三： 由于常量的值在编译的时候就被确定(优化)了。 在这里，"ab"和"cd"都是常量，因此变量str3的值在编译时就可以确定。
		 * 这行代码编译后的效果等同于： String str3 = "abcd";
		 */
		String str1 = "ab" + "cd"; // 1个对象
		String str11 = "abcd";
		System.out.println("str1 = str11 : " + (str1 == str11));
		// ↑true 编译期优化
		// ↑------------------------------------------------------over

		/**
		 * 情景四： 局部变量str2,str3存储的是存储两个拘留字符串对象(intern字符串对象)的地址。
		 * 
		 * 第三行代码原理(str2+str3)： 运行期JVM首先会在堆中创建一个StringBuilder类，
		 * 同时用str2指向的拘留字符串对象完成初始化， 然后调用append方法完成对str3所指向的拘留字符串的合并，
		 * 接着调用StringBuilder的toString()方法在堆中创建一个String对象，
		 * 最后将刚生成的String对象的堆地址存放在局部变量str3中。
		 * 
		 * 而str5存储的是字符串池中"abcd"所对应的拘留字符串对象的地址。 str4与str5地址当然不一样了。
		 * 
		 * 内存中实际上有五个字符串对象： 三个拘留字符串对象、一个String对象和一个StringBuilder对象。
		 */
		String str2 = "ab"; // 1个对象
		String str3 = "cd"; // 1个对象
		String str4 = str2 + str3;
		String str5 = "abcd";
		System.out.println("str4 = str5 : " + (str4 == str5));
		// false
		// ↑------------------------------------------------------over

		/**
		 * 情景五： JAVA编译器对string + 基本类型/常量 是当成常量表达式直接求值来优化的。
		 * 运行期的两个string相加，会产生新的对象的，存储在堆(heap)中
		 */
		String str6 = "b";
		String str7 = "a" + str6;
		String str67 = "ab";
		System.out.println("str7 = str67 : " + (str7 == str67));
		// ↑false
		// ↑str6为变量，在运行期才会被解析。
		final String str8 = "b";
		String str9 = "a" + str8;
		String str89 = "ab";
		System.out.println("str9 = str89 : " + (str9 == str89));
		// ↑true
		// ↑str8为常量变量，编译期会被优化
		// ↑------------------------------------------------------over
		String l = "22";
		String k = l;
		System.out.println(k == l);
	}

	static final int MAX = 1000 * 10000;
	static final String[] arr = new String[MAX];

	public static void internTest() {
		Integer[] DB_DATA = new Integer[10];
		Random random = new Random(10 * 10000);
		for (int i = 0; i < DB_DATA.length; i++) {
			DB_DATA[i] = random.nextInt();
		}
		long t = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			arr[i] = String.valueOf(DB_DATA[i % DB_DATA.length]).intern();
		}

		System.out.println((System.currentTimeMillis() - t) + "ms has elapsed when intern is used.");
		System.gc();
	}

	public static void noInternTest() {
		Integer[] DB_DATA = new Integer[10];
		Random random = new Random(10 * 10000);
		for (int i = 0; i < DB_DATA.length; i++) {
			DB_DATA[i] = random.nextInt();
		}
		long t = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			arr[i] = String.valueOf(DB_DATA[i % DB_DATA.length]);
		}

		System.out.println((System.currentTimeMillis() - t) + "ms has elapsed when intern is not used.");
		System.gc();
	}

	static final int MAX2 = 500 * 10000;
	static final String[] arr2 = new String[MAX];

	public static void internTestNoRepeat() {
		long t = System.currentTimeMillis();
		for (int i = 0; i < MAX2; i++) {
			arr2[i] = String.valueOf(i).intern();
		}

		System.out.println((System.currentTimeMillis() - t) + "ms has elapsed when intern is used.");
		System.gc();
	}

	public static void noInternTestNoRepeat() {
		long t = System.currentTimeMillis();
		for (int i = 0; i < MAX2; i++) {
			arr2[i] = String.valueOf(i);
		}

		System.out.println((System.currentTimeMillis() - t) + "ms has elapsed when intern is not used.");
		System.gc();
	}

	public static void internTestJDK8() {
		// 因为常量池所在的PermGen space与Heap space是隔离的，JDK7前
		// intern会将首次出现的字符串对象完全拷贝到常量池所在的Perm Gen space中。
		// 而JDK7后常量池搬到了Heap space，为了节省空间，intern会将首次出现的字符串对象的引用拷贝到常量池中。
		String str1 = new String("a") + new String("b");
		System.out.println(str1.intern() == str1);
		// ↑JDK6 false JDK8 true
		System.out.println(str1 == "ab");
		// ↑JDK6 false JDK8 true
		System.gc();
	}

	public static void internTestJDK8WithPredefinedString() {
		// 因为常量池所在的PermGen space与Heap space是隔离的，JDK7前
		// intern会将首次出现的字符串对象完全拷贝到常量池所在的Perm Gen space中。
		// 而JDK7后常量池搬到了Heap space，为了节省空间，intern会将首次出现的字符串对象的引用拷贝到常量池中。
		String str2 = "cd";

		String str1 = new String("c") + new String("d");
		System.out.println(str1.intern() == str1);
		// ↑JDK6 false JDK8 false str1.intern()的地址为str2对象的地址，str1为在heap
		// space上新创建的字符串对象，二者不是一个对象
		System.out.println(str1 == "cd");
		// ↑JDK6 false JDK8 false str1为在heap space上新创建的字符串对象，"cd"为str2对象的地址
		System.gc();
	}

	public static void internTestJDK8WithManyString() {
		String str1 = new String("e") + new String("f");
		String str2 = null;

		// heap space中存在大量字符串时，会导致intern失效，此情况应该与以下案例fastjson
		// BUG问题类似，常量池中字符串过多，超过一定限制，就不会再往常量池中放了。
		noInternTestNoRepeat();

		str2 = str1.intern();

		System.out.println(str2 == str1);
		// ↑JDK6 false JDK8 false str1为在heap
		// space上新创建的字符串对象，str2与str1对象所在位置不一致，所以不相等。
		System.out.println(str1 == "ef");
		// ↑JDK6 false JDK8 false str1为在heap
		// space上新创建的字符串对象，"ef"与str1对象所在位置不一致，所以不相等。
		System.gc();
	}

	public static void main(String[] args) throws InterruptedException {
		performanceTest();

		stringStorageTest();

		internTest();
		noInternTest();

		internTestNoRepeat();
		noInternTestNoRepeat();

		internTestJDK8();

		internTestJDK8WithPredefinedString();

		internTestJDK8WithManyString();
	}

}
