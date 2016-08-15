package zhuang.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @author guomianzhuang
 * 
 * some regex usage
 * */

public class StringReplace {

	private static final String REGEX_SCRIPT = "<\\s*script[^>]*?>[\\s\\S]*?<\\/script>";
	private static final String REGEX_STYLE = "<\\s*style[^>]*?>[\\s\\S]*?<\\/style>";
	private static final String REGEX_HTML = "<[^>]+>";
	private static final String REGEX_SPACE = "\\t|\\r|\\n|\\u3000|\\u00A0";
	// private static final String REGEX_HTML_P_IMG = "<(?!img|p|/p)[^>]+>";
	private static final String REGEX_HTML_PAIR_P = "<p>\\s*?</p>";
	private static final String REGEX_HTML_PAIR_SPACE = "<p>(&nbsp;)*?</p>";
	private static final String REGEX_HTML_SPACE_P = "<p>\\s*";
	// private static final String REGEX_HTML_SPACE_PTAIL = "</p>\\s*";
	private static final String REGEX_HTML_CLASS_P = "<p><img";
	// ?非贪婪匹配
	private static final String REGEX_PRE = "<pre>(((?!<\\/pre>)[\\s\\S])*)<\\/pre>";
	private static final String REGEX_OL = "<ol[^>]+>(((?!<\\/ol>)[\\s\\S])*)<\\/ol>";
	private static final String REGEX_TABLE = "<table>(((?!<\\/table>)[\\s\\S])*)<\\/table>";
	private static final String REGEX_LTGT = "&lt;[\\/\\s\\S]*?(&gt)";

	// match url end with space or chinese
	private static final String REGEX_LINK = "(http:|ftp:|https:).*?(\\s|[\u4e00-\u9fa5])";
	private static final String Dot_NET = "[\\s\\S].*?(.net)";
	private static final String REGEX_AD = "(参考价格|报价|定价|售价|预售)[^\\,\\。\\，]+[0-9]+(元|美元)";

