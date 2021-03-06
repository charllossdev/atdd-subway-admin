package nextstep.subway.station;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

		// then
		지하철역_생성_성공_확인(response);
	}

	@DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
	@Test
	void createStationWithDuplicateName() {
		// given
		지하철역_생성_요청("강남역");

		// when
		ExtractableResponse<Response> response = 지하철역_생성_요청("강남역");

		// then
		지하철역_생성_성공여부_확인(response, HttpStatus.BAD_REQUEST);
	}

	private void 지하철역_생성_성공여부_확인(ExtractableResponse<Response> response, HttpStatus badRequest) {
		assertThat(response.statusCode()).isEqualTo(badRequest.value());
	}

	@DisplayName("지하철역을 조회한다.")
	@Test
	void getStations() {
		/// given
		ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청("강남역");

		ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청("역삼역");

		// when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.get("/stations")
			.then().log().all()
			.extract();

		// then
		지하철역_생성_성공여부_확인(response, HttpStatus.OK);
		List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
			.map(it -> Long.parseLong(it.header("Location").split("/")[2]))
			.collect(Collectors.toList());
		List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
			.map(it -> it.getId())
			.collect(Collectors.toList());
		assertThat(resultLineIds).containsAll(expectedLineIds);
	}

	@DisplayName("지하철역을 제거한다.")
	@Test
	void deleteStation() {
		// given
		ExtractableResponse<Response> createResponse = 지하철역_생성_요청("강남역");

		// when
		String uri = createResponse.header("Location");
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.delete(uri)
			.then().log().all()
			.extract();

		// then
		지하철역_생성_성공여부_확인(response, HttpStatus.NO_CONTENT);
	}

	public ExtractableResponse<Response> 지하철역_생성_요청(String station) {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("name", station);

		// when
		return RestAssured.given().log().all()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then().log().all()
			.extract();
	}

	private void 지하철역_생성_성공_확인(ExtractableResponse<Response> response) {
		지하철역_생성_성공여부_확인(response, HttpStatus.CREATED);
		assertThat(response.header("Location")).isNotBlank();
	}
}
