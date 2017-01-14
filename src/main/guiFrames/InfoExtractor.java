package main.guiFrames;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

/**
 * This class extracts personal info regarding celebrities from their Wikipedia
 * page
 * 
 * @author ward-mattar
 */
public class InfoExtractor {
	private static final int fieldMaxLength = 30;
	private static final String wikipediaURL = "https://en.wikipedia.org/wiki/";
	public Map<String, List<PersonalInfo>> celebsInfo;

	public void getCelebInfoTable(final String name, final DefaultTableModel m) {
		if (m == null)
			return;
		while (m.getRowCount() > 0)
			m.removeRow(0);
		List<PersonalInfo> infoList = getCelebInfo(name);
		final Object temp[] = new Object[2];
		for (PersonalInfo ¢ : infoList) {
			temp[0] = ¢.getAttribute();
			temp[1] = ¢.getInfo();
			m.addRow(temp);
		}
	}

	public String getCelebInfoString(final String name) {
		String $ = "";
		List<PersonalInfo> infoList = getCelebInfo(name);
		for (PersonalInfo ¢ : infoList)
			$ += ¢.getAttribute() + ¢.getInfo() + "\n";
		return $.trim();
	}

	public List<PersonalInfo> getCelebInfo(final String name) {
		if (name == null)
			return null;
		if (celebsInfo.containsKey(name))
			return celebsInfo.get(name);
		List<PersonalInfo> $ = new LinkedList<>();
		Document doc;
		try {
			String celebName;
			switch (name) {
			case "Hayden":
				celebName = "Will Hayden";
				break;
			case "Judge Joe Brown":
				celebName = "Joe Brown (judge)";
				break;
			default:
				celebName = name;
			}
			doc = Jsoup.connect(getWikiURL(celebName)).timeout(3000).get();
		} catch (IOException e) {
			return new LinkedList<>($);
		}
		Element rawInfo, trimmedInfo;
		try {
			rawInfo = doc.select("[class*=info]").first();
			trimmedInfo = rawInfo.select("tbody").first();
			for (Element el : trimmedInfo.select("tr")) {
				Elements sons = el.children();
				if (sons.size() == 2) {
					if ("Hope Solo".equals(name) && !sons.get(0).select("span").isEmpty()
							|| "Tito Ortiz".equals(name) && sons.get(1).text().matches("^[0-9]+"))
						continue;
					$.add(new PersonalInfo((wrapString(sons.get(0).text()) + ": "),
							wrapString(sons.get(1).text().replaceAll("\\[[0-9]\\]", ""))));
				}

			}

		} catch (NullPointerException e) {
			return new LinkedList<>($);
		}
		celebsInfo.put(name, $);
		return new LinkedList<>($);
	}

	public static String wrapString(final String ¢) {
		return wrapString(¢, fieldMaxLength);
	}

	public static String wrapString(final String s, int lineLength) {
		if (s == null || s.length() < lineLength)
			return s;
		StringBuilder $ = new StringBuilder(s);
		for (int pos = $.indexOf(" ", lineLength); pos != -1 && pos < $.length();) {
			$.setCharAt(pos, '\n');
			++pos;
			for (; $.length() < pos; ++pos)
				if ($.charAt(pos) != ' ')
					break;
			pos = $.indexOf(" ", pos + lineLength);
		}
		return ($ + "").trim();
	}

	public String getWikiURL(final String name) {
		return name == null ? null : wikipediaURL + name.trim().replace(' ', '_');
	}

	public InfoExtractor() {
		celebsInfo = new LinkedHashMap<String, List<PersonalInfo>>();
	}
}