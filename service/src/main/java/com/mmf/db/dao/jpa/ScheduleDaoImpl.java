package com.mmf.db.dao.jpa;

import com.mmf.db.dao.ScheduleDao;
import com.mmf.db.model.CurriculumEntity;
import com.mmf.db.model.GroupEntity;
import com.mmf.db.model.ScheduleEntity;
import com.mmf.db.model.StudyEntity;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * User: svetlana.voyteh
 * Date: 19.03.13
 */
@Named
public class ScheduleDaoImpl extends GenericJpaDao<Long, ScheduleEntity> implements ScheduleDao{

    @Override
    protected Class<ScheduleEntity> getEntityClass() {
        return ScheduleEntity.class;
    }

    @Override
    public List<ScheduleEntity> getSchedule(int semester, int yearOfEntrance, String groupName, String subGroupName) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ScheduleEntity> criteriaQuery = criteriaBuilder.createQuery(ScheduleEntity.class);
        Root<ScheduleEntity> rootSchedule = criteriaQuery.from(ScheduleEntity.class);
        CriteriaQuery<ScheduleEntity> select = criteriaQuery.select(rootSchedule);

        Subquery<StudyEntity> studySubQuery = criteriaQuery.subquery(StudyEntity.class);
        Root<StudyEntity> rootStudy = studySubQuery.from(StudyEntity.class);
        Subquery<StudyEntity> selectStudy = studySubQuery.select(rootStudy);

        Subquery<GroupEntity> groupSubQuery = criteriaQuery.subquery(GroupEntity.class);
        Root<GroupEntity> rootGroup = groupSubQuery.from(GroupEntity.class);
        groupSubQuery.select(rootGroup);
        groupSubQuery.
                where(criteriaBuilder.and
                        (
                                criteriaBuilder.equal(rootGroup.get("year"), yearOfEntrance),
                                criteriaBuilder.or
                                        (
                                                criteriaBuilder.equal(rootGroup.get("name"), groupName),
                                                criteriaBuilder.equal(rootGroup.get("name"), subGroupName)
                                        )
                        )
                );

        Subquery<CurriculumEntity> curriculumSubQuery = criteriaQuery.subquery(CurriculumEntity.class);
        Root<CurriculumEntity> rootCurriculum = curriculumSubQuery.from(CurriculumEntity.class);
        curriculumSubQuery.select(rootCurriculum);
        curriculumSubQuery.where(criteriaBuilder.equal(rootCurriculum.get("semester"), semester));

        selectStudy.
                where(criteriaBuilder.and
                        (
                                criteriaBuilder.in(rootStudy.get("group")).value(groupSubQuery),
                                criteriaBuilder.in(rootStudy.get("curriculum")).value(curriculumSubQuery)
                        )
                );

        select.where(criteriaBuilder.in(rootSchedule.get("study")).value(selectStudy)).orderBy(criteriaBuilder.asc(rootSchedule.get("dayOfWeek")));

        TypedQuery<ScheduleEntity> query = entityManager.createQuery(select);

