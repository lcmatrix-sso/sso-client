package top.lcmatrix.fw.sso.client.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import top.lcmatrix.fw.sso.client.entity.TokenObject;

/**
 * 允许监听的一些事件
 * @author chris
 *
 */
public interface SSOEventListener {

	/**
	 * 当sso-client从sso-server验证登录成功后触发
	 * @param tokenObject
	 * @param request
	 * @param response
	 */
	public void onValidateSuccess(TokenObject tokenObject, HttpServletRequest request, HttpServletResponse response);
	
}
