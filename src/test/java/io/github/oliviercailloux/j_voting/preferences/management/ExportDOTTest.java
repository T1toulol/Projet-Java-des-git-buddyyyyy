package io.github.oliviercailloux.j_voting.preferences.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.io.CharStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ExportDOTTest {

	@Test
	public void testConvertToDotDirected() throws IOException {

		MutableGraph<String> graph = GraphBuilder.directed().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDOT.convertToDot(graph);

		String result = "digraph G {" + System.lineSeparator();
		result += "  a1;"+ System.lineSeparator();
		result += "  a2;"+ System.lineSeparator();
		result += "  a4;"+ System.lineSeparator();
		result += "  a3;"+ System.lineSeparator();
		result += "  a1 -> a2;" + System.lineSeparator();
		result += "  a1 -> a4;" + System.lineSeparator();
		result += "  a2 -> a3;" + System.lineSeparator();
		result += "  a2 -> a4;" + System.lineSeparator();
		result += "  a3 -> a4;" + System.lineSeparator();
		result += "}";

		assertEquals(result, graphDotFormat);
	}
	
	
	@Test
	public void testConvertToDotEndLine() throws IOException {

		MutableGraph<String> graph = GraphBuilder.directed().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDOT.convertToDot(graph, "\r");

		String result = "digraph G {\r";
		result += "  a1;\r";
		result += "  a2;\r";
		result += "  a4;\r";
		result += "  a3;\r";
		result += "  a1 -> a2;\r";
		result += "  a1 -> a4;\r";
		result += "  a2 -> a3;\r";
		result += "  a2 -> a4;\r";
		result += "  a3 -> a4;\r";
		result += "}";

		assertEquals(result, graphDotFormat);
	}
	
	
	@Test
	public void testConvertToDotException() throws IOException {

		MutableGraph<String> graphEncodingLine = GraphBuilder.directed().build();
		graphEncodingLine.putEdge("a1", "a2");
		graphEncodingLine.putEdge("a1", "a4");
		
		assertThrows(IllegalArgumentException.class, () -> ExportDOT.convertToDot(graphEncodingLine, "\t"));
		assertThrows(IllegalArgumentException.class, () -> ExportDOT.convertToDot(graphEncodingLine, "@"));
	}

	@Test
	public void testConvertToDotUndirected() throws IOException {

		MutableGraph<String> graph = GraphBuilder.undirected().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDOT.convertToDot(graph);

		String result1 = "graph G {" + System.lineSeparator();
		result1 += "  a1;"+ System.lineSeparator();;
		result1 += "  a2;"+ System.lineSeparator();;
		result1 += "  a4;"+ System.lineSeparator();;
		result1 += "  a3;"+ System.lineSeparator();;
		result1 += "  a1 -- a2;" + System.lineSeparator();
		result1 += "  a1 -- a4;" + System.lineSeparator();
		result1 += "  a2 -- a3;" + System.lineSeparator();
		result1 += "  a2 -- a4;" + System.lineSeparator();
		result1 += "  a4 -- a3;" + System.lineSeparator();
		result1 += "}";
		
		String result2 = "graph G {" + System.lineSeparator();
		result2 += "  a1;"+ System.lineSeparator();;
		result2 += "  a2;"+ System.lineSeparator();;
		result2 += "  a4;"+ System.lineSeparator();;
		result2 += "  a3;"+ System.lineSeparator();;
		result2 += "  a2 -- a1;" + System.lineSeparator();
		result2 += "  a4 -- a1;" + System.lineSeparator();
		result2 += "  a3 -- a2;" + System.lineSeparator();
		result2 += "  a4 -- a2;" + System.lineSeparator();
		result2 += "  a3 -- a4;" + System.lineSeparator();
		result2 += "}";
		
		assertTrue(graphDotFormat.equals(result1)||graphDotFormat.equals(result2));

	}
	
	
	@Test
	public void testExportDotToFile() throws IOException {

		MutableGraph<String> graph = GraphBuilder.directed().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		File file = new File("./src/test/resources/io/github/oliviercailloux/j_voting/preferences/management/FileDOTtest.dot");
		OutputStream fop = new FileOutputStream(file);
		ExportDOT.export(graph, fop);
		
		String resultDOTFile = CharStreams.toString(new InputStreamReader(
				ExportDOT.class.getResourceAsStream("FileDOTtest.dot"), "UTF-8"));

		String result = "digraph G {" + System.lineSeparator();
		result += "  a1;"+ System.lineSeparator();
		result += "  a2;"+ System.lineSeparator();
		result += "  a4;"+ System.lineSeparator();
		result += "  a3;"+ System.lineSeparator();
		result += "  a1 -> a2;" + System.lineSeparator();
		result += "  a1 -> a4;" + System.lineSeparator();
		result += "  a2 -> a3;" + System.lineSeparator();
		result += "  a2 -> a4;" + System.lineSeparator();
		result += "  a3 -> a4;" + System.lineSeparator();
		result += "}";

		assertEquals(result, resultDOTFile);
	} 
	
	
}
