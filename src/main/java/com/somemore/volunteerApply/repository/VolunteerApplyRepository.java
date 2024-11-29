package com.somemore.volunteerApply.repository;

import com.somemore.volunteerApply.domain.VolunteerApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VolunteerApplyRepository {

    VolunteerApply save(VolunteerApply volunteerApply);
    Optional<VolunteerApply> findById(Long id);
    List<UUID> findVolunteerIdsByRecruitIds(List<Long> recruitIds);
    Page<VolunteerApply> findAllByRecruitId(Long recruitId, Pageable pageable);
}
