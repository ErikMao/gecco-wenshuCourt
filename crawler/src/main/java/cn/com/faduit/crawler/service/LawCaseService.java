/**
 * @(#)LawCaseService.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月17日
 * 描　述：创建
 */

package cn.com.faduit.crawler.service;

import java.util.List;

import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.entity.LegalBase;

/**
 * @作者:maozf
 * @文件名:LawCaseService
 * @版本号:1.0
 * @创建日期:2016年6月17日
 * @描述:
 */

public interface LawCaseService {
	/**
	 * 添加案件信息
	 * @更新时间:2016年6月17日
	 * @更新作者:maozf
	 * @param lawCase
	 */
	public void addLawCase(LawCase lawCase);
	/**
	 * 添加法律依据，并更新案由
	 * @更新时间:2016年6月17日
	 * @更新作者:maozf
	 * @param legalBase
	 * @param lawCase caseId and caseAction are required
	 */
	public void updateLegalBaseAndCaseAction(List<LegalBase> legalBase,LawCase lawCase);
}
