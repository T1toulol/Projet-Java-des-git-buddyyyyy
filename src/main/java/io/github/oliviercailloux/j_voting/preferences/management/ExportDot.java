package io.github.oliviercailloux.j_voting.preferences.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The goal of the ExportDOT class is to export Google Guava Graph in DOT Format
 * The DOT format is a simple file format which describes graphs. This type of file is used by graph visualization applications. 
 * For more details about DOT format: https://www.graphviz.org/doc/info/lang.html
 */


public class ExportDot {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportDot.class);
	private final static String INDENTATION = "  ";
	private final static String ENDLINE=";";
	private final static String HEADER_DIGRAPH="digraph G {";
	private final static String HEADER_GRAPH="graph G {";
	private final static String CONNECTOR_DIGRAPH=" -> ";
	private final static String CONNECTOR_GRAPH=" -- ";	
	private OutputStreamWriter writerExport;
	
	
	/**
	 * Calls the export method with the parameter lineSeparator.
	 * By default the end of line encoding is given by System.lineSeparator().
	 * 
	 * @param graph can't be null
	 * @param stream can't be null
	 * @throws IOException
	 */
	public static void export(Graph<String> graph, OutputStream stream) throws IOException {
		export(graph, stream, System.lineSeparator());
	}
	
	/**
	 * Exports the graph from the parameters, converts it to DOT format and writes (with UTF-8 character encoding)
	 * it to the stream from the parameters (which is given to the streamExport attribute of ExportDOT class).
	 * This choice is based on René Nyffenegger examples (https://renenyffenegger.ch/notes/tools/Graphviz/examples/index)
	 * referenced in the official website about DOT format (https://www.graphviz.org/resources/#simple-examples-and-tutorials).
	 * 
	 * You can chose the end of line encoding (CR / CRLF / LF format) by the string lineSeparator given.
	 * 
	 * @param graph can't be null
	 * @param stream can't be null
	 * @param lineSeparator can't be null
	 * @throws IOException
	 */
	public static void export(Graph<String> graph, OutputStream stream, String lineSeparator) throws IOException {
		checkNotNull(graph);
		checkNotNull(stream);
		checkNotNull(lineSeparator);
		checkFormatVertex(graph.nodes());
		if(!lineSeparator.equals("\n")&&!lineSeparator.equals("\r\n")&&!lineSeparator.equals("\r")) {
			throw new IllegalArgumentException("Line ends must be encoded in CR / CRLF / LF format.");
		}		
		
		ExportDot exportInstance=new ExportDot();
		OutputStreamWriter outWriter=new OutputStreamWriter(stream, StandardCharsets.UTF_8);
		exportInstance.writerExport=outWriter;
		
		final String header = graph.isDirected() ? HEADER_DIGRAPH : HEADER_GRAPH;
		final String connector = graph.isDirected() ? CONNECTOR_DIGRAPH : CONNECTOR_GRAPH;
		exportInstance.writeAndSeparateOnStreamHeader(header, lineSeparator);
		
		for(String node : graph.nodes()) {
			exportInstance.writeAndSeparateOnStream(node, lineSeparator);
		}
		for(EndpointPair<String> edge : graph.edges()) {
			exportInstance.writeAndSeparateOnStream(edge.nodeU() + connector + edge.nodeV(), lineSeparator);
		}
		exportInstance.writeOnStream("}");
		LOGGER.debug("export DOT - OutputStream {}", exportInstance.writerExport.toString());
	}
	
	/**
	 * This method allowed to write the string given to the output stream 
	 * in the streamExport attribute of the ExportDOT class.
	 * @param str
	 * @throws IOException
	 */
	
	private void writeOnStream(String str) throws IOException {
		writerExport.write(str);
		writerExport.flush();
	}
	
	/**
	 * This method allowed to write the string given to the output stream
	 * in the streamExport attribute of the ExportDOT class.
	 * We must to define the end of line encoding. An indentation and an end of line are added to match the DOT format,
	 * for writing vertices and arcs/edges.
	 * @param str
	 * @param lineSeparator
	 * @throws IOException
	 */
	
	private void writeAndSeparateOnStream(String str, String lineSeparator) throws IOException {
		String lineDot=INDENTATION+str+ENDLINE;
		writerExport.write(lineDot);
		writerExport.write(lineSeparator);
		writerExport.flush();
		
	}
	
	/**
	 * This method allowed to write the string given to the output stream
	 * in the streamExport attribute of the ExportDOT class.
	 * We must to define the end of line encoding. The header in DOT format have no indentation & end of line.
	 * @param headerDot
	 * @param lineSeparator
	 * @throws IOException
	 */
	
	private void writeAndSeparateOnStreamHeader(String headerDot, String lineSeparator) throws IOException {
		writerExport.write(headerDot);
		writerExport.write(lineSeparator);
		writerExport.flush();
	}
	
	
	/**
	 * Calls the convertToDot method only with the graph in parameter
	 * By default the end of line encoding is given by System.lineSeparator(). 
	 * @param a string graph
	 * @return the graph in DOT format
	 * @throws IOException
	 */
	
	public static String convertToDot(Graph<String> graph) throws IOException {
		return convertToDot(graph, System.lineSeparator());
	}

	/**
	 * Takes the graph and convert it to a DOT format String with the end of line encoding chosen (in CR / CRLF / LF format).
	 * For more informations about DOT format: <a href="http://www.graphviz.org/doc/info/lang.html">GraphViz website</a>.
	 * @param a string graph
	 * @return the graph in DOT format
	 * @throws IOException 
	 */
	public static String convertToDot(Graph<String> graph, String lineSeparator) throws IOException {
		LOGGER.debug("Convert to DOT :");
		checkNotNull(graph);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		export(graph, stream, lineSeparator);
		final String graphDotString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
		LOGGER.debug("DOT graph : {}", graphDotString);
		return graphDotString;
	}
	

	/**
	 * This method is greatly inspired from the method getVertexId of the DOTExporter<V,E> class.
	 * For more informations: <a href="https://jgrapht.org/javadoc-1.1.0/org/jgrapht/ext/DOTExporter.html">DOTExporter</a>.
	 * 
	 * Return if the nodes' ids respect the dot language described in
	 * http://www.graphviz.org/doc/info/lang.html Quoted from above mentioned
	 * source: An ID is valid if it meets one of the following criteria:
	 * 
	 * <ul>
	 * <li>any string of alphabetic characters, underscores or digits, not beginning
	 * with a digit;
	 * <li>a number [-]?(.[0-9]+ | [0-9]+(.[0-9]*)? );
	 * <li>any double-quoted string ("...") possibly containing escaped quotes (\");
	 * <li>an HTML string (<...>).
	 * </ul>
	 * 
	 * @param Set<String> myNodes
	 * @return if the ids are in the good format or not
	 */
	private static void checkFormatVertex(Set<String> myNodes) {
		for (String aNode : myNodes) {
			boolean isAlphaDig = aNode.matches("[a-zA-Z]+([\\w_]*)?");
			boolean isDoubleQuoted = aNode.matches("\".*\"");
			boolean isDotNumber = aNode.matches("[-]?([.][0-9]+|[0-9]+([.][0-9]*)?)");
			boolean isHtml = aNode.matches("<.*>");
			if (!isAlphaDig && !isDotNumber && !isDoubleQuoted && !isHtml) {
				throw new IllegalArgumentException("The name of atleast one vertex can't be converted in DOT format : " + aNode);
			}
		}
	}
	
	
	/**
	 * This enum class gives all lines separators (CRLF, CR, LF) that can be accepted in DOT format.
	 */
	public enum LineSeparator{
		CRLF("\r\n"),CR("\r"),LF("\n");
		private String value;
		
		LineSeparator(String lineSeparator){
			this.value=lineSeparator;
		}
		
		public String getValue() {
			return this.value;
		}
	}
}
