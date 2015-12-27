
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JobInfo {

	private String payScope;// 薪资范围

	private String city;// 所在城市

	private String description;// 职位描述

	private Map<String, Integer> specialySkills = new Hashtable<String, Integer>();// 存储所有专业技能与出现次数

	// 所有三个字母以上的单词认为是专业名词
	public void setSpecialySkill(String content) {
		Pattern pattern = Pattern.compile("[a-zA-Z]{3,}");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			putSkill(matcher.group());
		}
	}

	// 把所有专业技能名词存放在hashtable中并计算次数
	private void putSkill(String key) {
		if (specialySkills.containsKey(key)) {
			specialySkills.put(key, specialySkills.get(key) + 1);
		} else {
			specialySkills.put(key, 1);
		}
	}

	public String getMatcherResult(String content, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	// 对map按照专业技能出现次数进行排序，并返回list
	public List<Entry<String, Integer>> orderByMap() {
		Iterator<Map.Entry<String, Integer>> it = specialySkills.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();
			// 筛选掉出现次数少于10次的
			if (entry.getValue() < 10) {
				it.remove();
				continue;
			}
			// 筛选掉java web ee等无用单词
			if ("null".equals(entry.getKey()) || "and".equals(entry.getKey()) || "web".equals(entry.getKey())) {
				it.remove();
			}
		}
		// 排序
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(specialySkills.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		return list;
	}

	@Override
	public String toString() {
		String result = "";
		result += "工资范围：" + this.payScope + "\r\n";
		result += "城市：" + this.city + "\r\n";
		result += "职位描述：" + this.description + "\r\n";
		result = result.replaceAll("<.+?>", "\r\n");
		result = result.replaceAll("&nbsp;", " ");
		return result.toLowerCase();
	}

	public String getPayScope() {
		return payScope;
	}

	public void setPayScope(String payScope) {
		this.payScope = payScope;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
