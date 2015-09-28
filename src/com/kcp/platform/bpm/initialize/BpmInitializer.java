package com.kcp.platform.bpm.initialize;

import org.activiti.engine.ProcessEngines;
import org.springframework.stereotype.Component;

import com.kcp.platform.sys.initialize.AbstractInitializer;

@Component
public class BpmInitializer extends AbstractInitializer {

	@Override
	public String getDesc() {
		return "工作流";
	}

	@Override
	public void init() {
		ProcessEngines.init();
	}

}
