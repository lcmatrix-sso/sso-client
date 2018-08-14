package top.lcmatrix.fw.sso.client;

public class Constant {
	
	/**
	 * cookie在request header中的名字
	 */
	public static final String COOKIE_IN_HEADER = "cookie";

	/**
	 * token object在session中存储的key
	 */
	public static final String SESSION_TOKEN = "_SSO_SESSION_TOKEN";
	
	/**
	 * token object在context中存储的key前缀
	 */
	public static final String CONTEXT_ATTR_TOKEN_PREFIX = "_SSO_TOKEN__";
	
	/**
	 * sso-client的服务基地址在sso-server中存储的key前缀
	 */
	public static final String CONTEXT_ATTR_CLIENT_BASE_URLS = "_SSO_ATTR_CLIENT_BASE_URLS";
	
	/**
	 * 通过重定向url验证登录时，url附带的token参数名称
	 */
	public static final String PARAM_TOKEN = "_SSO_TOKEN";
	
	/**
	 * sso-server的http接口前缀
	 */
	public static final String SSO_SERVER_PATH = "SSOServer";
	
	/**
	 * sso-server端同步请求验证接口名称
	 */
	public static final String ENDPOINT_SERVER_SYNC_REQUEST_VALIDATE = "syncRequestValidate";
	
	/**
	 * sso-server端异步请求（ajax）请求验证接口名称
	 */
	public static final String ENDPOINT_SERVER_AJAX_REQUEST_VALIDATE = "ajaxRequestValidate";
	
	/**
	 * sso-server端登录页面接口名称
	 */
	public static final String ENDPOINT_SERVER_LOGIN_PAGE = "loginPage";
	
	/**
	 * sso-server端验证token合法性的接口名称
	 */
	public static final String ENDPOINT_SERVER_VALIDATETOKEN = "validateToken";
	
	/**
	 * sso-client端的http接口前缀
	 */
	public static final String SSO_CLIENT_PATH = "SSOClient";
	
	/**
	 * sso-client端注销接口名称
	 */
	public static final String ENDPOINT_CLIENT_LOGOUT = "logout";
	
	/**
	 * sso-client端代理url接口名称
	 */
	public static final String ENDPOINT_CLIENT_PROXY = "proxy";
}