	/**
	 * filter Ad article
	 * @param htmlStr : article content
	 * @return true while article is a Ad article
	 */
	public static boolean adDetect(String htmlStr) {
		boolean res = false;
		Pattern p_html = Pattern.compile(REGEX_AD, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		if (m_html.find()) {
			res = true;
		}
		return res;
	}

	/**
	 * remove all html tag
	 * @param article content
	 * @return filtered content
	 */
	public static String removeAllHtmlTag(String htmlStr) {

		// 过滤 style | script 标签
		htmlStr = removeHtmlScriptAndStyle(htmlStr);

		// 过滤html标签
		Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(" ");

		// 过滤回车、换行、制表符标签
		Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll("");

		// 过滤 HTML空格
		htmlStr = htmlStr.replaceAll("&nbsp;", "");

		return htmlStr.trim();
	}

	/**
	 * filter all html tag, url and space
	 * @param html
	 * @return filtered content
	 */
	public static String removeLink(String htmlStr) {

		// 过滤 style | script 标签
		Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(" ");

		// 过滤style标签
		Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(" ");

		// 过滤html标签
		Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(" ");

		// 过滤http链接标签
		Pattern p_link = Pattern.compile(REGEX_LINK, Pattern.CASE_INSENSITIVE);
		Matcher m_link = p_link.matcher(htmlStr);
		htmlStr = m_link.replaceAll(" ");

		// 过滤回车、换行、制表符标签
		Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll("");

		// 过滤 HTML空格
		htmlStr = htmlStr.replaceAll("&nbsp;", "");

		return htmlStr.trim();
	}

	public static String removeHtmlScriptAndStyle(String htmlStr) {
		// 过滤script标签
		Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll("");

		// 过滤style标签
		Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll("");

		return htmlStr.trim();
	}

	/**
	 * remove all html tag and code
	 * @param content
	 * @return filtered content
	 */
	public static String removeLinkAndPre(String htmlStr) {
		htmlStr = htmlStr.replace("$", "\\$");

		Pattern p_pre = Pattern.compile(REGEX_PRE, Pattern.CASE_INSENSITIVE);
		Matcher m_pre = p_pre.matcher(htmlStr);
		htmlStr = m_pre.replaceAll("");

		p_pre = Pattern.compile(REGEX_OL, Pattern.CASE_INSENSITIVE);
		m_pre = p_pre.matcher(htmlStr);
		htmlStr = m_pre.replaceAll("");

		p_pre = Pattern.compile(REGEX_TABLE, Pattern.CASE_INSENSITIVE);
		m_pre = p_pre.matcher(htmlStr);
		htmlStr = m_pre.replaceAll("");

		htmlStr = removeLink(htmlStr);

		return htmlStr;
	}

	/**
	 * filter original style, then restore the html tag
	 * @param flag is no use
	 * @return content
	 * */
	public static String replaceArticleIrregular(String htmlStr, boolean flag) {

		htmlStr = htmlStr.replace("$", "\\$");
		List<String> preStrs = new ArrayList<String>();

		// 提取pre标签的内容,并替换为空字符串
		Pattern p_pre = Pattern.compile(REGEX_PRE, Pattern.CASE_INSENSITIVE);
		Matcher m_pre = p_pre.matcher(htmlStr);
		while (m_pre.find()) {
			preStrs.add(m_pre.group(1));
		}
		htmlStr = m_pre.replaceAll("<pre><\\/pre>");

		// 为img标签添加p标签包含
		htmlStr = addTagP(htmlStr);

		// 过滤回车、换行、制表符标签
		Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(" ");

		// 过滤p紧接的空格
		Pattern p_ps = Pattern.compile(REGEX_HTML_SPACE_P, Pattern.CASE_INSENSITIVE);
		Matcher m_ms = p_ps.matcher(htmlStr);
		htmlStr = m_ms.replaceAll("<p>");

		// 为img 前的p加上class
		Pattern p_img = Pattern.compile(REGEX_HTML_CLASS_P, Pattern.CASE_INSENSITIVE);
		Matcher m_img = p_img.matcher(htmlStr);
		htmlStr = m_img.replaceAll("<p class=\"article-img\"><img");

		// 过滤空对p
		Pattern p_pair = Pattern.compile(REGEX_HTML_PAIR_P, Pattern.CASE_INSENSITIVE);
		Matcher m_pair = p_pair.matcher(htmlStr);
		htmlStr = m_pair.replaceAll("");
		
		//过滤掉  只含占位符的P
		Pattern p_s = Pattern.compile(REGEX_HTML_PAIR_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_p = p_s.matcher(htmlStr);
		htmlStr = m_p.replaceAll("");

		// 过滤连续换行
		htmlStr = htmlStr.replaceAll("\n+", "\n");

		// 还原pre标签内容
		Pattern p_pre2 = Pattern.compile("<pre><\\/pre>", Pattern.CASE_INSENSITIVE);
		Matcher m_pre2 = p_pre2.matcher(htmlStr);
		int i = 0;
		StringBuffer result = new StringBuffer();
		while (m_pre2.find()) {
			String pre = "<pre>" + preStrs.get(i) + "<\\/pre>";
			m_pre2.appendReplacement(result, pre);
			i++;
		}
		m_pre2.appendTail(result);
		htmlStr = result.toString();

		return htmlStr.trim();
	}

	/**
	 * remove &lt &gt
	 * */
	public static String remove_ltgt(String htmlStr) {
		// 去掉文本中的标签
		Pattern p_space = Pattern.compile(REGEX_LTGT, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(" ");
		return htmlStr.trim();
	}
	
	/**
	 * remove all space
	 * */
	public static String removeAllSpace(String htmlStr) {
		// 过滤回车、换行、制表符、全角空格、不间断空格\u00a0
		Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(" ");

		return htmlStr.trim();
	}

	
	public static String addTagP(String htmlStr) {
		StringBuffer sb = new StringBuffer();
		// Document doc = Jsoup.parseBodyFragment(htmlStr);
		Document doc = Jsoup.parse(htmlStr);
		Element e = doc.select("body").get(0);
		List<Node> node = e.childNodes();
		int flag = 0;
		for (Node n : node) {
			String name = n.nodeName();
			if (name == "p") {
				sb.append(n.toString().trim());
			} else if (name == "img") {
				sb.append("<p>").append(n.toString().trim()).append("</p>");
			} else {
				if (flag == 0) {
					sb.append("<p>").append(n.toString().trim());
					flag = 1;
				} else {
					sb.append(n.toString().trim());
				}
				Node next = n.nextSibling();
				if (n.nextSibling() == null) {
					sb.append("</p>");
				} else if (next.nodeName() == "p" || next.nodeName() == "img") {
					sb.append("</p>");
					flag = 0;
				}
			}
		}
		return sb.toString();
	}
	
	public static String getText(String content) {
		System.out.println(content);
		Pattern p_script = Pattern.compile("<strong>[\\s\\S]*?<\\/strong>", Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(content);
		return m_script.group();
	}
}
