package com.example.setty.controller;

import com.example.setty.dto.MemberRequestDto;
import com.example.setty.entity.Member;
import com.example.setty.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Map;

//@Tag(name = "회원 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Operation(summary = "회원가입 API")
    @PostMapping("/register")
    public ResponseEntity<?> addMember(@Valid @RequestBody MemberRequestDto member) {
        try {
            Member entity = modelMapper.map(member, Member.class);
            Long id = memberService.join(entity);
            return ResponseEntity.ok(Map.of("message", "Registration successful", "memberId", id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        // 1. 요청 데이터에서 email, password 가져오기
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // 2. 데이터베이스에서 이메일과 패스워드 확인
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/userDB", "유저명", "비밀번호");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT nickname FROM Member WHERE email = ? AND password = ?")) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 3. 로그인 성공 - 닉네임 가져오기
                    String nickname = resultSet.getString("nickname");
                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful",
                            "token", "example-token-123", // 나중에 진짜 토큰으로 바꿀 수 있음
                            "nickname", nickname
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Internal Server Error"
            ));
        }

        // 4. 로그인 실패하면 401 응답
        return ResponseEntity.status(401).body(Map.of(
                "message", "Invalid username or password"
        ));
    }

}