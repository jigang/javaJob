
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SpriderUtil {

	private static SpriderUtil uniqueInstance = new SpriderUtil();
	
	public static SpriderUtil getInstance(){
		return uniqueInstance;
	}
	
	// 根据url获取html内容
	public String getHtmlContent(String urlStr) {
		BufferedReader reader = null;
		String str = null;
		URL url;
		HttpURLConnection urlConn = null;
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			// 设置requestHeader内容UA
			urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64)"
					+ " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");

			if (urlConn.getResponseCode() == 200) {// 响应码为200时表示连接成功
				// BufferedReaader输入流读取URL响应内容
				reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
				String line = null;
				StringBuffer sb = new StringBuffer();
				// 逐行遍历添加到sb里
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				str = sb.toString();
				System.out.println(Thread.currentThread().getName() + "爬取" + urlStr + "完成");
			} else {
				System.out.println("获取网页源码失败，响应代码为：" + urlConn.getResponseCode());
			}
		} catch (MalformedURLException e) {
			System.out.println("输入的url有误！");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接和资源
			urlConn.disconnect();
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	// 把content写入本地path路径中
		public void saveContent(String path, String content) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
				writer.write(content);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}
