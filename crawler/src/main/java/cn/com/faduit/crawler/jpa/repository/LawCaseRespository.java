/**
 * @(#)LawCaseRespository.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-7
 * 描　述：创建
 */

package cn.com.faduit.crawler.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.com.faduit.crawler.entity.LawCase;

/**
 * @作者:maozf
 * @文件名:LawCaseRespository
 * @版本号:1.0
 * @创建日期:2016-7-7
 * @描述:
 */
public interface LawCaseRespository extends JpaRepository<LawCase, String>,
		JpaSpecificationExecutor<LawCase> {
	
	public List<LawCase> findByCrawlerTaskId(String crawlerTaskId);
	@Query("select distinct(l.crawlerTaskId) from LawCase l")
	public List<String> findDistinctCrawlerTaskIdList();
}
