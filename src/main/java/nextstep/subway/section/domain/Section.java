package nextstep.subway.section.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Line line;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upStationId", nullable = true)
	private Station upStation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "downStationId", nullable = true)
	private Station downStation;

	private int distance;

	public List<Station> getStations() {
		return Arrays.asList(upStation, downStation);
	}

	public Section(Line line, Station upStation, Station downStation, int distance) {
		if (upStation != null && upStation.equals(downStation)) {
			throw new RuntimeException("중복된 Station 입니다.");
		}
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}
}
