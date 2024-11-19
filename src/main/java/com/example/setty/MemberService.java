package com.example.setty;
package com.example.setty.service;

import com.example.setty.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface MemberService {
    public abstract Long join(Member member);
}
