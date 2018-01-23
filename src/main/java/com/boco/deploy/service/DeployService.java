package com.boco.deploy.service;

import java.util.List;

import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.InstanceData;
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
	 * 向主机部署实例
	 * @param instanceDatas 安装实例对象集合
	 * @return loggerId日志文件名称集合
	 * @throws Exception 
	 */
	public List<String> installPackage(List<InstanceData> instanceDatas) throws Exception;
	
	/**
	 * 卸载主机上的实例
	 * @param instanceDatas 安装实例对象集合
	 * @return loggerId日志文件名称集合
	 * @throws Exception 
	 */
	public List<String> uninstallPackage(List<InstanceData> instanceDatas) throws Exception;

	/**
	 * 
	 * @param logId 日志文件id
	 * @param startIndex 开始行数 0:从头读取,-1：从末尾读取,其他:从指定行开始读取
	 * @param lineNum  行数
	 * @return
	 */
	public InstallLogData getInstallLog(String logId,int startIndex,int lineNum);
}
