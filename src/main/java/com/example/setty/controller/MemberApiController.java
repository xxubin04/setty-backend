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
}
