package com.boco.deploy.service;

import java.util.List;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;

public interface DeployService {

	/**
	 * 获取安装包列表，支持正则过滤
	 * @param exp 正则过滤条件, null 返回全部
	 * @return
	 */
	public List<PackageData> getAllPackage(String exp);
	
	/**
	 * 获取安装变量默认值
	 * @param packageId
	 * @return
	 */
	public VariableData getVariable(String packageId);
	
	/**
	 * 设置安装变量默认值
	 * @param variableData
	 * @return
	 */
	public boolean setVariable(String packageId,VariableData variableData);
	
	/**
	 * 向主机部署安装包
	 * @param packageId
	 * @param hostData
	 * @param variableData 安装变量,不传用默认,仅生效一次
	 * @return loggerId日志文件名称
	 * @throws Exception 
	 */
	public String installPackage(String packageId,List<HostData> hostDatas,VariableData variableData) throws Exception;
	
	/**
	 * 卸载指定主机上的安装包
	 * @param packageId
	 * @param hostData
	 * @param variableData 安装变量,不传用默认,仅生效一次
	 * @return loggerId日志文件名称
	 * @throws Exception 
	 */
	public String uninstallPackage(String packageId,List<HostData> hostDatas,VariableData variableData) throws Exception;
	
	/**
	 * 
	 * @param logId 日志文件id
	 * @param startIndex 开始行数 0:从头读取,-1：从末尾读取,其他:从指定行开始读取
	 * @param lineNum  行数
	 * @return
	 */
	public InstallLogData getInstallLog(String logId,int startIndex,int lineNum);
}
