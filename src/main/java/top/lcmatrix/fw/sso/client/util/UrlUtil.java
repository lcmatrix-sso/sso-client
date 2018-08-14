package top.lcmatrix.fw.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlUtil {
	
	/**
	 * 能保证只encode一次的encode方法
	 * @param s
	 * @param charset
	 * @return
	 */
	public static String encodeJustOnce(String s, String charset) {
		String d = decode(s, charset);
		if(d.equals(s)){
			//没有encode过，需要encode
			return encode(s, charset);
		}else{
			return s;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String decode(String s, String charset){
		try {
			return URLDecoder.decode(s, charset);
		} catch (UnsupportedEncodingException e) {
			return URLDecoder.decode(s);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String encode(String s, String charset){
		try {
			return URLEncoder.encode(s, charset);
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(s);
		}
	}
	
	/**
	 * 拼接url的各个部分，处理多余的/
	 * @param partsOfUrl
	 * @return
	 */
	public static String concatUrl(String ... partsOfUrl){
		StringBuilder url = new StringBuilder(partsOfUrl[0].endsWith("/") ? 
				partsOfUrl[0].substring(0, partsOfUrl[0].length() - 1) : partsOfUrl[0]);
		for(int i = 1; i < partsOfUrl.length; i++){
			String s = partsOfUrl[i];
			url.append("/" + (s.startsWith("/") ? s.substring(1) : s));
		}
		return url.toString();
	}
}
