/**
 * @(#)ZuiMing.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @作者:maozf
 * @文件名:ZuiMing
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Entity
@Table(name = "zui_ming")
public class ZuiMing implements Serializable {

	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = -3681533307873859115L;
	/**
	 * 顶级类别
	 */
	public static final String PARENT_ROOT = "000000";
	
	private String bh;
	private String mc;
	private String parentBh;
	/**
	 * @return the bh
	 */
	@Id
	@Column(name = "bh", unique = true, nullable = false, length = 6)
	public String getBh() {
		return bh;
	}
	/**
	 * @param bh the bh to set
	 */
	public void setBh(String bh) {
		this.bh = bh;
	}
	/**
	 * @return the mc
	 */
	@Column(name = "mc", length = 50)
	public String getMc() {
		return mc;
	}
	/**
	 * @param mc the mc to set
	 */
	public void setMc(String mc) {
		this.mc = mc;
	}
	/**
	 * @return the parentBh
	 */
	@Column(name = "parent_bh", length = 2)
	public String getParentBh() {
		return parentBh;
	}
	/**
	 * @param parentBh the parentBh to set
	 */
	public void setParentBh(String parentBh) {
		this.parentBh = parentBh;
	}
	
	
	
}
