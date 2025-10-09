package team.mjk.agent.domain.passport.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.mjk.agent.domain.passport.domain.Passport;
import team.mjk.agent.domain.passport.domain.PassportRepository;
import team.mjk.agent.domain.passport.presentation.exception.PassportNotFoundException;

@RequiredArgsConstructor
@Repository
public class PassportRepositoryImpl implements PassportRepository {

  private final PassportJpaRepository passportJpaRepository;

  @Override
  public void save(Passport passport) {
    passportJpaRepository.save(passport);
  }

  @Override
  public Passport findByMemberId(Long memberId) {
    return passportJpaRepository.findByMemberId(memberId)
            .orElseThrow(PassportNotFoundException::new);
  }

}
