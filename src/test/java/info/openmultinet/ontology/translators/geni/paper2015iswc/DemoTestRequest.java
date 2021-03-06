package info.openmultinet.ontology.translators.geni.paper2015iswc;

import info.openmultinet.ontology.Parser;
import info.openmultinet.ontology.exceptions.InvalidModelException;
import info.openmultinet.ontology.translators.geni.RequestConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

public class DemoTestRequest {

	private RequestConverter converter;
	private Parser parser;

	@Before
	public void setup() throws InvalidModelException, JAXBException {
		this.parser = new Parser();
		this.converter = new RequestConverter();
	}

	@Test
	public void testLoginRoundtrip() throws JAXBException,
			InvalidModelException, IOException, XMLStreamException {

		System.out.println("================================================");
		System.out.println("Reading Graph");
		System.out.println("================================================");
		parser.read("/omn/paper2015iswc/omn_2_request.ttl");
		final Model model = parser.getModel();
		System.out.println(Parser.toString(model));

		System.out.println("================================================");
		System.out.println("Converting to RSpec");
		System.out.println("================================================");
		long start;
		start = System.nanoTime();
		final String rspec = RequestConverter.getRSpec(model);
		System.out.println(rspec);
		Assert.assertTrue("should be a request",
				rspec.contains("type=\"request\""));
		System.out.println("Duration: " + (System.nanoTime() - start));

		System.out.println("================================================");
		System.out.println("Converting back to Graph");
		System.out.println("================================================");
		start = System.nanoTime();
		final InputStream inputRspec = new ByteArrayInputStream(
				rspec.getBytes());
		final Model newModel = RequestConverter.getModel(inputRspec);
		System.out.println(Parser.toString(newModel));
		System.out.println("Duration: " + (System.nanoTime() - start));
	}

}
