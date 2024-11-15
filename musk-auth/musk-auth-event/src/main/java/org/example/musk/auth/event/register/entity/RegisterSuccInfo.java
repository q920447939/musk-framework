package org.example.musk.auth.event.register.entity;


import lombok.Data;
import org.example.musk.auth.entity.member.MemberDO;

@Data
public class RegisterSuccInfo {
    private MemberDO memberDO;

    private int inviteMemberSimpleId;

}
