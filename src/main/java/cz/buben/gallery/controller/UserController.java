package cz.buben.gallery.controller;

import cz.buben.gallery.dto.UserDto;
import cz.buben.gallery.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Nonnull
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(this.userService.getAll());
    }

    @Nonnull
    @PostMapping("/users")
    public ResponseEntity<Long> create(@RequestBody UserDto dto) {
        Long id = this.userService.createUser(dto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Nonnull
    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody UserDto dto) {
        dto.setId(id);
        this.userService.updateUser(dto);
        return ResponseEntity.ok().body(null);
    }
}
