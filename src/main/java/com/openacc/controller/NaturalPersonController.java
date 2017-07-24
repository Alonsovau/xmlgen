package com.openacc.controller;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openacc.vo.XmlElementVO;

@Controller
@EnableAutoConfiguration
public class NaturalPersonController {

	@RequestMapping(value = "/naturalPerson")
	public String person(Model model) {
		return "naturalPerson";
	}
		
	@RequestMapping(value = "/naturalPerson/preview", method = RequestMethod.POST)
	@ResponseBody
	public List<XmlElementVO> preview(@ModelAttribute XmlElementVO xmlVO) {
		return getXmlList(xmlVO);
	}

	private List<XmlElementVO> getXmlList(XmlElementVO xmlVO) {
		List<XmlElementVO> previewList = new ArrayList<XmlElementVO>();
		int clientNum = xmlVO.getClientnumInt();
		String idStr = xmlVO.getId_transformed();
		int idLen = xmlVO.getId_transformed().length();
		int processidLen = xmlVO.getProcessid().length();
		String processidStr = xmlVO.getProcessid();

		for (int i = 0; i < clientNum; i++) {
			XmlElementVO tmp = new XmlElementVO();
			// 这里实现+1的效果；
			tmp.setClassify(xmlVO.getClassify());
			tmp.setClientname(xmlVO.getClientname() + (i + 1));
			tmp.setClientnum(xmlVO.getClientnum());
			tmp.setClientregion(xmlVO.getClientregion());
			tmp.setClienttype(xmlVO.getClienttype());
			tmp.setExclientidtype(xmlVO.getExclientidtype());
			tmp.setExcompanyid(xmlVO.getExcompanyid());
			if (idLen > 8) {
				tmp.setId_transformed(idStr.substring(0, 8) + (Long.valueOf(idStr.substring(8, idLen)) + i));
			} else {
				tmp.setId_transformed(String.valueOf(xmlVO.getId_transformedInt() + i));
			}
			tmp.setIdtype(xmlVO.getIdtype());
			tmp.setNationality(xmlVO.getNationality());
			tmp.setOption_id(xmlVO.getOption_id());
			tmp.setProcessdate(xmlVO.getProcessdate());
			tmp.setProcessid(
					processidStr.substring(0, 14) + (Long.valueOf(processidStr.substring(14, processidLen)) + i));
			tmp.setProcesstype(xmlVO.getProcesstype());
			tmp.setSeqno(String.valueOf(xmlVO.getSeqnoInt() + i));
			previewList.add(tmp);
		}
		return previewList;
	}

