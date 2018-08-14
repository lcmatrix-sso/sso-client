package top.lcmatrix.fw.sso.client.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
	
	private static OkHttpClient globalHttpClient = new OkHttpClient();
	
	public static Callback CALLBACK_NOTHING = new Callback() {
		@Override
		public void onResponse(Call call, Response response) throws IOException {
		}
		
		@Override
		public void onFailure(Call call, IOException e) {
		}
	};
	
	public static OkHttpClient globalOkHttpClient() {
		return globalHttpClient;
	}
	
	public static Response get(String url, Map<String, String> params) throws IOException{
		return globalHttpClient.newCall(OkHttpUtil
				.buildGetRequest(url, params)).execute();
	}
	
	public static void getAsync(String url, Map<String, String> params, Callback callback) throws IOException{
		globalHttpClient.newCall(OkHttpUtil
				.buildGetRequest(url, params)).enqueue(callback == null ? CALLBACK_NOTHING : callback);
	}
	
	public static Response post(String url, Map<String, String> params) throws IOException{
		return globalHttpClient.newCall(OkHttpUtil
				.buildPostRequest(url, params)).execute();
	}
	
	public static void postAsync(String url, Map<String, String> params, Callback callback) throws IOException{
		globalHttpClient.newCall(OkHttpUtil
				.buildPostRequest(url, params)).enqueue(callback == null ? CALLBACK_NOTHING : callback);
	}

	public static RequestBody buildFormBody(Map<String, String> params) {
		FormBody.Builder formBuilder = new FormBody.Builder();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				formBuilder.addEncoded(entry.getKey(), entry.getValue());
			}
		}
		return formBuilder.build();
	}

	public static Request buildPostRequest(String url, Map<String, String> params) {
		return new Request.Builder().url(url).post(buildFormBody(params)).build();
	}

	public static Request buildGetRequest(String url) {
		return new Request.Builder().url(url).get().build();
	}
	
	public static Request buildGetRequest(String url, Map<String, String> params) {
		return new Request.Builder().url(buildGetUrl(url, params)).get().build();
	}

	public static String buildGetUrl(String originUrl, Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			return originUrl;
		}
		StringBuilder paramStr = new StringBuilder("");
		for (Entry<String, String> entry : params.entrySet()) {
			paramStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		String sParam = paramStr.substring(1);
		return originUrl + "?" + sParam;
	}
}
