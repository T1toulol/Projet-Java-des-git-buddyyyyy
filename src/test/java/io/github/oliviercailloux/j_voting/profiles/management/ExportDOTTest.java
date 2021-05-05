package io.github.oliviercailloux.j_voting.profiles.management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.io.IOException;

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
	public void testConvertToDotUndirected() throws IOException {

		MutableGraph<String> graph = GraphBuilder.undirected().build();
		graph.putEdge("a1", "a2");
		graph.putEdge("a1", "a4");
		graph.putEdge("a2", "a3");
		graph.putEdge("a2", "a4");
		graph.putEdge("a3", "a4");

		String graphDotFormat = ExportDOT.convertToDot(graph);

		String result = "graph G {" + System.lineSeparator();
		result += "  a1;"+ System.lineSeparator();;
		result += "  a2;"+ System.lineSeparator();;
		result += "  a4;"+ System.lineSeparator();;
		result += "  a3;"+ System.lineSeparator();;
		result += "  a1 -- a2;" + System.lineSeparator();
		result += "  a1 -- a4;" + System.lineSeparator();
		result += "  a2 -- a3;" + System.lineSeparator();
		result += "  a2 -- a4;" + System.lineSeparator();
		result += "  a4 -- a3;" + System.lineSeparator();
		result += "}";

		assertEquals(result, graphDotFormat);
	}
}
