package com.melonsail.app.melonfriends.daos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class XMLSQLParser {
	private static final String TAG = "XMLSQLParser";
	
	private static final String sXML_Filename = "MFQuery.xml";
	private static final String sXML_Script_XPath = "//Root/Statement/Details";
	private static final String sXML_NodeValue_ScriptID = "@scriptId";
	
	/**
	 * Retrieve SQL statement from XML
	 * @param context
	 * @param scriptID
	 * @param inputParams
	 * @return
	 */
	public static String fGetSQLScript(Context context, String scriptID) {
		String script = "";
		// load xml documents
		Document document = fGetDocument( context, sXML_Filename );
		
		// select the statement by ID
		@SuppressWarnings("unchecked")
		List<Node> nodes = document.selectNodes(sXML_Script_XPath);
		for (Node node : nodes) {
			String nodeValue = node.valueOf(sXML_NodeValue_ScriptID);
			if (scriptID.equals(nodeValue) ) {
				script = fGetNodeData(node);
				break;
			}
		}
		
		// replace input params
		// script = fParseParams2SQL(script, inputParams);
		
		// Log.i(TAG, script);
		return script;
	}
	
	/**
	 * Replace "?" in SQL script with actual input params
	 * @param script
	 * @param inputParams
	 * @return
	 */
	public static String fParseParams2SQL(String script, String[] inputParams) {
		for (int i=0; i < inputParams.length; i++) {
			script = script.replaceFirst("\\?", inputParams[i]);
		}
		return script;
	}
	
	/**
	 * Get XML document via filename
	 * @param context
	 * @param xmlFileName
	 * @return
	 */
	public static Document fGetDocument( Context context, final String xmlFileName ) {
		AssetManager assetManager = context.getAssets();
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			InputStream inputStream = assetManager.open(xmlFileName);			
			document = reader.read( inputStream );
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

// {{ Function Not In Use Yet
	/**
	 * Get attribute of a particular node
	 * @param node
	 * @return
	 */
	public static List<Attribute> fGetNodeAttributes( Node node ) {
		Element e = (Element) node;
		@SuppressWarnings("unchecked")
		List<Attribute> list = e.attributes();
		for (Attribute attribute : list) {
			String name = attribute.getName();
			Log.i(TAG, "Name : " + name );
		}
		return list;
	}
	
	/**
	 * Get data of particular node
	 * Including CDATA
	 * @param node
	 * @return
	 */
	public static String fGetNodeData(Node node) {
		Element e = (Element) node;
		String data = e.getData().toString();
		return data;
	}
	   
    /**
    * Create xml documents, not in use
    * @return
    */
	public static Document fbuildDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement( "ROOT" );
		 
		root.addElement( "AUTHOR" ).addAttribute( "name",
		    "Muthu" ).addAttribute( "location", "India" ).addText(
		    "Javawave.blogspot.com" );
		 
		root.addElement( "AUTHOR" ).addAttribute( "name",
		    "Krish" ).addAttribute( "location", "India" ).addText(
		    "Javabeat.net" );
		 
		return document;
	}

	/**
	 * Write XML to file
	 * @param document
	 * @param filename
	 * @throws IOException
	 */
	public static void fWrite( Document document, String filename ) throws IOException {
		// write to a file output.xml
		XMLWriter writer = new XMLWriter( new FileWriter( filename ) );
		writer.write( document );
		writer.close();
		 
		// Pretty print the document to System.out
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter( System.out, format );
		writer.write( document );
		 
		// Compact format to System.out
		format = OutputFormat.createCompactFormat();
		writer = new XMLWriter( System.out, format );
		writer.write( document );
	}

	/**
	* Convert a Document to a String
	*
	* @param document - Document object which has to be converted to a string object
	* @return
	*/
	public static String fConvertDocument2String( Document document ) {
		return document.asXML();
	}
	   
	/**
	* Convert a XML String to a Document object
	*
	* @param xml - XML string which has to be converted to a Document object
	* @return
	*/
	public static Document fConvertString2Document( String xml ){
		Document document = null;
		try {
			document = DocumentHelper.parseText( xml );
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
// }}
}
