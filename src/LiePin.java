

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiePin extends JobInfo{
	
	private static final String PATH = "D:\\JavaInLiePin.txt";//信息存放路径
	
	private static final String PAY_SCOPE_REG= "<p class=\"job-main-title\">(.+?)<em>";
	
	private static final String CITY_REG = "<i class=\"icons24 icons24-position\"></i>(.+?)</span>";
	
	private static final String DESC_REG = "<h3>职位描述：</h3>.+?<div class=\"content content-word\">(.+?)</div>";

	public static final String JAVA_URL = "http://www.liepin.com/zhaopin/?pubTime=&salary=&searchType=0&clean_condition=&jobKind=&isAnalysis=&init=1&searchField=1&key=java&industries=&jobTitles=&dqs=&compscale=&compkind=";
	
	public static final int pageCount = 5;//爬取java招聘页数
	
	private LinkedList<String> urlList = new LinkedList<String>();//需要爬取的url队列
	
	public int javaPageCount = 0;//java页面计数器
	
	// 开始爬取猎聘网入口
	public void start(){
		
		// 先爬取搜索出来的java招聘网页url
		spriderUrl(JAVA_URL);
		
		// 开启5个线程同时运行
		List<Thread> threadList = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					spriderDetail();
				}
			});
			thread.start();
			threadList.add(thread);
		}
		
		// 主线程等待其他线程执行完毕
		for (Thread t : threadList){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 爬取招聘页面详细信息
	private void spriderDetail(){
		while (!urlList.isEmpty()){
			String url = getUrl();
			String content = SpriderUtil.getInstance().getHtmlContent(url);
			if (content != null){
				setPayScope(getMatcherResult(content, PAY_SCOPE_REG));
				setCity(getMatcherResult(content, CITY_REG));
				setDescription(getMatcherResult(content, DESC_REG));
				// 把爬取内容写入文件
				String laGouStr = this.toString();
				SpriderUtil.getInstance().saveContent(PATH, laGouStr);
				setSpecialySkill(laGouStr);
				javaPageCount++;
			}
		}
	}
	
	// 爬取猎聘网java招聘前n页搜索内容并把job url放进urlList
	private void spriderUrl(String url){
		SpriderUtil getHtml = new SpriderUtil();
		String javaSearchContent = getHtml.getHtmlContent(url);
		Pattern pattern = Pattern.compile("job-info.+?href=\"(.+?)\" target");
		Matcher matcher = pattern.matcher(javaSearchContent);
		while (matcher.find()){
			urlList.add(matcher.group(1));
		}
		if (getMatcherResult(javaSearchContent, "…</span><a href=\".+?curPage=(.+?)\">下一页").equals("10")){
			System.out.println("结束吧");
			return;
		}
		// 递归调用循环爬取
		spriderUrl(getMatcherResult(javaSearchContent, "…</span><a href=\"(.+?)\">下一页</a>"));
	}
	
	private synchronized String getUrl(){
		String url = urlList.getFirst();
		urlList.removeFirst();
		return url;
	}
	
}
