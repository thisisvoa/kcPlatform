package com.kcp.platform.common.authorization.action;

import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.kcp.platform.common.authorization.menutree.MenuTreeUtil;
import com.kcp.platform.common.authorization.service.AuthoriztationService;
import com.kcp.platform.common.menu.entity.Menu;
import com.kcp.platform.common.org.entity.Org;
import com.kcp.platform.common.user.entity.User;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.sys.security.exception.LoginExceptionResolver;
import com.kcp.platform.util.CipherUtil;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.web.listener.OnLineListener;
import com.kcp.platform.web.listener.OnlineStore;
import com.jit.util.GACertParser;

/**
 *
 *<pre>类描述：</pre><br>
 *<pre>
 *</pre> 
 */
@Controller
public class LoginController extends BaseMultiActionController{
	
	@Autowired
	private AuthoriztationService authoriztationService;
	
	/**
	 * 跳转至登录页
	 * @param request
	 * @return
	 */
	@RequestMapping("tologin.html")
	public ModelAndView tologin(HttpServletRequest request, HttpServletResponse response, Model model){
		log.debug("来自IP[" + request.getRemoteHost() + "]的访问");
		String cs = request.getParameter("cs");
		request.setAttribute("cs", cs);
		String isCas = (String)request.getAttribute("isCas");
		if("1".equals(isCas)){
			String username = (String)request.getAttribute("username");
			String encodePassword = (String)request.getAttribute("encodePassword");
			return login(username, encodePassword, request, response, model, "isPkiLogin");
		}
		return new ModelAndView("framework/login");
	}
	
	
	@RequestMapping("toleft.html")
	public String toleft(HttpServletRequest request, Model model){
		User user = SecurityContext.getCurrentUser();
		//获取菜单
		List<Menu> menuList = authoriztationService.getAssignedMenuList(user);
		String menuHTML = MenuTreeUtil.bulidMenuHTML(menuList,request.getContextPath());
		model.addAttribute("menuHTML",menuHTML);
		return "framework/left";
	}
	
	@RequestMapping("toindex.html")
	public String toindex(HttpServletRequest request){
		return "framework/index";
	}
	
	@RequestMapping("index.html")
	public String index(HttpServletRequest request){
		return "framework/index";
	}
	
	@RequestMapping("/unauthorized")
	public String unauthorized(HttpServletRequest request){
		return "framework/login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		// 销毁会话相关对象
		Subject currentUser = SecurityContext.getSubject();
		currentUser.logout();
		request.getSession().removeAttribute("loginUser");
		request.getSession().invalidate();
		return "framework/login";
	}
	
	@RequestMapping("login.html")
	public ModelAndView login(@RequestParam(required=false,value="username")String username, 
			@RequestParam(required=false,value="password")String password, HttpServletRequest request, HttpServletResponse response, Model model, String isFlag) {
		// 登录鉴证
		CipherUtil cu = new CipherUtil();
		UsernamePasswordToken token = null;
		//PKI登陆取出的密码已经加密，不需要在加密.
		if("isPkiLogin".equals(isFlag)){
			token = new UsernamePasswordToken(username, password);
		}else{
			token = new UsernamePasswordToken(username, cu.generatePassword(password));
		}
		String xtgly = request.getParameter("xtgly");
		String isCas = request.getParameter("isCas");
		if(StringUtils.isEmpty(isCas)){
			isCas = (String)request.getAttribute("isCas");
		}
		if(isCas != null && "1".equals(isCas)){
			token.setRememberMe(true);
		}
		model.addAttribute("isCas", isCas);
		model.addAttribute("xtgly", xtgly);
		String ip = request.getRemoteHost();
		String sessionId = request.getSession().getId();
		SecurityContext.setCurrentRemoteIp(ip);
		SecurityContext.setSessionId(sessionId);
		try {
			Subject currentUser = SecurityContext.getSubject();
			currentUser.login(token);
			User user = SecurityContext.getCurrentUser();
			request.getSession().setAttribute("loginUser", user);
			String requestURL = request.getParameter("service");
			model.addAttribute("requestURL", requestURL);
			//跳转至境外人员查询界面
			//this.getServletContext().getRequestDispatcher("/jwry/jwryxxMain.html").forward(request, response);
			//return null;
			//return new ModelAndView(new RedirectView("toMain.html"));
			return new ModelAndView("framework/nav");
		} catch (Exception e) {
			BusinessException ex = LoginExceptionResolver.resolveException(e);
			model.addAttribute("username", username);
			model.addAttribute("login_fail_msg",ex.getMessage());
		}
		return new ModelAndView("framework/login");
	}
	
