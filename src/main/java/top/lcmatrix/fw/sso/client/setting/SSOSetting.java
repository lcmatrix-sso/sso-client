package top.lcmatrix.fw.sso.client.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="top.lcmatrix.fw.sso")
public class SSOSetting {

	private String urlEncoding = "UTF-8";
	
	private String myBaseUrl;
	
	private String serverBaseUrl;

	public String getMyBaseUrl() {
		return myBaseUrl;
	}

	public void setMyBaseUrl(String myBaseUrl) {
		this.myBaseUrl = myBaseUrl;
	}

	public String getServerBaseUrl() {
		return serverBaseUrl;
	}

	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	public String getUrlEncoding() {
		return urlEncoding;
	}

	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

}
