package com.example.physio_ease.Service;

import com.example.physio_ease.Entity.PhysiotherapistEntity;
import com.example.physio_ease.dto.PhysiotherapistDTO;

import java.util.List;

public interface PhysiotherapistService {
    /*
    =============
    Instead of fetching all the data form the entity,
    we use dto for refined data
    =============
     */
//    List<PhysiotherapistEntity>getAllPhysiotherapists();
    PhysiotherapistEntity findPhysiotherapistById(Long physiotherapistId);
    List<PhysiotherapistDTO>getAllPhysiotherapists();
    List<PhysiotherapistEntity> getPhysiotherapistsBySpecialtyKeyword(String keyword);
    List<PhysiotherapistEntity>getPhysiotherapistsByLocationKeyword(String location);
}
