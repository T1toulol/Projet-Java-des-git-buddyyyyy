package io.github.oliviercailloux.j_voting.preferences.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;

/**
 * 
 * @author chat-
 *
 */


public class ExportDOT {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ExportDOT.class);
	
	/**
	 * Calls the export method with the parameter endLine null.
	 * 
	 * @param graph can't be null
	 * @param stream can't be null
	 * @throws IOException
	 */
	public static void export(Graph<String> graph, OutputStream stream) throws IOException {
		export(graph, stream, null);
	}
	
	/**
	 * Exports the graph from the parameters, converts it to DOT format and writes
	 * it to the stream from the parameters.
	 * If the parameter endLine is null, it will take the value ";" by default.
	 * 
	 * @param graph can't be null
	 * @param stream can't be null
	 * @param endLine can be null
	 * @throws IOException
	 */
	public static void export(Graph<String> graph, OutputStream stream, String endLine) throws IOException {
		if (graph == null) {
			throw new IllegalStateException("The graph can't be null.");
		}
		if (!checkFormatVertex(graph.nodes())) {
			throw new IllegalStateException("The name of atleast one vertex can't be converted in DOT format.");
		}
		if (endLine == null) {
			endLine = ";"; 
		}
		String connector = "";
		String header = "";
		String indentation = "  ";
		// Inspired from
		// https://github.com/oliviercailloux/jmcda-utils/blob/master/src/main/java/org/decision_deck/utils/relation/graph/mess/DOTExporterTemp.java
		if (graph.isDirected()) {
			connector = " -> ";
			header = "digraph G {";
		} else {
			connector = " -- ";
			header = "graph G {";
		}
		// End of inspiration
		writeAndSeparateOnStream(header, stream);
		
		for(String node : graph.nodes()) {
			writeAndSeparateOnStream(indentation + node + endLine, stream);
		}
		for(EndpointPair<String> edge : graph.edges()) {
			writeAndSeparateOnStream(indentation + edge.nodeU() + connector + edge.nodeV() + endLine, stream);
		}
		
		writeOnStream("}", stream);
		logger.debug("export DOT - OutputStream {}", stream.toString());
		
		
	}
	private static void writeOnStream(String str, OutputStream stream) throws IOException {
		stream.write(str.getBytes(StandardCharsets.UTF_8));
	}
	private static void writeAndSeparateOnStream(String str, OutputStream stream) throws IOException {
		stream.write(str.getBytes(StandardCharsets.UTF_8));
		stream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Take the graph and convert it to a DOT format String.
	 * For more informations about DOT format : http://www.graphviz.org/doc/info/lang.html
	 * @param graph can't be null
	 * @return the graph in DOT format
	 * @throws IOException 
	 */
	public static String convertToDot(Graph<String> graph) throws IOException {
		logger.debug("Convert to DOT :");
		if (graph == null) {
			throw new IllegalArgumentException("The graph can't be null.");
		}
		if (!checkFormatVertex(graph.nodes())) {
			throw new IllegalArgumentException("The name of atleast one vertex can't be converted in DOT format.");
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		export(graph, stream);
		final String graphDotString = new String(stream.toByteArray());
		logger.debug("DOT graph : {}", graphDotString);
		return graphDotString;
	}

	/**
	 * This method is greatly inspired from the method getVertexId made by M.
	 * Olivier Cailloux at :
	 * https://github.com/oliviercailloux/jmcda-utils/blob/master/src/main/java/org/decision_deck/utils/relation/graph/mess/DOTExporterTemp.java
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
	private static boolean checkFormatVertex(Set<String> myNodes) {
		for (String aNode : myNodes) {
			boolean isAlphaDig = aNode.matches("[a-zA-Z]+([\\w_]*)?");
			boolean isDoubleQuoted = aNode.matches("\".*\"");
			boolean isDotNumber = aNode.matches("[-]?([.][0-9]+|[0-9]+([.][0-9]*)?)");
			boolean isHTML = aNode.matches("<.*>");
			if (!isAlphaDig && !isDotNumber && !isDoubleQuoted && !isHTML) {
				return false;
			}
		}
		return true;
	}
}
