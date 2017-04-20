/**
 * @(#)LawCaseServiceImpl.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月17日
 * 描　述：创建
 */

package cn.com.faduit.crawler.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.faduit.crawler.jpa.repository.LawCaseRespository;
import cn.com.faduit.crawler.jpa.repository.LegalBaseRespository;

import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.entity.LegalBase;
import cn.com.faduit.crawler.service.LawCaseService;
import cn.com.faduit.crawler.utils.MD5Util;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者:maozf
 * @文件名:LawCaseServiceImpl
 * @版本号:1.0
 * @创建日期:2016年6月17日
 * @描述:
 */
@Service("lawCaseService")
public class LawCaseServiceImpl implements LawCaseService {
	private static final Logger logger = Logger.getLogger(LawCaseServiceImpl.class);
	
	@Autowired
	private LawCaseRespository lawCaseRespository;
	
	@Autowired
	private LegalBaseRespository legalBaseRespository;
	
	/* (non-Javadoc)
	 * @see com.sunrise.crawlerdemo.service.LawCaseService#addLawCase(com.sunrise.crawlerdemo.model.LawCase)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addLawCase(LawCase lawCase) {
		lawCaseRespository.save(lawCase);
	}

	/* (non-Javadoc)
	 * @see com.sunrise.crawlerdemo.service.LawCaseService#updateLegalBaseAndCaseAction(java.util.List, com.sunrise.crawlerdemo.model.LawCase)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLegalBaseAndCaseAction(List<LegalBase> legalBases, LawCase lawCase) {
		List<String> ids = new ArrayList<String>();
		//去重入库
		for(LegalBase legalBase:legalBases){
			String id = MD5Util.MD5(legalBase.getCaseId()+legalBase.getLawFile()+legalBase.getLawFileItem());
			legalBase.setId(id);			
			if(ids.contains(id)){
				continue;
			}
			try{
				legalBaseRespository.save(legalBase);
				ids.add(id);
			}catch(DuplicateKeyException ex){
				logger.warn("记录已存在:"+JSONObject.toJSONString(legalBase));
			}
		}
		if(StringUtils.isNotBlank(lawCase.getCauseAction())){
			LawCase lc = lawCaseRespository.getOne(lawCase.getCaseId());
			lc.setCauseAction(lawCase.getCauseAction());
			lc.setCaseDsr(lawCase.getCaseDsr());
			lawCaseRespository.save(lc);
			//lawCaseMapper.updateCaseAction(lawCase);
		}		
	}

}