	@RequestMapping(value = "/naturalPerson/download")
	public void download(@ModelAttribute XmlElementVO xmlVO, final HttpServletResponse response) {
		List<String> xmlList = createXML(xmlVO);

		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyyMMdd HHmmss");
		String currdate=format.format(date).substring(0,8);
		String fileNameStr = "00000001";
		
		try {
			if (xmlList == null) {
				response.getOutputStream().write("xml num error".getBytes());
			}

			if (xmlList != null && xmlList.size() == 1) {
				byte[] data;
				data = xmlList.get(0).getBytes("gbk");
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename=\"cfmmc_J_"+currdate+"_"+fileNameStr +".xml\"");
				response.addHeader("Content-Length", "" + data.length);
				response.setContentType("application/xml;charset=UTF-8");
				OutputStream out = new BufferedOutputStream(response.getOutputStream());
				out.write(data);
				out.flush();
				out.close();
			}

			if (xmlList != null && xmlList.size() > 1) {
				String zipName = "NaturalPerson.zip";
				response.setContentType("APPLICATION/OCTET-STREAM");
				response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
				ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
				try {
					for (int i = 0; i < xmlList.size(); i++) {
						String xml = xmlList.get(i);
						byte[] data = xml.getBytes("gbk");
						out.putNextEntry(new ZipEntry("cfmmc_J_"+currdate+"_"+String.format("%08d", Integer.valueOf(fileNameStr)+i) + ".xml"));
						out.write(data);
						out.flush();
						out.closeEntry();
						response.flushBuffer();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					out.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> createXML(XmlElementVO xmlVO) {
		String xml = "";
		List<String> xmlList = new ArrayList<String>();
		SAXReader reader = new SAXReader();
		try {

			// xmlnum 数字与clientnum 数字不符
			if (xmlVO.getXmlnumInt() > 1 && xmlVO.getXmlnumInt() != xmlVO.getClientnumInt()) {
				return null;
			}

			// 1个xml
			if (xmlVO.getXmlnumInt() == 1) {
				Document doc = reader.read(this.getClass().getClassLoader().getResourceAsStream("自然人.xml"));
				Element root = doc.getRootElement();
				Element demoPackage = (Element) root.element("package");
				// 先删除，留一个空的packagelist
				root.remove(demoPackage);

				// 获取 demo package 来生成新的package节点
				List<XmlElementVO> xmlVOList = getXmlList(xmlVO);
				for (int i = 0; i < xmlVOList.size(); i++) {
					XmlElementVO tmpVO = xmlVOList.get(i);
					// 克隆一个package
					Element newPackage = (Element) demoPackage.clone();
					resetPackageElement(newPackage, tmpVO, i);
					root.add(newPackage);
				}
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("gbk");
				StringWriter sw = new StringWriter();
				XMLWriter xMLWriter = new XMLWriter(sw, outputFormat);
				xMLWriter.write(doc);
				xMLWriter.close();
				xml = sw.toString();
				System.out.println(xml);
				xmlList.add(xml);
			} else {
				// 多个xml
				List<XmlElementVO> xmlVOList = getXmlList(xmlVO);
				for (int i = 0; i < xmlVOList.size(); i++) {
					XmlElementVO tmpVO = xmlVOList.get(i);
					Document doc = reader.read(this.getClass().getClassLoader().getResourceAsStream("自然人.xml"));
					Element root = doc.getRootElement();
					Element demoPackage = (Element) root.element("package");
					// 直接修改demoPackage就好了
					resetPackageElement(demoPackage, tmpVO, i);

					OutputFormat outputFormat = OutputFormat.createPrettyPrint();
					outputFormat.setEncoding("gbk");
					StringWriter sw = new StringWriter();
					XMLWriter xMLWriter = new XMLWriter(sw, outputFormat);
					xMLWriter.write(doc);
					xMLWriter.close();
					xml = sw.toString();
					System.out.println(xml);
					xmlList.add(xml);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlList;
	}

	private void resetPackageElement(Element newPackage, XmlElementVO tmpVO, int seqNo) {
		newPackage.attribute("seqno").setValue(seqNo + "");// 将其它类型的转为字符串
		Element process = newPackage.element("process");
		process.attribute("processid").setValue(tmpVO.getProcessid());
		process.attribute("processdate").setValue(tmpVO.getProcessdate());
		process.attribute("processtype").setValue(tmpVO.getProcesstype());

		Element personInfo = process.element("person_info");
		personInfo.attribute("clienttype").setValue(tmpVO.getClienttype());
		//
		personInfo.attribute("clientname").setValue(tmpVO.getClientname());
		personInfo.attribute("clientregion").setValue(tmpVO.getClientregion());
		personInfo.attribute("idtype").setValue(tmpVO.getIdtype());
		personInfo.attribute("nationality").setValue(tmpVO.getNationality());
		personInfo.attribute("id_transformed").setValue(tmpVO.getId_transformed());
		personInfo.attribute("excompanyid").setValue(tmpVO.getExcompanyid());
		personInfo.attribute("exclientidtype").setValue(tmpVO.getExclientidtype());
		personInfo.attribute("classify").setValue(tmpVO.getClassify());

		Element exchange_spec = personInfo.element("exchange_spec");
		Element question = exchange_spec.element("question");
		Element option = question.element("option");
		option.attribute("option_id").setValue(tmpVO.getOption_id());

	}

	// *生成xml
	// public String createXML(XmlElementVO xmlVO) {
	// String xml = "";
	// // 生成根节点，并为根节点添加属性和属性值
	// Element root = (Element)
	// DocumentHelper.createElement("package_list").addAttribute("version",
	// "1.3")
	// .addAttribute("from", "cfmmc").addAttribute("to", "J");
	//
	// Element childElement =
	// DocumentHelper.createElement("package").addAttribute("seqserial", "1")
	// .addAttribute("seqno", xmlVO.getSeqno());
	// // childElement.addText(xmlVO.getJd1());
	// root.add(childElement);
	//
	// Element childElement2 =
	// DocumentHelper.createElement("process").addAttribute("processid",
	// xmlVO.getProcessid())
	// .addAttribute("processstatus", "7").addAttribute("processdate",
	// xmlVO.getProcessdate())
	// .addAttribute("processtime", "17:07:34").addAttribute("processtype",
	// xmlVO.getProcesstype())
	// .addAttribute("businesstype", "1").addAttribute("cfmmcreturncode", "0")
	// .addAttribute("cfmmcreturnmsg",
	// "数据格式检查完成;客户身份验证通过;").addAttribute("exreturncode", "")
	// .addAttribute("exreturnmsg", "");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement.add(childElement2);
	//
	// Element childElement3 = DocumentHelper.createElement("person_info")
	// .addAttribute("clientregion", xmlVO.getClientregion())
	// .addAttribute("nationality",
	// xmlVO.getNationality()).addAttribute("clienttype", xmlVO.getClienttype())
	// .addAttribute("futuresid", "00000258").addAttribute("exchangeid",
	// "J").addAttribute("companyid", "2366")
	// .addAttribute("excompanyid", xmlVO.getExcompanyid())
	// .addAttribute("exclientidtype",
	// xmlVO.getExclientidtype()).addAttribute("exclientid", "")
	// .addAttribute("clientname", xmlVO.getClientname()).addAttribute("idtype",
	// xmlVO.getIdtype())
	// .addAttribute("id_original", "340101860324003")
	// .addAttribute("id_transformed",
	// xmlVO.getId_transformed()).addAttribute("classify", xmlVO.getClassify())
	// .addAttribute("appropriatype", "").addAttribute("birthday", "1986-04-15")
	// .addAttribute("opendate", "2015-11-15").addAttribute("gender", "1")
	// .addAttribute("compclientid",
	// "xxxxxx8888888").addAttribute("phone_countrycode", "86")
	// .addAttribute("phone_areacode", "021").addAttribute("phone_number",
	// "5896123400")
	// .addAttribute("addr_zipcode", "1111122222").addAttribute("addr_country",
	// "中国")
	// .addAttribute("addr_province", "156310000").addAttribute("addr_city",
	// "156310107")
	// .addAttribute("addr_address", "aa联系地址中的地址").addAttribute("order_name",
	// "指定下单人18")
	// .addAttribute("order_idtype", "1").addAttribute("order_id",
	// "511423198604150450")
	// .addAttribute("order_phone_countrycode",
	// "86").addAttribute("order_phone_areacode", "021")
	// .addAttribute("order_phone_number",
	// "789652100022").addAttribute("order_addr_zipcode", "0123456789")
	// .addAttribute("order_addr_country",
	// "中国").addAttribute("order_addr_province", "156310000")
	// .addAttribute("order_addr_city",
	// "156310107").addAttribute("order_addr_address", "指定下单人联系地址中的地址")
	// .addAttribute("fund_name", "资金调拨人18").addAttribute("fund_idtype", "9")
	// .addAttribute("fund_id",
	// "-fff-ff-f18").addAttribute("fund_phone_countrycode", "86")
	// .addAttribute("fund_phone_areacode",
	// "021").addAttribute("fund_phone_number", "12345678902222")
	// .addAttribute("fund_addr_zipcode",
	// "1111200000").addAttribute("fund_addr_country", "中国111")
	// .addAttribute("fund_addr_province",
	// "资金调拨人联系地址中的省").addAttribute("fund_addr_city", "资金调拨人联系地址中市")
	// .addAttribute("fund_addr_address", "资金调拨人联系地址").addAttribute("bill_name",
	// "结算单确认人18")
	// .addAttribute("bill_idtype", "6").addAttribute("bill_id",
	// "-ggg-g-gg-g18")
	// .addAttribute("bill_phone_countrycode",
	// "结算单确认").addAttribute("bill_phone_areacode", "结算单0022")
	// .addAttribute("bill_phone_number", "123456789055552222111878787877")
	// .addAttribute("bill_addr_zipcode",
	// "1000529999").addAttribute("bill_addr_country", "中国789500")
	// .addAttribute("bill_addr_province",
	// "结算单确认人联系地址中的省").addAttribute("bill_addr_city", "结算单确认人联系地址中的市")
	// .addAttribute("bill_addr_address",
	// "结算单确认人联系地址中的地址").addAttribute("bill_addr_province", "结算单确认人联系地址中的省")
	// .addAttribute("bill_addr_city", "结算单确认人联系地址中的市");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement2.add(childElement3);
	//
	// Element childElement4 = DocumentHelper.createElement("bankacc_list");
	// childElement3.add(childElement4);
	//
	// Element childElement5 =
	// DocumentHelper.createElement("bankacc").addAttribute("accountname",
	// "msmsms18")
	// .addAttribute("bankid", "01").addAttribute("accountno",
	// "银行账号18").addAttribute("branchname", "开户行网点18");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement4.add(childElement5);
	//
	// Element childElement6 = DocumentHelper.createElement("exchange_spec");
	// childElement3.add(childElement6);
	//
	// Element childElement7 =
	// DocumentHelper.createElement("question").addAttribute("question_id", "2")
	// .addAttribute("content", "客户所开户的营业部代码");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement6.add(childElement7);
	//
	// Element childElement8 =
	// DocumentHelper.createElement("option").addAttribute("option_id",
	// xmlVO.getOption_id())
	// .addAttribute("option_content", "总部").addAttribute("is_selected", "1")
	// .addAttribute("option_content", "总部");
	//
	// // childElement2.addText(xmlVO.getJd2());
	// childElement7.add(childElement8);
	//
	// Element childElement9 =
	// DocumentHelper.createElement("referencedoc_list");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement3.add(childElement9);
	//
	// Element childElement10 =
	// DocumentHelper.createElement("referencedoc").addAttribute("idtype", "50")
	// .addAttribute("docid", "QY0C9B3R3");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement9.add(childElement10);
	//
	// Element childElement11 =
	// DocumentHelper.createElement("referencedoc").addAttribute("idtype", "6")
	// .addAttribute("docid", "huzhao18");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement9.add(childElement11);
	//
	// Element childElement12 =
	// DocumentHelper.createElement("referencedoc").addAttribute("idtype", "9")
	// .addAttribute("docid", "people18");
	// // childElement2.addText(xmlVO.getJd2());
	// childElement9.add(childElement12);
	//
	// Document responseDoc = DocumentHelper.createDocument(root);
	// // String xml = responseDoc.asXML();
	// // System.out.println(xml);
	// // return xml;
	//
	// // String filename = "jinchun.xml";
	// OutputFormat outputFormat = OutputFormat.createPrettyPrint();
	// outputFormat.setEncoding("UTF-8");
	// StringWriter sw = new StringWriter();
	// XMLWriter xMLWriter = new XMLWriter(sw, outputFormat);
	// try {
	// xMLWriter.write(responseDoc);
	// xMLWriter.close();
	// xml = sw.toString();
	// System.out.println(xml);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return xml;
	// }

	/*	*//** * 解析XML */
	/*
	 * public void parseXML() {
	 * 
	 * try {
	 * 
	 * SAXReader sAXReader = new SAXReader();
	 * 
	 * // 要解析的xml文件 File file = new File("jinchun2.xml");
	 * 
	 * Document document = sAXReader.read(file);
	 * 
	 * // 获取根节点 Element acreen = document.getRootElement();
	 * 
	 * // 打印根节点属性 System.out.println(acreen.attributeValue("id"));
	 * System.out.println(acreen.attributeValue("name"));
	 * 
	 * // 获取子节点 List<Element> helloments = acreen.elements("子节点"); for (Element
	 * helloment : helloments) {
	 * 
	 * // 打印子节点属性 System.out.println(helloment.attributeValue("id"));
	 * System.out.println(helloment.getText());
	 * 
	 * } } catch (DocumentException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * } }
	 */

}