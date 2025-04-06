package com.example.physio_ease.Controller;

import com.example.physio_ease.Entity.PhysiotherapistEntity;
import com.example.physio_ease.Service.impl.PhysiotherapistServiceImpl;
import com.example.physio_ease.dto.PhysiotherapistDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/physiotherapists")
public class PhysiotherapistController {
    private final PhysiotherapistServiceImpl physiotherapistService;

    /*
    ============
    ENDPOINT FOR GETTING ALL PHYSIOTHERAPIST - SAMAD
    ============
     */
    @GetMapping
    public List<PhysiotherapistDTO> getAllPhysiotherapists() {
        return physiotherapistService.getAllPhysiotherapists();
    }

    /* ==============
    This is for the homepage filtering(scalable)
    ============== */
    @GetMapping("specialty/keyword/{keyword}")
    public List<PhysiotherapistEntity>getPhysiotherapistsSpecialtyKeyword(@PathVariable String keyword) {
        return physiotherapistService.getPhysiotherapistsBySpecialtyKeyword(keyword);
    }
    @GetMapping("specialty/location/{location}")
    public List<PhysiotherapistEntity>getPhysiotherapistsByLocationKeyword(@PathVariable String location){
        return physiotherapistService.getPhysiotherapistsByLocationKeyword(location);
    }

}
