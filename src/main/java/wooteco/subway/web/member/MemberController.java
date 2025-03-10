package wooteco.subway.web.member;

import static wooteco.subway.web.member.LoginMember.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request.toMember());
        return ResponseEntity
            .created(URI.create("/members/" + member.getId()))
            .build();
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMemberByEmail(@LoginMember(type = Type.EMAIL) Member member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
        @LoginMember(type = Type.ID) Member member,
        @RequestBody UpdateMemberRequest param
    ) {
        memberService.updateMember(member, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@LoginMember(type = Type.ID) Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.noContent().build();
    }
}
