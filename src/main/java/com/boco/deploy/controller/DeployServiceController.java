package com.boco.deploy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;
import com.boco.deploy.service.DeployService;

@RestController
public class DeployServiceController {
	
	@Autowired
	private DeployService deployService;
	
    @RequestMapping(value="/package",method=RequestMethod.GET)
	public List<PackageData> getAllPackage(@RequestParam String exp){
    	return deployService.getAllPackage(exp);
    }

    @RequestMapping(value="/variable",method=RequestMethod.GET)
	public VariableData getVariable(@RequestParam String packageId){
    	return deployService.getVariable(packageId);
    }
	
    @RequestMapping(value="/variable",method=RequestMethod.POST)
	public boolean setVariable(@RequestBody VariableData variableData){
    	return deployService.setVariable(variableData.getPackageId(), variableData);
    }
	
    @RequestMapping(value="/install",method=RequestMethod.POST)
	public String installPackage(@RequestBody InstallPackageRequest request){
		return deployService.installPackage(request.getPackageId(), request.getHostDatas(), request.getVariableData());
	}
	
    @RequestMapping(value="/uninstall",method=RequestMethod.POST)
	public String uninstallPackage(@RequestBody InstallPackageRequest request){
		return deployService.uninstallPackage(request.getPackageId(), request.getHostDatas(), request.getVariableData());	
	}
	
    @RequestMapping(value="/log",method=RequestMethod.GET)
	public InstallLogData getInstallLog(@RequestParam String logId,@RequestParam int startIndex,@RequestParam int lineNum){
		return deployService.getInstallLog(logId, startIndex, lineNum);
	}

}
