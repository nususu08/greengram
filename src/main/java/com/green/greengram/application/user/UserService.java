package com.green.greengram.application.user;

import com.green.greengram.application.user.model.*;
import com.green.greengram.config.enumcode.model.EnumUserRole;
import com.green.greengram.config.model.JwtUser;
import com.green.greengram.config.util.ImgUploadManager;
import com.green.greengram.entity.User;
import com.green.greengram.entity.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImgUploadManager imgUploadManager;

    @Transactional
    public void signUp(UserSignUpReq req, MultipartFile pic) {
        String hashedPassword = passwordEncoder.encode(req.getUpw());

        User user = new User();
        user.setNickName(req.getNickName());
        user.setUid(req.getUid());
        user.setUpw(hashedPassword);
        user.addUserRoles(req.getRoles());

        userRepository.save(user);

        if(pic != null) {
            String savedFileName = imgUploadManager.saveProfilePic(user.getUserId(), pic);
            user.setPic(savedFileName);
        }
    }

    public UserSignInDto signIn(UserSignInReq req) {
        User user = userRepository.findByUid(req.getUid()); // 일치하는 아이디가 있는지 확인, null이 넘어오면 uid가 없음
        // passwordEncoder 내부에는 JBcrypt 객체가 있다.
        if(user == null || !passwordEncoder.matches(req.getUpw(), user.getUpw())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디/비밀번호를 확인해 주세요.");
        }
        //user 튜플을 가져왔는데 user_role에 저장되어 있는 데이터까지 가져올 수 있었던건 양방향 관계 설정을 했기 때문에 가능
        List<UserRole> roles2 = user.getUserRoles();
        List<EnumUserRole> resultList = new ArrayList<>(roles2.size());
        for (UserRole role : roles2) {
            resultList.add(role.getUserRoleIds().getRoleCode());
        }

        List<EnumUserRole> roles = user.getUserRoles().stream().map(item -> item.getUserRoleIds().getRoleCode()).toList();
        log.info("roles: {}", roles);
        JwtUser jwtUser = new JwtUser(user.getUserId(), roles);

        UserSignInRes userSignInRes = UserSignInRes.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName() == null ? user.getUid() : user.getNickName())
                .pic(user.getPic())
                .build();

        return UserSignInDto.builder()
                .jwtUser(jwtUser)
                .userSignInRes(userSignInRes)
                .build();
    }

    public UserProfileGetRes getProfileUser(UserProfileGetDto dto) {
       return userMapper.findProfileByUserId(dto);
    }

}