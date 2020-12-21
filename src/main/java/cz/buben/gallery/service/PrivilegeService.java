package cz.buben.gallery.service;

import cz.buben.gallery.dto.PrivilegeDto;
import cz.buben.gallery.mapper.PrivilegeMapper;
import cz.buben.gallery.repository.PrivilegeRepository;
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
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;

    @Nonnull
    public List<PrivilegeDto> getAll() {
        return StreamSupport.stream(this.privilegeRepository.findAll().spliterator(), false)
                .map(this.privilegeMapper::privilegeToDto)
                .collect(Collectors.toList());
    }
}
