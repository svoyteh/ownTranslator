package com.mmf.db.dao;

import com.mmf.business.domain.Schedule;
import com.mmf.db.model.ScheduleEntity;

import java.util.List;

/**
 * svetlana.voyteh
 * 05.03.13
 */
public interface ScheduleDao extends GenericDao<Long, ScheduleEntity>{
    List<ScheduleEntity> getSchedule(int semester, int yearOfEntrance, String groupName, String subGroupName);
    List<ScheduleEntity> getScheduleForDay(int semester, int yearOfEntrance, String groupName, String subGroupName, int day);

    List<ScheduleEntity> getSchedule(int semester, long lecturerId);
    List<ScheduleEntity> getScheduleForDay(int semester, long lecturerId, int day);
}