        return query.getResultList();
    }

    @Override
    public List<ScheduleEntity> getScheduleForDay(int semester, int yearOfEntrance, String groupName, String subGroupName, int day) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ScheduleEntity> criteriaQuery = criteriaBuilder.createQuery(ScheduleEntity.class);
        Root<ScheduleEntity> rootSchedule = criteriaQuery.from(ScheduleEntity.class);
        CriteriaQuery<ScheduleEntity> select = criteriaQuery.select(rootSchedule);

        Subquery<StudyEntity> studySubQuery = criteriaQuery.subquery(StudyEntity.class);
        Root<StudyEntity> rootStudy = studySubQuery.from(StudyEntity.class);
        Subquery<StudyEntity> selectStudy = studySubQuery.select(rootStudy);

        Subquery<GroupEntity> groupSubQuery = criteriaQuery.subquery(GroupEntity.class);
        Root<GroupEntity> rootGroup = groupSubQuery.from(GroupEntity.class);
        groupSubQuery.select(rootGroup);
        if ("".equals(subGroupName)){
            groupSubQuery.
                    where(criteriaBuilder.and
                            (
                                    criteriaBuilder.equal(rootGroup.get("year"), yearOfEntrance),
                                    criteriaBuilder.like(rootGroup.<String>get("name"), groupName + "%")
                            )
                    );
        } else {
            groupSubQuery.
                    where(criteriaBuilder.and
                            (
                                    criteriaBuilder.equal(rootGroup.get("year"), yearOfEntrance),
                                    criteriaBuilder.or
                                            (
                                                    criteriaBuilder.equal(rootGroup.get("name"), groupName),
                                                    criteriaBuilder.equal(rootGroup.get("name"), subGroupName)
                                            )
                            )
                    );
        }
        Subquery<CurriculumEntity> curriculumSubQuery = criteriaQuery.subquery(CurriculumEntity.class);
        Root<CurriculumEntity> rootCurriculum = curriculumSubQuery.from(CurriculumEntity.class);
        curriculumSubQuery.select(rootCurriculum);
        curriculumSubQuery.where(criteriaBuilder.equal(rootCurriculum.get("semester"), semester));

        selectStudy.
                where(criteriaBuilder.and
                        (
                                criteriaBuilder.in(rootStudy.get("group")).value(groupSubQuery),
                                criteriaBuilder.in(rootStudy.get("curriculum")).value(curriculumSubQuery)
                        )
                );

        select.where(criteriaBuilder.and(
                criteriaBuilder.in(rootSchedule.get("study")).value(selectStudy)),
                criteriaBuilder.equal(rootSchedule.get("dayOfWeek"), day)
                ).orderBy(criteriaBuilder.asc(rootSchedule.get("disciplineTime").get("number"))
        );

        TypedQuery<ScheduleEntity> query = entityManager.createQuery(select);
        return query.getResultList();
    }

    @Override
    public List<ScheduleEntity> getSchedule(int semester, long lecturerId) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ScheduleEntity> criteriaQuery = criteriaBuilder.createQuery(ScheduleEntity.class);
        Root<ScheduleEntity> rootSchedule = criteriaQuery.from(ScheduleEntity.class);
        CriteriaQuery<ScheduleEntity> select = criteriaQuery.select(rootSchedule);

        Subquery<StudyEntity> studySubQuery = criteriaQuery.subquery(StudyEntity.class);
        Root<StudyEntity> rootStudy = studySubQuery.from(StudyEntity.class);
        Subquery<StudyEntity> selectStudy = studySubQuery.select(rootStudy);

        Subquery<CurriculumEntity> curriculumSubQuery = criteriaQuery.subquery(CurriculumEntity.class);
        Root<CurriculumEntity> rootCurriculum = curriculumSubQuery.from(CurriculumEntity.class);
        curriculumSubQuery.select(rootCurriculum);
        curriculumSubQuery.where(criteriaBuilder.equal(criteriaBuilder.mod(rootCurriculum.<Integer>get("semester"), 2), semester));

        selectStudy.
                where(criteriaBuilder.and
                        (
                                criteriaBuilder.equal(rootStudy.get("lecturer").get("id"), lecturerId),
                                criteriaBuilder.in(rootStudy.get("curriculum")).value(curriculumSubQuery)
                        )
                );

        select.where(criteriaBuilder.in(rootSchedule.get("study")).value(selectStudy)).orderBy(criteriaBuilder.asc(rootSchedule.get("dayOfWeek")));

        TypedQuery<ScheduleEntity> query = entityManager.createQuery(select);

        return query.getResultList();
    }

    @Override
    public List<ScheduleEntity> getScheduleForDay(int semester, long lecturerId, int day) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ScheduleEntity> criteriaQuery = criteriaBuilder.createQuery(ScheduleEntity.class);
        Root<ScheduleEntity> rootSchedule = criteriaQuery.from(ScheduleEntity.class);
        CriteriaQuery<ScheduleEntity> select = criteriaQuery.select(rootSchedule);

        Subquery<StudyEntity> studySubQuery = criteriaQuery.subquery(StudyEntity.class);
        Root<StudyEntity> rootStudy = studySubQuery.from(StudyEntity.class);
        Subquery<StudyEntity> selectStudy = studySubQuery.select(rootStudy);

        Subquery<CurriculumEntity> curriculumSubQuery = criteriaQuery.subquery(CurriculumEntity.class);
        Root<CurriculumEntity> rootCurriculum = curriculumSubQuery.from(CurriculumEntity.class);
        curriculumSubQuery.select(rootCurriculum);
        curriculumSubQuery.where(criteriaBuilder.equal(criteriaBuilder.mod(rootCurriculum.<Integer>get("semester"), 2), semester));

        selectStudy.
                where(criteriaBuilder.and
                        (
                                criteriaBuilder.equal(rootStudy.get("lecturer").get("id"), lecturerId),
                                criteriaBuilder.in(rootStudy.get("curriculum")).value(curriculumSubQuery)
                        )
                );

        select.where(criteriaBuilder.and(
                criteriaBuilder.in(rootSchedule.get("study")).value(selectStudy)),
                criteriaBuilder.equal(rootSchedule.get("dayOfWeek"), day)
        ).orderBy(criteriaBuilder.asc(rootSchedule.get("disciplineTime").get("number"))
        );

        TypedQuery<ScheduleEntity> query = entityManager.createQuery(select);

        return query.getResultList();
    }
}
