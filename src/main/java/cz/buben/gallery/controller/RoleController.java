package cz.buben.gallery.controller;

import cz.buben.gallery.dto.RoleDto;
import cz.buben.gallery.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getAll() {
        return ResponseEntity.ok(this.roleService.getAll());
    }
}
