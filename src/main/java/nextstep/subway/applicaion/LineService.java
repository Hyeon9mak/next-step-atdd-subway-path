package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.domain.Section.*;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        if (request.getUpStationId() != null && request.getDownStationId() != null && request.getDistance() != 0) {
            Station upStation = stationService.findById(request.getUpStationId());
            Station downStation = stationService.findById(request.getDownStationId());
            Section section = new SectionBuilder(line)
                    .setDownStation(downStation)
                    .setUpStation(upStation)
                    .setDistance(request.getDistance())
                    .build();
            line.addSection(section);
        }
        return createLineResponse(line);
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (lineRequest.getName() != null) {
            line.setName(lineRequest.getName());
        }
        if (lineRequest.getColor() != null) {
            line.setColor(lineRequest.getColor());
        }
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Section section = new SectionBuilder(line)
                .setUpStation(upStation)
                .setDownStation(downStation)
                .setDistance(sectionRequest.getDistance())
                .build();
        line.addSection(section);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                createSectionResponses(line),
                createStationResponses(line)
        );
    }

    private List<SectionResponse> createSectionResponses(Line line) {
        if(line.isEmptySections()){
            return Collections.emptyList();
        }
        List<Section> sections = line.getSections().getValuesOrderBy();

        return sections.stream()
                .map(SectionResponse::new)
                .collect(Collectors.toList());
    }


    private List<StationResponse> createStationResponses(Line line) {
        if (line.isEmptySections()) {
            return Collections.emptyList();
        }

        List<Station> stations = line.getAllStations();

        return stations.stream()
                .map(stationService::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        Station station = stationService.findById(stationId);

        line.removeLastSection(station);

    }
}
