package cn.com.faduit.crawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LegalBase entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "legal_base")
public class LegalBase implements java.io.Serializable {

	// Fields

	private String id;
	private String caseId;
	private String lawFile;
	private String lawFileItem;
	private String lawFileItemContent;

	// Constructors

	/** default constructor */
	public LegalBase() {
	}

	/** minimal constructor */
	public LegalBase(String id) {
		this.id = id;
	}

	/** full constructor */
	public LegalBase(String id, String caseId, String lawFile,
			String lawFileItem, String lawFileItemContent) {
		this.id = id;
		this.caseId = caseId;
		this.lawFile = lawFile;
		this.lawFileItem = lawFileItem;
		this.lawFileItemContent = lawFileItemContent;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "case_id", length = 50)
	public String getCaseId() {
		return this.caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@Column(name = "law_file", length = 200)
	public String getLawFile() {
		return this.lawFile;
	}

	public void setLawFile(String lawFile) {
		this.lawFile = lawFile;
	}

	@Column(name = "law_file_item", length = 200)
	public String getLawFileItem() {
		return this.lawFileItem;
	}

	public void setLawFileItem(String lawFileItem) {
		this.lawFileItem = lawFileItem;
	}

	@Column(name = "law_file_item_content", length = 65535)
	public String getLawFileItemContent() {
		return this.lawFileItemContent;
	}

	public void setLawFileItemContent(String lawFileItemContent) {
		this.lawFileItemContent = lawFileItemContent;
	}

}