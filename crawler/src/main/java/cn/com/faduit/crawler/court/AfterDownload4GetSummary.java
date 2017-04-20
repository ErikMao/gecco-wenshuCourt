/**
 * @(#)AfterDownload4GetSummary.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月17日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.entity.LegalBase;
import cn.com.faduit.crawler.service.LawCaseService;
import cn.com.faduit.crawler.web.core.util.ApplicationContextUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.annotation.GeccoClass;
import com.geccocrawler.gecco.downloader.AfterDownload;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;

/**
 * @作者:maozf
 * @文件名:AfterDownload4GetSummary
 * @版本号:1.0
 * @创建日期:2016年6月17日
 * @描述:
 */
@GeccoClass(GetSummary.class)
public class AfterDownload4GetSummary implements AfterDownload {
	private static final Logger logger = Logger.getLogger(AfterDownload4GetSummary.class);
	/* (non-Javadoc)
	 * @see com.geccocrawler.gecco.downloader.AfterDownload#process(com.geccocrawler.gecco.request.HttpRequest, com.geccocrawler.gecco.response.HttpResponse)
	 */
	@Override
	public void process(HttpRequest request, HttpResponse response) {
		// "{RelateInfo:[{ name: \"审理法院\", key: \"court\", value: \"最高人民法院\" },{ name: \"案件类型\", key: \"caseType\", value: \"刑事案件\" },{ name: \"案由\", key: \"reason\", value: \"\" },{ name: \"审理程序\", key: \"trialRound\", value: \"再审\" },{ name: \"裁判日期\", key: \"trialDate\", value: \"2014-12-29\" },],LegalBase:[{法规名称:\u0027《中华人民共和国刑事诉讼法（2012年）》\u0027,Items:[{法条名称:\u0027第二百四十二条\u0027,法条内容:\u0027    第二百四十二条　当事人及其法定代理人、近亲属的申诉符合下列情形之一的，人民法院应当重新审判：[ly]    （一）有新的证据证明原判决、裁定认定的事实确有错误，可能影响定罪量刑的；[ly]    （二）据以定罪量刑的证据不确实、不充分、依法应当予以排除，或者证明案件事实的主要证据之间存在矛盾的；[ly]    （三）原判决、裁定适用法律确有错误的；[ly]    （四）违反法律规定的诉讼程序，可能影响公正审判的；[ly]    （五）审判人员在审理该案件的时候，有贪污受贿，徇私舞弊，枉法裁判行为的。[ly]\u0027},{法条名称:\u0027第二百四十三条第一款\u0027,法条内容:\u0027    第二百四十三条各级人民法院院长对本院已经发生法律效力的判决和裁定，如果发现在认定事实上或者在适用法律上确有错误，必须提交审判委员会处理。[ly]    最高人民法院对各级人民法院已经发生法律效力的判决和裁定，上级人民法院对下级人民法院已经发生法律效力的判决和裁定，如果发现确有错误，有权提审或者指令下级人民法院再审。[ly]    最高人民检察院对各级人民法院已经发生法律效力的判决和裁定，上级人民检察院对下级人民法院已经发生法律效力的判决和裁定，如果发现确有错误，有权按照审判监督程序向同级人民法院提出抗诉。[ly]    人民检察院抗诉的案件，接受抗诉的人民法院应当组成合议庭重新审理，对于原判决事实不清楚或者证据不足的，可以指令下级人民法院再审。[ly]\u0027},{法条名称:\u0027第二百四十四条\u0027,法条内容:\u0027    第二百四十四条　上级人民法院指令下级人民法院再审的，应当指令原审人民法院以外的下级人民法院审理；由原审人民法院审理更为适宜的，也可以指令原审人民法院审理。[ly]\u0027}]}]}"
		String content = response.getContent();
		if(content== null || content.contains("remind")){
			try {
				logger.error("remind sleep 30s");
				Thread.sleep(1000*60*30L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//logger.info(content);
		//String c1 = content.substring(1, content.length()-1);//.replaceAll("\\\"", "\"");
		String s1 = JsonUtils.clean(content);
		//logger.info(c1);
		/*String s1 = c1.replaceAll("u0027", "\"")
				.replaceAll("([{|,|:])\\s*\\\\\"", "$1\"")
				.replaceAll("\\\\\"\\s*([}|,|:])", "\"$1");*/
				//.replaceAll("([^\\\\]?)(\\\\\\\")", "$1\"");
		//System.out.println(s1);
		JSONObject relateInfoJson = null;
		try{
			relateInfoJson = JSONObject.parseObject(s1);
		}catch(JSONException e){
			logger.error(content);
			logger.error(s1);
		}
		if(relateInfoJson!=null ){
			JSONArray relateInfoJr = relateInfoJson.getJSONArray("RelateInfo");
			JSONArray legalBaseJr = relateInfoJson.getJSONArray("LegalBase");
		
			LawCaseService lawCaseService = ApplicationContextUtil.getContext().getBean("lawCaseService", LawCaseService.class);
			String docId = ((HttpPostRequest)request).getFields().get("docId").toString();
			//System.out.println("docId:"+docId);
			//System.out.println("url:"+request.getUrl());
			List<LegalBase> lbList = new ArrayList<LegalBase>();
			for(int i=0;i<legalBaseJr.size();i++){				
				JSONObject jo = legalBaseJr.getJSONObject(i);
				String lawFile = jo.getString("法规名称");
				JSONArray itemJr = jo.getJSONArray("Items");
				for(int j=0;j<itemJr.size();j++){
					LegalBase lb = new LegalBase();
					JSONObject itemJo = itemJr.getJSONObject(j);
					lb.setCaseId(docId);
					lb.setLawFile(lawFile);
					String lawFileItem = itemJo.getString("法条名称");
					lb.setLawFileItem(lawFileItem);
					lbList.add(lb);
					String lawFileItemContent = itemJo.getString("法条内容");
					lb.setLawFileItemContent(lawFileItemContent);
				}
			}
			logger.info(JSON.toJSONString(lbList));
			LawCase lawCase = new LawCase();
			lawCase.setCaseId(docId);
			for(int i=0;i<relateInfoJr.size();i++){
				JSONObject jo = relateInfoJr.getJSONObject(i);
				if("当事人".equals(jo.getString("name"))){
					lawCase.setCaseDsr(jo.getString("value"));				
				}else if("案由".equals(jo.getString("name"))){
					lawCase.setCauseAction(jo.getString("value"));
				}
			}
			logger.info(JSON.toJSONString(lawCase));
			try{
				lawCaseService.updateLegalBaseAndCaseAction(lbList, lawCase);				
			}catch(Exception ex){
				logger.error(ex);
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		String data = FileUtils.readFileToString(new File("./c.json"));
		data = data.substring(1,data.length()-1)
				.replaceAll("u0027", "\"")
				.replaceAll("([^\\\\]?)(\\\\\\\")", "$1\"");
		
		System.out.println(data.contains("\\u0027"));
		
		//String data = "{RelateInfo:[{ name: \"审理法院\", key: \"court\", value: \"最高人民法院\" },{ name: \"案件类型\", key: \"caseType\", value: \"刑事案件\" },{ name: \"案由\", key: \"reason\", value: \"\" },{ name: \"审理程序\", key: \"trialRound\", value: \"再审\" },{ name: \"裁判日期\", key: \"trialDate\", value: \"2014-12-29\" },],LegalBase:[{法规名称:\u0027《中华人民共和国刑事诉讼法（2012年）》\u0027,Items:[{法条名称:\u0027第二百四十二条\u0027,法条内容:\u0027    第二百四十二条　当事人及其法定代理人、近亲属的申诉符合下列情形之一的，人民法院应当重新审判：[ly]    （一）有新的证据证明原判决、裁定认定的事实确有错误，可能影响定罪量刑的；[ly]    （二）据以定罪量刑的证据不确实、不充分、依法应当予以排除，或者证明案件事实的主要证据之间存在矛盾的；[ly]    （三）原判决、裁定适用法律确有错误的；[ly]    （四）违反法律规定的诉讼程序，可能影响公正审判的；[ly]    （五）审判人员在审理该案件的时候，有贪污受贿，徇私舞弊，枉法裁判行为的。[ly]\u0027},{法条名称:\u0027第二百四十三条第一款\u0027,法条内容:\u0027    第二百四十三条各级人民法院院长对本院已经发生法律效力的判决和裁定，如果发现在认定事实上或者在适用法律上确有错误，必须提交审判委员会处理。[ly]    最高人民法院对各级人民法院已经发生法律效力的判决和裁定，上级人民法院对下级人民法院已经发生法律效力的判决和裁定，如果发现确有错误，有权提审或者指令下级人民法院再审。[ly]    最高人民检察院对各级人民法院已经发生法律效力的判决和裁定，上级人民检察院对下级人民法院已经发生法律效力的判决和裁定，如果发现确有错误，有权按照审判监督程序向同级人民法院提出抗诉。[ly]    人民检察院抗诉的案件，接受抗诉的人民法院应当组成合议庭重新审理，对于原判决事实不清楚或者证据不足的，可以指令下级人民法院再审。[ly]\u0027},{法条名称:\u0027第二百四十四条\u0027,法条内容:\u0027    第二百四十四条　上级人民法院指令下级人民法院再审的，应当指令原审人民法院以外的下级人民法院审理；由原审人民法院审理更为适宜的，也可以指令原审人民法院审理。[ly]\u0027}]}]}";
		System.out.println(data.replaceAll("\\\u0027", "\""));
		JSONObject obj = JSONObject.parseObject(data);
		System.out.println(obj.getJSONArray("RelateInfo").size());
		JSONArray relateInfoJr = obj.getJSONArray("RelateInfo");
		JSONArray legalBaseJr = obj.getJSONArray("LegalBase");
		if(relateInfoJr!=null && relateInfoJr.size() > 0){
			
			List<LegalBase> lbList = new ArrayList<LegalBase>();
			for(int i=0;i<legalBaseJr.size();i++){				
				JSONObject jo = legalBaseJr.getJSONObject(i);
				String lawFile = jo.getString("法规名称");
				JSONArray itemJr = jo.getJSONArray("Items");
				for(int j=0;j<itemJr.size();j++){
					LegalBase lb = new LegalBase();
					//lb.setCaseId(docId);
					lb.setLawFile(lawFile);
					String lawFileItem = itemJr.getJSONObject(j).getString("法条名称");
					lb.setLawFileItem(lawFileItem);
					lbList.add(lb);
				}
			}
			System.out.println(JSON.toJSONString(lbList));
		}
		//.replaceAll("\\\u0027", "\"")
		//String ret = (String)new javax.script.ScriptEngineManager().getEngineByName("js").eval("(\"" + data + "\")");
		//System.out.println(ret);
		//System.out.println(retObj.);
	}

}
