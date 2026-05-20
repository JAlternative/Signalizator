package backend.service;

import backend.persistence.entity.Instrument;
import backend.persistence.entity.Source;
import backend.persistence.repository.InstrumentRepository;
import backend.persistence.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketDirectoryService {

    private final InstrumentRepository instrumentRepository;
    private final SourceRepository sourceRepository;

    public Optional<Instrument> getInstrumentById(Long id) {
        return instrumentRepository.findById(id);
    }

    public List<Instrument> getEnabledInstruments() {
        return instrumentRepository.findByEnabledTrue();
    }

    public List<Instrument> getEnabledMoexInstruments() {
        Optional<Source> source = getSourceByCode("MOEX");
        return source.map(value -> instrumentRepository.findAllBySourceId(value.getId())
                .stream()
                .filter(Instrument::getEnabled)
                .toList()).orElseGet(List::of);
    }

    @Transactional
    public Optional<Instrument> addToWatchlist(Long id) {
        Optional<Instrument> instrument = getInstrumentById(id);

        if (instrument.isEmpty()) {
            return Optional.empty();
        }

        Instrument inst = instrument.get();
        inst.setEnabled(true);

        return Optional.of(instrumentRepository.save(inst));
    }

    //---------------------------------------------------------------------------------


    public List<Source> getEnabledSources() {
        return sourceRepository.findByEnabledTrue();
    }

    public Optional<Source> getSourceByCode(String code) {
        return sourceRepository.findByCode(code);
    }
}
