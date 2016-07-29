/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esale.frontend.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vutt3
 */
public class SSOCookie {

	private static final String COOKIES_DOMAIN = ".zing.vn";
	private static final Logger log = LoggerFactory.getLogger(SSOCookie.class);
	private static final String format = "EEE, dd-MMM-yyyy HH:mm:ss zzz";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(format);
	
	public static Map<String, String> getAllCookie(HttpServletRequest req) {
		Map<String, String> rs = new HashMap<String, String>();
		Cookie[] cookies = req.getCookies();
		if (cookies == null || cookies.length == 0) {
			return rs;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			rs.put(cookie.getName(), cookie.getValue());
		}
		return rs;
	}
	public static Cookie getCookie(HttpServletRequest req, String cookieName) {
		Cookie[] cookies = req.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}


	
	public static void clearCookie(String name, String path, String domain, HttpServletRequest req, HttpServletResponse resp) {
		try {
			SSOCookie.setCookie(name, "deleted", 0, false, path, domain, req, resp);

		} catch (Exception e) {
		}
	}

	public static void clearCookie(String name, String path, HttpServletRequest req, HttpServletResponse resp) {
		clearCookie(name, path, COOKIES_DOMAIN, req, resp);
	}

	public static void clearCookie(String name, HttpServletRequest req, HttpServletResponse resp) {
		clearCookie(name, "/", COOKIES_DOMAIN, req, resp);
	}

	public static void setCookie2(String cookieName, String value, int expire, boolean httponly, String path, String domain, HttpServletRequest req, HttpServletResponse resp) {
		try {
			Cookie cookie = getCookie(req, cookieName);
			if (cookie == null) {
				cookie = new Cookie(cookieName, value);
			}
			cookie.setMaxAge(expire); // expire in second
			// 0: expire now
			// -1: expire after close browser
			cookie.setPath(path);
			cookie.setDomain(domain);
//            cookie.setVersion(1);
//            cookie.setHttpOnly(httponly);
			resp.setHeader("P3P", "CP=\"NOI ADM DEV PSAi COM NAV OUR OTRo STP IND DEM\"");
			resp.addCookie(cookie);
		} catch (Exception e) {
			log.error("Exception at setCookie " + e.getMessage(), e);
		}
	}

	public static void setCookie(String cookieName, String value, int expire, boolean httponly, String path, HttpServletRequest req, HttpServletResponse resp) {
		setCookie(cookieName, value, expire, httponly, path, COOKIES_DOMAIN, req, resp);
	}

	public static void setCookie(String cookieName, String value, int expire, boolean httponly, HttpServletRequest req, HttpServletResponse resp) {
		setCookie(cookieName, value, expire, httponly, "/", COOKIES_DOMAIN, req, resp);
	}

	public static void setCookie(String cookieName, String value, int expire, HttpServletRequest req, HttpServletResponse resp) {
		setCookie(cookieName, value, expire, false, "/", COOKIES_DOMAIN, req, resp);
	}

	public static void setCookie(String cookieName, String value, HttpServletRequest req, HttpServletResponse resp) {
		setCookie(cookieName, value, -1, false, "/", COOKIES_DOMAIN, req, resp); // default is end of session
	}

	public static void setCookie(String cookieName, String value, int expire, boolean httponly, String path, String domain, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String strExpire = "";
			Calendar cal = Calendar.getInstance();
			if (expire == 0) { // expire now
//                cal.add(Calendar.YEAR, -10);
//                String ex = formatter.format(cal.getTime());
				String ex = "Thu, 01 Jan 1970 00:00:01 GMT";
				strExpire = ";Expires=" + ex;
			} else if (expire > 0) {
				cal.add(Calendar.MILLISECOND, expire * 1000);
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
				String ex = formatter.format(cal.getTime());
				strExpire = ";Expires=" + ex;
			}
			// else expire < -1: expires after browser is closed.

			String strHttponly = "";
			if (httponly == true) {
				strHttponly = ";HttpOnly";
			}

			String headerValue = cookieName + "=" + value + ";Path=" + path + ";Domain=" + domain + strExpire + strHttponly;
			resp.setHeader("P3P", "CP=\"NOI ADM DEV PSAi COM NAV OUR OTRo STP IND DEM\"");
			resp.setHeader("Vary", "Accept-Encoding");
			resp.addHeader("Set-Cookie", headerValue);

		} catch (Exception e) {
		}
	}
	
	public static void removeCookie(HttpServletResponse resp,List<String> names){
		String removeHeader = "%s=deleted; Expires=Thu, 01-Jan-1970 00:00:01 GMT; Path=%s; Domain=%s; HttpOnly";
		for(String name : names){
			String header = String.format(removeHeader, name,"/",COOKIES_DOMAIN,true);
			resp.addHeader("Set-Cookie", header);
		}
	}
	public static void removeCookie(HttpServletResponse resp,String name,String Path,String domain,boolean httponly){
		String httpOnly = (httponly == true) ? "; HttpOnly" : "";
		String header = String.format("%s=deleted; Expires=Thu, 01-Jan-1970 00:00:01 GMT; Path=%s; Domain=%s%s ", name,Path,domain,httpOnly);
		resp.setHeader("Set-Cookie", header);
	}
	
}
