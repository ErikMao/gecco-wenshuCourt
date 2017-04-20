package cn.com.faduit.crawler.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CrawlerLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "crawler_log")
public class CrawlerLog implements java.io.Serializable {
	public static final String StatusOfStart = "0";//开始
	public static final String StatusOfFinish = "1";//完成
	// Fields

	private String id;
	private Timestamp createTime;
	private String status;
	private Timestamp finishTime;

	// Constructors

	/** default constructor */
	public CrawlerLog() {
	}

	/** minimal constructor */
	public CrawlerLog(String id) {
		this.id = id;
	}

	/** full constructor */
	public CrawlerLog(String id, Timestamp createTime, String status,
			Timestamp finishTime) {
		this.id = id;
		this.createTime = createTime;
		this.status = status;
		this.finishTime = finishTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 14)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "create_time", length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "status", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "finish_time", length = 19)
	public Timestamp getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

}