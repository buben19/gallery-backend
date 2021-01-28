package cz.buben.gallery.service;

import cz.buben.gallery.dto.UserDto;
import cz.buben.gallery.mapper.UserMapper;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Nonnull
    public List<UserDto> getAll() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(this.userMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Nonnull
    public Long createUser(@Nonnull UserDto dto) {
        User user = this.userMapper.dtoToUser(dto);
        return this.userRepository.save(user).getId();
    }

    public void updateUser(@Nonnull UserDto dto) {
        User user = this.userMapper.dtoToUser(dto);
        this.userRepository.save(user);
    }
}
