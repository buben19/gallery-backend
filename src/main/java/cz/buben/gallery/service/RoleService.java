package cz.buben.gallery.service;

import cz.buben.gallery.dto.RoleDto;
import cz.buben.gallery.mapper.RoleMapper;
import cz.buben.gallery.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Nonnull
    public List<RoleDto> getAll() {
        return StreamSupport.stream(this.roleRepository.findAll().spliterator(), false)
                .map(this.roleMapper::roleToDto)
                .collect(Collectors.toList());
    }
}
