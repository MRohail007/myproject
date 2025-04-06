package com.example.physio_ease.Repository;

import com.example.physio_ease.Entity.PhysiotherapistEntity;
import com.example.physio_ease.dto.PhysiotherapistDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysiotherapistRepository extends JpaRepository<PhysiotherapistEntity, Long>  {
    List<PhysiotherapistEntity> findBySpecialtyContainingIgnoreCase(String keyword);
    List<PhysiotherapistEntity>findByLocationContainingIgnoreCase(String location);
}
