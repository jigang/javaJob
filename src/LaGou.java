
public class LaGou extends JobInfo implements Runnable {
	
	private static final String PATH = "D:\\JavaInLaGou.txt";//信息存放路径

	private static final String FIND_JAVA_REG= "<title>[j|J]ava(.+?)</title>";
	
	private static final String PAY_SCOPE_REG= "<span class=\"red\">(.+?)</span>";
	
	private static final String CITY_REG = "<span class=\"red\">.+?</span>.+?<span>(.+?)</span>";
	
	private static final String DESC_REG = "<h3 class=\"description\">职位描述</h3>(.+?)</dd>";
	
	private String url = "http://www.lagou.com/jobs/";//拉勾具体招聘信息网址
	
	private int jobNumber = 1156800;//起始页
	
	private int urlCount = 500;//每个线程爬取的页面数

	public int javaPageCount = 0;//java页面计数器

	@Override
	public void run() {
		int number = getJobNumber();
		//拉勾搜索页面是用js动态生成，不能直接获取html源码, 比较麻烦，故暂时用此方法搜索java招聘内容
		for (int i = number; i < (number + urlCount); i++) {
			String urlStr = url + i + ".html";
			SpriderUtil getHtml = new SpriderUtil();
			String content = getHtml.getHtmlContent(urlStr);
			if (content == null) {//内容为空说明没有得到源码结束本次循环
				continue;
			}
			String title = getMatcherResult(content, FIND_JAVA_REG);
			if (title != null){//不为空说明匹配到java招聘内容
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

	
	private synchronized int getJobNumber() {
		jobNumber += urlCount;
		return jobNumber;
	}

	public int getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(int urlCount) {
		this.urlCount = urlCount;
	}
	
	
}
