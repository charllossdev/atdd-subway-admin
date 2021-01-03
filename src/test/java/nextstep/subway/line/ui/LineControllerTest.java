package nextstep.subway.line.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;

@ExtendWith(MockitoExtension.class)
class LineControllerTest {

	@Mock
	private LineService lineService;

	private LineController controller;

	@BeforeEach
	void setUp() {
		controller = new LineController(lineService);
	}

	@Test
	void createLineAndSectionTest() {
		// given
		LineRequest request = new LineRequest("서울역", "green", 1L, 2L, 100);

		// when

		// then
	}
}
