package kr.purred.auth.models.imp.repository;

import kr.purred.auth.models.domains.PuMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuMemberRepository extends JpaRepository<PuMember, String>
{
}
