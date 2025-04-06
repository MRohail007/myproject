package com.example.physio_ease.Service.impl;

import com.example.physio_ease.Entity.PhysiotherapistEntity;
import com.example.physio_ease.Repository.PhysiotherapistRepository;
import com.example.physio_ease.Service.PhysiotherapistService;
import com.example.physio_ease.dto.PhysiotherapistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhysiotherapistServiceImpl implements PhysiotherapistService {

    private final PhysiotherapistRepository physiotherapistRepository;

    /* =============
    Fetch a Physiotherapist from Database
    ============= */
    @Override
    public PhysiotherapistEntity findPhysiotherapistById(Long physiotherapistId) {
        return physiotherapistRepository.findById(physiotherapistId).orElseThrow(() -> new IllegalArgumentException("Unable to find physiotherapist"));
    }

    /*
    ==========
    This is a general function for getting all physiotherapist when needed in
    the application
    ==========
     */
    @Override
    public List<PhysiotherapistDTO> getAllPhysiotherapists() {
        return physiotherapistRepository.findAll().stream()
                .map(this::convertPhysiotherapistToDTO)
                .collect(Collectors.toList());
    }

    /*
    ==========
    Todo: Helper method move to utils
    ==========
     */
    private PhysiotherapistDTO convertPhysiotherapistToDTO(PhysiotherapistEntity physiotherapistEntity) {
        return new PhysiotherapistDTO(
                physiotherapistEntity.getId(),
                physiotherapistEntity.getFirstName(),
                physiotherapistEntity.getLastName(),
                physiotherapistEntity.getRegistrationNumber(),
                physiotherapistEntity.getSpecialty(),
                physiotherapistEntity.getTelephoneNo(),
                physiotherapistEntity.getQualification(),
                physiotherapistEntity.getClinicName(),
                physiotherapistEntity.getAvailability(),
                physiotherapistEntity.getExperience(),
                physiotherapistEntity.getLocation(),
                physiotherapistEntity.getSlot(),
                physiotherapistEntity.getImage()
        );
    }

    /*
    ============
    Todo: Use DTO instead for refined response data
    For filtering physio based on their specialty (Sports,
    ============
     */
    @Override
    public List<PhysiotherapistEntity> getPhysiotherapistsBySpecialtyKeyword(String keyword) {
        return physiotherapistRepository.findBySpecialtyContainingIgnoreCase(keyword);
    }

    @Override
    public List<PhysiotherapistEntity> getPhysiotherapistsByLocationKeyword(String location) {
        return physiotherapistRepository.findByLocationContainingIgnoreCase(location);
    }
}
