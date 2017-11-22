/**
 * 
 */
package org.Recollect.Core.deserialize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * @author amartinez
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class JaxbUnmarshal {

	private static Logger logger = LogManager.getLogger(JaxbUnmarshal.class);
	
	private Object data;
		
	public JaxbUnmarshal(File file, Class[] classType) {
		logger.debug(String.format("read file %s", file));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
			Unmarshaller u = jc.createUnmarshaller();
			
			data = ((JAXBElement<Object>) u.unmarshal(file)).getValue();			
		} catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal file %s", e));
		}	   
	}
	
	public JaxbUnmarshal(InputStream inputStream, Class[] classType) {
		try {
		    JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
		    
		    data = ((JAXBElement<Object>) u.unmarshal(inputStream)).getValue();		    
		} catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal InputStream %s", e));
		}
	}
	
	
	public JaxbUnmarshal(URL url, Class[] classType) {
		logger.debug(String.format("read url %s", url));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
		    
		    data = ((JAXBElement<Object>) u.unmarshal(url)).getValue();
		} catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal URL %s", e));
		}
	}
	
	public JaxbUnmarshal(StringBuffer stringbuffer, Class[] classType) {
		logger.debug(String.format("read StringBuffer %s", stringbuffer));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();
		    
		    data = ((JAXBElement<Object>) u.unmarshal(new StreamSource(new StringReader(stringbuffer.toString())))).getValue();
		} catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal StringBuffer %s", e));
		}	
	}
	
	public JaxbUnmarshal(Node node, Class<?> classType) throws IOException {
		logger.debug(String.format("read Node %s", node));
		try {			
		    Source xmlSource = new DOMSource(node);
		    Unmarshaller unmarshaller = JAXBContext.newInstance(classType).createUnmarshaller();
		    
		    data =  ((JAXBElement<Object>) unmarshaller.unmarshal(xmlSource, classType)).getValue();			
		}catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal Node %s", e));
		}			
	}
	
	@SuppressWarnings("deprecation")
	public JaxbUnmarshal(SAXSource saxSource, Class[] classType) {
		logger.debug(String.format("read SAXSource %s", saxSource));
		try {			
			// configure a validating SAX2.0 parser (Xerces2)
			final String JAXP_SCHEMA_LANGUAGE =
	           "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
			final String JAXP_SCHEMA_LOCATION =
	           "http://java.sun.com/xml/jaxp/properties/schemaSource";
			final String W3C_XML_SCHEMA =
	           "http://www.w3.org/2001/XMLSchema";

			System.setProperty( "javax.xml.parsers.SAXParserFactory",
	                           "org.apache.xerces.jaxp.SAXParserFactoryImpl" );

			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(true);
			SAXParser saxParser = spf.newSAXParser();

			try {
				saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
				saxParser.setProperty(JAXP_SCHEMA_LOCATION, "http://....");
			} catch (SAXNotRecognizedException x) {
	           // exception handling omitted
			}

//			XMLReader xmlReader = saxParser.getXMLReader();
//			SAXSource source =
//					new SAXSource( xmlReader, new InputSource( "http://..." ) );

			// Setup JAXB to unmarshal
			JAXBContext jc = JAXBContext.newInstance(classType);
			Unmarshaller u = jc.createUnmarshaller();
			ValidationEventCollector vec = new ValidationEventCollector();
			u.setEventHandler( vec );

			// turn off the JAXB provider's default validation mechanism to
			// avoid duplicate validation
			u.setValidating( false );

			// unmarshal
			data = ((JAXBElement<Object>) u.unmarshal(saxSource)).getValue();
			
			// check for events
			if( vec.hasEvents() ) {
				// iterate over events
			}
		}catch (JAXBException | ParserConfigurationException | SAXException e) {
			logger.error(String.format("JaxbUnmarshal SAXSource %s", e));
		}		
	}
	
	
	public JaxbUnmarshal(XMLStreamReader xmlStreamReader, Class[] classType) {
		logger.debug(String.format("read XMLStreamReader %s", xmlStreamReader));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();

		    data = u.unmarshal( xmlStreamReader );
		} catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal XMLStreamReader %s", e));
		}		
	}

	public JaxbUnmarshal(XMLEventReader xmlEventReader, Class[] classType) {
		logger.debug(String.format("read XMLEventReader %s", xmlEventReader));
		try {
			JAXBContext jc = JAXBContext.newInstance(classType);
		    Unmarshaller u = jc.createUnmarshaller();

		    data = ((JAXBElement<Object>) u.unmarshal(xmlEventReader)).getValue();
		}catch (JAXBException e) {
			logger.error(String.format("JaxbUnmarshal XMLEventReader %s", e));
		}			
	}
	
	public Object getObject() {
		return data;
	}
	
}