	/**
	 * pki登录
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pkilogin")
	public ModelAndView pkiLogin(HttpServletRequest request,Model model,HttpServletResponse response) {
		try {
			X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
			if (certs == null) {
				model.addAttribute("login_fail_msg","错误！请提交证书！");
				return new ModelAndView("framework/login");
			}
			X509Certificate gaX509Cert = null;
			gaX509Cert = certs[0];
			GACertParser gcp = new GACertParser();
			gcp.setCert(gaX509Cert);
			// 姓名，身份证号码，组织机构代码
			String[] cards = gcp.getParseredDN().split(",");
			//String[] cards = new String[]{"省厅","520123182111225565","520000000000"};
			System.out.println("姓名：" + cards[0] + "身份证号码：" + cards[1] + "组织机构代码：" + cards[2]);
			System.out.println("警种:" + gcp.getPoliceType() + ",职级编码:" + gcp.getDutyLevel() + ",正副职:" 
					                   + gcp.getCharge() + "岗位编码:" + gcp.getStation());
			/**
			 *@author huangyp 后台输出信息如下： 姓名：夏军
			 *         身份证号码：522101197412312033组织机构代码：520000230300
			 *         警种:23,职级编码:0191,正副职:0002岗位编码:1604
			 *         提取pki中的信息到此结束，请与表中用户信息关联，以下代码可以先不用
			 */
			if(StringUtils.isEmpty(cards[1])){
				model.addAttribute("login_fail_msg","错误！未找到有效的身份证号，请联系管理员！");
				return new ModelAndView("framework/login");
			}
			User user = authoriztationService.getUserBySfzh(cards[1].toUpperCase());
			if (user == null) {
				model.addAttribute("login_fail_msg","错误！没有注册，请联系管理员！");
				return new ModelAndView("framework/login");
			} else {
				String username = user.getYhdlzh();
				String password = user.getYhdlmm();
				return this.login(username, password, request, response, model, "isPkiLogin");
				//return "framework/nav";
			}
		} catch (Exception e) {
			System.out.println("错误！" + e.getMessage());// －－此处即为获取并显示统一提示信息的方法。
		}
		return new ModelAndView("framework/login");
	}
	
	@RequestMapping("toMain.html")
	public String toMain(HttpServletRequest request, Model model){
		User user = SecurityContext.getCurrentUser();
		//获取菜单
		List<Menu> menuList = authoriztationService.getAssignedMenuList(user);
		String menuHTML = MenuTreeUtil.bulidMenuHTML(menuList,request.getContextPath());
		model.addAttribute("menuHTML",menuHTML);
		
		//取得用户所在的机构名称
		Org org = authoriztationService.getOrgByUser(user);
		request.setAttribute("ssdwmc", org.getDwmc().replace("贵州省", ""));
		request.setAttribute("loginTotalCount", OnlineStore.getInstance().getHisOnline());
		request.setAttribute("onlineCount", OnlineStore.getInstance().getOnlineCount());
		String isCas = (String)request.getParameter("isCas");
		model.addAttribute("isCas", isCas);
		return "framework/main";
	}
	
	@RequestMapping("toAssignMain")
	public String toAssignMain(HttpServletRequest request){
		return "common/authorization/assignMain";
	}
}
