package io.github.oliviercailloux.j_voting.preferences.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.io.CharStreams;
import io.github.oliviercailloux.j_voting.profiles.management.ReadODS;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		String resultDotFile = CharStreams
				.toString(new InputStreamReader(ExportDot.class.getResourceAsStream("FileDotTest.dot"), "UTF-8"));
		assertEquals(resultDotFile, graphDotFormat);
	}

	@Test
	public void testConvertToDotEndLine() throws IOException {

		MutableGraph<String> graph = GraphBuilder.directed().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDot.convertToDot(graph, System.lineSeparator());
		String resultDotFile = CharStreams
				.toString(new InputStreamReader(ExportDot.class.getResourceAsStream("FileDotTest.dot"), "UTF-8"));
		assertEquals(resultDotFile, graphDotFormat);
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

		String graphDotFormat = ExportDot.convertToDot(graph, System.lineSeparator());

		String resultDotFile1 = CharStreams.toString(
				new InputStreamReader(ExportDot.class.getResourceAsStream("FileDotTestUndirected1.dot"), "UTF-8"));
		String resultDotFile2 = CharStreams.toString(
				new InputStreamReader(ExportDot.class.getResourceAsStream("FileDotTestUndirected2.dot"), "UTF-8"));
		assertTrue(graphDotFormat.equals(resultDotFile1) || graphDotFormat.equals(resultDotFile2));

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
		ExportDot.export(graph, stream, System.lineSeparator());
		final String graphDotString = new String(stream.toByteArray(), StandardCharsets.UTF_8);
		String resultDotFile = CharStreams
				.toString(new InputStreamReader(ExportDot.class.getResourceAsStream("FileDotTest.dot"), "UTF-8"));
		assertEquals(resultDotFile, graphDotString);
	}
}
