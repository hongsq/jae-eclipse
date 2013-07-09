/**
 * 
 */
package com.jae.eclipse.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Hongsq
 *
 */
public class XMLUtil {
	public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
	
	/**
	 * @param namespaceAware	是否解析名称空间
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document newDocument(boolean namespaceAware) throws ParserConfigurationException{
		DocumentBuilder documentBuilder = newDocumentBuilder(namespaceAware);
		return documentBuilder.newDocument();
	}

	/**
	 * @param namespaceAware	是否解析名称空间
	 * @return
	 * @throws ParserConfigurationException
	 */
	private static DocumentBuilder newDocumentBuilder(boolean namespaceAware) throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(namespaceAware);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder;
	}
	
	/**
	 * 给element添加名称空间
	 * @param element
	 * @param prefix
	 * @param uri
	 */
	public static void addNameSpace(Element element, String prefix, String uri) {
		if (prefix.length() > 0)
			element.setAttributeNS(XMLNS_URI, "xmlns:" + prefix, uri);
		else {
			element.setAttributeNS(XMLNS_URI, "xmlns", uri);
		}
	}
	
	public static Map<String,String> getNameSpaces(Element element){
		Map<String,String> map = new HashMap<String, String>();
		
		NamedNodeMap attrMap = element.getAttributes();
		int length = attrMap.getLength();
		for (int i = 0; i < length; i++) {
			Node node = attrMap.item(i);
			
			if(node.getNodeType() != Node.ATTRIBUTE_NODE)
				continue;
			
			Attr attr = (Attr) node;
			
			String namespace = attr.getNamespaceURI();
			
			if(null == namespace)
				continue;
			
			if(!namespace.trim().endsWith("/"))
				namespace = namespace + "/";
			
			if(XMLNS_URI.equalsIgnoreCase(namespace)){
				String prefix = attr.getLocalName();
				String uri = attr.getNodeValue();
				
				map.put(prefix, uri);
			}
			
		}
		
		return Collections.unmodifiableMap(map);
	}
	
	/**
	 * 保存Node
	 * @param node
	 * @param writer
	 * @param encoding
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	public static void storeNode(Node node, Writer writer,  String encoding) throws TransformerException, UnsupportedEncodingException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// 得到一个Transformer对象。
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		
		/*
		 * 将被变换的Document对象封装到一个DOMSource对象， 以 DOM树的形式充当转换 Source 树的持有者。
		 */
		DOMSource source = new DOMSource(node);
		/*
		 * 将变换得到XML文件对象封装到一个StreamResult对象。 充当转换结果的持有者， 可以为 XML、纯文本、HTML
		 * 或某些其他格式的标记。
		 */
		StreamResult result = new StreamResult(writer);
		// 实施变换。
		transformer.transform(source, result);
	}

	/**
	 * 加载XML
	 * @param input
	 * @param namespaceAware	是否解析名称空间
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static Document loadXML(InputStream input, boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder documentBuilder = newDocumentBuilder(namespaceAware);
		Document doc = documentBuilder.parse(input);
		return doc;
	}
	
	/**
	 * 获取目标名称空间
	 * @param document
	 * @return
	 */
	public static String getTargetNamespace(Document document){
		String tns = document.getDocumentElement().getAttributeNS(null, "targetNamespace");
		
		return tns;
	}
}
