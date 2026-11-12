package br.com.walmart.freight.utest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.walmart.freight.models.Logistic;
import br.com.walmart.freight.repositories.RouteDistanceRepository;
import br.com.walmart.freight.repositories.RouteEdge;
import br.com.walmart.freight.services.FindShortestPathServiceImpl;

@ExtendWith(MockitoExtension.class)
class FindShortestPathServiceImplTest {

	@Mock
	private RouteDistanceRepository repository;

	@InjectMocks
	private FindShortestPathServiceImpl service;

	private final List<RouteEdge> sp = List.of(
			new RouteEdge("A", "B", 10), new RouteEdge("B", "D", 15.5),
			new RouteEdge("A", "C", 20), new RouteEdge("C", "D", 30),
			new RouteEdge("B", "E", 50.5), new RouteEdge("D", "E", 30),
			new RouteEdge("D", "A", 30.5));

	private Float distance(String from, String to) {
		when(repository.findAllBy("SP")).thenReturn(sp);
		return service.baseOn(new Logistic("SP", from, to, 10f, 2.5f));
	}

	@Test
	void baseOn_multipleRoutes_returnsMinimumDistance() {
		assertEquals(25.5f, distance("A", "D"));
	}

	@Test
	void baseOn_longerFirstHopIsStillTheCheapest_returnsMinimumDistance() {
		// A->E: via B,D = 55.5 beats B,E = 60.5 and C,D,E = 80
		assertEquals(55.5f, distance("A", "E"));
	}

	@Test
	void baseOn_routeThroughACycle_returnsMinimumDistance() {
		// D->C has to go back through A (D,A,C) and must not loop forever
		assertEquals(50.5f, distance("D", "C"));
	}

	@Test
	void baseOn_routesAreDirectional_returnsNoRoute() {
		assertEquals(-1f, distance("E", "A"));
	}

	@Test
	void baseOn_unknownCity_returnsNoRoute() {
		assertEquals(-1f, distance("A", "Z"));
	}

	@Test
	void baseOn_unknownMap_returnsNoRoute() {
		when(repository.findAllBy("XX")).thenReturn(List.of());

		assertEquals(-1f, service.baseOn(new Logistic("XX", "A", "D", 10f, 2.5f)));
	}

	@Test
	void baseOn_sameOriginAndDestination_costsNothing() {
		// used to return the cost of the shortest loop A->B->D->A
		assertEquals(0f, distance("A", "A"));
	}

	@Test
	void baseOn_sameOriginAndDestinationButCityDoesNotExist_returnsNoRoute() {
		assertEquals(-1f, distance("Z", "Z"));
	}

	@Test
	void baseOn_zeroDistanceRoute_isAllowed() {
		when(repository.findAllBy("SP")).thenReturn(List.of(new RouteEdge("A", "B", 0), new RouteEdge("B", "C", 4)));

		assertEquals(4f, service.baseOn(new Logistic("SP", "A", "C", 10f, 2.5f)));
	}

	@Test
	void baseOn_denseMapWithAstronomicalPathCount_staysFastAndIsCorrect() {
		// Complete DAG of 60 nodes: ~2^58 distinct paths. The old Cypher enumeration never finished.
		final int nodes = 60;
		final List<RouteEdge> dense = new ArrayList<>();
		for (int i = 0; i < nodes; i++) {
			for (int j = i + 1; j < nodes; j++) {
				dense.add(new RouteEdge("N" + i, "N" + j, weight(i, j)));
			}
		}
		when(repository.findAllBy("DENSE")).thenReturn(dense);

		final Float result = assertTimeoutPreemptively(Duration.ofSeconds(2),
				() -> service.baseOn(new Logistic("DENSE", "N0", "N" + (nodes - 1), 10f, 2.5f)));

		assertEquals(expectedByDynamicProgramming(nodes), result);
	}

	private static int weight(int i, int j) {
		return 1 + ((i * 7 + j * 3) % 5);
	}

	/** Independent oracle: in a DAG numbered by position, best[j] = min over i < j of best[i] + weight(i, j). */
	private static float expectedByDynamicProgramming(int nodes) {
		final int[] best = new int[nodes];
		for (int j = 1; j < nodes; j++) {
			best[j] = Integer.MAX_VALUE;
			for (int i = 0; i < j; i++) {
				best[j] = Math.min(best[j], best[i] + weight(i, j));
			}
		}
		return best[nodes - 1];
	}

}
