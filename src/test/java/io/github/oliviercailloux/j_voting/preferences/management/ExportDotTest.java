package io.github.oliviercailloux.j_voting.preferences.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExportDotTest {

	@Test
	public void testConvertToDotDirected() throws IOException {

		MutableGraph<String> graph = GraphBuilder.directed().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDot.convertToDot(graph);

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
		result=new String(result.getBytes(), StandardCharsets.UTF_8);
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

		String graphDotFormat = ExportDot.convertToDot(graph, "\r");

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
		result=new String(result.getBytes(), StandardCharsets.UTF_8);
		assertEquals(result, graphDotFormat);
	}
	
	
	@Test
	public void testConvertToDotException() throws IOException {

		MutableGraph<String> graphEncodingLine = GraphBuilder.directed().build();
		graphEncodingLine.putEdge("a1", "a2");
		graphEncodingLine.putEdge("a1", "a4");
		
		assertThrows(IllegalArgumentException.class, () -> ExportDot.convertToDot(graphEncodingLine, "\t"));
		assertThrows(IllegalArgumentException.class, () -> ExportDot.convertToDot(graphEncodingLine, "@"));
	}

	@Test
	public void testConvertToDotUndirected() throws IOException {

		MutableGraph<String> graph = GraphBuilder.undirected().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDot.convertToDot(graph);

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
		result1=new String(result1.getBytes(), StandardCharsets.UTF_8);
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
		result2=new String(result2.getBytes(), StandardCharsets.UTF_8);
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
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ExportDot.export(graph, stream);
		final String graphDotString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
		
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
		result=new String(result.getBytes(), StandardCharsets.UTF_8);
		assertEquals(result, graphDotString);
	} 
	
	
}
