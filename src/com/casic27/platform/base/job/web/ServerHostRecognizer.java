package com.casic27.platform.base.job.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.casic27.platform.base.job.JobSchedulerManager;
import com.casic27.platform.base.job.core.JobSchedulerRecognizer;
import com.casic27.platform.base.job.service.JobSchedulerService;
import com.casic27.platform.core.web.BaseMultiActionController;


/**
 */
@Controller
public class ServerHostRecognizer extends BaseMultiActionController {

    @Autowired
    private JobSchedulerService jobSchedulerService;

    /**
     * 受理{@link com.casic27.platform.base.job.core.JobSchedulerRecognizer}发送的任务调度服务器识别的请求，解析出
     * 本任务调度服务器的地址（IP和端口）.
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("job/serverHostRecoginzer")
    public void handleDefault(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (!JobSchedulerManager.isMounted()) {
            String info = "任务调度识别请求接收控制器接收到请求...";
            logger.info(info);
            String ip = request.getLocalAddr();
            int port = request.getLocalPort();
            String baseRoot = request.getContextPath();

            String hostUrl = "http://" + ip + ":" + port + baseRoot;
            String hostUrlAlias = null;
            if (port == 80) {
                hostUrlAlias = "http://" + ip + baseRoot;
            }

            jobSchedulerService.mountJobScheduler(hostUrl,hostUrlAlias);
            info = "识别完成.本任务调度服务的地址是：" + hostUrl;
            logger.info(info);
        }
        response.getWriter().write(JobSchedulerRecognizer.RESPONSE_WORD);
    }
}
