package com.example.setty.controller;

import com.example.setty.dto.MemberRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.setty.*;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    private final ModelMapper modelMapper;

    @Operation(summary = "회원 신규 추가(가입)")
    @PostMapping("/api/v1/member")
    public ResponseEntity<Long> addMember(@Valid @RequestBody MemberRequestDto member) {
        Member entity = modelMapper.map(member, Member.class);
        Long id = memberService.join(entity);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
