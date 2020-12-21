package cz.buben.gallery.controller;

import cz.buben.gallery.dto.PrivilegeDto;
import cz.buben.gallery.service.PrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @GetMapping("/privileges")
    public ResponseEntity<List<PrivilegeDto>> getAll() {
        return ResponseEntity.ok(this.privilegeService.getAll());
    }
}
