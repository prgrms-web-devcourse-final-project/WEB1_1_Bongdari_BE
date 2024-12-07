package com.somemore.note.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.center.domain.QCenter;
import com.somemore.note.domain.Note;
import com.somemore.note.domain.QNote;
import com.somemore.note.repository.mapper.NoteDetailViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForCenter;
import com.somemore.note.repository.mapper.NoteReceiverViewForVolunteer;
import com.somemore.volunteer.domain.QVolunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class NoteRepositoryImpl implements NoteRepository {

    private final JPAQueryFactory queryFactory;
    private final NoteJpaRepository noteJpaRepository;

    private static final QNote note = QNote.note;
    private static final QVolunteer volunteer = QVolunteer.volunteer;
    private static final QCenter center = QCenter.center;

    private static final String SENDER_ID = "senderId";
    private static final String SENDER_NAME = "senderName";

    @Override
    public Note save(Note note) {
        return noteJpaRepository.save(note);
    }

    @Override
    public Page<NoteReceiverViewForCenter> findNotesByReceiverIsCenter(UUID centerId, Pageable pageable) {

        BooleanExpression activeVolunteer = isActiveVolunteer();
        BooleanExpression condition = isReceiver(centerId);

        List<NoteReceiverViewForCenter> results = queryFactory
                .select(Projections.constructor(
                        NoteReceiverViewForCenter.class,
                        note.id,
                        note.title,
                        volunteer.id.as(SENDER_ID),
                        volunteer.nickname.as(SENDER_NAME),
                        note.isRead
                ))
                .from(note)
                .join(volunteer).on(note.senderId.eq(volunteer.id).and(activeVolunteer))
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(note.createdAt.desc())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(note.count())
                .from(note)
                .where(condition);

        return PageableExecutionUtils.getPage(results, pageable, count::fetchOne);
    }

    @Override
    public Page<NoteReceiverViewForVolunteer> findNotesByReceiverIsVolunteer(UUID volunteerId, Pageable pageable) {
        BooleanExpression activeCenter = center.deleted.eq(false);
        BooleanExpression condition = isReceiver(volunteerId);

        List<NoteReceiverViewForVolunteer> results = queryFactory
                .select(Projections.constructor(
                        NoteReceiverViewForVolunteer.class,
                        note.id,
                        note.title,
                        center.id.as(SENDER_ID),
                        center.name.as(SENDER_NAME),
                        note.isRead
                ))
                .from(note)
                .join(center).on(note.senderId.eq(center.id).and(activeCenter))
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(note.createdAt.desc())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(note.count())
                .from(note)
                .where(condition);

        return PageableExecutionUtils.getPage(results, pageable, count::fetchOne);
    }

    @Override
    public Optional<NoteDetailViewForCenter> findNoteDetailViewReceiverIsCenter(Long noteId) {

        BooleanExpression activeVolunteer = isActiveVolunteer();

        NoteDetailViewForCenter result = queryFactory
                .select(Projections.constructor(
                        NoteDetailViewForCenter.class,
                        note.id,
                        note.title,
                        note.content,
                        volunteer.id.as(SENDER_ID),
                        volunteer.nickname.as(SENDER_NAME),
                        volunteer.imgUrl.as("senderProfileImgLink"),
                        note.createdAt
                ))
                .from(note)
                .join(volunteer).on(note.senderId.eq(volunteer.id).and(activeVolunteer))
                .where(note.id.eq(noteId).and(note.deleted.eq(false)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private static BooleanExpression isReceiver(UUID receiverId) {
        return note.receiverId.eq(receiverId)
                .and(note.deleted.eq(false));
    }

    private BooleanExpression isActiveVolunteer() {
        return volunteer.deleted.eq(false);
    }

}
