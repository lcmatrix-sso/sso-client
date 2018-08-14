package top.lcmatrix.fw.sso.client.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.lcmatrix.fw.sso.client.Constant;
import top.lcmatrix.fw.sso.client.setting.SSOSetting;
import top.lcmatrix.fw.sso.client.util.UrlUtil;

@RequestMapping(Constant.SSO_CLIENT_PATH)
@Controller
public class SSOClientController {
	
	@Autowired
	private SSOSetting ssoSetting;

	/**
	 * 用于代理前端无法被拦截的静态页面，使之可以被拦截器处理
	 * @param url
	 * @return
	 */
	@GetMapping(Constant.ENDPOINT_CLIENT_PROXY)
	public String proxy(String url) {
		if(url != null){
			url = UrlUtil.decode(url, ssoSetting.getUrlEncoding());
		}
		return "redirect:" + url;
	}
	
	/**
	 * 单点注销接口，供sso-server调用
	 * @param token
	 * @param request
	 */
	@RequestMapping(Constant.ENDPOINT_CLIENT_LOGOUT)
	@ResponseBody
	public void logout(String token, HttpServletRequest request){
		HttpSession session = (HttpSession) request.getServletContext().getAttribute(Constant.CONTEXT_ATTR_TOKEN_PREFIX + token);
		if(session != null){
			session.invalidate();
		}
	}
	
}
