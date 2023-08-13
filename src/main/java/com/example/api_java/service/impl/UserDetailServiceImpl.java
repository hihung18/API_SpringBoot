package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.RoleName;
import com.example.api_java.model.dto.*;
import com.example.api_java.model.entity.Role;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IRoleRepository;
import com.example.api_java.repository.IUserDetailRepository;
import com.example.api_java.security.jwt.JwtUtils;
import com.example.api_java.security.services.UserDetailsImpl;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements IBaseService<UserDetailDTO, Long>, IModelMapper<UserDetailDTO, UserDetail> {
  final private AuthenticationManager authenticationManager;
  final private IRoleRepository roleRepository;
  final private PasswordEncoder encoder;
  final private JwtUtils jwtUtils;

  private final ModelMapper modelMapper;
  private final IUserDetailRepository userDetailRepository;

  public UserDetailServiceImpl(AuthenticationManager authenticationManager, IRoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, ModelMapper modelMapper, IUserDetailRepository userDetailRepository) {
    this.authenticationManager = authenticationManager;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;

    this.modelMapper = modelMapper;
    this.userDetailRepository = userDetailRepository;
  }

  public ResponseEntity<?> checkLogin(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    // if go there, the user/password is correct
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // generate jwt to return to client
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    UserDetail entity = this.getDetail(userDetails.getId());
    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles, entity.getFirstName() + " " + entity.getLastName()));
  }

  public ResponseEntity<?> changePass(ChangePassRequest request) {
    if (!userDetailRepository.existsByUsername(request.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username not found!"));
    }
    UserDetail user = userDetailRepository.findByUsername(request.getUsername()).get();
    System.out.println(user.getPassword());
    System.out.println(encoder.encode(request.getOldPassword()));
    if (!encoder.matches(request.getOldPassword(), user.getPassword()))
      throw new NotFoundException("Username and old password not match");
    user.setPassword(encoder.encode(request.getPassword()));
    userDetailRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("Change password successfully!"));
  }

  public ResponseEntity<?> changePassByEmail(ChangePassByEmailRequest request) {
    if (!userDetailRepository.existsByEmail(request.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email not found!"));
    }
    UserDetail user = userDetailRepository.findByEmail(request.getEmail()).get();
    user.setPassword(encoder.encode(request.getPassword()));
    userDetailRepository.save(user);
    return ResponseEntity.ok(new MessageResponse("Change password successfully!"));
  }

  public ResponseEntity<?> register(SignupRequest signUpRequest) {
    if (userDetailRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }
    if (userDetailRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    UserDetailDTO dto = modelMapper.map(signUpRequest, UserDetailDTO.class);
    save(dto);
    return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.CREATED);
  }

  private Role getRole(String strRole) {
    if (strRole == null || strRole.equals("")) {
      return roleRepository.findByName(RoleName.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

    } else {
      switch (strRole) {
        case "ROLE_ADMIN":
          return roleRepository.findByName(RoleName.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        case "ROLE_SHOP":
          return roleRepository.findByName(RoleName.ROLE_SHOP)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        default:
          return roleRepository.findByName(RoleName.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      }
    }
  }

  private UserDetail getDetail(Long id) {
    UserDetail detail = userDetailRepository.findById(id)
            .orElse(new UserDetail(1L, "", "", "", null));
    return detail;
  }

  @Override
  public List<UserDetailDTO> findAll() {
    return createFromEntities(userDetailRepository.findAll());
  }

  @Override
  public UserDetailDTO findById(Long id) {
    UserDetail entity = userDetailRepository.findById(id).orElseThrow(() -> new NotFoundException(UserDetail.class, id));
    return createFromE(entity);
  }

  @Override
  public UserDetailDTO update(Long id, UserDetailDTO signupRequest) {
    UserDetail entity = userDetailRepository.findById(id).orElseThrow(() -> new NotFoundException(UserDetail.class, id));
    entity = updateEntity(entity, signupRequest);
    saveDetail(entity, signupRequest);
    return createFromE(userDetailRepository.save(entity));
  }

  private void updateDetail(UserDetailDTO signUpRequest) {

    UserDetail entity = userDetailRepository.findByUsername(signUpRequest.getUsername())
            .orElseThrow(() -> new NotFoundException("Username =" + signUpRequest.getUsername() + " not found!"));
    saveDetail(entity, signUpRequest);
  }

  private void saveDetail(UserDetail entity, UserDetailDTO signUpRequest) {
    UserDetail userDetail = new UserDetail(entity.getId()
            , signUpRequest.getFirstName() == null ? signUpRequest.getUsername() : signUpRequest.getFirstName()
            , signUpRequest.getLastName() == null ? signUpRequest.getUsername() : signUpRequest.getLastName()
            , signUpRequest.getAddress() == null ? "" : signUpRequest.getAddress(), entity.getRole());
    userDetailRepository.save(userDetail);
  }

  @Override
  public UserDetailDTO save(UserDetailDTO dto) {
    try {
      UserDetail entity = createFromD(dto);
      userDetailRepository.save(entity);
      updateDetail(dto);
      return createFromE(entity);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public UserDetailDTO delete(Long id) {
    Optional<UserDetail> entity = Optional.ofNullable(userDetailRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(UserDetail.class, id)));
    System.out.println(entity.get().getId());
    try {
      userDetailRepository.delete(getDetail(id));
      userDetailRepository.delete(entity.get());
    } catch (Exception e) {

    }
    return createFromE(entity.get());
  }

  @Override
  public UserDetail createFromD(UserDetailDTO dto) {
    UserDetail user = modelMapper.map(dto, UserDetail.class);
    user.setRole(getRole(dto.getRole().name()));
    user.setPassword(encoder.encode(dto.getPassword()));
    return user;
  }

  @Override
  public UserDetailDTO createFromE(UserDetail entity) {
    UserDetailDTO dto = modelMapper.map(entity, UserDetailDTO.class);
    try {
      UserDetail detail = this.getDetail(entity.getId());
      dto.setAddress(detail.getAddress());
      dto.setRole(entity.getRole().getName());
      dto.setFirstName(detail.getFirstName());
      dto.setLastName(detail.getLastName());
    } catch (Exception e) {

    }
    dto.setPassword("");
    return dto;
  }

  @Override
  public UserDetail updateEntity(UserDetail entity, UserDetailDTO dto) {
    if (entity != null && dto != null) {
      entity.setRole(roleRepository.findByName(dto.getRole()).orElseThrow(() -> new NotFoundException(UserDetail.class, dto.getId())));
      if (dto.getPassword() != null)
        entity.setPassword(encoder.encode(dto.getPassword()));
      if (dto.getUsername() != null)
        entity.setUsername(dto.getUsername());
      entity.setEmail(dto.getEmail());
    }
    return entity;
  }
}
