package com.kcp.platform.common.log.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kcp.platform.core.web.BaseMultiActionController;
import com.kcp.platform.core.exception.BusinessException;
import com.kcp.platform.core.page.Page;
import com.kcp.platform.core.page.PageContextHolder;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.sys.security.context.SecurityContext;
import com.kcp.platform.ui.model.GridData;
import com.kcp.platform.util.StringUtils;
import com.kcp.platform.common.log.entity.Gdpz;
import com.kcp.platform.common.log.service.GdpzService;
import com.kcp.platform.common.user.entity.User;

@Controller
@RequestMapping("gdpz")
public class GdpzController extends BaseMultiActionController{
	@Autowired
	private GdpzService gdpzService;

	/**
	 * 跳转至主编辑页面
	 */
	@RequestMapping("/toMain")
	public ModelAndView toMain(HttpServletRequest request){
		return new ModelAndView("common/log/gdpzMain");
	}
	
	/**
	 * Grid数据源
	 */
	@RequestMapping("gdpzList")
	public @ResponseBody GridData gdpzList(HttpServletRequest request){
	  	String gdmc = StringUtils.getNullBlankStr(request.getParameter("gdmc"));
	  	String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
	  	String sjclm = StringUtils.getNullBlankStr(request.getParameter("sjclm"));
		Map<String,Object> queryMap = new HashMap<String,Object>();
	 	queryMap.put("gdmc", gdmc);
	 	queryMap.put("gdbmc", gdbmc);
	 	queryMap.put("sjclm", sjclm);
		List<Gdpz> result = gdpzService.findGdpzPaging(queryMap);
		Page page = PageContextHolder.getPage();
		return new GridData(result, page.getPage(), page.getTotalPages(), page.getTotalItems());
	}
	
	/**
	 * 跳转至新增页面
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(HttpServletRequest request){
		return new ModelAndView("common/log/gdpzEdit")
					.addObject("type","add");
	}
	
	/**
	 * 新增操作
	 */
	@RequestMapping("addGdpz")
	public @ResponseBody void addGdpz(HttpServletRequest request){
		Gdpz gdpz = assembleGdpz(request);
		gdpzService.addGdpz(gdpz);
	}
	
	/**
	 * 跳转至修改页面
	 */
	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		Gdpz gdpz = gdpzService.getGdpzById(id);
		if(gdpz==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/gdpzEdit")
					.addObject("gdpz",gdpz)
					.addObject("type","update");
	}
	
	/**
	 * 修改操作
	 */
	@RequestMapping("updateGdpz")
	public @ResponseBody void updateGdpz(HttpServletRequest request){
		Gdpz gdpz = assembleGdpz(request);
		gdpzService.updateGdpz(gdpz);
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("deleteGdpz")
	public @ResponseBody void deleteGdpz(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				gdpzService.deleteGdpz(id);
			}
		}
	}

	/**
	 * 启用操作
	 */
	@RequestMapping("enabledGdpz")
	public @ResponseBody void enabledGdpz(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				Gdpz gdpz = gdpzService.getGdpzById(id);
				gdpz.setSybz(CommonConst.ENABLE_FLAG);
				gdpzService.updateGdpz(gdpz);
			}
		}
	}

	/**
	 * 禁用操作
	 */
	@RequestMapping("forbidGdpz")
	public @ResponseBody void forbidGdpz(HttpServletRequest request){
		String ids = StringUtils.getNullBlankStr(request.getParameter("ids"));
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr = ids.split(",");
			for(String id:idArr){
				Gdpz gdpz = gdpzService.getGdpzById(id);
				gdpz.setSybz(CommonConst.DISABLE_FLAG);
				gdpzService.updateGdpz(gdpz);
			}
		}
	}
	
	/**
	 * 跳转至查看页面
	 */
	@RequestMapping("toView")
	public ModelAndView toView(HttpServletRequest request){
		String id = StringUtils.getNullBlankStr(request.getParameter("id"));
		Gdpz gdpz = gdpzService.getGdpzById(id);
		if(gdpz==null){
			throw new BusinessException("选择的记录不存在！");
		}
		return new ModelAndView("common/log/gdpzView")
					.addObject("gdpz",gdpz);
	}
	
	@RequestMapping("validateGdbmc")
	public @ResponseBody Object[] validateGdbmc(HttpServletRequest request){
		String zjid = StringUtils.getNullBlankStr(request.getParameter("zjid"));
		String fieldId = StringUtils.getNullBlankStr(request.getParameter("fieldId"));
		String gdbmc = StringUtils.getNullBlankStr(request.getParameter("fieldValue"));
		gdbmc = gdbmc.toUpperCase();
		Gdpz gdpz = new Gdpz();
		gdpz.setZjid(zjid);
		gdpz.setGdbmc(gdbmc);
		int count = gdpzService.countGdpz(gdpz);
		Object[] result = new Object[2];
		result[0] = fieldId;
		if(count>0){
			result[1] = false;
		}else{
			result[1] = true;
		}
		return result;
	}
	
	/**
	 * 组装页面传递过来的参数
	 */
	private Gdpz assembleGdpz(HttpServletRequest request){
		String type = StringUtils.getNullBlankStr(request.getParameter("type"));
		User user = SecurityContext.getCurrentUser();
		Gdpz gdpz = new Gdpz();
		if(CommonConst.ADD_OPERMODEL.equalsIgnoreCase(type)){
			gdpz.setJlcjsj(new Date());
			gdpz.setJlcjyh(user.getZjid());
			gdpz.setJlgxsj(new Date());
			gdpz.setJlgxyh(user.getZjid());
		}else if(CommonConst.UPDATE_OPERMODEL.equalsIgnoreCase(type)){
			String zjid = StringUtils.getNullBlankStr(request.getParameter("zjid"));
			gdpz = gdpzService.getGdpzById(zjid);
			gdpz.setJlgxsj(new Date());
			gdpz.setJlgxyh(user.getZjid());
		}	
		String gdmc = StringUtils.getNullBlankStr(request.getParameter("gdmc"));
		String gdbmc = StringUtils.getNullBlankStr(request.getParameter("gdbmc"));
		String sjclm = StringUtils.getNullBlankStr(request.getParameter("sjclm"));
		String sjclSjlx = StringUtils.getNullBlankStr(request.getParameter("sjclSjlx"));
		String sjclGs = StringUtils.getNullBlankStr(request.getParameter("sjclGs"));
		int gdzq = Integer.parseInt(request.getParameter("gdzq"));
		String gdzqDw = StringUtils.getNullBlankStr(request.getParameter("gdzqDw"));
		int ycsj = Integer.parseInt(request.getParameter("ycsj"));
		String zxsj = StringUtils.getNullBlankStr(request.getParameter("zxsj"));
		String sybz = StringUtils.getNullBlankStr(request.getParameter("sybz"));
		String bz = StringUtils.getNullBlankStr(request.getParameter("bz"));
		if("1".equals(sjclSjlx)){
			sjclGs = "";
		}
		gdpz.setGdmc(gdmc);
		gdpz.setGdbmc(gdbmc);
		gdpz.setSjclm(sjclm);
		gdpz.setSjclSjlx(sjclSjlx);	
		gdpz.setSjclGs(sjclGs);
		gdpz.setGdzq(gdzq);
		gdpz.setGdzqDw(gdzqDw);
		gdpz.setYcsj(ycsj);
		gdpz.setZxsj(zxsj);
		gdpz.setSybz(sybz);
		gdpz.setBz(bz);
		return gdpz;
	}
}