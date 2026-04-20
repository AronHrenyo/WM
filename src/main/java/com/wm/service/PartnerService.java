package com.wm.service;

import com.wm.entity.Partner;
import com.wm.repository.PartnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerService {

    private final PartnerRepository repository;

    public List<Partner> findAll() {
        return repository.findAll();
    }

    public Partner findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + id));
    }

    public Partner create(Partner partner) {
        return repository.save(partner);
    }

    public Partner update(Long id, Partner partner) {
        Partner existing = findById(id);

        existing.setPartnerName(partner.getPartnerName());
        existing.setPartnerEmail(partner.getPartnerEmail());
        existing.setPartnerTelephone(partner.getPartnerTelephone());
        existing.setPartnerAddress(partner.getPartnerAddress());

        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}