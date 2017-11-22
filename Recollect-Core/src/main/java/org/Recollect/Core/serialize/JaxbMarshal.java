/**
 * 
 */
package org.Recollect.Core.serialize;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

/**
 * @author amartinez
 *
 */
public class JaxbMarshal {

	
	private static Logger logger = LogManager.getLogger(JaxbMarshal.class);
	
	private Marshaller marshaller;
	private JAXBElement<?> jaxbElement;
	
	
	public JaxbMarshal(JAXBElement<?> jaxbElement, Class<?> classType) {
		try {			
			JAXBContext oaidcContext = JAXBContext.newInstance(classType);
			this.marshaller = oaidcContext.createMarshaller();
			this.jaxbElement = jaxbElement;
		} catch (Exception e) {
			logger.error(e);
		}
		
	}
	
	
	public void marshaller(OutputStream stream) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(jaxbElement, stream);
	}
	
	public void marshaller(OutputStream stream, Charset charset, boolean formatted, boolean fragment) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, fragment);
		marshaller.marshal(jaxbElement, stream);
	}
	
	public void marshaller(Writer writer) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(jaxbElement, writer);
	}
	
	public void marshaller(Node node) throws JAXBException {
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(jaxbElement, node);
	}
}
