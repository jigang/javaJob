import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SpriderControler {

	private static int THREAD_COUNT = 5;

	public static void main(String[] args) {
		spriderLiePin();
//		spriderLaGou();
	}

	public static void spriderLiePin() {
		long startTime = System.currentTimeMillis();
		LiePin liePin = new LiePin();
		liePin.start();

		System.out.println("--------爬取完成，用时" + (System.currentTimeMillis() - startTime) / 1000 + "秒---------");
		System.out.println("--------总共爬取页面" + liePin.javaPageCount + "个--------");
		List<Entry<String, Integer>> list = liePin.orderByMap();
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i).toString();
			System.out.println(id);
		}
	}

	public static void spriderLaGou() {
		long startTime = System.currentTimeMillis();
		LaGou laGou = new LaGou();
		List<Thread> threadList = new ArrayList<Thread>();
		for (int i = 0; i < THREAD_COUNT; i++) {
			Thread thread = new Thread(laGou, "线程：" + i);
			thread.start();
			threadList.add(thread);
		}
		try {
			for (Thread thread : threadList) {
				thread.join();// 主线程等待其他线程结束
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("--------爬取完成，用时" + (System.currentTimeMillis() - startTime) / 1000 + "秒---------");
		System.out.println("--------总共爬取页面" + THREAD_COUNT * laGou.getUrlCount() + "个--------");
		System.out.println("--------java招聘页面" + laGou.javaPageCount + "个---------");
		// 排序
		List<Entry<String, Integer>> list = laGou.orderByMap();
		for (int i = 0; i < list.size(); i++) {
			String id = list.get(i).toString();
			System.out.println(id);
		}
	}

}
