package backend.service;

import backend.persistence.entity.Instrument;
import backend.persistence.entity.Source;
import backend.persistence.repository.InstrumentRepository;
import backend.persistence.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketDirectoryService {

    private final InstrumentRepository instrumentRepository;
    private final SourceRepository sourceRepository;

    public List<Instrument> getEnabledInstruments() {
        return instrumentRepository.findByEnabledTrue();
    }

    public List<Instrument> getEnabledMoexInstruments() {
       Optional<Source> source = findSourceByCode("MOEX");
        return source.map(value -> instrumentRepository.findAllBySourceId(value.getId())
                .stream()
                .filter(Instrument::getEnabled)
                .toList()).orElseGet(List::of);
    }

    public List<Source> getEnabledSources() {
        return sourceRepository.findByEnabledTrue();
    }

    public Optional<Source> findSourceByCode(String code) {
        return sourceRepository.findByCode(code);
    }



}
