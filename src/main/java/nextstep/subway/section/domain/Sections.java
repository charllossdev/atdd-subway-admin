package nextstep.subway.section.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Getter;
import nextstep.subway.common.exception.ExistException;
import nextstep.subway.common.exception.NothingException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@Getter
@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL) //, orphanRemoval = true
	private final List<Section> sections = new ArrayList<>();

	public List<Station> getStations() {
		Set<Station> result = new LinkedHashSet<>();
		for (Section section : this.sections) {
			result.addAll(section.getStations());
		}
		return new ArrayList<>(result);
	}

	public void createSection(Line line, Station upStation, Station downStation, int distance) {
		this.sections.add(new Section(line, upStation, downStation, distance));
	}

	public void add(Section target) {
		validateSection(target);

		if (isContainStation(target.getUpStation())) {
			this.sections.stream()
				.filter(section -> section.getUpStation().equals(target.getUpStation()))
				.findFirst()
				.ifPresent(section -> section.updateUpStation(target));
			this.sections.add(target);
			return;
		}

		if (isContainStation(target.getDownStation())) {
			this.sections.stream()
				.filter(section -> section.getDownStation().equals(target.getDownStation()))
				.findFirst()
				.ifPresent(section -> section.updateDownStation(target));
			this.sections.add(target);
		}
	}

	private void validateSection(Section target) {
		boolean upStationExist = isContainStation(target.getUpStation());
		boolean downStationExist = isContainStation(target.getDownStation());

		if (upStationExist && downStationExist) {
			throw new ExistException("이미 존재하는 구간입니다.");
		}

		if (!upStationExist && !downStationExist) {
			throw new NothingException("등록할 수 없는 구간입니다.");
		}
	}

	private boolean isContainStation(Station target) {
		return this.sections.stream()
			.anyMatch(station -> station.getStations().contains(target));
	}
}
