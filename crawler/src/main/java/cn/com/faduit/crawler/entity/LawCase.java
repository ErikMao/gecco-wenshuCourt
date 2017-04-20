package cn.com.faduit.crawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LawCase entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "law_case")
public class LawCase implements java.io.Serializable {

	// Fields

	private String caseId;
	private String title;
	private String content;
	private String pubDate;
	private String court;
	private String caseAh;
	private String caseType;
	private String causeAction;
	private String caseDate;
	private String caseDsr;
	private String caseSlcx;
	private String createTime;
	private String crawlerUrl;
	private String crawlerTaskId;

	// Constructors

	/** default constructor */
	public LawCase() {
	}

	/** minimal constructor */
	public LawCase(String caseId) {
		this.caseId = caseId;
	}

	/** full constructor */
	public LawCase(String caseId, String title, String content, String pubDate,
			String court, String caseAh, String caseType, String causeAction,
			String caseDate, String caseDsr, String caseSlcx,
			String createTime, String crawlerUrl, String crawlerTaskId) {
		this.caseId = caseId;
		this.title = title;
		this.content = content;
		this.pubDate = pubDate;
		this.court = court;
		this.caseAh = caseAh;
		this.caseType = caseType;
		this.causeAction = causeAction;
		this.caseDate = caseDate;
		this.caseDsr = caseDsr;
		this.caseSlcx = caseSlcx;
		this.createTime = createTime;
		this.crawlerUrl = crawlerUrl;
		this.crawlerTaskId = crawlerTaskId;
	}

	// Property accessors
	@Id
	@Column(name = "case_id", unique = true, nullable = false, length = 50)
	public String getCaseId() {
		return this.caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@Column(name = "title", length = 200)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "pub_date", length = 8)
	public String getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	@Column(name = "court", length = 200)
	public String getCourt() {
		return this.court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	@Column(name = "case_ah")
	public String getCaseAh() {
		return this.caseAh;
	}

	public void setCaseAh(String caseAh) {
		this.caseAh = caseAh;
	}

	@Column(name = "case_type", length = 100)
	public String getCaseType() {
		return this.caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	@Column(name = "cause_action", length = 250)
	public String getCauseAction() {
		return this.causeAction;
	}

	public void setCauseAction(String causeAction) {
		this.causeAction = causeAction;
	}

	@Column(name = "case_date", length = 8)
	public String getCaseDate() {
		return this.caseDate;
	}

	public void setCaseDate(String caseDate) {
		this.caseDate = caseDate;
	}

	@Column(name = "case_dsr", length = 250)
	public String getCaseDsr() {
		return this.caseDsr;
	}

	public void setCaseDsr(String caseDsr) {
		this.caseDsr = caseDsr;
	}

	@Column(name = "case_slcx", length = 100)
	public String getCaseSlcx() {
		return this.caseSlcx;
	}

	public void setCaseSlcx(String caseSlcx) {
		this.caseSlcx = caseSlcx;
	}

	@Column(name = "create_time", length = 14)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "crawler_url", length = 250)
	public String getCrawlerUrl() {
		return this.crawlerUrl;
	}

	public void setCrawlerUrl(String crawlerUrl) {
		this.crawlerUrl = crawlerUrl;
	}

	@Column(name = "crawler_task_id")
	public String getCrawlerTaskId() {
		return this.crawlerTaskId;
	}

	public void setCrawlerTaskId(String crawlerTaskId) {
		this.crawlerTaskId = crawlerTaskId;
	}

}