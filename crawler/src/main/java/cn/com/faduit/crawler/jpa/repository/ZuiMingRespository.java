/**
 * @(#)ZuiMingRespository.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.com.faduit.crawler.entity.ZuiMing;

/**
 * @作者:maozf
 * @文件名:ZuiMingRespository
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
public interface ZuiMingRespository extends JpaRepository<ZuiMing, String>,
JpaSpecificationExecutor<ZuiMing> {

}
