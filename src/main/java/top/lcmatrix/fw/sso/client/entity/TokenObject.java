package top.lcmatrix.fw.sso.client.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TokenObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6153111785631087452L;
	
	public TokenObject(String token, Object user, String cookie) {
		super();
		this.token = token;
		this.user = user;
		this.cookie = cookie;
	}

	private String token;
	private Object user;
	/**
	 * 用于保存sso-client的cookie，用于ajax请求续期
	 */
	private String cookie;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Object getUser() {
		return user;
	}
	@SuppressWarnings("unchecked")
	public <T> T getUser(Class<T> clazz) {
		if(user == null){
			return null;
		}
		if(user.getClass().equals(clazz)){
			return (T) user;
		}
		if(user instanceof JSONObject){
			return JSON.parseObject(((JSONObject)user).toJSONString(), clazz);
		}
		throw new ClassCastException("class " + user.getClass().getName() + " can not be cast to " + clazz.getName());
	}
	public void setUser(Object user) {
		this.user = user;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
