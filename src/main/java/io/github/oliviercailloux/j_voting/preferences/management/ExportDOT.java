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
 * The goal of the ExportDOT class is to export Google Guava Graph in DOT Format
 * The DOT format is a simple file format which describes graphs. This type of file is used by graph visualization applications. 
 * For more details about DOT format: https://www.graphviz.org/doc/info/lang.html
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
	 * Exports the graph from the parameters, converts it to DOT format and writes (with UTF-8 character encoding)
	 * it to the stream from the parameters.
	 * If the parameter endLine is null, it will take the value ";" by default followed by a System.lineSeparator().
	 * This choice is based on René Nyffenegger examples (https://renenyffenegger.ch/notes/tools/Graphviz/examples/index)
	 * referenced in the official website about DOT format (https://www.graphviz.org/resources/#simple-examples-and-tutorials).
	 * 
	 * @param graph can't be null
	 * @param stream can't be null
	 * @param endLine can be null
	 * @throws IOException
	 */
	public static void export(Graph<String> graph, OutputStream stream, String endLineArg) throws IOException {
		if (graph == null) {
			throw new NullPointerException("The graph can't be null.");
		}
		if (!checkFormatVertex(graph.nodes())) {
			throw new IllegalArgumentException("The name of atleast one vertex can't be converted in DOT format.");
		}

		final String endLine = endLineArg==null ? ";" : endLineArg;
		final String header = graph.isDirected() ? "digraph G {" : "graph G {";
		final String connector = graph.isDirected() ? " -> " : " -- ";
		final String indentation = "  ";

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
	 * For more informations about DOT format: http://www.graphviz.org/doc/info/lang.html
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
	 * This method is greatly inspired from the method getVertexId of the {@link org.jgrapht.ext.DOTExporter} class
	 * For more informations:https://jgrapht.org/javadoc-1.1.0/org/jgrapht/ext/DOTExporter.html
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
